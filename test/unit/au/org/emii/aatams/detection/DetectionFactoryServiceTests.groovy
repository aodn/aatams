package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import grails.test.*
import com.vividsolutions.jts.geom.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

class DetectionFactoryServiceTests extends GrailsUnitTestCase 
{
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"

    def detectionFactoryService
    def standardParams
    
    Receiver receiver
    ReceiverDeployment deployment
    ReceiverRecovery recovery

    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(DetectionFactoryService, true)
        detectionFactoryService = new DetectionFactoryService()
        
        mockDomain(RawDetection)
        mockDomain(InvalidDetection)
        mockDomain(ValidDetection)
        
        mockDomain(DetectionSurgery)
        mockDomain(Sensor)
        mockDomain(Surgery)
        mockDomain(Tag)
        
        standardParams = 
                [(DetectionFactoryService.DATE_AND_TIME_COLUMN):"2009-12-08 06:44:24",
                 (DetectionFactoryService.RECEIVER_COLUMN):"VR3UWM-354",
                 (DetectionFactoryService.TRANSMITTER_COLUMN):"A69-1303-62347",
                 (DetectionFactoryService.TRANSMITTER_NAME_COLUMN):"shark tag",
                 (DetectionFactoryService.TRANSMITTER_SERIAL_NUMBER_COLUMN):"1234",
                 (DetectionFactoryService.STATION_NAME_COLUMN):"Neptune SW 1",
                 (DetectionFactoryService.LATITUDE_COLUMN):-40.1234f,
                 (DetectionFactoryService.LONGITUDE_COLUMN):45.1234f]   
             
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
    
    void testValid()
    {
        def validDetection = 
            detectionFactoryService.newDetection(standardParams)
         
        assertNotNull(validDetection)
        assertTrue(validDetection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(validDetection.timestamp))
    }

    void testValidSensor()
    {
        def validDetection = 
            detectionFactoryService.newDetection(
                standardParams + [(DetectionFactoryService.SENSOR_VALUE_COLUMN):123, (DetectionFactoryService.SENSOR_UNIT_COLUMN):"ADC"])
         
        assertNotNull(validDetection)
        assertTrue(validDetection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(validDetection.timestamp))
    }

    void testDuplicate()
    {
        def validDetection = detectionFactoryService.newDetection(standardParams)
        assertNotNull(validDetection)
        
        def duplicateDetection = detectionFactoryService.newDetection(standardParams)
        assertNotNull(duplicateDetection)
        assertTrue(duplicateDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.DUPLICATE, duplicateDetection.reason)
        
        standardParams[(DetectionFactoryService.DATE_AND_TIME_COLUMN)] = "2009-12-08 06:45:24"
        def validDetection2 = detectionFactoryService.newDetection(standardParams)
        assertNotNull(validDetection2)
        assertEquals(2, ValidDetection.count())
    }
    
    void testUnknownReceiver()
    {
        standardParams[(DetectionFactoryService.RECEIVER_COLUMN)] = "XYZ"
        def unknownReceiverDetection = 
            detectionFactoryService.newDetection(standardParams)
            
        assertNotNull(unknownReceiverDetection)
        assertTrue(unknownReceiverDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.UNKNOWN_RECEIVER, unknownReceiverDetection.reason)
        assertEquals("Unknown receiver code name XYZ", unknownReceiverDetection.message)
    }
    
    void testNoDeploymentAtTime()
    {
        deployment.setDeploymentDateTime(new DateTime("2010-12-08T06:44:24", DateTimeZone.forID("GMT")))
        deployment.save()
        
        def noDeploymentAtTimeDetection = 
            detectionFactoryService.newDetection(standardParams)
            
        assertNotNull(noDeploymentAtTimeDetection)
        assertTrue(noDeploymentAtTimeDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, noDeploymentAtTimeDetection.reason)
        assertEquals("No deployment at time 2009-12-08 06:44:24 for receiver VR3UWM-354", noDeploymentAtTimeDetection.message)
    }

