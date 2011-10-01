package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import grails.test.*

import com.vividsolutions.jts.geom.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

class VueDetectionFileProcessorServiceTests extends GrailsUnitTestCase 
{
    def detectionFactoryService
    def vueDetectionFileProcessorService
    
    Receiver receiver
    ReceiverDeployment deployment
    ReceiverRecovery recovery

    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(DetectionFactoryService, true)
        detectionFactoryService = new DetectionFactoryService()
        mockLogging(VueDetectionFileProcessorService, true)
        vueDetectionFileProcessorService = new VueDetectionFileProcessorService()
        vueDetectionFileProcessorService.detectionFactoryService = detectionFactoryService
        vueDetectionFileProcessorService.metaClass.getRecords = { getRecords(it) }
       
        mockLogging(Surgery)
        
        mockDomain(RawDetection)
        mockDomain(InvalidDetection)
        mockDomain(ValidDetection)
        
        mockDomain(DetectionSurgery)
        mockDomain(DeviceStatus)
        mockDomain(Sensor)
        mockDomain(Surgery)
        mockDomain(Tag)
        
        receiver = new Receiver(codeName:"VR3UWM-354")
        mockDomain(Receiver, [receiver])
        
        deployment = new ReceiverDeployment(receiver:receiver,
                                            deploymentDateTime:new DateTime("2008-12-08T06:44:24"))
                                        
        receiver.addToDeployments(deployment)
        mockDomain(ReceiverDeployment, [deployment])

        recovery = new ReceiverRecovery(deployment:deployment)
        mockDomain(ReceiverRecovery, [recovery])
        recovery.save()
        
        receiver.save()
        
        deployment.recovery = recovery
        deployment.save()
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
        
        assertEquals(getRecords(download).size(), download.detections.size())
        assertEquals(2, download.detections.size())
        assertEquals(1, download.numDuplicates())
    }
    
    List<Map<String, String>> getRecords(downloadFile)
    {
        def standardParams = 
                [(DetectionFactoryService.DATE_AND_TIME_COLUMN):"2009-12-08 06:44:24",
                 (DetectionFactoryService.RECEIVER_COLUMN):"VR3UWM-354",
                 (DetectionFactoryService.TRANSMITTER_COLUMN):"A69-1303-62347",
                 (DetectionFactoryService.TRANSMITTER_NAME_COLUMN):"shark tag",
                 (DetectionFactoryService.TRANSMITTER_SERIAL_NUMBER_COLUMN):"1234",
                 (DetectionFactoryService.STATION_NAME_COLUMN):"Neptune SW 1",
                 (DetectionFactoryService.LATITUDE_COLUMN):-40.1234f,
                 (DetectionFactoryService.LONGITUDE_COLUMN):45.1234f]   
             
        def validDetection = [:] + standardParams
        def duplicateDetection = [:] + validDetection
        
        return [validDetection, duplicateDetection]
    }
}
