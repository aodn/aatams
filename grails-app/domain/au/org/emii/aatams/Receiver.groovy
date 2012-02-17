package au.org.emii.aatams

import au.org.emii.aatams.detection.RawDetection
import org.joda.time.DateTime

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
    static transients = ['name', 'deviceID', 'status', 'currentRecovery']
	
    static mapping = 
    {
        organisation sort:'name'
		cache true
		detections cache:true
    }
    
    static searchable = [only: ['name']]
    
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
	
	private boolean hasActiveDeployment(dateTime)
	{
		def activeDeployments = deployments.findAll { it.isActive(dateTime) }
		return !activeDeployments.isEmpty() 
	}
	
	private ReceiverRecovery getCurrentRecovery(dateTime)
	{
		if (hasActiveDeployment(dateTime))
		{
			return null
		}
		
		def recoveries = deployments*.recovery
		recoveries?.removeAll
		{
			(it == null) || it.recoveryDateTime.isAfter(dateTime)
		}

		recoveries = recoveries?.sort
		{
			a, b ->
			
			a.recoveryDateTime <=> b.recoveryDateTime
		}
		
		if (recoveries && !recoveries.isEmpty())
		{
			return recoveries.last()
		}
		
		return null
	}

	DeviceStatus getStatus(DateTime dateTime)
	{
		if (hasActiveDeployment(dateTime))
		{
			return DeviceStatus.findByStatus(DeviceStatus.DEPLOYED)
		}
		else
		{
			def currentRecovery = getCurrentRecovery(dateTime)
			if (currentRecovery)
			{
				return currentRecovery.status
			}
		}
		
		return DeviceStatus.findByStatus(DeviceStatus.NEW, [cache:true])
	}
	
	DeviceStatus getStatus()
	{
		return getStatus(now())
	}
	
	private DateTime now()
	{
		return new DateTime()
	}

	DeviceStatus getStatus(ReceiverDeployment deployment)
	{
		if (deployments*.id?.contains(deployment.id))
		{
			try
			{
				removeFromDeployments(deployment)
				return getStatus(deployment.deploymentDateTime)
			}
			finally
			{
				addToDeployments(deployment)
			}
		}
		
		return getStatus(deployment.deploymentDateTime)
	}

	boolean canDeploy(deployment)
	{
		if (deployments*.id?.contains(deployment.id))
		{
			try
			{
				removeFromDeployments(deployment)
				return canDeployAtTime(deployment.deploymentDateTime)
			}
			finally
			{
				addToDeployments(deployment)
			}
		}
		
		return canDeployAtTime(deployment.deploymentDateTime)
	}
	
    boolean canDeployAtTime(dateTime)
    {
        DeviceStatus deployedStatus = DeviceStatus.findByStatus('DEPLOYED')
        DeviceStatus retiredStatus = DeviceStatus.findByStatus('RETIRED')
        
		log.debug("Status: " + getStatus(dateTime) + ", at time: " + dateTime)
        if ([deployedStatus, retiredStatus].contains(getStatus(dateTime)))
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
