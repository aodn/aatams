package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder as GrailsConfig

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import org.joda.time.*

import java.util.Date;
import java.util.TimeZone

/**
 * Implementation of the detection/surgery matching algorithm as outlined here:
 * 
 * http://redmine.emii.org.au/issues/379
 * 
 * Essentially, input is a set of detection parameters (as recorded by a receiver
 * and exported with the VUE application), output is a detection record plus
 * 0 or more DetectionSurgeries.
 */
class DetectionFactoryService 
{
    static transactional = true

	private Map<String, List<Sensor>> sensorCache = new WeakHashMap<String, List<Sensor>>()
	
    /**
     * Creates a detection given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
	def newDetection(downloadFile, params) throws FileProcessingException
    {
		def format = DetectionFormat.newFormat(downloadFile.type)		
		def nativeParams = format.parseRow(params)
        def detection = initDetection(downloadFile, nativeParams)
        assert(detection)
		
        if (!detection.valid)
        {
			;
        }
		else
		{
			matchToTags(detection)
		}
		
        return detection
    }
    
    Collection rescanForSurgery(Surgery surgery)
    {
        DeviceStatus retiredStatus = DeviceStatus.findByStatus(DeviceStatus.RETIRED, [cache:true])
        if (surgery.tag.status == retiredStatus)
        {
            return []
        }
        
        def newDetectionSurgeries = []
        
		surgery.tag.sensors.each
		{
			sensor ->

			def matchingDetections = ValidDetection.findAllByTransmitterId(sensor.transmitterId, [cache:true])
			def numRecords = matchingDetections.size()
			log.info("Rescanning for surgery on sensor: " + sensor + ", " + numRecords + " detections match tag.")
			int percentProgress = 0
			long startTime = System.currentTimeMillis()
			
			matchingDetections.eachWithIndex
			{
				detection, i ->
				
				if (surgery.isInWindow(detection.timestamp))
				{
					def newDetSurgery = createDetectionSurgery(surgery, sensor, detection)
					newDetectionSurgeries.add(newDetSurgery)
				}
				
				percentProgress = logProgress(i, numRecords, percentProgress, surgery, startTime)
			}
	
		}
		
		log.debug("Num new detection surgeries: " + newDetectionSurgeries.size())
        return newDetectionSurgeries
    }
	
	void rescanForDeployment(ReceiverDeployment deployment)
	{
		assert(deployment.recovery) : "Deployment must have associated recovery"
		def matchingInvalidDets = 
			InvalidDetection.findAllByReceiverNameAndTimestampBetween(
				deployment.receiver.name, 
				deployment.deploymentDateTime.toDate(), 
				deployment.recovery.recoveryDateTime.toDate()).grep {
				
			it.reason != InvalidDetectionReason.DUPLICATE
		}
			
		long matchingInvalidDetsCount = matchingInvalidDets.size()	
		log.info("${matchingInvalidDetsCount} matching invalid detections found, promoting to valid...")
		
		int percentComplete = 0
		
		matchingInvalidDets.eachWithIndex {
			
			invalidDet, i ->
			
			ValidDetection validDet = 
				new ValidDetection(timestamp: invalidDet.timestamp,
								   receiverName: invalidDet.receiverName,
								   stationName: invalidDet.stationName,
								   transmitterId: invalidDet.transmitterId,
								   transmitterName: invalidDet.transmitterName,
								   transmitterSerialNumber: invalidDet.transmitterSerialNumber,
								   location: invalidDet.location,
								   sensorValue: invalidDet.sensorValue,
								   sensorUnit: invalidDet.sensorUnit,
								   receiverDownload: invalidDet.receiverDownload,
								   receiverDeployment: deployment)
			log.debug("Saving valid detection: ${validDet}, deleting invalid detection: ${invalidDet}")
			validDet.save()
			invalidDet.delete()
			
			def oldPercentComplete = percentComplete
			percentComplete = i / matchingInvalidDetsCount
			if (oldPercentComplete != percentComplete)
			{
				log.info("${percentComplete}% detections promoted")
			}
		}
	}
    
	private int logProgress(i, numRecords, percentProgress, surgery, startTime)
	{
		float progress = (float)i/numRecords * 100
		if ((int)progress > percentProgress)
		{
			percentProgress = (int)progress
			
			String progressMsg =
				"Surgery processing, id: " + surgery.id +
				", progress: " + percentProgress +
				"%, average time per record: " + (float)(System.currentTimeMillis() - startTime) / (i + 1) + "ms"
				
			if ((percentProgress % 10) == 0)
			{
				log.info(progressMsg)
			}
			else
			{
				log.debug(progressMsg)
			}
		}
		
		return percentProgress
	}
	
    private def initDetection(downloadFile, nativeParams)
    {
		def detectionValidator = new DetectionValidator()
        assert(detectionValidator)
		
		if (detectionValidator.validate(downloadFile, nativeParams))
		{
			return createValidDetection(nativeParams + 
					   	                [receiverDownload:downloadFile, 
										 receiverDeployment:detectionValidator.deployment,
										 receiver:detectionValidator.receiver])
		}
		else
		{
			return createInvalidDetection(nativeParams + 
                                          [receiverDownload:downloadFile, 
                                           reason:detectionValidator.invalidReason, 
                                           message:detectionValidator.invalidMessage])
		}
    }
    
    private void matchToTags(detection)
    {
        assert(detection)

		def sensors = findSensors(detection.transmitterId)
        sensors.each
        {
            sensor -> sensor.tag.surgeries.each
            {
                surgery ->

                if (!surgery.isInWindow(detection.timestamp))
                {
                    return
                }
                
				createDetectionSurgery(surgery, sensor, detection)
            }
        }
    }
	
	private List<Sensor> findSensors(transmitterId)
	{
		if (!sensorCache.containsKey(transmitterId))
		{
			def sensorsWithId = 
				Sensor.findAllByTransmitterId(transmitterId, [cache:true])
				
			sensorsWithId = sensorsWithId.grep
			{
				it.tag.status != DeviceStatus.findByStatus(DeviceStatus.RETIRED, [cache:true])
			}
			
			sensorCache.put(transmitterId, 
					  	    sensorsWithId)
		}
		
		return sensorCache[transmitterId]
	}
	
    protected def createDetectionSurgery(surgery, sensor, detection)
    {
		return new DetectionSurgery(
			surgery:surgery,
			sensor:sensor,
			detection:detection).save()
    }
	
	protected def createValidDetection(params)
	{
		return new ValidDetection(params).save()
	}
	
	protected def createInvalidDetection(params)
	{
		return new InvalidDetection(params).save()
	}
}
