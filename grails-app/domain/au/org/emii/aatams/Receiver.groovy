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
    
    static String constructCodeName(params)
    {
        DeviceModel model = DeviceModel.get(params.model.id)
        assert(model): "model cannot be null"
        
        return String.valueOf(model) + "-" + params.serialNumber
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
