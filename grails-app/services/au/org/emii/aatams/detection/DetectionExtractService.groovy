package au.org.emii.aatams.detection

import java.io.OutputStream;

import org.apache.shiro.SecurityUtils

import au.org.emii.aatams.export.AbstractStreamingExporterService
import au.org.emii.aatams.util.GeometryUtils
import groovy.sql.Sql

class DetectionExtractService extends AbstractStreamingExporterService
{
    static transactional = false
	
	def dataSource
	def permissionUtilsService
	
	public List extractPage(filterParams)
	{
		log.debug("Querying database, offset: " + filterParams.offset)
		def results = filterParams.sql.rows(constructQuery(filterParams, filterParams.max, filterParams.offset))
		log.debug("Query finished, num results: " + results.size())
		
		return results
	}

    public Long getCount(filterParams)
    {
        if (!filterParams || filterParams.isEmpty() || filterParams?.filter == null || filterParams?.filter == [:])
        {
			return ValidDetection.count()
		}

		log.debug("Querying database, offset: " + filterParams.offset)
		def results = filterParams.sql.rows(constructQuery(filterParams, null, null, true))
		log.debug("results: " + results)
		
		return results.count[0]
    }
    
	protected String getReportName()
	{
		return "detection"
	}
	
	protected def applyEmbargo(results, params) 
	{
		def now = new Date()
		
		results = results.grep {
			
			row ->
			
			boolean isEmbargoed = (row.embargo_date && row.embargo_date.after(now) && !hasReadPermission(row.release_project_id, params))
			
			return !isEmbargoed
		}
		
		return results
	}
	
	protected void writeCsvData(final filterParams, OutputStream out)
	{
		filterParams.sql = new Sql(dataSource)
		filterParams.projectPermissionCache = [:]
		
		super.writeCsvData(filterParams, out)
	}

    /**
     * Allow mocking in tests (where "create_matview" function is not available).
     */
    private String getDetectionExtractViewName()
    {
        return "detection_extract_view_mv"
    }

	private String constructQuery(filterParams, limit, offset)
    {
        return constructQuery(filterParams, limit, offset, false)
    }
    
	private String constructQuery(filterParams, limit, offset, count)
	{
        String query

        if (count)
        {
            query = "select count(*) from ${getDetectionExtractViewName()} "
        }
        else
        {
            query = "select * from ${getDetectionExtractViewName()} "
        }
        
		List<String> whereClauses = []
		
		["project": filterParams?.filter?.receiverDeployment?.station?.installation?.project?.in?.getAt(1),
		 "installation": filterParams?.filter?.receiverDeployment?.station?.installation?.in?.getAt(1),
		 "station": filterParams?.filter?.receiverDeployment?.station?.in?.getAt(1),
		 "transmitter_id": filterParams?.filter?.in?.getAt(1),
		 "spcode": filterParams?.filter?.detectionSurgeries?.surgery?.release?.animal?.species?.in?.getAt(1)
		 ].each
	 	{
		    k, v ->

			if (v)
			{
				whereClauses += (k + " in (" + toSqlFormat(v) + ") ")
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

        if (!count)
        {
            if ((limit != null) && !count)
            {
                query += "limit " + limit
            }

            if (offset != null)
            {
                query += " offset " + offset
            }
        }
        
		log.debug("Query: " + query)
		
		return query
	}
	
	def generateReport(filterParams, req, res)
	{
		super.generateReport(filterParams, req, res)
	}

	protected List readData(filterParams)
	{
		return extractPage(filterParams)
	}
	
	protected def writeCsvChunk(resultList, OutputStream out) 
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
			out << row.organisation << ","
			out << ((row.sensor_value == null) ? "" : row.sensor_value) << ","
			out << ((row.sensor_unit == null) ? "" : row.sensor_unit)
			
			out << "\n"
		}
		
		return resultList.size()
	}

	protected void writeCsvHeader(OutputStream out) 
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
		out << "organisation,"
		out << "sensor value,"
		out << "sensor unit"
		
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
	
	private boolean hasReadPermission(projectId, params)
	{
		if (projectId == null)
		{
			return true
		}
		
		if (!params.projectPermissionCache.containsKey(projectId))
		{
	        String permissionString = permissionUtilsService.buildProjectReadPermission(projectId)
	        params.projectPermissionCache.put(projectId, SecurityUtils.subject.isPermitted(permissionString))
		}
		
		assert(params.projectPermissionCache.containsKey(projectId))
		return params.projectPermissionCache[projectId]
	}
}
