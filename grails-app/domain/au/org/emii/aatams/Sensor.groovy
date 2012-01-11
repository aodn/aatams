package au.org.emii.aatams

/**
 * Represents a single sensor belonging to a tag (there can be more than one
 * sensor on each tag).
 */
class Sensor implements Embargoable
{
    static belongsTo = [tag:Tag]
	
	static hasMany = [detectionSurgeries:DetectionSurgery]
	static transients = ['transmitterId', 'project', 'codeName']
	
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
		pingCode index: 'ping_code_idx'
	}
	
	static searchable =
	{
		tag(component:true)
	}

	String getTransmitterId()
	{
		return tag?.codeMap?.codeMap + "-" + pingCode
	}
	
	String getCodeName()
	{
		return getTransmitterId()
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
		return getTransmitterId()
	}
	
	Project getProject()
	{
		return tag.project
	}
	
	def applyEmbargo()
	{
		if (tag.applyEmbargo())
		{
			return this
		}
		
		return null
	}
}
