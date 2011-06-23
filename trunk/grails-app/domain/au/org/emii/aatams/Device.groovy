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
    
    /**
     * Date when data from this device is no longer embargoed (may be null to
     * indicate that no embargo exists).
     * 
     * Note: this field is not actually used in any business logic.
     */
    Date embargoDate
    DeviceStatus status
    
    static constraints =
    {
        codeName(blank:false)
        serialNumber(blank:false)
        embargoDate(nullable:true)
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
