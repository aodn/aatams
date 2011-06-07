package au.org.emii.aatams

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
    Date timestamp
    
    static belongsTo = [receiver: Receiver]
    
    /**
     * This is modelled as a one-to-many relationship, due to the fact that tags
     * transmit only code map and ping ID which is not guaranteed to be unique
     * between manufacturers, although in reality the relationship will *usually*
     * be one-to-one.
     */
    static hasMany = [tags:Tag]
    
    /**
     * Station name, as recorded in VUE database (this should match 
     * this.receiver.station.name, but *may* be different, so it is 
     * recorded here also for completeness).
     */
    String stationName;
    
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
        timestamp(max:new Date())
        receiver()
        transmitterName(nullable:true, blank:true)
        transmitterSerialNumber(nullable:true, blank:true)
        stationName(nullable:true, blank:true)
        location(nullable:true)
    }
    
    String toString()
    {
        return timestamp.toString() + " " + receiver.toString()
    }
}
