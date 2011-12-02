package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import grails.test.*
import grails.plugin.searchable.*

import com.vividsolutions.jts.geom.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

class VueDetectionFileProcessorServiceTests extends AbstractVueDetectionFileProcessorServiceTests 
{

    protected void setUp() 
    {
        super.setUp()
        
		mockLogging(DetectionFactoryService, true)
		detectionFactoryService = new DetectionFactoryService()
		mockLogging(DetectionValidatorService, true)
		detectionFactoryService.detectionValidatorService = new DetectionValidatorService()
		
		mockLogging(VueDetectionFileProcessorService, true)
		
		vueDetectionFileProcessorService = new VueDetectionFileProcessorService()
		vueDetectionFileProcessorService.detectionFactoryService = detectionFactoryService
		vueDetectionFileProcessorService.searchableService = searchableService
		vueDetectionFileProcessorService.metaClass.getRecords = { getRecords(it) }
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

        vueDetectionFileProcessorService.process(download)
        
        assertEquals(getRecords(download).size(), ValidDetection.count() + InvalidDetection.count())
        
        assertEquals(1, ValidDetection.count())
        assertEquals(6, InvalidDetection.count())
            
        assertEquals(1, InvalidDetection.findAllByReason(InvalidDetectionReason.DUPLICATE).size())
        assertEquals(3, InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER).size())
		
		assertTrue(InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER)*.receiverName.contains("AAA"))
		assertTrue(InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER)*.receiverName.contains("BBB"))
		assertEquals(2, InvalidDetection.findAllByReason(InvalidDetectionReason.UNKNOWN_RECEIVER)*.receiverName.unique().size())

		assertEquals(1 ,InvalidDetection.findAllByReason(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME).size())
		assertEquals(1 ,InvalidDetection.findAllByReason(InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME).size())
    }
}
