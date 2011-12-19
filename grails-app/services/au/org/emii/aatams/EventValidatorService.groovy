package au.org.emii.aatams

class EventValidatorService extends VueExportValidatorService 
{
	protected boolean isEventBeforeDeploymentDateTime(theDeployment)
	{
		assert(theDeployment)
		
		// Use the intialisation date/time, if it's configured.
		def comparisonDateTime
		if (theDeployment.initialisationDateTime)
		{
			comparisonDateTime = theDeployment.initialisationDateTime
		}
		else
		{
			log.debug("Receiver deployment does not have configured initialisation date/time, deployment: " + theDeployment)
			comparisonDateTime = theDeployment.deploymentDateTime
		}
		
		assert(comparisonDateTime)
		return comparisonDateTime?.toDate().after(params.timestamp)
	}
	
	protected boolean isDuplicate()
	{
		return ValidReceiverEvent.isDuplicate(params)
	}
}

