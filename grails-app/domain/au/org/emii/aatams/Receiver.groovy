package au.org.emii.aatams

/**
 * A receiver is a device deployed in the ocean that is able to receive 'ping'
 * transmissions from tags attached or surgically implanted into fish.
 */
class Receiver extends Device
{
    /**
     * Detection recorded at the receiver (may also include SensorDetections).
     */
    static hasMany = [detections: Detection, deployments: ReceiverDeployment]
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
