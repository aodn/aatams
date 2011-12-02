package au.org.emii.aatams.detection

import java.util.List;
import java.util.Map;

import au.org.emii.aatams.*
import grails.plugin.searchable.SearchableService
import grails.test.*
import org.joda.time.DateTime

abstract class AbstractVueDetectionFileProcessorServiceTests extends GrailsUnitTestCase 
{
    def detectionFactoryService
    def vueDetectionFileProcessorService
	
    def searchableService
    
    Receiver receiver
    ReceiverDeployment deployment
    ReceiverRecovery recovery
	
    protected void setUp() 
	{
        super.setUp()
		
        mockLogging(SearchableService, true)
        searchableService = new SearchableService()
        searchableService.metaClass.startMirroring = {}
        searchableService.metaClass.stopMirroring = {}
        assert(searchableService)
        
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

        recovery = new ReceiverRecovery(deployment:deployment,
                                        recoveryDateTime:new DateTime("2010-12-08T06:44:24"))
        mockDomain(ReceiverRecovery, [recovery])
        recovery.save()
        
        receiver.save()
        
        deployment.recovery = recovery
        deployment.save()
		
		AbstractBatchProcessor.metaClass.getRecords = { getRecords(it) }
    }

    protected void tearDown() 
	{
        super.tearDown()
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
        
        def unknownReceiver1 = [:] + validDetection
        unknownReceiver1[(DetectionFactoryService.RECEIVER_COLUMN)] = "AAA"
        
        def unknownReceiver2 = [:] + validDetection
        unknownReceiver2[(DetectionFactoryService.RECEIVER_COLUMN)] = "BBB"
        
        def unknownReceiver3 = [:] + validDetection
        unknownReceiver3[(DetectionFactoryService.DATE_AND_TIME_COLUMN)] = "2009-12-08 06:47:24"
        unknownReceiver3[(DetectionFactoryService.RECEIVER_COLUMN)] = "BBB"

        def noDeploymentDetection = [:] + standardParams
        noDeploymentDetection[(DetectionFactoryService.DATE_AND_TIME_COLUMN)] = "2007-12-08 06:44:24"
        
        def noRecoveryDetection = [:] + standardParams
        noRecoveryDetection[(DetectionFactoryService.DATE_AND_TIME_COLUMN)] = "2010-12-08 06:44:24"
        
        return [validDetection, duplicateDetection, unknownReceiver1, unknownReceiver2, unknownReceiver3, noDeploymentDetection, noRecoveryDetection]
    }
}
