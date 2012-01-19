package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Detection validation utilities.
 * 
 * Stores some state to minimise queries.
 * 
 * @author jburgess
 */
class DetectionValidatorService extends VueExportValidatorService
{
	protected boolean isEventBeforeDeploymentDateTime(theDeployment)
	{
		assert(theDeployment)
		
		return theDeployment.deploymentDateTime.toDate().after(params.timestamp)
	}
	
	protected boolean isDuplicate()
	{
		return false
		// This is now checked at the end of each batch in JdbcTemplateVueDetectionFileProcessorService.
//		return ValidDetection.isDuplicate(params)
	}
}

