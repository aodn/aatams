package au.org.emii.aatams

class JdbcTemplateEventFactoryService extends EventFactoryService 
{
	protected def createEvent(downloadFile, deployment, eventDate, eventParams)
	{
		return ["data": eventParams[DATA_COLUMN],
				"description" : eventParams[DESCRIPTION_COLUMN],
				"receiverDeploymentId": deployment.id,
				"receiverDownloadId": downloadFile.id,
				"timestamp": eventDate,
				"units": eventParams[UNITS_COLUMN]]
	}
}
