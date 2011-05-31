package au.org.emii.aatams

/**
 * Status of the device. e.g. NEW, DEPLOYED, RECOVERED, RETIRED, LOST, STOLEN,
 * DAMAGED.
 *
 * Devices start off as NEW and go to DEPLOYED automatically.  The statues which
 * follow are set by the user/upload as part of device recovery process.
 */
class DeviceStatus
{
    String status
    
    static constraints =
    {
        status(blank:false, unique:true)
    }
    
    String toString()
    {
        return status
    }
}
