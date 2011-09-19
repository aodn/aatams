package au.org.emii.aatams

class Tag extends Device
{
    List<Surgery> surgeries = new ArrayList<Surgery>()
    
    static hasMany = [sensors:Sensor, 
                      surgeries:Surgery, 
                      detectionSurgeries:DetectionSurgery]
    static belongsTo = [project: Project]
    
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
        codeMap(blank:false)
        pingCode(unique:true)
        transmitterType()
        expectedLifeTimeDays(nullable:true)
    }
    
    static transients = ['codeMapPingCode']
    
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
}
