package au.org.emii.aatams

import au.org.emii.aatams.util.StringUtils

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
                      surgeries:Surgery]

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
    
    static transients = ['expectedLifeTimeDaysAsString', 'deviceID', 'pinger', 'pingCode', 'pingCodes', 'transmitterTypeNames', 'nonPingerSensors']
    
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
		return StringUtils.removeSurroundingBrackets(String.valueOf(sensors*.toString()))
	}
	
	Sensor getPinger()
	{
		def searchPinger = sensors.find
		{
			it.transmitterType == TransmitterType.findByTransmitterTypeName('PINGER', [cache:true])
		}
		return searchPinger
	}
	
	void setPingCode(Integer newPingCode)
	{
		if (!pinger)
		{
			Sensor newPinger = new Sensor(tag: this, pingCode: newPingCode, transmitterType: TransmitterType.findByTransmitterTypeName('PINGER', [cache:true]))
			addToSensors(newPinger)
		}
		else
		{
			assert(pinger)
			pinger.pingCode = newPingCode
		}	
	}
	
	Integer getPingCode()
	{
		return pinger?.pingCode
	}
	
	String getPingCodes()
	{
		return StringUtils.removeSurroundingBrackets(String.valueOf(sensors*.pingCode))
	}
	
	String getTransmitterTypeNames()
	{
		return StringUtils.removeSurroundingBrackets(String.valueOf(sensors*.transmitterType.transmitterTypeName))
	}

	List<Sensor> getNonPingerSensors()
	{
		def nonPingers = sensors.grep
		{
			it?.transmitterType != TransmitterType.findByTransmitterTypeName('PINGER', [cache:true])
		}
		
		return nonPingers	
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
