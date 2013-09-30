package au.org.emii.aatams.detection

import java.util.Map;

import au.org.emii.aatams.DetectionSurgery
import au.org.emii.aatams.FileProcessingException
import au.org.emii.aatams.Statistics
import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.util.SqlUtils

import au.org.emii.aatams.bulk.BulkImportException

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils

import groovy.sql.Sql

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
    def grailsApplication
	def jdbcTemplateDetectionFactoryService
	
	protected int getBatchSize()
	{
		return 500
	}
	
	void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
	{
		super.process(downloadFile)

        promoteProvisional()
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
		def useHibernameSeqIdForDetectionSurgery = true

		def connection = dataSource.getConnection()

		def validDetectionInsertStatements   = ValidDetection.prepareInsertStatement(connection)
		def invalidDetectionInsertStatements = InvalidDetection.prepareInsertStatement(connection)
		def detectionSurgeryInsertStatements = DetectionSurgery.prepareInsertStatement(connection, useHibernameSeqIdForDetectionSurgery)
		
		context.detectionBatch.each
		{
			if (it.clazz == "au.org.emii.aatams.detection.ValidDetection")
			{
				ValidDetection.addToPreparedStatement(validDetectionInsertStatements, it);

				it.detectionSurgeries.each
				{
					detSurgery ->
	
					DetectionSurgery.addToPreparedStatement(
						detectionSurgeryInsertStatements,
						detSurgery,
						useHibernameSeqIdForDetectionSurgery)
				}
			}
			else if (it.clazz == "au.org.emii.aatams.detection.InvalidDetection")
			{
				InvalidDetection.addToPreparedStatement(invalidDetectionInsertStatements, it)
			}
			else
			{
				assert(false): "Unknown detection class: " + it.clazz
			}
		}

		try {
			log.debug("Inserting ValidDetections records...")
			int [] validDetectionUpdateCounts = validDetectionInsertStatements.executeBatch()
			log.debug("Batch successfully inserted, " + validDetectionUpdateCounts.length + " records")

			log.debug("Inserting InvalidDetections records...")
			int [] invalidDetectionUpdateCounts = invalidDetectionInsertStatements.executeBatch()
			log.debug("Batch successfully inserted, " + invalidDetectionUpdateCounts.length + " records")

			log.debug("Inserting DetectionSurgeries records...")
			int [] detectionSurgeryUpdateCounts = detectionSurgeryInsertStatements.executeBatch()
			log.debug("Batch successfully inserted, " + detectionSurgeryUpdateCounts.length + " records")

		} catch (java.sql.SQLException t) {
			log.info(t.getNextException().getMessage())
			throw t
		}
	}

    void processSingleRecord(downloadFile, map, context) throws FileProcessingException
    {
        def detection = jdbcTemplateDetectionFactoryService.newDetection(downloadFile, map)
        context.detectionBatch.addAll(detection)
    }

    private void promoteProvisional()
    {
        def sql = Sql.newInstance(dataSource)

        // Insert provisional dets in to materialized view.
        def provDetsCount = sql.firstRow('select count(*) from detection_extract_view where provisional = true;').count
        sql.execute("insert into detection_extract_view_mv (select * from detection_extract_view where provisional = true)")

        // Update statistics.
        def numValidDets = Statistics.findByKey("numValidDetections")
        numValidDets.value += provDetsCount

        // Clear 'provisional' flag on detections.
        sql.execute("update valid_detection set provisional = false where provisional = true")
        sql.execute("update detection_extract_view_mv set provisional = false where provisional = true")
    }
}
