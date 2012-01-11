package au.org.emii.aatams

/**
 * Represents a physical tag (which may be attached at any one time to an animal via a surgery).
 * 
 * @author jburgess
 *
 */
class Tag extends Device implements Embargoable
{
    List<Surgery> surgeries = new ArrayList<Surgery>()
	List<Sensor> sensors = new ArrayList<Sensor>()
	
    static hasMany = [sensors:Sensor, 
                      surgeries:Surgery, 
                      detectionSurgeries:DetectionSurgery]

    Project project
	static belongsTo = [codeMap: CodeMap]
	
    /**
     * The expected lifetime (in days) of a tag once is it deployed.  This
     * value is used to derive the "window of operation" of a Surgery when 
     * searching for surgeries to match against detections.
     * 
     * If not specified, then assume infinity.
     */
    Integer expectedLifeTimeDays

    static constraints =
    {
        project(nullable:true)
        expectedLifeTimeDays(nullable:true)
    }
    
    static transients = ['expectedLifeTimeDaysAsString', 'deviceID', 'pingCodes', 'transmitterTypeNames']
    
    static searchable =
    {
        project(component:true)
    }
    
	static mapping =
	{
		cache true
		surgeries cache:true
		detectionSurgeries cache:true
	}
	
    // For reports...
    String getExpectedLifeTimeDaysAsString()
    {
        if (!expectedLifeTimeDays)
        {
            return ""
        }
        
        return String.valueOf(expectedLifeTimeDays)
    }
    
	String toString()
	{
		return getDeviceID()
	}
	
	String getDeviceID()
	{
		return removeSurroundingBrackets(String.valueOf(sensors*.toString()))
	}
	
	String getPingCodes()
	{
		return removeSurroundingBrackets(String.valueOf(sensors*.pingCode))
	}
	
	String getTransmitterTypeNames()
	{
		return removeSurroundingBrackets(String.valueOf(sensors*.transmitterType.transmitterTypeName))
	}

	private String removeSurroundingBrackets(listAsString)
	{
		return listAsString[1..listAsString.size() - 2]
	}
	
    def applyEmbargo()
    {
        boolean embargoed = false
        
        surgeries.each
        {
            embargoed |= it.release.isEmbargoed()
        }

        if (this instanceof Sensor)
        {
            tag.surgeries.each
            {
                embargoed |= it.release.isEmbargoed()
            }
        }

        if (!embargoed)
        {
            return this
        }
        
        log.debug("Tag is embargoed, id: " + id)
        return null
    }
}
