package au.org.emii.aatams

class Tag extends Device implements Embargoable
{
    List<Surgery> surgeries = new ArrayList<Surgery>()
    
    static hasMany = [sensors:Sensor, 
                      surgeries:Surgery, 
                      detectionSurgeries:DetectionSurgery]

    Project project
    String codeMap
    Integer pingCode

    TransmitterType transmitterType
    
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
        codeMap(blank:false)
        pingCode(unique:true)
        transmitterType()
        expectedLifeTimeDays(nullable:true)
    }
    
    static transients = ['codeMapPingCode', 'expectedLifeTimeDaysAsString']
    
    static searchable = true
    
    String toString()
    {
        return getCodeMapPingCode()
    }
    
    /**
     * The ID dynamically constructed from Device's properties.
     */
    String getCodeMapPingCode()
    {
        return codeMap + "-" + String.valueOf(pingCode)
    }
    
    static String constructCodeName(params)
    {
        return params.codeMap + "-" + params.pingCode
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
    
    Embargoable applyEmbargo()
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
