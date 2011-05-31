package au.org.emii.aatams

class Tag extends Device
{
    static hasMany = [sensors:Sensor]
    
    String codeMap
    Integer pingCode

    static constraints =
    {
        codeMap(blank:false)
        pingCode()
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
        return codeMap + " - " + String.valueOf(pingCode)
    }
}
