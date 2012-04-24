package au.org.emii.aatams.detection

import groovy.sql.Sql

import java.util.Map
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

import org.apache.shiro.SecurityUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import au.org.emii.aatams.util.GeometryUtils

class DetectionExtractService 
{
    static transactional = false
	
	def dataSource
	def permissionUtilsService
	private Map<Integer, Boolean> projectPermissionCache = [:]
 
	def extractPage(filterParams, sql, limit, offset)
	{
		log.debug("Querying database, offset: " + offset)
		def results = sql.rows(constructQuery(filterParams, limit, offset))
		log.debug("Query finished, num results: " + results.size())
		
		return results
	}

	private applyEmbargo(results) 
	{
		def now = new Date()
		
		results.each
		{
			row ->

			if (row.embargo_date && row.embargo_date.after(now))
			{
				if (!hasReadPermission(row.project_id))
				{
					row.species_name = ""
					row.spcode = ""
					row.sensor_id = ""
				}
			}
		}
	}
	
	private String constructQuery(filterParams, limit, offset)
	{
		String query = '''select * from detection_extract_view '''
		List<String> whereClauses = []
		
		["project": filterParams?.filter?.receiverDeployment?.station?.installation?.project?.in?.getAt(1),
		 "installation": filterParams?.filter?.receiverDeployment?.station?.installation?.in?.getAt(1),
		 "station": filterParams?.filter?.receiverDeployment?.station?.in?.getAt(1),
		 "sensor_id": filterParams?.filter?.detectionSurgeries?.sensor?.in?.getAt(1),
		 "spcode": filterParams?.filter?.detectionSurgeries?.surgery?.release?.animal?.species?.in?.getAt(1)
		 ].each
	 	{
		    k, v ->

			if (v)
			{
				whereClauses += ("trim(" + k + ") in (" + toSqlFormat(v) + ") ")
			}
		}
		 
		["timestamp": filterParams?.filter?.between].each
		{
			k, v ->

			if (v)
			{
				assert(v?."1") : "Start date/time must be specified"
				assert(v?."2") : "End date/time must be specified"
				
				whereClauses += (k + " between '" + new java.sql.Timestamp(v?."1"?.getTime()) + "' and '" + new java.sql.Timestamp(v?."2"?.getTime()) + "' ")
			}
		}
			 
		if (whereClauses.size() > 0)
		{
			query += "where "
			
			whereClauses.eachWithIndex
			{
				clause, i ->
				
				if (i != 0)
				{
					query += "and "
				}
				
				query += clause
			}
		}
		
		query += "limit " + limit + " offset " + offset
		
		log.debug("Query: " + query)
		
		return query
	}
	
	def generateReport(filterParams, req, res)
	{
		long startTime = System.currentTimeMillis()
		
		projectPermissionCache = [:]
		
		OutputStream out = null
		
		// Select the appropriate content encoding based on the
		// client's Accept-Encoding header. Choose GZIP if the header
		// includes "gzip". Choose ZIP if the header includes "compress".
		// Choose no compression otherwise.
		String encodings = req.getHeader("Accept-Encoding");
		if (encodings != null && encodings.indexOf("gzip") != -1) 
		{
			// Go with GZIP
			res.setHeader("Content-Encoding", "gzip");
			out = new GZIPOutputStream(res.getOutputStream());
		}
		else if (encodings != null && encodings.indexOf("compress") != -1) 
		{
			// Go with ZIP
			res.setHeader("Content-Encoding", "x-compress");
			out = new ZipOutputStream(res.getOutputStream());
			((ZipOutputStream)out).putNextEntry(new ZipEntry("dummy name"));
		}
		else 
		{
			// No compression
			out = res.getOutputStream();
		}

		res.setHeader("Vary", "Accept-Encoding");
		res.setHeader("Content-disposition", "attachment; filename=" + "detectionExtract.csv")
		res.contentType = "text/csv"
		res.characterEncoding = "UTF-8"

		try
		{
			writeCsvHeader(out)
			writeCsvData(filterParams, out)
		}
		finally
		{
			// Write the compression trailer and close the output stream
			out.close()
			log.info("Elapsed time (ms): " + (System.currentTimeMillis() - startTime))
		}
	}

