package au.org.emii.aatams

/**
 * Common base class for receivers and tags.
 * TODO: this should be abstract but this is causing "unmapped" exceptions
 * with hibernate.
 */
class Device 
{
    DeviceModel model       
    String serialNumber
    
    DeviceStatus status
    
    String comment
    
    static constraints =
    {
        serialNumber(blank:false, unique:true)
        status()
        comment(nullable:true, blank:true)
    }
    
    static mapping =
    {
        cache true
        codeName index:'code_name_index'
        status index:'device_status_index'
    }
    
    static transients = ['deviceID']
    
    String getComment()
    {
        if (!comment)
        {
            return ""
        }
        
        return comment
    }
}
