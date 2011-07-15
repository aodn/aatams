package au.org.emii.aatams

class Tag extends Device
{
    static hasMany = [sensors:Sensor, surgeries:Surgery]
    
    String codeMap
    Integer pingCode

    TransmitterType transmitterType

    static constraints =
    {
        codeMap(blank:false)
        pingCode()
        transmitterType()
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
