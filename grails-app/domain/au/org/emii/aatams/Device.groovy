package au.org.emii.aatams

/**
 * Common base class for receivers and tags.
 * TODO: this should be abstract but this is causing "unmapped" exceptions
 * with hibernate.
 */
class Device 
{
    String codeName
    
    DeviceModel model       
    String serialNumber
    static belongsTo = [project: Project]
    
    DeviceStatus status
    
    static constraints =
    {
        codeName(blank:false)
        serialNumber(blank:false)
        status()
    }
    
    static transients = ['deviceID']
    
    String toString()
    {
        return getDeviceID()
    }
    
    /**
     * The ID dynamically constructed from Device's properties.
     */
    String getDeviceID()
    {
        return codeName
    }
}
