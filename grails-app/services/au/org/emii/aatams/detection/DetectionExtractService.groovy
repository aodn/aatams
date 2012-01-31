package au.org.emii.aatams.detection

import groovy.sql.Sql
import org.joda.time.DateTime
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class DetectionExtractService 
{
    static transactional = false
	
	def dataSource
	
	def extractPage(filterParams, sql, limit, offset)
	{
		return sql.rows(constructQuery(filterParams, limit, offset))
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
	
	def generateReport(filterParams, response)
	{
		response.setHeader("Content-disposition", "attachment; filename=" + "detectionExtract.csv")
		response.contentType = "text/csv"
		response.characterEncoding = "UTF-8"
		
		response.outputStream << "timestamp,"
		response.outputStream << "station name,"
		response.outputStream << "latitude,"
		response.outputStream << "longitude,"
		response.outputStream << "receiver ID,"
		response.outputStream << "tag ID,"
		response.outputStream << "species,"
		response.outputStream << "uploader,"
		response.outputStream << "transmitter ID,"
		response.outputStream << "organisation"
		
		response.outputStream << "\n"

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
				
				response.outputStream << row.formatted_timestamp << ","
				response.outputStream << row.station << ","
				response.outputStream << row.latitude << ","
				response.outputStream << row.longitude << ","
				response.outputStream << row.receiver_name << ","
				response.outputStream << row.sensor_id << ","
				response.outputStream << row.species_name << ","
				response.outputStream << row.uploader << ","
				response.outputStream << row.transmitter_id << ","
				response.outputStream << row.organisation
				
				response.outputStream << "\n"
			}
			
			offset += resultList.size()
			resultList = extractPage(filterParams, sql, limit, offset)
		}
		
		sql.close()
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
}
