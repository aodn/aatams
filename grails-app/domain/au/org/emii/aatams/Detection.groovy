package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point

/**
 * Tag detections are received and stored on a receiver.  Each detection 
 * represents a valid 'ping' signal from a tag attached to an animal, which may
 * include information from one or two sensors on the tag.  Sensor information
 * can include temperature, depth, acceleration, pH and potentially other
 * readings in the future.
 */
class Detection 
{
    /**
     * UTC timestamp.
     */
    Date timestamp
    
    static belongsTo = [receiverDeployment: ReceiverDeployment]
    static transients = ['project', 'scrambledLocation']
    
    /**
     * This is modelled as a many-to-many relationship, due to the fact that tags
     * transmit only code map and ping ID which is not guaranteed to be unique
     * between manufacturers, although in reality the relationship will *usually*
     * be one-to-one.
     * 
     * Additionally, the relationship is modelled via surgery, due to the fact
     * that a tag could potentially be reused on several animals.
     */
    // Note: initialise with empty list so that detectionSurgeries.isEmpty()
    // returns true (I thought that the initialisation should happen when save()
    // is called but apparently not.
    List<DetectionSurgery> detectionSurgeries = new ArrayList<DetectionSurgery>()
    static hasMany = [detectionSurgeries:DetectionSurgery]
    
    String receiverName;
    
    /**
     * Station name, as recorded in VUE database (this should match 
     * this.receiver.station.name, but *may* be different, so it is 
     * recorded here also for completeness).
     */
    String stationName;
    
    /**
     * Record the actual ID transmitted by the tag.
     */
    String transmitterId;
    
    /**
     * May be different (as with station name above).
     */
    String transmitterName;
    
    String transmitterSerialNumber;
    
    /**
     * May be different (as with station name above).
     */
    Point location;
    
    static constraints = 
    {
        timestamp()
        receiverDeployment()
        transmitterName(nullable:true, blank:true)
        transmitterSerialNumber(nullable:true, blank:true)
        stationName(nullable:true, blank:true)
        location(nullable:true)
    }
    
    static mapping =
    {
        timestamp index:'timestamp_index'
    }
    
    String toString()
    {
        return timestamp.toString() + " " + String.valueOf(receiverDeployment?.receiver)
    }
    
    // Convenience method.
    Project getProject()
    {
        return receiverDeployment?.station?.installation?.project
    }
    
    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
    
    boolean similar(other)
    {
        return \
           (timestamp == other?.timestamp) \
        && (receiverDeployment.id == other?.receiverDeployment.id) \
        && (receiverName == other?.receiverName) \
        && (stationName == other?.stationName) \
        && (transmitterId == other?.transmitterId) \
        && (transmitterName == other?.transmitterName) \
        && (transmitterSerialNumber == other?.transmitterSerialNumber) \
        && (location == other?.location)
    }
}
