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
        
		mockLogging(DetectionValidatorService, true)
		detectionFactoryService.detectionValidatorService = new DetectionValidatorService()
		
        mockLogging(Surgery)
        
        mockDomain(RawDetection)
        mockDomain(InvalidDetection)
        mockDomain(ValidDetection)
        
        mockDomain(DetectionSurgery)
        mockDomain(DeviceStatus)
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
    
	private RawDetection newDetection(downloadFile, params)
	{
		return detectionFactoryService.newDetection(downloadFile, params)
	}
	
    void testValid()
    {
        def validDetection = newDetection(new ReceiverDownloadFile(), standardParams)
         
		assertEquals("2009-12-08 06:44:24", validDetection.formattedTimestamp)
		 
        assertNotNull(validDetection)
        assertTrue(validDetection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(validDetection.timestamp))
        
		assertEquals(deployment.id, validDetection.receiverDeployment.id)
		assertEquals(receiver.id, validDetection.receiverDeployment.receiver.id)
    }

    void testValidSensor()
    {
        def validDetection = newDetection(new ReceiverDownloadFile(), 
                standardParams + [(DetectionFactoryService.SENSOR_VALUE_COLUMN):123, (DetectionFactoryService.SENSOR_UNIT_COLUMN):"ADC"])
         
        assertNotNull(validDetection)
        assertTrue(validDetection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(validDetection.timestamp))
        
		assertEquals(deployment.id, validDetection.receiverDeployment.id)
		assertEquals(receiver.id, validDetection.receiverDeployment.receiver.id)
    }

    void testDuplicate()
    {
        def validDetection = newDetection(new ReceiverDownloadFile(), standardParams)
        assertNotNull(validDetection)
        
        def duplicateDetection = newDetection(new ReceiverDownloadFile(), standardParams)
        assertNotNull(duplicateDetection)
        assertTrue(duplicateDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.DUPLICATE, duplicateDetection.reason)
        
		assertEquals(deployment.id, validDetection.receiverDeployment.id)
		assertEquals(receiver.id, validDetection.receiverDeployment.receiver.id)

        standardParams[(DetectionFactoryService.DATE_AND_TIME_COLUMN)] = "2009-12-08 06:45:24"
        def validDetection2 = newDetection(new ReceiverDownloadFile(), standardParams)
        assertNotNull(validDetection2)
        assertEquals(2, ValidDetection.count())
    }
    
    void testUnknownReceiver()
    {
        standardParams[(DetectionFactoryService.RECEIVER_COLUMN)] = "XYZ"
        def unknownReceiverDetection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
            
        assertNotNull(unknownReceiverDetection)
        assertTrue(unknownReceiverDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.UNKNOWN_RECEIVER, unknownReceiverDetection.reason)
        assertEquals("Unknown receiver code name XYZ", unknownReceiverDetection.message)
        
        assertTrue(receiver.detections.isEmpty())
        assertFalse(receiver.detections.contains(unknownReceiverDetection))
        assertTrue(deployment.detections.isEmpty())
        assertFalse(deployment.detections.contains(unknownReceiverDetection))
    }
    
    void testNoDeploymentAtTime()
    {
        deployment.setDeploymentDateTime(new DateTime("2010-12-08T06:44:24", DateTimeZone.forID("GMT")))
        deployment.save()
        
        def noDeploymentAtTimeDetection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
            
        assertNotNull(noDeploymentAtTimeDetection)
        assertTrue(noDeploymentAtTimeDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, noDeploymentAtTimeDetection.reason)
        assertEquals("No deployment at time 2009-12-08 06:44:24 for receiver VR3UWM-354", noDeploymentAtTimeDetection.message)

        assertTrue(receiver.detections.isEmpty())
        assertFalse(receiver.detections.contains(noDeploymentAtTimeDetection))
        assertTrue(deployment.detections.isEmpty())
        assertFalse(deployment.detections.contains(noDeploymentAtTimeDetection))
    }

    void testNoRecoveryAtTime()
    {
        recovery.setRecoveryDateTime(new DateTime("2008-12-08T06:44:24", DateTimeZone.forID("GMT")))
        recovery.save()

        def noRecoveryAtDateTimeDetection =
            newDetection(new ReceiverDownloadFile(), standardParams)
        
        assertNotNull(noRecoveryAtDateTimeDetection)
        assertTrue(noRecoveryAtDateTimeDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME, noRecoveryAtDateTimeDetection.reason)
        assertEquals("No recovery at time 2009-12-08 06:44:24 for receiver VR3UWM-354", noRecoveryAtDateTimeDetection.message)

        assertTrue(receiver.detections.isEmpty())
        assertFalse(receiver.detections.contains(noRecoveryAtDateTimeDetection))
        assertTrue(deployment.detections.isEmpty())
        assertFalse(deployment.detections.contains(noRecoveryAtDateTimeDetection))
    }
    
    void testNoMatchingSurgeries() 
    {
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
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
    
    def release
    def release1
    def release2
    
    private void setupData()
    {
        DeviceStatus deployed = new DeviceStatus(status:DeviceStatus.DEPLOYED)
        mockDomain(DeviceStatus, [deployed])
        deployed.save()
        
        tag = new Tag(codeName:"A69-1303-62347", codeMap:"A69-1303", pingCode:62347, status:deployed)
        
        tag1 = new Tag(codeName:"A69-1303-1111", codeMap:"A69-1303", pingCode:1111, status:deployed)
        tag2 = new Tag(codeName:"A69-1303-1111", codeMap:"A69-1303", pingCode:1111, status:deployed)
        def tagList = [tag, tag1, tag2]
        mockDomain(Tag, tagList)
        
        sensor = new Sensor(codeName:"A69-1609-12345", tag:tag, codeMap:"A69-1609", pingCode:12345, status:deployed)
        mockDomain(Sensor, [sensor])
        
        release = new AnimalRelease(releaseDateTime:new DateTime("2009-12-07T06:44:24"))
        release1 = new AnimalRelease(releaseDateTime:new DateTime("2009-12-07T06:44:24"))
        release2 = new AnimalRelease(releaseDateTime:new DateTime("2009-12-07T06:44:24"))
        def releaseList = [release, release1, release2]
        mockDomain(AnimalRelease, releaseList)

        surgery = new Surgery(tag:tag, timestamp:new DateTime("2008-12-08T06:44:24"), release:release)
        surgery1 = new Surgery(tag:tag1, timestamp:new DateTime("2008-12-08T06:44:24"), release:release1)
        surgery2 = new Surgery(tag:tag2, timestamp:new DateTime("2008-12-08T06:44:24"), release:release2)
        def surgeryList = [surgery, surgery1, surgery2]
        
        mockDomain(Surgery, surgeryList)

        tag.addToSurgeries(surgery)
        tag1.addToSurgeries(surgery1)
        tag2.addToSurgeries(surgery2)
        
        tagList.each { it.save() }
        surgeryList.each { it.save() }
        
        release.addToSurgeries(surgery)
        release.addToSurgeries(surgery1)
        release.addToSurgeries(surgery2)
        releaseList.each { it.save() }
        
        sensor.save()
        
    }
    
    void testOneMatchingTagSurgery() 
    {
        setupData()
        
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

		def detectionSurgery = DetectionSurgery.list()[0]
        assertEquals(1, DetectionSurgery.count())      
        assertEquals(tag, detectionSurgery.tag)
        assertEquals(surgery, detectionSurgery.surgery)
    }
    
    void testOneMatchingSensorSurgery() 
    {
        setupData()
        standardParams[(DetectionFactoryService.TRANSMITTER_COLUMN)] = "A69-1609-12345"
        
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

		def detectionSurgery = DetectionSurgery.list()[0]
        assertEquals(1, DetectionSurgery.count())      
        assertEquals(sensor, detectionSurgery.tag)
        assertEquals(surgery, detectionSurgery.surgery)
    }
    
    void testMultipleMatchingSurgeries() 
    {
        setupData()
        standardParams[(DetectionFactoryService.TRANSMITTER_COLUMN)] = "A69-1303-1111"
        
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

		assertEquals(2, DetectionSurgery.count())
		
		def detectionSurgery1 = DetectionSurgery.list()[0]
		assertEquals(tag1, detectionSurgery1.tag)
		assertEquals(surgery1, detectionSurgery1.surgery)

		def detectionSurgery2 = DetectionSurgery.list()[1]
		assertEquals(tag2, detectionSurgery2.tag)
		assertEquals(surgery2, detectionSurgery2.surgery)
    }
    
    void testReleaseStatusNonCurrent()
    {
        setupData()
        
        release.status = AnimalReleaseStatus.FINISHED
        
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertTrue(detection.detectionSurgeries.isEmpty())
    }
    
    void testTagRetired()
    {
        setupData()
        standardParams[(DetectionFactoryService.TRANSMITTER_COLUMN)] = "A69-1303-1111"
        
        DeviceStatus retiredStatus = new DeviceStatus(status:DeviceStatus.RETIRED)
        mockDomain(DeviceStatus, [retiredStatus])
        
        tag1.setStatus(retiredStatus)
        tag1.save()
        
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

		assertEquals(1, DetectionSurgery.count())
		def detectionSurgery = DetectionSurgery.list()[0]
		assertEquals(tag2, detectionSurgery.tag)
		assertEquals(surgery2, detectionSurgery.surgery)
    }
    
    void testBeforeTagWindow()
    {
        setupData()
        standardParams[(DetectionFactoryService.TRANSMITTER_COLUMN)] = "A69-1303-1111"
        
        release1.releaseDateTime = new DateTime("2009-12-09T06:44:24")
        release1.save()
        
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

   		assertEquals(1, DetectionSurgery.count())
		def detectionSurgery = DetectionSurgery.list()[0]
		assertEquals(tag2, detectionSurgery.tag)
		assertEquals(surgery2, detectionSurgery.surgery)
    }
    
    void testAfterTagWindow()
    {
        setupData()
        standardParams[(DetectionFactoryService.TRANSMITTER_COLUMN)] = "A69-1303-1111"
        
        surgery1.grailsApplication = [config:[tag:[expectedLifeTime:[gracePeriodDays:10]]]]
		surgery2.grailsApplication = [config:[tag:[expectedLifeTime:[gracePeriodDays:10]]]]
		
        tag1.expectedLifeTimeDays = 10
        tag1.save()
        
        release1.releaseDateTime = new DateTime("2009-11-18T06:44:24")
        release1.save()

		assertEquals(0, DetectionSurgery.count())
		
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

		assertEquals(1, DetectionSurgery.count())
		def detectionSurgery = DetectionSurgery.list()[0]
		assertEquals(tag2, detectionSurgery.tag)
		assertEquals(surgery2, detectionSurgery.surgery)

        // So we don't get a duplicate.
        detection.setTimestamp(new Date())
        detection.save()
		
		// Move the release time so that detection is now within the window.
        release1.releaseDateTime = new DateTime("2009-11-19T06:44:24")
        release1.save()

		assertEquals(1, DetectionSurgery.count())
		
        // "2009-12-08 06:44:24"
        detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
           
		assertEquals(3, DetectionSurgery.count())
		def detectionSurgery1 = DetectionSurgery.list()[1]
		assertEquals(tag1, detectionSurgery1.tag)
		assertEquals(surgery1, detectionSurgery1.surgery)
		def detectionSurgery2 = DetectionSurgery.list()[2]
		assertEquals(tag2, detectionSurgery2.tag)
		assertEquals(surgery2, detectionSurgery2.surgery)
    }
    
    void testRescan()
    {
        def detection = 
            newDetection(new ReceiverDownloadFile(), standardParams)
         
        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))
        assertTrue(detection.detectionSurgeries.isEmpty())      

        setupData()

        Collection<RawDetection> updatedDetections = detectionFactoryService.rescanForSurgery(surgery)
        assertEquals(1, updatedDetections.size())
        def updatedDetection = updatedDetections[0]
        assertEquals(detection, updatedDetection)
		
		assertEquals(1, DetectionSurgery.count())
		def detectionSurgery = DetectionSurgery.list()[0]
		assertEquals(tag, detectionSurgery.tag)
		assertEquals(surgery, detectionSurgery.surgery)
    }
}
