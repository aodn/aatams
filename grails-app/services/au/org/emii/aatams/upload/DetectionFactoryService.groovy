package au.org.emii.aatams.upload

import au.org.emii.aatams.Detection
import au.org.emii.aatams.Receiver
import au.org.emii.aatams.Sensor
import au.org.emii.aatams.SensorDetection
import au.org.emii.aatams.Tag

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

class DetectionFactoryService 
{

    static transactional = true

    static final String DATE_AND_TIME_COLUMN = "ÔªøDate and Time (UTC)"
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
    
    /**
     * Creates a detection given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
    Detection newDetection(detectionParams) throws FileProcessingException 
    {
        //
        // First, see if we can find matching tag(s) - if not try matching 
        // sensor(s) - to determine what kind of Detection this actually is.
        //
        
        // Transmitter ID (a.k.a. Tag) is combination of tag code map and ping ID.
        String transmitterID = detectionParams[TRANSMITTER_COLUMN]
        
        StringBuilder codeMapBuilder = new StringBuilder()
        StringBuilder pingIDBuilder = new StringBuilder()
        parseCodeMapAndPingID(transmitterID, codeMapBuilder, pingIDBuilder)

        def tags = Tag.findAllByCodeMapAndPingCode(codeMapBuilder.toString(), pingIDBuilder.toString())

        Detection retDetection = null
        
        if (tags.size() == 0)
        {
            // No matching tag - it must be a "SensorDetection"
            def sensors = Sensor.findAllByCodeMapAndPingCode(codeMapBuilder.toString(), pingIDBuilder.toString())
            sensors.each
            {
                tags.add(${it}.tag)
            }
            assert (sensors.size() > 0) : "Detection must belong to at least one tag or sensor"
            
            Float sensorValue = detectionParams[SENSOR_VALUE_COLUMN]?: Float.parseFloat(detectionParams[SENSOR_VALUE_COLUMN])
            String sensorUnit = detectionParams[SENSOR_UNIT_COLUMN]
            
            SensorDetection sensorDetection = new SensorDetection()
            sensorDetection.addToSensors(sensors)
            retDetection = sensorDetection
        }
        else if (tags.size() == 1)
        {
            // The normal case.
            retDetection = new Detection()
        }
        else
        {
            // Extremely unlikely - but possible.
            log.warn("Multplie tags match identifier: " + transmitterID)
            retDetection = new Detection()
        }
        
        Date detectionDate = new Date().parse("d/M/yy H:m", detectionParams[DATE_AND_TIME_COLUMN])
        retDetection.timestamp = detectionDate
        
        Receiver receiver = Receiver.findByCodeName(detectionParams[RECEIVER_COLUMN])
        String errMsg = "Unknown receiver name: " + detectionParams[RECEIVER_COLUMN]
        assert(receiver != null): errMsg
        if (receiver == null)
        {
            throw new FileProcessingException(errMsg)
        }
        retDetection.receiver = receiver

        String transmitterName = detectionParams[TRANSMITTER_NAME_COLUMN]
        retDetection.transmitterName = transmitterName
        
        String transmitterSerialNumber = detectionParams[TRANSMITTER_SERIAL_NUMBER_COLUMN]
        retDetection.transmitterSerialNumber = transmitterSerialNumber

        Float lat = detectionParams[LATITUDE_COLUMN]
        Float lon = detectionParams[LONGITUDE_COLUMN]
        Point location = new GeometryFactory().createPoint(new Coordinate(lon, lat))
        location.setSRID(4326)
        retDetection.location = location
        
        // Warn if not same as related installation station location.
        // TODO
        
        return retDetection
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
            
            index--;
        }
        
        if (!found)
        {
            throw new FileProcessingException("Invalid transmitter ID, no delimiter: " + transmitterID)
        }
        
        pingIDBuilder.append(transmitterID.substring(index + 1, transmitterID.length()))
        codeMapBuilder.append(transmitterID.substring(0, index))
    }
}
