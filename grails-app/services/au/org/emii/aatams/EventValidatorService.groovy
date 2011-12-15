package au.org.emii.aatams

class EventValidatorService extends VueExportValidatorService 
{
	protected boolean isEventBeforeDeploymentDateTime(theDeployment)
	{
		assert(theDeployment)
		
		if (!theDeployment.initialisationDateTime)
		{
			log.debug("Receiver deployment does not have configured initialisation date/time, deployment: " + theDeployment)
			return true
		}
		
		return theDeployment.initialisationDateTime?.toDate().after(params.timestamp)
	}
	
	protected boolean isDuplicate()
	{
		return ValidReceiverEvent.isDuplicate(params)
	}
}

