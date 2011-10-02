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
        RawDetection detection = detectionFactoryService.newDetection(downloadFile, map)
        downloadFile.addToDetections(detection)
    }
}
