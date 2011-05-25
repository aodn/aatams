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
     * Calibration slope.
     */
    Integer slope
    
    /**
     * Calibration intercept.
     */
    Integer intercept
    
}