    void testNoRecoveryAtTime()
    {
        recovery.setRecoveryDateTime(new DateTime("2008-12-08T06:44:24", DateTimeZone.forID("GMT")))
        recovery.save()

        def noRecoveryAtDateTimeDetection =
            detectionFactoryService.newDetection(standardParams)
        
        assertNotNull(noRecoveryAtDateTimeDetection)
        assertTrue(noRecoveryAtDateTimeDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME, noRecoveryAtDateTimeDetection.reason)
        assertEquals("No recovery at time 2009-12-08 06:44:24 for receiver VR3UWM-354", noRecoveryAtDateTimeDetection.message)
    }
    
    
    void testNoMatchingSurgeries() 
    {
        def detection = 
            detectionFactoryService.newDetection(standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertTrue(detection.detectionSurgeries?.isEmpty())        
    }
    
    def tag
    def tag1
    def tag2
    
    def sensor
    
    def surgery
    def surgery1
    def surgery2
    
    private void setupData()
    {
        tag = new Tag(codeName:"A69-1303-62347", codeMap:"A69-1303", pingCode:62347)
        
        tag1 = new Tag(codeName:"A69-1303-1111", codeMap:"A69-1303", pingCode:1111)
        tag2 = new Tag(codeName:"A69-1303-1111", codeMap:"A69-1303", pingCode:1111)
        def tagList = [tag, tag1, tag2]
        mockDomain(Tag, tagList)
        
        sensor = new Sensor(codeName:"A69-1609-12345", tag:tag, codeMap:"A69-1609", pingCode:12345)
        mockDomain(Sensor, [sensor])
        
        surgery = new Surgery(tag:tag, timestamp:new DateTime("2008-12-08T06:44:24"))
        surgery1 = new Surgery(tag:tag1, timestamp:new DateTime("2008-12-08T06:44:24"))
        surgery2 = new Surgery(tag:tag2, timestamp:new DateTime("2008-12-08T06:44:24"))
        def surgeryList = [surgery, surgery1, surgery2]
        
        mockDomain(Surgery, surgeryList)
        
        tag.addToSurgeries(surgery)
        tag1.addToSurgeries(surgery1)
        tag2.addToSurgeries(surgery2)
        
        tagList.each { it.save() }
        surgeryList.each { it.save() }
        
        sensor.save()
    }
    
    void testOneMatchingTagSurgery() 
    {
        setupData()
        
        def detection = 
            detectionFactoryService.newDetection(standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertEquals(1, detection.detectionSurgeries.size())      
        assertEquals(tag, detection.detectionSurgeries[0].tag)
        assertEquals(surgery, detection.detectionSurgeries[0].surgery)
    }
    
    void testOneMatchingSensorSurgery() 
    {
        setupData()
        standardParams[(DetectionFactoryService.TRANSMITTER_COLUMN)] = "A69-1609-12345"
        
        def detection = 
            detectionFactoryService.newDetection(standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertEquals(1, detection.detectionSurgeries.size())      
        assertEquals(sensor, detection.detectionSurgeries[0].tag)
        assertEquals(surgery, detection.detectionSurgeries[0].surgery)
    }
    
    void testMultipleMatchingSurgeries() 
    {
        setupData()
        standardParams[(DetectionFactoryService.TRANSMITTER_COLUMN)] = "A69-1303-1111"
        
        def detection = 
            detectionFactoryService.newDetection(standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertEquals(2, detection.detectionSurgeries.size())      
        assertTrue(detection.detectionSurgeries*.tag.contains(tag1))
        assertTrue(detection.detectionSurgeries*.tag.contains(tag2))
        assertTrue(detection.detectionSurgeries*.surgery.contains(surgery1))
        assertTrue(detection.detectionSurgeries*.surgery.contains(surgery2))
    }
    
    void testReleaseStatusNonCurrent()
    {
    }
    
    void testTagRetired()
    {
    }
    
    void testBeforeTagWindow()
    {
    }
    
    void testAfterTagWindow()
    {
    }
    
    void testRescan()
    {
//        Collection<Detection> rescanForSurgery(Surgery surgery)
    }
}
