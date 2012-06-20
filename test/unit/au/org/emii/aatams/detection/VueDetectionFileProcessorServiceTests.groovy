package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import grails.test.*
import grails.plugin.searchable.*

import com.vividsolutions.jts.geom.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import org.springframework.context.support.DelegatingMessageSource;

class VueDetectionFileProcessorServiceTests extends AbstractVueDetectionFileProcessorServiceTests 
{
	def detectionNotificationService
	
	protected void setUp() 
    {
        super.setUp()
        
		mockLogging(DetectionFactoryService, true)
		detectionFactoryService = new DetectionFactoryService()
		mockLogging(DetectionValidator, true)
		
		mockLogging(VueDetectionFileProcessorService, true)
		
		vueDetectionFileProcessorService = new VueDetectionFileProcessorService()
		vueDetectionFileProcessorService.detectionFactoryService = detectionFactoryService
		
		detectionNotificationService = new DetectionNotificationService()
		vueDetectionFileProcessorService.detectionNotificationService = detectionNotificationService
		vueDetectionFileProcessorService.searchableService = searchableService
		vueDetectionFileProcessorService.metaClass.getReader = { getReader(it) }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

	void testProcess()
    {
        ReceiverDownloadFile download = new ReceiverDownloadFile()
        mockDomain(ReceiverDownloadFile, [download])
        download.save()

		boolean notificationEmailsSent = false
		detectionNotificationService.metaClass.sendDetectionNotificationEmails = 
		{
			downloadFile -> 
			
			notificationEmailsSent = true 
		}
		
        vueDetectionFileProcessorService.process(download)
		assertTrue(notificationEmailsSent)

        assertEquals(vueDetectionFileProcessorService.getRecords(download).size(), ValidDetection.count() + InvalidDetection.count())
        
		assertEquals(2, ValidDetection.count())
        assertEquals(5, InvalidDetection.count())
            
        assertEquals(0, InvalidDetection.findAllByReason(InvalidDetectionReason.DUPLICATE).size())
        assertEquals(3, InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER).size())
		
		assertTrue(InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER)*.receiverName.contains("AAA-111"))
		assertTrue(InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER)*.receiverName.contains("BBB-111"))
		assertEquals(2, InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER)*.receiverName.unique().size())

		assertEquals(1 ,InvalidDetection.findAllByReason(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME).size())
		assertEquals(1 ,InvalidDetection.findAllByReason(InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME).size())
    }
}
