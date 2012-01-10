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
    Float slope
    
    /**
     * Calibration intercept.
     */
    Float intercept

    static constraints =
    {
        tag()
        codeMap(nullable:true)
        pingCode(min:0)
        transmitterType()
        unit(nullable:true)
        slope(nullable:true)
        intercept(nullable:true)
		status()
    }
	
	static mapping = 
	{
		cache true
	}
	
	DeviceStatus getStatus()
	{
		return tag?.getStatus()
	}
	
	CodeMap getCodeMap()
	{
		return tag?.getCodeMap()
	}
	
	String toString()
	{
		return codeName
	}
}
