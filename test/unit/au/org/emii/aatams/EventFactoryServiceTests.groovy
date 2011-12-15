package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import grails.test.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class EventFactoryServiceTests extends GrailsUnitTestCase 
{
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"

    def eventFactoryService
    def standardParams
    
    Receiver receiver
    ReceiverDeployment deployment
    ReceiverRecovery recovery

	def downloadFile
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(EventFactoryService, true)
		eventFactoryService = new EventFactoryService()
		
		mockLogging(EventValidatorService, true)
		eventFactoryService.eventValidatorService = new EventValidatorService()
		
		standardParams =
				[(EventFactoryService.DATE_AND_TIME_COLUMN):"2009-12-08 06:44:24",
				 (EventFactoryService.RECEIVER_COLUMN):"VR3UWM-354",
				 (EventFactoryService.DESCRIPTION_COLUMN):"Initialization",
				 (EventFactoryService.DATA_COLUMN):"",
				 (EventFactoryService.UNITS_COLUMN):""]
			 
		receiver = new Receiver(codeName:"VR3UWM-354")
		mockDomain(Receiver, [receiver])
		
		deployment = new ReceiverDeployment(receiver:receiver,
											initialisationDateTime:new DateTime("2008-12-08T02:00:00"),
											deploymentDateTime:new DateTime("2008-12-08T06:44:24"))
										
		receiver.addToDeployments(deployment)
		mockDomain(ReceiverDeployment, [deployment])
		
		recovery = new ReceiverRecovery(deployment:deployment)
		mockDomain(ReceiverRecovery, [recovery])
		recovery.save()
		
		receiver.save()
		
		deployment.recovery = recovery
		deployment.save()
		
		mockDomain(ReceiverEvent)
		mockDomain(ValidReceiverEvent)
		
		downloadFile = new ReceiverDownloadFile()
		mockDomain(ReceiverDownloadFile, [downloadFile])
		downloadFile.save()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testDuplicateEvent() 
	{
		def validEvent = newEvent(new ReceiverDownloadFile(), standardParams)
		assertNotNull(validEvent)
		
		def duplicateEvent = newEvent(new ReceiverDownloadFile(), standardParams)
		assertNotNull(duplicateEvent)
		assertTrue(duplicateEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.DUPLICATE, duplicateEvent.reason)
		assertEquals("", duplicateEvent.message)
    }
	
	void testUnknownReceiver()
	{
		standardParams[EventFactoryService.RECEIVER_COLUMN] = "VR3UWM-123"
		def invalidEvent = newEvent(new ReceiverDownloadFile(), standardParams)
		assertNotNull(invalidEvent)
		assertTrue(invalidEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.UNKNOWN_RECEIVER, invalidEvent.reason)
		assertEquals("Unknown receiver code name VR3UWM-123", invalidEvent.message)
	}
	
	void testNoInitializedReceiverAtDateTime()
	{
		deployment.initialisationDateTime = null
		
		// test deployment.initializationDateTime
		def notInitialisedEvent = newEvent(new ReceiverDownloadFile(), standardParams)
		
		assertNotNull(notInitialisedEvent)
		assertTrue(notInitialisedEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, notInitialisedEvent.reason)
		assertEquals("No deployment at time 2009-12-08 06:44:24 for receiver VR3UWM-354", notInitialisedEvent.message)
	}
	
	void testInitializedDateTimeAfterEventDateTime()
	{
		deployment.initialisationDateTime = new DateTime("2009-12-08T07:44:24Z")
		
		// test deployment.initializationDateTime
		def notInitialisedEvent = newEvent(new ReceiverDownloadFile(), standardParams)
		
		assertNotNull(notInitialisedEvent)
		assertTrue(notInitialisedEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, notInitialisedEvent.reason)
		assertEquals("No deployment at time 2009-12-08 06:44:24 for receiver VR3UWM-354", notInitialisedEvent.message)
	}
	
	void testNoRecoveryAtDateTime()
	{
		recovery.setRecoveryDateTime(new DateTime("2008-12-08T06:44:24", DateTimeZone.forID("GMT")))
		recovery.save()

		def noRecoveryAtDateTimeEvent =
			newEvent(new ReceiverDownloadFile(), standardParams)
		
		assertNotNull(noRecoveryAtDateTimeEvent)
		assertTrue(noRecoveryAtDateTimeEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME, noRecoveryAtDateTimeEvent.reason)
		assertEquals("No recovery at time 2009-12-08 06:44:24 for receiver VR3UWM-354", noRecoveryAtDateTimeEvent.message)
	}
	
	void testValidEvent()
	{
		standardParams[EventFactoryService.DESCRIPTION_COLUMN] = "Blanking"
		standardParams[EventFactoryService.DATA_COLUMN] = "260"
		standardParams[EventFactoryService.UNITS_COLUMN] = "ms"
		
		def validEvent = newEvent(downloadFile, standardParams)
		
		assertNotNull(validEvent)
		assertTrue(validEvent instanceof ValidReceiverEvent)
		assertEquals("Blanking", validEvent.description)
		assertEquals("260", validEvent.data)
		assertEquals("ms", validEvent.units)
		assertEquals(downloadFile.id, validEvent.receiverDownload.id)
		assertEquals(deployment.id, validEvent.receiverDeployment.id)
		assertEquals(receiver.id, validEvent.receiverDeployment.receiver.id)
	}
	
	private ReceiverEvent newEvent(downloadFile, params)
	{
		return eventFactoryService.newEvent(downloadFile, params)
	}
}
