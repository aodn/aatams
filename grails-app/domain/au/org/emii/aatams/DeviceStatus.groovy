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
    static final String NEW = "NEW"
    static final String DEPLOYED = "DEPLOYED"
    static final String RETIRED = "RETIRED"
    
    String status
    
    static constraints =
    {
        status(blank:false, unique:true)
    }
	
	static mapping =
	{
		cache true
	}
    
    String toString()
    {
        return status
    }
	
	static def listRecoveryStatuses()
	{
		return list() - DeviceStatus.findByStatus(NEW) - DeviceStatus.findByStatus(DEPLOYED)
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.is(obj))
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceStatus other = (DeviceStatus) obj;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
}
