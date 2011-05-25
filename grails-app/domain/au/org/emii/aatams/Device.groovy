package au.org.emii.aatams

/**
 * Common base class for receivers and tags.
 */
abstract class Device 
{
    String codeName
    DeviceModel model
    String serialNumber
    Project project
    
    /**
     * Date when data from this device is no longer embargoed (may be null to
     * indicate that no embargo exists).
     */
    Date embargoDate
    DeviceStatus status
    
    static constraints = 
    {
        
    }
    
    String toString()
    {
        return model.toString() + ": " + serialNumber
    }
}
