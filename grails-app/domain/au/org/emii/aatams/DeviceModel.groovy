package au.org.emii.aatams

class DeviceModel 
{
    String modelName;
    DeviceManufacturer manufacturer;
    
    String toString()
    {
        return manufacturer.toString() + " " + modelName
    }
}
