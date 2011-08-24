package au.org.emii.aatams

class DeviceModel 
{
    static belongsTo = [manufacturer:DeviceManufacturer]
    String modelName;
    
    static constraints =
    {
        modelName(blank:false, unique:true)
        manufacturer()
    }
    
    String toString()
    {
        return modelName
    }
}
