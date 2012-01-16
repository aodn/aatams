package au.org.emii.aatams

import au.org.emii.aatams.detection.RawDetection

/**
 * A receiver is a device deployed in the ocean that is able to receive 'ping'
 * transmissions from tags attached or surgically implanted into fish.
 */
class Receiver extends Device
{
    /**
     * Detections recorded at the receiver.
     */
    Set<RawDetection> detections = new HashSet<RawDetection>()
    static hasMany = [detections: RawDetection, deployments: ReceiverDeployment]
    static belongsTo = [organisation: Organisation]
    static transients = ['name', 'deviceID']
	
    static mapping = 
    {
        organisation sort:'name'
		cache true
		detections cache:true
    }
    
    static searchable = true
    
	String getName()
	{
		return String.valueOf(model) + "-" + serialNumber
	}
	
	String getDeviceID()
	{
		return getName()
	}
	
	String toString()
	{
		return getName()
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
	
	/**
	 * Backward compatibility.
	 */
	static Receiver findByName(name, params=[:])
	{
		def tokens = name.tokenize("-")
		assert(tokens.size() == 2): "Invalid receiver name: " + name
		
		return Receiver.findByModelAndSerialNumber(ReceiverDeviceModel.findByModelName(tokens[0], params), 
												   tokens[1], params)
	}
}
