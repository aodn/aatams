package au.org.emii.aatams.detection

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import au.org.emii.aatams.*
import grails.test.*

class JdbcTemplateVueDetectionFileProcessorServiceTests extends AbstractVueDetectionFileProcessorServiceTests 
{
    protected void setUp() 
	{
        super.setUp()
        
		mockLogging(JdbcTemplateDetectionFactoryService, true)
		def jdbcTemplateDetectionFactoryService = new JdbcTemplateDetectionFactoryService()
		mockLogging(DetectionValidatorService, true)
		jdbcTemplateDetectionFactoryService.detectionValidatorService = new DetectionValidatorService()
		
		mockLogging(VueDetectionFileProcessorService, true)
		
		vueDetectionFileProcessorService = new JdbcTemplateVueDetectionFileProcessorService()
		vueDetectionFileProcessorService.jdbcTemplateDetectionFactoryService = jdbcTemplateDetectionFactoryService
		vueDetectionFileProcessorService.searchableService = searchableService
		vueDetectionFileProcessorService.metaClass.getRecords = { getRecords(it) }
		vueDetectionFileProcessorService.metaClass.insertDetections = { insertDetections() }
		vueDetectionFileProcessorService.metaClass.insertDetectionSurgeries = { insertDetectionSurgeries() }
		
		DeviceStatus status = new DeviceStatus(status: "DEPLOYED")
		mockDomain(DeviceStatus, [status])
		status.save()
		
		Tag tag = new Tag(codeName:"A69-1303-62347", status:status)
		mockDomain(Tag, [tag])
		
		AnimalRelease release = new AnimalRelease(releaseDateTime:new DateTime("2009-12-07T06:50:24"))
		
		Surgery surgery = new Surgery(tag:tag, timestamp:new DateTime("2009-12-07T06:50:24"), release:release)
		mockDomain(Surgery, [surgery])
		surgery.save()
		
		tag.addToSurgeries(surgery)
		tag.save()
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
    }
	
	List<Map<String, String>> getRecords(downloadFile)
	{
		def retList = super.getRecords(downloadFile) 
		
		retList.add([(DetectionFactoryService.DATE_AND_TIME_COLUMN):"2009-12-08 06:50:24",
				 (DetectionFactoryService.RECEIVER_COLUMN):"VR3UWM-354",
				 (DetectionFactoryService.TRANSMITTER_COLUMN):"A69-1303-62347",
				 (DetectionFactoryService.TRANSMITTER_NAME_COLUMN):"shark tag",
				 (DetectionFactoryService.TRANSMITTER_SERIAL_NUMBER_COLUMN):"1234",
				 (DetectionFactoryService.STATION_NAME_COLUMN):"Neptune SW 1",
				 (DetectionFactoryService.LATITUDE_COLUMN):-40.1234f,
				 (DetectionFactoryService.LONGITUDE_COLUMN):45.1234f])
		
		return retList
	}
	
	private void insertDetections()
	{
		assertEquals(8, vueDetectionFileProcessorService.detectionBatch.size())
		assertEquals(3, vueDetectionFileProcessorService.detectionSurgeryBatch.size())
	}

	private void insertDetectionSurgeries()
	{
	}
}
