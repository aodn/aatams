package au.org.emii.aatams.detection

import org.springframework.jdbc.core.JdbcTemplate

import au.org.emii.aatams.DetectionSurgery
import au.org.emii.aatams.Surgery

class JdbcTemplateDetectionFactoryService extends DetectionFactoryService 
{
	def dataSource
	def sessionFactory
	
	protected def createValidDetection(params)
	{
		return (params 
			    + ["valid": true,
				   "clazz": "au.org.emii.aatams.detection.ValidDetection", 
				   "receiverDownloadId": params.receiverDownload.id,
				   "receiverDeploymentId": params.receiverDeployment.id,
				   "message": "",
				   "reason": "",
                   "provisional": true,
				   "detectionSurgeries": new ArrayList()])
	}
	
	protected def createInvalidDetection(params)
	{
		return (params 
			    + ["valid": false,
				   "clazz": "au.org.emii.aatams.detection.InvalidDetection", 
				   "receiverDownloadId": params.receiverDownload.id,
				   "detectionSurgeries": new ArrayList()])
	}
	
	protected def createDetectionSurgery(surgery, sensor, detection)
	{
		def detectionSurgery = 
			 [surgeryId:surgery.id,
			  sensorId:sensor.id,
			  detectionId:detection.id]
		
		if (detection instanceof RawDetection)
		{
			// Causes problems with hibernate (tries to call getId(), resulting in BasicPropertyAccessorException).
		}
		else
		{
			detection["detectionSurgeries"].add(detectionSurgery)
		}
		 
		return detectionSurgery	 
	}
	
	Collection rescanForSurgery(Surgery surgery)
	{
		def newDetectionSurgeries = super.rescanForSurgery(surgery)
		
		if (newDetectionSurgeries.isEmpty())
		{
			log.debug("No existing detections found")
		}
		else
		{
			def insertStatementList = newDetectionSurgeries.collect
			{
				DetectionSurgery.toSqlInsert(it, false)
			}
			
			batchUpdate(insertStatementList.toArray(new String[0]))
		}
	}
	
	void batchUpdate(String[] statements)
	{
		log.debug("Inserting " + statements.size() + " records...")
		JdbcTemplate insert = new JdbcTemplate(dataSource)
		insert.batchUpdate(statements)
		log.debug("Batch successfully inserted")
	}
}
