package au.org.emii.aatams.detection

import au.org.emii.aatams.DetectionSurgery

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
	List<Map> detectionSurgeryBatch
	
	protected int getBatchSize()
	{
		return 10000
	}
	
	protected void startBatch()
	{
		log.debug("Start batch")
		
		// Create lists
		detectionBatch = new ArrayList<Map>()
		detectionSurgeryBatch = new ArrayList<Map>()
	}
	
	protected void endBatch()
	{
		log.debug("End batch, inserting detections...")
		insertDetections()
		insertDetectionSurgeries()
	}
	
	private void insertDetections()
	{
		SqlParameterSource[] detectionsBatch = SqlParameterSourceUtils.createBatch(detectionBatch.toArray(new Map[0]))
		def insertDetectionsStatement = 
			"INSERT INTO RAW_DETECTION (LOCATION, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, " +
			"STATION_NAME, TIMESTAMP, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, CLASS, RECEIVER_DEPLOYMENT_ID, MESSAGE, REASON) " +
			" VALUES(:location, :receiverDownloadId, :receiverName, :sensorUnit, :sensorValue, :stationName, :timestamp, :transmitterId, " +
			":transmitterName, :transmitterSerialNumber, :clazz, :receiverDeploymentId, :message, :reason)"
		
		doInsert(detectionsBatch, insertDetectionsStatement)
	}

	private void insertDetectionSurgeries()
	{
		SqlParameterSource[] detectionSurgeriesBatch = SqlParameterSourceUtils.createBatch(detectionSurgeryBatch.toArray(new Map[0]))
		def insertDetectionsStatement = 
			"INSERT INTO DETECTION_SURGERY (DETECTION_ID, SURGERY_ID, TAG_ID) " +
			" VALUES(:detectionId, :surgeryId, :tagId)"
		
		doInsert(detectionSurgeriesBatch, insertDetectionsStatement)
	}
	
	private void doInsert(batch, statement)
	{
		log.debug("Inserting " + batch.size() + " records...")
		log.debug("Statement: " + statement)
		
		SimpleJdbcTemplate insert = new SimpleJdbcTemplate(dataSource)
		int[] updateCounts = insert.batchUpdate(
			statement,
			batch)

		log.debug("Batch successfully inserted")	
	}
	
    void processSingleRecord(downloadFile, map)
    {
		def newDetectionObjects = jdbcTemplateDetectionFactoryService.newDetection(downloadFile, map)
        detectionBatch.addAll(newDetectionObjects["detection"])
		detectionSurgeryBatch.addAll(newDetectionObjects["detectionSurgeries"])
    }
}