	private boolean shouldReadAsync()
	{
		return true
	}
	
	ExecutorService executor = Executors.newCachedThreadPool()
	def resultListQueue
	volatile finishedQuery
	
	private void writeCsvData(final filterParams, OutputStream out) 
	{
		resultListQueue = new LinkedBlockingQueue<List>(ConfigurationHolder.config.rawDetection.extract.queryQueueSize)
		finishedQuery = false
		
		final def sql = new Sql(dataSource)
		final def limit = ConfigurationHolder.config.rawDetection.extract.limit
		
		if (shouldReadAsync())
		{
			executor.execute(new Runnable() 
			{
				public void run()
				{
					readData(filterParams, sql, limit)
				}
			})
		}
		else
		{
			readData(filterParams, sql, limit)
		}
		
		// Write data...
		try
		{
			while (!resultListQueue.isEmpty() || !finishedQuery)
			{
				def resultList = resultListQueue.poll(2, TimeUnit.SECONDS)
				
				if (resultList)
				{
					applyEmbargo(resultList)
					writeCsvChunk(resultList, out)
				}
			}
		}
		catch (Exception e)
		{
			log.warn("Data extract exception, cancelling database query...", e)
			executor.shutdownNow()
			executor = Executors.newCachedThreadPool()
		}
		finally
		{
			sql.close()
		}
	}

	private void readData(filterParams, sql, limit) 
	{
		try
		{
			def offset = 0

			def resultList = extractPage(filterParams, sql, limit, offset)

			while (resultList.size() > 0)
			{
				resultListQueue.put(resultList)

				offset += resultList.size()
				resultList = extractPage(filterParams, sql, limit, offset)
			}
		}
		catch (InterruptedException e)
		{
			//					System.out.println("Database query interrupted", e)
		}
		finally
		{
			finishedQuery = true
		}
	}

	private def writeCsvChunk(resultList, OutputStream out) 
	{
		resultList.each
		{
			row ->

			out << row.formatted_timestamp << ","
			out << row.station << ","
			out << GeometryUtils.scrambleCoordinate(row.latitude) << ","
			out << GeometryUtils.scrambleCoordinate(row.longitude) << ","
			out << row.receiver_name << ","
			out << row.sensor_id << ","
			out << row.species_name << ","
			out << row.uploader << ","
			out << row.transmitter_id << ","
			out << row.organisation

			out << "\n"
		}
		
		return resultList.size()
	}

	private void writeCsvHeader(OutputStream out) 
	{
		out << "timestamp,"
		out << "station name,"
		out << "latitude,"
		out << "longitude,"
		out << "receiver ID,"
		out << "tag ID,"
		out << "species,"
		out << "uploader,"
		out << "transmitter ID,"
		out << "organisation"

		out << "\n"
	}
	
	private String toSqlFormat(String formParam)
	{
		List<String> values = Arrays.asList(formParam.trim().split(",")).collect { it.trim() }
		
		String sqlFormat = ""
		values.eachWithIndex
		{
			value, i ->
			
			sqlFormat += "'" + value + "'" + ", "
		}
		
		return sqlFormat[0..sqlFormat.length() - 3]
	}
	
	private boolean hasReadPermission(projectId)
	{
		if (!projectPermissionCache.containsKey(projectId))
		{
	        String permissionString = permissionUtilsService.buildProjectReadPermission(projectId)
	        projectPermissionCache.put(projectId, SecurityUtils.subject.isPermitted(permissionString))
		}
		
		assert(projectPermissionCache.containsKey(projectId))
		return projectPermissionCache[projectId]
	}
}
