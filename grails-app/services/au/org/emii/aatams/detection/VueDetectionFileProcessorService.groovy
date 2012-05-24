package au.org.emii.aatams.detection

import au.org.emii.aatams.FileProcessingException;
import au.org.emii.aatams.Person;
import au.org.emii.aatams.ReceiverDownloadFile;
import au.org.emii.aatams.Sensor;

import au.org.emii.aatams.*

import org.springframework.context.i18n.LocaleContextHolder as LCH

/**
 * Process a file downloaded from receiver's VUE application.
 */
class VueDetectionFileProcessorService extends AbstractBatchProcessor
{
    def detectionFactoryService
	def detectionNotificationService
	
    static transactional = true
	
    void processSingleRecord(downloadFile, map)
    {
        detectionFactoryService.newDetection(downloadFile, map)
    }
	
	void process(ReceiverDownloadFile downloadFile) throws FileProcessingException
	{
		super.process(downloadFile)
		
		detectionNotificationService.sendDetectionNotificationEmails(downloadFile)
	}
}
