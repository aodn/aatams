package au.org.emii.aatams.detection

import au.org.emii.aatams.*


/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueDetectionFileProcessorService extends AbstractBatchProcessor
{
    def detectionFactoryService
    
    static transactional = true
	
    void processSingleRecord(downloadFile, map)
    {
        Map newDetectionObjects = detectionFactoryService.newDetection(downloadFile, map)
		newDetectionObjects.each 
		{
			k, v -> 
			
			v.each { it.save() } 
		}
    }
}
