package au.org.emii.aatams

import grails.test.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

class DetectionFactoryServiceTests extends GrailsUnitTestCase 
{
    def service
    
    protected void setUp() 
    {
        super.setUp()

        mockLogging(DetectionFactoryService)
        service = new DetectionFactoryService()

        mockDomain(Detection)
        mockDomain(DetectionSurgery)
        mockDomain(Receiver)
//        mockDomain(ReceiverDeployment)
//        mockDomain(ReceiverRecovery)

        mockDomain(Sensor)
        mockDomain(Tag)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testNoMatchingSurgeries() 
    {
        def receiverCodeName = "VRW1234"
        Receiver receiver = new Receiver(codeName:receiverCodeName,
                                         organisation:new Organisation(),
                                         model:new DeviceModel(),
                                         serialNumber:"1234",
                                         status:new DeviceStatus()).save()
        ReceiverDeployment deployment = 
            new ReceiverDeployment(deploymentDateTime:new DateTime("2010-02-15T12:34:56+10:00"),
                                   receiver:receiver)
        receiver.addToDeployments(deployment)
        
        
        ReceiverRecovery recovery =
            new ReceiverRecovery(recoveryDateTime:new DateTime("2012-02-15T12:34:56+10:00"),
                                 deployment:deployment)
        deployment.recovery = recovery
        
        def detectionParams = 
        [
            (DetectionFactoryService.DATE_AND_TIME_COLUMN):"2011-08-17 12:34:56",
            (DetectionFactoryService.RECEIVER_COLUMN):receiverCodeName,
            (DetectionFactoryService.TRANSMITTER_COLUMN):"A69-1303-1234",
            (DetectionFactoryService.TRANSMITTER_NAME_COLUMN):null,
            (DetectionFactoryService.TRANSMITTER_SERIAL_NUMBER_COLUMN):null,
            (DetectionFactoryService.SENSOR_VALUE_COLUMN):null,
            (DetectionFactoryService.SENSOR_UNIT_COLUMN):null,
            (DetectionFactoryService.STATION_NAME_COLUMN):null,
            (DetectionFactoryService.LATITUDE_COLUMN):null,
            (DetectionFactoryService.LONGITUDE_COLUMN):null
        ]
        
        Detection detection = service.newDetection(detectionParams)
        
        assertNotNull(detection)
        assertTrue(detection.detectionSurgeries?.isEmpty())
        
        
        // TODO: check detection properties set properly.
    }
    

//    void testOneMatchingSurgeries() 
//    void testMultipleMatchingSurgeries() 
//    
//    void testSensorDetection()
//    void testNonSensorDetection()
//    
//    void testDuplicateDetection()
//    
//    void testReleaseStatusActive()
//    void testReleaseStatusNonActive()
//    void testTagRetired()
//    void testBeforeTagWindow()
//    void testAfterTagWindow()
//    
//    void testUnknownReceiver() - check sensible error message is returned.
//    void testNoDeployment() - expect exception
//    void testOneDeploymentAfterDetectionDate() - expect exception
//    void testOneRecoeryBeforeDetectionDate() - expect exception
//    void testMultipleDeployments()    // not sure?
//    

}
