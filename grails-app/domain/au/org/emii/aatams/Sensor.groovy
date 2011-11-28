package au.org.emii.aatams

/**
 * Represents a single sensor belonging to a tag (there can be more than one
 * sensor on each tag).
 * 
 * Sensors are themselves modelled as tags, as that's how they appear to users
 * (i.e. as separate tags) even though they physically belong to a Tag.
 */
class Sensor extends Tag
{
    static belongsTo = [tag:Tag]
    
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
        unit(nullable:true)
        slope(nullable:true)
        intercept(nullable:true)
    }
	
	static mapping = 
	{
		cache true
	}
}
