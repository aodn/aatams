package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder as GrailsConfig

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import org.joda.time.*
import java.text.DateFormat
import java.text.SimpleDateFormat
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

    static final String DATE_AND_TIME_COLUMN = "\uFEFFDate and Time (UTC)"  // \uFEFF is a zero-width non-breaking space.
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
    
    static DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    
    static
    {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT:00"))
    }
    
    /**
     * Creates a detection given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
    RawDetection newDetection(params) throws FileProcessingException 
    {
        def nativeParams = toNativeParams(params)
        
        RawDetection detection = initDetection(nativeParams)
        assert(detection)
        
        if (!detection.isValid())
        {
            return detection
        }
        
        matchToTags(detection)
        
        return detection
    }
    
    RawDetection initDetection(nativeParams)
    {
        DetectionValidator validator = new DetectionValidator(params:nativeParams)
        assert(validator)
        
        if (validator.isDuplicate())
        {
            return new InvalidDetection(nativeParams + [reason:InvalidDetectionReason.DUPLICATE])
        }
        
        if (validator.isUnknownReceiver())
        {
            return new InvalidDetection(nativeParams + 
                                        [reason:InvalidDetectionReason.UNKNOWN_RECEIVER, 
                                         message:"Unknown receiver code name " + nativeParams.receiverName])
        }
        
        if (validator.hasNoDeploymentsAtDateTime())
        {
            return new InvalidDetection(nativeParams + 
                                        [reason:InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, 
                                         message:"No deployment at time " + simpleDateFormat.format(nativeParams.timestamp) + " for receiver " + nativeParams.receiverName])
        }

        if (validator.hasNoRecoveriesAtDateTime())
        {
            return new InvalidDetection(nativeParams + 
                                        [reason:InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME, 
                                         message:"No recovery at time " + simpleDateFormat.format(nativeParams.timestamp) + " for receiver " + nativeParams.receiverName])
        }
        
        return new ValidDetection(nativeParams + [receiverDeployment:validator.deployment]).save()
    }
    
    private static Map toNativeParams(params)
    {
        def timestamp = new Date().parse(DATE_FORMAT, params[DATE_AND_TIME_COLUMN]+ " " + "UTC")
        
        def retMap =
               [timestamp:timestamp,
                receiverName:params[RECEIVER_COLUMN],
                transmitterId:params[TRANSMITTER_COLUMN],
                transmitterName:params[TRANSMITTER_NAME_COLUMN],
                transmitterSerialNumber:params[TRANSMITTER_SERIAL_NUMBER_COLUMN],
                sensorValue:params[SENSOR_VALUE_COLUMN],
                sensorUnit:params[SENSOR_UNIT_COLUMN],
                stationName:params[STATION_NAME_COLUMN]] 

        // Latitude and longitude are optional.
        if ((params[LATITUDE_COLUMN] != null) && (params[LONGITUDE_COLUMN] != null))
        {
            Point location = new GeometryFactory().createPoint(new Coordinate(params[LONGITUDE_COLUMN], params[LATITUDE_COLUMN]))
            location.setSRID(4326)
            retMap.location = location
        }
        
        return retMap
    }
    
    private void matchToTags(detection)
    {
        assert(detection)
        
        def tags = Tag.findAllByCodeName(detection.transmitterId) 
        tags.each
        {
            tag -> tag.surgeries.each
            {
                createDetectionSurgery(it, tag, detection)
            }
        }

        def sensors = Sensor.findAllByCodeName(detection.transmitterId) 
        sensors.each
        {
            sensor -> sensor.tag.surgeries.each
            {
                createDetectionSurgery(it, sensor, detection)
            }
        }
    }
    
    private void createDetectionSurgery(surgery, tag, detection)
    {
        DetectionSurgery detectionSurgery =
            new DetectionSurgery(surgery:surgery, 
                                 tag:tag, 
                                 detection:detection)

        detection.addToDetectionSurgeries(detectionSurgery)
        surgery.addToDetectionSurgeries(detectionSurgery)
        tag.addToDetectionSurgeries(detectionSurgery)

        detection.save()
        surgery.save()
        tag.save()
    }
}
