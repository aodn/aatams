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
    
    DeviceStatus status
    
    String comment
    
    static constraints =
    {
        codeName(blank:false)
        serialNumber(blank:false, unique:true)
        status()
        comment(nullable:true, blank:true)
    }
    
    static mapping =
    {
        codeName index:'code_name_index'
        status index:'status_index'
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
