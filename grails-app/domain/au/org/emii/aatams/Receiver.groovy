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
    
    static mapping = 
    {
        organisation sort:'name'
    }
    
    static String constructCodeName(params)
    {
        DeviceModel model = DeviceModel.get(params.model.id)
        return String.valueOf(model) + "-" + params.serialNumber
    }
}
