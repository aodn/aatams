package au.org.emii.aatams.detection

import groovy.sql.Sql
import org.joda.time.DateTime

class DetectionExtractService 
{
    static transactional = false
	
	def dataSource
	
    def extract(filterParams) 
	{
		long startTime = System.currentTimeMillis()
		
		def sql = new Sql(dataSource)
		
		sql.execute("set work_mem = '1GB'")
		def result = sql.rows('''select * from detection_extract_view where 
							project like '%' || :project_name || '%' 
			            and installation like '%' || :installation_name || '%'
			            and station like '%' || :station_name || '%'
			            and sensor_id like '%' || :sensor_id || '%' 
						and spcode like '%' || :spcode || '%' 
						and timestamp between :min_timestamp and :max_timestamp ''',
						
						[project_name: filterParams?.eq?.receiverDeployment?.station?.installation?.project?.name ?: '',
						 installation_name: filterParams?.eq?.receiverDeployment?.station?.installation?.name ?: '',
						 station_name: filterParams?.eq?.receiverDeployment?.station?.name ?: '',
						 sensor_id: filterParams?.eq?.detectionSurgeries?.tag?.codeName ?: '',
						 spcode: filterParams?.eq?.detectionSurgeries?.surgery?.release?.animal?.species?.spcode ?: '',
						 min_timestamp: filterParams?.between?.timestamp?.getAt(0) ? new java.sql.Timestamp(filterParams?.between?.timestamp?.getAt(0).getTime()) : new java.sql.Timestamp(new DateTime("1970-01-01").getMillis()),
						 max_timestamp: filterParams?.between?.timestamp?.getAt(1) ? new java.sql.Timestamp(filterParams?.between?.timestamp?.getAt(1).getTime()) : new java.sql.Timestamp(new DateTime("2100-01-01").getMillis())])
		
//		File detectionExtractFile = File.createTempFile("detectionExtract", "csv")
//		detectionExtractFile.deleteOnExit()
//		
//		def result = sql.rows('''COPY (select * from detection_extract_view where 
//							project like '%' || :project_name || '%' 
//			            and installation like '%' || :installation_name || '%'
//			            and station like '%' || :station_name || '%'
//			            and sensor_id like '%' || :sensor_id || '%' 
//						and spcode like '%' || :spcode || '%' 
//						and timestamp between :min_timestamp and :max_timestamp) To ' ''' + detectionExtractFile.getAbsolutePath() + ''' ' WITH CSV ''',
//						
//						[project_name: filterParams?.eq?.receiverDeployment?.station?.installation?.project?.name ?: '',
//						 installation_name: filterParams?.eq?.receiverDeployment?.station?.installation?.name ?: '',
//						 station_name: filterParams?.eq?.receiverDeployment?.station?.name ?: '',
//						 sensor_id: filterParams?.eq?.detectionSurgeries?.tag?.codeName ?: '',
//						 spcode: filterParams?.eq?.detectionSurgeries?.surgery?.release?.animal?.species?.spcode ?: '',
//						 min_timestamp: filterParams?.between?.timestamp?.getAt(0) ? new java.sql.Timestamp(filterParams?.between?.timestamp?.getAt(0).getTime()) : new java.sql.Timestamp(new DateTime("1970-01-01").getMillis()),
//						 max_timestamp: filterParams?.between?.timestamp?.getAt(1) ? new java.sql.Timestamp(filterParams?.between?.timestamp?.getAt(1).getTime()) : new java.sql.Timestamp(new DateTime("2100-01-01").getMillis())])
		
		
		sql.execute("reset work_mem")
		sql.close()
		
		println "Detection extract query time (ms): " + (System.currentTimeMillis() - startTime)
		return result
		
//		return detectionExtractFile
    }
}
