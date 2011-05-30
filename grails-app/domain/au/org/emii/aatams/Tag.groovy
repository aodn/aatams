package au.org.emii.aatams

class Tag extends Device
{
    static hasMany = [sensors:Sensor]
    
    String codeMap
    Integer pingCode

}
