package au.org.emii.aatams

/**
 * Represents a single sensor belonging to a tag (there can be more than one
 * sensor on each tag).
 */
class Sensor 
{
    static belongsTo = [tag:Tag]
    
    String codeMap
    Integer pingCode
    TransmitterType transmitterType
    
    /**
     * Sensor units.
     */
    String unit
    
    /**
     * Calibration slope.
     */
    Integer slope
    
    /**
     * Calibration intercept.
     */
    Integer intercept

    static constraints =
    {
        tag()
        codeMap(blank:false)
        pingCode(min:0)
        transmitterType()
        unit(blank:false)
        slope()
        intercept()
    }
    
    String toString()
    {
        return codeMap + " - " + String.valueOf(pingCode)
    }
}
