package au.org.emii.aatams.detection

import java.util.Map;
import java.util.zip.GZIPOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

import groovy.sql.Sql
import org.joda.time.DateTime
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.apache.shiro.SecurityUtils

import au.org.emii.aatams.util.GeometryUtils;

class DetectionExtractService 
{
    static transactional = false
	
	def dataSource
	def permissionUtilsService
	private Map<Integer, Boolean> projectPermissionCache = [:]
 
	def extractPage(filterParams, sql, limit, offset)
	{
		def results = sql.rows(constructQuery(filterParams, limit, offset))
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
		
		return results
	}
	
	private String constructQuery(filterParams, limit, offset)
	{
		String query = '''select * from detection_extract_view '''
		List<String> whereClauses = []
		
		["project": filterParams?.in?.receiverDeployment?.station?.installation?.project?.name,
		 "installation": filterParams?.in?.receiverDeployment?.station?.installation?.name,
		 "station": filterParams?.in?.receiverDeployment?.station?.name,
		 "sensor_id": filterParams?.in?.detectionSurgeries?.tag?.codeName,
		 "spcode": filterParams?.in?.detectionSurgeries?.surgery?.release?.animal?.species?.spcode
		 ].each
	 	{
		    k, v ->
			
			if (v)
			{
				whereClauses += (k + " in (" + toSqlFormat(v) + ") ")
			}
		}
		 
		["timestamp": filterParams?.between?.timestamp].each
		{
			k, v ->
			
			if (v)
			{
				whereClauses += (k + " between '" + new java.sql.Timestamp(v[0].getTime()) + "' and '" + new java.sql.Timestamp(v[1].getTime()) + "' ")
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

		def sql = new Sql(dataSource)
		def offset = 0
		def limit = ConfigurationHolder.config.rawDetection.extract.limit
		
		def resultList = extractPage(filterParams, sql, limit, offset)
		while (resultList.size() > 0)
		{
			log.debug ("num rows: " + resultList.size() + ", offset: " + offset)

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
			
			offset += resultList.size()
			resultList = extractPage(filterParams, sql, limit, offset)
		}
		
		sql.close()
		
		log.error("Elapsed time (ms): " + (System.currentTimeMillis() - startTime))
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
