package au.org.emii.aatams.detection

import au.org.emii.aatams.DetectionSurgery;

class JdbcTemplateDetectionFactoryService extends DetectionFactoryService 
{
	protected def createValidDetection(params)
	{
		return (params 
			    + ["valid": true,
				   "clazz": "au.org.emii.aatams.detection.ValidDetection", 
				   "receiverDownloadId": params.receiverDownload.id,
				   "receiverDeploymentId": params.receiverDeployment.id,
				   "message": "",
				   "reason": "",
				   "detectionSurgeries": new ArrayList()])
	}
	
	protected def createInvalidDetection(params)
	{
		return (params 
			    + ["valid": false,
				   "clazz": "au.org.emii.aatams.detection.InvalidDetection", 
				   "receiverDownloadId": params.receiverDownload.id,
				   "detectionSurgeries": new ArrayList()])
	}
	
	protected def createDetectionSurgery(surgery, tag, detection)
	{
		def detectionSurgery = 
			 [surgeryId:surgery.id,
			  tagId:tag.id]
			
		detection["detectionSurgeries"].add(detectionSurgery) 
		return detectionSurgery	 
	}
}
