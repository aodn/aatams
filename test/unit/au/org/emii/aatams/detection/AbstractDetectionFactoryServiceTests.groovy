package au.org.emii.aatams.detection

import grails.test.GrailsUnitTestCase;
import org.joda.time.DateTime
import au.org.emii.aatams.*

abstract class AbstractDetectionFactoryServiceTests extends GrailsUnitTestCase 
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
	
	protected RawDetection newDetection(downloadFile, params)
	{
		return detectionFactoryService.newDetection(downloadFile, params)
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
	
	protected void setupData()
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
}
