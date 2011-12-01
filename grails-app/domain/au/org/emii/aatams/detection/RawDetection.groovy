package au.org.emii.aatams.detection

import au.org.emii.aatams.ReceiverDownloadFile
import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.*
import java.text.ParseException

/**
 * A 1:1 mapping of a single CSV record from receiver export.
 */
class RawDetection 
{
    /**
     * UTC timestamp.
     */
    Date timestamp
    
    String receiverName
    
    String stationName
    
    /**
     * Record the actual ID transmitted by the tag.
     */
    String transmitterId
    
    /**
     * May be different (as with station name above).
     */
    String transmitterName
    
    String transmitterSerialNumber
    
    /**
     * May be different (as with station name above).
     */
    Point location

    Float sensorValue
    String sensorUnit
    
    static transients = ['scrambledLocation', 'valid']
    
    static constraints = 
    {
        transmitterName(nullable:true, blank:true)
        transmitterSerialNumber(nullable:true, blank:true)
        sensorValue(nullable:true)
        sensorUnit(nullable:true, blank:true)
        stationName(nullable:true, blank:true)
        location(nullable:true)
		
		// Workaround for problem where jenkins build is failing - not sure why.
		// Getting "ValidationException" when trying to save ValidDetection.
		scrambledLocation(nullable:true)
    }

    static belongsTo = [receiverDownload:ReceiverDownloadFile]
    
    static mapping =
    {
        timestamp index:'timestamp_index'
        cache true
    }
    
    static searchable =
    {
        root(false)
    }
    
    boolean isValid()
    {
        return true
    }
    
    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
}
