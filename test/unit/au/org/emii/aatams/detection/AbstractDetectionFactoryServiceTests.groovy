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
		
		mockDomain(InvalidDetection)
		mockDomain(ValidDetection)
		
		mockDomain(DetectionSurgery)
		mockDomain(DeviceStatus)
		mockDomain(Sensor)
		mockDomain(Surgery)
		mockDomain(Tag)
		
		standardParams =
				[(VueDetectionFormat.DATE_AND_TIME_COLUMN):"2009-12-08 06:44:24",
				 (VueDetectionFormat.RECEIVER_COLUMN):"VR3UWM-354",
				 (VueDetectionFormat.TRANSMITTER_COLUMN):"A69-1303-62347",
				 (VueDetectionFormat.TRANSMITTER_NAME_COLUMN):"shark tag",
				 (VueDetectionFormat.TRANSMITTER_SERIAL_NUMBER_COLUMN):"1234",
				 (VueDetectionFormat.STATION_NAME_COLUMN):"Neptune SW 1",
				 (VueDetectionFormat.LATITUDE_COLUMN):-40.1234f,
				 (VueDetectionFormat.LONGITUDE_COLUMN):45.1234f]
		
		ReceiverDeviceModel rxrModel = new ReceiverDeviceModel(modelName:"VR3UWM")
		mockDomain(ReceiverDeviceModel, [rxrModel])
		rxrModel.save()		
			 
		receiver = new Receiver(serialNumber:"354", model:rxrModel)
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
	def sensor0
	def sensor1
	def sensor2

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
		
		CodeMap a69_1303 = new CodeMap(codeMap: "A69-1303")
		mockDomain(CodeMap, [a69_1303])
		a69_1303.save()
		
		tag = new Tag(codeMap:a69_1303, status:deployed)
		sensor0 = new Sensor(pingCode:62347, tag:tag)
		sensor = new Sensor(tag:tag, pingCode:12345)
		
		assertEquals("A69-1303-62347", sensor0.transmitterId)
		assertEquals("A69-1303-12345", sensor.transmitterId)
		
		tag.addToSensors(sensor0)
		tag.addToSensors(sensor)
		
		tag1 = new Tag(codeMap:a69_1303, pingCode:1111, status:deployed)
		sensor1 = new Sensor(pingCode:1111, tag:tag1)
		tag1.addToSensors(sensor1)
		
		tag2 = new Tag(codeMap:a69_1303, pingCode:1111, status:deployed)
		sensor2 = new Sensor(pingCode:1111, tag:tag2)
		tag2.addToSensors(sensor2)
		
		def tagList = [tag, tag1, tag2]
		mockDomain(Tag, tagList)
		
		def sensorList = [sensor, sensor0, sensor1, sensor2]
		mockDomain(Sensor, sensorList)
		
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
		
		sensorList.each { it.save() }
	}
}
