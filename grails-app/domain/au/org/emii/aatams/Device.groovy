package au.org.emii.aatams

/**
 * Common base class for receivers and tags.
 */
abstract class Device 
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
        
    }
    
    String toString()
    {
        return getID()
    }
    
    /**
     * The ID dynamically constructed from Device's properties.
     */
    String getID()
    {
        return model.toString() + " - " + serialNumber
    }
}
