package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder as GrailsConfig

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import org.joda.time.*
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

    static final String DATE_AND_TIME_COLUMN = "Date and Time (UTC)"
    static final String RECEIVER_COLUMN = "Receiver"
    static final String TRANSMITTER_COLUMN = "Transmitter"
    static final String TRANSMITTER_NAME_COLUMN = "Transmitter Name"
    static final String TRANSMITTER_SERIAL_NUMBER_COLUMN = "Transmitter Serial"
    static final String SENSOR_VALUE_COLUMN = "Sensor Value"
    static final String SENSOR_UNIT_COLUMN = "Sensor Unit"
    static final String STATION_NAME_COLUMN = "Station Name"
    static final String LATITUDE_COLUMN = "Latitude"
    static final String LONGITUDE_COLUMN = "Longitude"
    
    static final String TRANSMITTER_ID_DELIM = "-"
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    
	def detectionValidatorService
	
	private Map<String, List<Sensor>> sensorCache = new WeakHashMap<String, List<Sensor>>()
	
    /**
     * Creates a detection given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
	def newDetection(downloadFile, params) throws FileProcessingException
    {
		def nativeParams = toNativeParams(params)

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
        assert(detectionValidatorService)
		
		if (detectionValidatorService.validate(downloadFile, nativeParams))
		{
			return createValidDetection(nativeParams + 
					   	                [receiverDownload:downloadFile, 
										 receiverDeployment:detectionValidatorService.deployment,
										 receiver:detectionValidatorService.receiver])
		}
		else
		{
			return createInvalidDetection(nativeParams + 
                                          [receiverDownload:downloadFile, 
                                           reason:detectionValidatorService.invalidReason, 
                                           message:detectionValidatorService.invalidMessage])
		}
    }
    
	private static def getFloat(stringVal)
	{
		try
		{
			return Float.valueOf(stringVal)
		}
		catch (NullPointerException e)
		{
			return null
		}
		catch (NumberFormatException e)
		{
			return null
		}
	}
	
    private static Map toNativeParams(params)
    {
        def timestamp = new Date().parse(DATE_FORMAT, params[DATE_AND_TIME_COLUMN] + " " + "UTC")
        
        def retMap =
               [timestamp:timestamp,
                receiverName:params[RECEIVER_COLUMN],
                transmitterId:params[TRANSMITTER_COLUMN],
                transmitterName:params[TRANSMITTER_NAME_COLUMN],
                transmitterSerialNumber:params[TRANSMITTER_SERIAL_NUMBER_COLUMN],
                sensorValue:getFloat(params[SENSOR_VALUE_COLUMN]),
                sensorUnit:params[SENSOR_UNIT_COLUMN],
                stationName:params[STATION_NAME_COLUMN]] 

        // Latitude and longitude are optional.
        if ((params[LATITUDE_COLUMN] != null) && (params[LONGITUDE_COLUMN] != null))
        {
            Point location = new GeometryFactory().createPoint(new Coordinate(getFloat(params[LONGITUDE_COLUMN]), getFloat(params[LATITUDE_COLUMN])))
            location.setSRID(4326)
            retMap.location = location
        }
		else
		{
			retMap.location = null
		}
        
        return retMap
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
