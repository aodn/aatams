package au.org.emii.aatams

import au.org.emii.aatams.detection.RawDetection

/**
 * A receiver is a device deployed in the ocean that is able to receive 'ping'
 * transmissions from tags attached or surgically implanted into fish.
 */
class Receiver extends Device
{
    /**
     * Detection recorded at the receiver (may also include SensorDetections).
     */
    Set<RawDetection> detections = new HashSet<RawDetection>()
    static hasMany = [detections: RawDetection, deployments: ReceiverDeployment]
    static belongsTo = [organisation: Organisation]
    
    static mapping = 
    {
        organisation sort:'name'
		cache true
		detections cache:true
    }
    
    static searchable = true
    
	String toString()
	{
		return String.valueOf(model) + "-" + serialNumber
	}
	
    boolean canDeploy()
    {
        DeviceStatus deployedStatus = DeviceStatus.findByStatus('DEPLOYED')
        DeviceStatus retiredStatus = DeviceStatus.findByStatus('RETIRED')
        
        if ([deployedStatus, retiredStatus].contains(status))
        {
            return false
        }
        
        return true
    }
}
