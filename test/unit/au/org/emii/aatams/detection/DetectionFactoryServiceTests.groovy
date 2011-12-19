package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import grails.test.*
import com.vividsolutions.jts.geom.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

class DetectionFactoryServiceTests extends AbstractDetectionFactoryServiceTests 
{
    protected void setUp() 
    {
        super.setUp()
		
		mockLogging(DetectionFactoryService, true)
		detectionFactoryService = new DetectionFactoryService()
		
		mockLogging(DetectionValidatorService, true)
		detectionFactoryService.detectionValidatorService = new DetectionValidatorService()
    }

    protected void tearDown() 
    {
        super.tearDown()
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

        Collection<DetectionSurgery> newDetSurgeries = detectionFactoryService.rescanForSurgery(surgery)
        assertEquals(1, newDetSurgeries.size())
        assertEquals(detection, newDetSurgeries[0].detection)
		
		assertEquals(1, DetectionSurgery.count())
		def detectionSurgery = DetectionSurgery.list()[0]
		assertEquals(tag, detectionSurgery.tag)
		assertEquals(surgery, detectionSurgery.surgery)
    }
}
