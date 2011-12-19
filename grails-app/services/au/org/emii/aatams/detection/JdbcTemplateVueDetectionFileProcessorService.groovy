package au.org.emii.aatams.detection

import java.util.Map;

import au.org.emii.aatams.DetectionSurgery
import au.org.emii.aatams.util.SqlUtils

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils

/**
 *  Uses JDBC template (rather than GORM) to persist detection records.
 *  
 * @author jburgess
 *
 */
class JdbcTemplateVueDetectionFileProcessorService extends VueDetectionFileProcessorService 
{
    static transactional = true
	
	def dataSource
	def jdbcTemplateDetectionFactoryService
	
	List<Map> detectionBatch
	
	protected int getBatchSize()
	{
		return 10000
	}
	
	protected void startBatch()
	{
		log.debug("Start batch")
		
		// Create lists
		detectionBatch = new ArrayList<Map>()
	}
	
	protected void endBatch()
	{
		log.debug("End batch, inserting detections...")
		insertDetections()
	}
	
	private void insertDetections()
	{
		def insertStatementList = []
		
		detectionBatch.each
		{
			insertStatementList.add(RawDetection.toSqlInsert(it))
			
			it.detectionSurgeries.each
			{
				detSurgery ->

				insertStatementList.add(DetectionSurgery.toSqlInsert(detSurgery, true))
			}
		}
		
		batchUpdate(insertStatementList.toArray(new String[0]))
	}
	
	void batchUpdate(String[] statements)
	{
		log.debug("Inserting " + statements.size() + " records...")
		JdbcTemplate insert = new JdbcTemplate(dataSource)
		insert.batchUpdate(statements)
		log.debug("Batch successfully inserted")	
	}
	
    void processSingleRecord(downloadFile, map)
    {
		def detection = jdbcTemplateDetectionFactoryService.newDetection(downloadFile, map)
        detectionBatch.addAll(detection)
    }
}
