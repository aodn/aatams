package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.event.EventFormat;
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
		
		mockLogging(EventValidator, true)
		
		standardParams =
				[(EventFormat.DATE_AND_TIME_COLUMN):"2008-12-08 12:44:24",
				 (EventFormat.RECEIVER_COLUMN):"VR3UWM-354",
				 (EventFormat.DESCRIPTION_COLUMN):"Initialization",
				 (EventFormat.DATA_COLUMN):"",
				 (EventFormat.UNITS_COLUMN):""]
			 
		ReceiverDeviceModel model = new ReceiverDeviceModel(modelName:"VR3UWM")
		mockDomain(ReceiverDeviceModel, [model])
		model.save()
		
		receiver = new Receiver(serialNumber:"354", model:model)
		mockDomain(Receiver, [receiver])
		
		deployment = new ReceiverDeployment(receiver:receiver,
											initialisationDateTime:new DateTime("2008-12-08T02:00:00"),
											deploymentDateTime:new DateTime("2008-12-09T06:44:24"))
										
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
		
		downloadFile = new ReceiverDownloadFile(type: ReceiverDownloadFileType.EVENTS_CSV)
		mockDomain(ReceiverDownloadFile, [downloadFile])
		downloadFile.save()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testDuplicateEvent() 
	{
		def validEvent = newEvent(new ReceiverDownloadFile(type: ReceiverDownloadFileType.EVENTS_CSV), standardParams)
		assertNotNull(validEvent)
		
		def duplicateEvent = newEvent(new ReceiverDownloadFile(type: ReceiverDownloadFileType.EVENTS_CSV), standardParams)
		assertNotNull(duplicateEvent)
		assertTrue(duplicateEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.DUPLICATE, duplicateEvent.reason)
		assertEquals("", duplicateEvent.message)
    }
	
	void testUnknownReceiver()
	{
		standardParams[EventFormat.RECEIVER_COLUMN] = "VR3UWM-123"
		def invalidEvent = newEvent(new ReceiverDownloadFile(type: ReceiverDownloadFileType.EVENTS_CSV), standardParams)
		assertNotNull(invalidEvent)
		assertTrue(invalidEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.UNKNOWN_RECEIVER, invalidEvent.reason)
		assertEquals("Unknown receiver code name VR3UWM-123", invalidEvent.message)
	}
	
	void testNoInitializedReceiverAtDateTime()
	{
		deployment.initialisationDateTime = null
		
		// test deployment.initializationDateTime
		def notInitialisedEvent = newEvent(new ReceiverDownloadFile(type: ReceiverDownloadFileType.EVENTS_CSV), standardParams)
//		initialisationDateTime:new DateTime("2008-12-08T02:00:00"),
//		deploymentDateTime:new DateTime("2008-12-09T06:44:24"))

		assertNotNull(notInitialisedEvent)
		assertTrue(notInitialisedEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, notInitialisedEvent.reason)
		assertEquals("No deployment at time 2008-12-08 12:44:24 for receiver VR3UWM-354", notInitialisedEvent.message)
	}
	
	void testInitializedDateTimeAfterEventDateTime()
	{
		deployment.initialisationDateTime = new DateTime("2009-12-08T07:44:24Z")
		
		// test deployment.initializationDateTime
		def notInitialisedEvent = newEvent(new ReceiverDownloadFile(type: ReceiverDownloadFileType.EVENTS_CSV), standardParams)
		
		assertNotNull(notInitialisedEvent)
		assertTrue(notInitialisedEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME, notInitialisedEvent.reason)
		assertEquals("No deployment at time 2008-12-08 12:44:24 for receiver VR3UWM-354", notInitialisedEvent.message)
	}
	
	void testNoRecoveryAtDateTime()
	{
		recovery.setRecoveryDateTime(new DateTime("2008-12-08T06:44:24", DateTimeZone.forID("GMT")))
		recovery.save()

		def noRecoveryAtDateTimeEvent =
			newEvent(new ReceiverDownloadFile(type: ReceiverDownloadFileType.EVENTS_CSV), standardParams)
		
		assertNotNull(noRecoveryAtDateTimeEvent)
		assertTrue(noRecoveryAtDateTimeEvent instanceof InvalidReceiverEvent)
		assertEquals(InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME, noRecoveryAtDateTimeEvent.reason)
		assertEquals("No recovery at time 2008-12-08 12:44:24 for receiver VR3UWM-354", noRecoveryAtDateTimeEvent.message)
	}
	
	void testValidEvent()
	{
		standardParams[EventFormat.DESCRIPTION_COLUMN] = "Blanking"
		standardParams[EventFormat.DATA_COLUMN] = "260"
		standardParams[EventFormat.UNITS_COLUMN] = "ms"
		
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
