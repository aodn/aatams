package au.org.emii.aatams

class JdbcTemplateEventFactoryService extends EventFactoryService 
{
	protected def createValidEvent(params)
	{
		return (params
				+ ["clazz" : "au.org.emii.aatams.ValidReceiverEvent",
				   "receiverDownloadId": params.receiverDownload.id,
				   "receiverDeploymentId": params.receiverDeployment.id,
				   "message": "",
				   "reason": ""])
	}
	
	protected def createInvalidEvent(params)
	{
		return (params
				+ ["clazz" : "au.org.emii.aatams.InvalidReceiverEvent",
				   "receiverDownloadId": params.receiverDownload.id])
	}
}
