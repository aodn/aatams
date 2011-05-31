package au.org.emii.aatams

/**
 * Some tags may include one or more sensors, for which a code map and ping
 * ID combination is transmitted by the tag.  These transmissions are modelled
 * as a specialisation of Detection, as they also include a sensor value. 
 */
class SensorDetection extends Detection
{
    /**
     * TODO: may be this needs to be a 1:many relationship (since code map/ping
     * may not be enough to uniquely identify a sensor)?
     */
    static belongsTo = [sensor: Sensor]
    
    Float uncalibratedValue
    
    static constraints =
    {
        sensor()
        uncalibratedValue()
    }
    
    String toString()
    {
        return super.toString() + ", " + String.valueOf(uncalibratedValue) 
            + sensor.getUnit()
    }
}
