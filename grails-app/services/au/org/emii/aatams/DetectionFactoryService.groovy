package au.org.emii.aatams

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import org.joda.time.*

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
    
    /**
     * Creates a detection given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
    Detection newDetection(detectionParams) throws FileProcessingException 
    {
        def detection = createDetection(detectionParams)
        
        // Check that we don't already have an existing matching detection 
        // (in which case just return the existing detection).
        Detection.findAllByTimestamp(detection.timestamp).each
        {
            if (it.equals(detection))
            {
                log.warn(  "Returning existing matching detection for params: " + String.valueOf(detectionParams) 
                         + ", detection: " + String.valueOf(it))
                return it
            }
        }
        
        // The list of surgeries which match detection parameters.
        def matchingSurgeries = findMatchingSurgeries(detectionParams, detection)
        
        def numMatchingSurgeries = matchingSurgeries.size()
        if (numMatchingSurgeries == 0)
        {
            // Must be a non-AATAMS tag, or just not entered in to database yet.
            log.warn("No surgeries found for detection, params: " + String.valueOf(detectionParams))
        }
        else if (numMatchingSurgeries == 1)
        {
            // Normal case, no log message.
        }
        else (numMatchingSurgeries)     // > 1
        {
            log.warn(  "Multiple surgeries found for detection, params: " + String.valueOf(detectionParams) 
                     + ", surgeries: " + String.valueOf(matchingSurgeries))
        }
        
        matchingSurgeries.each
        {
            DetectionSurgery detectionSurgery = 
                new DetectionSurgery(surgery:it,
                                     detection:detection)
            detection.addToDetectionSurgeries(detectionSurgery)
            it.addToDetectionSurgeries(detectionSurgery)
            it.save()
        }
        
        detection.save()
        
        return detection
    }
    
    /**
     * It's either a Detection or SensorDetection, based on whether a sensor
     * value and unit is present in the parameters.
     */
    Detection createDetection(detectionParams) throws FileProcessingException
    {
        Float sensorValue = (detectionParams[SENSOR_VALUE_COLUMN] != null) ? Float.parseFloat(detectionParams[SENSOR_VALUE_COLUMN]) : null
        String sensorUnit = detectionParams[SENSOR_UNIT_COLUMN]
        
        Detection retDetection = null
        if (sensorValue && sensorUnit)
        {
            retDetection = new SensorDetection()
            retDetection.uncalibratedValue = sensorValue
            retDetection.sensorUnit = sensorUnit
        }
        else
        {
            retDetection = new Detection()
        }
        
        String dateString = detectionParams[DATE_AND_TIME_COLUMN] + " " + "UTC"
        log.debug("Parsing date string: " + dateString)
        Date detectionDate = new Date().parse(DATE_FORMAT, dateString)
        retDetection.timestamp = detectionDate
        
        retDetection.receiverName = detectionParams[RECEIVER_COLUMN]
        Receiver receiver = Receiver.findByCodeName(retDetection.receiverName)
        String errMsg = "Unknown receiver name: " + detectionParams[RECEIVER_COLUMN]
        //assert(receiver != null): errMsg
        if (receiver == null)
        {
            throw new FileProcessingException(errMsg)
        }
        
        // Find the appropriate receiver deployment (based on the timestamp of
        // the detection and the deployment/recovery timestamps.
        ReceiverDeployment deployment = findReceiverDeployment(receiver, detectionDate)
        retDetection.receiverDeployment = deployment

        String transmitterName = detectionParams[TRANSMITTER_NAME_COLUMN]
        retDetection.transmitterName = transmitterName
        
        String transmitterSerialNumber = detectionParams[TRANSMITTER_SERIAL_NUMBER_COLUMN]
        retDetection.transmitterSerialNumber = transmitterSerialNumber

        String stationName = detectionParams[STATION_NAME_COLUMN]
        retDetection.stationName = stationName

        Float lat = detectionParams[LATITUDE_COLUMN]
        Float lon = detectionParams[LONGITUDE_COLUMN]
        log.debug("Detection co-ordinates, lon: " + String.valueOf(lon) + ", lat: " + String.valueOf(lat))
        
        // Latitude and longitude are optional.
        if ((lat != null) && (lon != null))
        {
            Point location = new GeometryFactory().createPoint(new Coordinate(lon, lat))
            location.setSRID(4326)
            retDetection.location = location
            
            // Warn if not same as related installation station location.
            // TODO
        }
        
        return retDetection
    }

    /**
     * Find matching surgeries based on:
     * 
     * - tags matching Transmitter ID (which is itself is a concatenation of 
     *   the tag code map and ping code);
     * - releases which are ACTIVE
     * - tag window of operation
     * - tags which are not RETIRED 
     */
    List<Surgery> findMatchingSurgeries(detectionParams, detection)
    {
        // Transmitter ID (a.k.a. Tag) is combination of tag code map and ping ID.
        String transmitterID = detectionParams[TRANSMITTER_COLUMN]
        
        StringBuilder codeMapBuilder = new StringBuilder()
        StringBuilder pingIDBuilder = new StringBuilder()
        parseCodeMapAndPingID(transmitterID, codeMapBuilder, pingIDBuilder)

        log.debug("Searching for tags with codeMap = " + codeMapBuilder.toString() + " and ping ID = " + pingIDBuilder.toString() + "...")
        def tags = Tag.findAllByCodeMapAndPingCode(codeMapBuilder.toString(), pingIDBuilder.toString())
        
        // Also include parent tags of any matching sensors.
        // Note: the surgery is recorded as between an animal and a tag, not for each sensor.
        def sensors = Sensor.findAllByCodeMapAndPingCode(codeMapBuilder.toString(), pingIDBuilder.toString())
        tags.addAll(sensors*.tag)
        
        // Filter out retired tags.
        tags.grep(
        {
            if (it.status == DeviceStatus.findByStatus('RETIRED'))
            {
                log.warn("Detection matches retired tag: " + String.valueOf(it))
                return false
            }

            return true
        })
    
        // We now have a list of tags to go from.
        def surgeries = (tags*.surgeries).flatten()
        
        // Filter out based on release status and tag window of operation.
        surgeries.grep(
        {
            surgery ->
            
            if (surgery?.release?.status != AnimalReleaseStatus.ACTIVE)
            {
                return false
            }
            
            // Now check window of operation.
            if (!surgery?.tag?.expectedLifeTimeDays)
            {
                // Expected life time not specified (therefore there's no
                // window limit.
                return true
            }
            
            def startWindow = surgery?.release?.releaseDateTime
            if (new DateTime(detection.timestamp).isBefore(startWindow))
            {
                return false
            }
            
            def endWindow = startWindow.plusDays(surgery?.tag?.expectedLifeTimeDays).plusDays(grailsApplication.config.tag.expectedLifeTime.gracePeriodDays)
            if (new DateTime(detection.timestamp).isAfter(endWindow))
            {
                return false
            }
            
            return true    
        })
        
        return surgeries
    }
   
    void parseCodeMapAndPingID(transmitterID, codeMapBuilder, pingIDBuilder) throws FileProcessingException
    {
        if (transmitterID.length() < 3)
        {
            throw new FileProcessingException("Invalid transmitter ID, must be at least 3 characters long: " + transmitterID)
        }
        
        // Search backwards in the string as the ping code will always be the
        // last token.
        int index = transmitterID.length() - 1
        boolean found = false
        while (!found && (index >= 0))
        {
            if (transmitterID.charAt(index) == TRANSMITTER_ID_DELIM)
            {
                found = true
            }
            else
            {
                index--;
            }
        }
        
        if (!found)
        {
            throw new FileProcessingException("Invalid transmitter ID, no delimiter: " + transmitterID)
        }
        
        pingIDBuilder.append(transmitterID.substring(index + 1, transmitterID.length()))
        codeMapBuilder.append(transmitterID.substring(0, index))
    }
    
    ReceiverDeployment findReceiverDeployment(receiver, detectionDate)
    {
        List<ReceiverDeployment> deployments = receiver.deployments.grep(
        {
            // Check that the receiver was deployed before the detection and
            // that there is a valid recovery and that the recovery date is
            // after the detection.
            if (   (it.deploymentDateTime.toDate() <= detectionDate)
                && (it?.recovery.recoveryDateTime?.toDate() >= detectionDate))
            {
                return true
            }
            else
            {
                return false
            }
        }).sort{it.deploymentDateTime}
        
        // There should be one and only one matching deployment.
        if (deployments.size() != 1)
        {
            log.warn("There are not exactly one matching deployment for receiver: " + receiver + ", detection date: " + detectionDate)
        }
        
        return deployments?.first()
    }
}
