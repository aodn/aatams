package au.org.emii.aatams.detection

import au.org.emii.aatams.DetectionSurgery;

class JdbcTemplateDetectionFactoryService extends DetectionFactoryService 
{
	// TODO: use hibernate sequence
	static count = System.currentTimeMillis()
	
	protected def createDetectionSurgery(surgery, tag, detection)
	{
		return
			 [surgeryId:surgery.id,
			  tagId:tag.id,
			  detectionId: 1]//detection.id]
	}
	
	protected def createValidDetection(params)
	{
		return (params 
			    + ["valid": true,
				   "clazz": "au.org.emii.aatams.detection.ValidDetection", 
				   "receiverDownloadId": params.receiverDownload.id,
				   "receiverDeploymentId": params.receiverDeployment.id,
				   "message": "",
				   "reason": ""])
	}
	
	protected def createInvalidDetection(params)
	{
//		return new InvalidDetection(params)
		return (params 
			    + ["valid": false,
				   "clazz": "au.org.emii.aatams.detection.InvalidDetection", 
				   "receiverDownloadId": "",
				   "receiverDownloadId": params.receiverDownload.id
				   ])
	}
}
