package au.org.emii.aatams

/**
 * Represents a single sensor belonging to a tag (there can be more than one
 * sensor on each tag).
 */
class Sensor implements Embargoable
{
    static belongsTo = [tag:Tag]
	
	static hasMany = [detectionSurgeries:DetectionSurgery]
	static transients = ['project', 'codeName', 'codeMap', 'status']
	
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
	
	/**
	 * Concatenation of code map and ping code, e.g. "A69-9002-1234".
	 */
	String transmitterId

    static constraints =
    {
        tag()
        pingCode()
		transmitterType()
        unit(nullable:true)
        slope(nullable:true)
        intercept(nullable:true)
		transmitterId(nullable:true)
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

	void setPingCode(Integer pingCode)
	{
		this.pingCode = pingCode
		refreshTransmitterId()
	}
	
	void setTag(Tag tag)
	{
		this.tag = tag
		refreshTransmitterId()
	}
	
	private void refreshTransmitterId()
	{
		transmitterId = codeMap?.codeMap + '-' + pingCode
	}
	
	String getCodeName()
	{
		return transmitterId
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
		return transmitterId
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
