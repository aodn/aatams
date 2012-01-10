package au.org.emii.aatams

/**
 * Represents a single sensor belonging to a tag (there can be more than one
 * sensor on each tag).
 */
class Sensor extends Tag
{
    static belongsTo = [tag:Tag]
	
	static hasMany = [detectionSurgeries:DetectionSurgery]

	Integer pingCode
	TransmitterType transmitterType

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
        pingCode()
		transmitterType()
        unit(nullable:true)
        slope(nullable:true)
        intercept(nullable:true)
    }
	
	static mapping = 
	{
		cache true
	}
	
	static searchable =
	{
		tag(component:true)
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
		return String.valueOf(tag.codeMap) + "-" + pingCode
	}
}
