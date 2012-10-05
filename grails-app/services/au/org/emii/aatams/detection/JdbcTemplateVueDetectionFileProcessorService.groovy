package au.org.emii.aatams.detection

import java.util.Map;

import au.org.emii.aatams.DetectionSurgery
import au.org.emii.aatams.util.SqlUtils

import au.org.emii.aatams.bulk.BulkImportException

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
	
//	List<Map> detectionBatch
	
	protected int getBatchSize()
	{
		return 500
	}
	
	protected void startBatch(context)
	{
		log.debug("Start batch")
		
		// Create lists
		context.detectionBatch = new ArrayList<Map>()
	}
	
	protected void endBatch(context)
	{
		if (context.detectionBatch.isEmpty())
		{
			log.warn("Detection batch empty.")
		}
		else
		{
			log.debug("Start mark duplicates...")
			markDuplicates(context)
			log.debug("End mark duplicates.")
			
			log.debug("End batch, inserting detections...")
			insertDetections(context)
		}
		
		super.endBatch(context)
	}
	
	private void markDuplicates(context)
	{
		context.detectionBatch.each 
		{
			newDetection ->
			
			def c = ValidDetection.createCriteria()
			def duplicateCount = c.get() 
			{
				projections
				{
					rowCount()
				}
				
				and
				{
					eq("receiverName", newDetection.receiverName)
					eq("timestamp", newDetection.timestamp)
					eq("transmitterId", newDetection.transmitterId)
				}
			}
			
			if (duplicateCount > 0)
			{
				newDetection["clazz"] = "au.org.emii.aatams.detection.InvalidDetection"
				newDetection["message"] = "Invalid detection: duplicate"
				newDetection["reason"] = InvalidDetectionReason.DUPLICATE
			}
		}
	}
	
	private void insertDetections(context)
	{
		def insertStatementList = []
		
		context.detectionBatch.each
		{
			if (it.clazz == "au.org.emii.aatams.detection.ValidDetection")
			{
				insertStatementList += ValidDetection.toSqlInsert(it)

				it.detectionSurgeries.each
				{
					detSurgery ->
	
					insertStatementList.add(DetectionSurgery.toSqlInsert(detSurgery, true))
				}
			}
			else if (it.clazz == "au.org.emii.aatams.detection.InvalidDetection")
			{
				insertStatementList += InvalidDetection.toSqlInsert(it)
			}
			else
			{
				assert(false): "Unknown detection class: " + it.clazz
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
	
    void processSingleRecord(downloadFile, map, context)
    {
		try
		{
			def detection = jdbcTemplateDetectionFactoryService.newDetection(downloadFile, map)
	        context.detectionBatch.addAll(detection)
		}
		catch (BulkImportException e)
		{
			
		}
    }
}
