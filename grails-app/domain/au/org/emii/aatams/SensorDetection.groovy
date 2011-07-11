package au.org.emii.aatams

/**
 * Some tags may include one or more sensors, for which a code map and ping
 * ID combination is transmitted by the tag.  These transmissions are modelled
 * as a specialisation of Detection, as they also include a sensor value. 
 */
class SensorDetection extends Detection
{
    static hasMany = [sensors: Sensor]
    
    Float uncalibratedValue
    String sensorUnit
    
    static constraints =
    {
        timestamp()
        receiverDeployment()
        transmitterName(nullable:true, blank:true)
        transmitterSerialNumber(nullable:true, blank:true)
        stationName(nullable:true, blank:true)
        location(nullable:true)
        uncalibratedValue()
    }
    
    String toString()
    {
        return super.toString() + ", " + String.valueOf(uncalibratedValue) 
    }
}
