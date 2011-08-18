package au.org.emii.aatams

import grails.test.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

class DetectionFactoryServiceTests extends GrailsUnitTestCase 
{
    def service
    
    WKTReader reader = new WKTReader();

    protected void setUp() 
    {
        super.setUp()

        mockLogging(DetectionFactoryService)
        service = new DetectionFactoryService()

        mockDomain(Detection)
        mockDomain(DetectionSurgery)
        mockDomain(DeviceStatus)
        mockDomain(Receiver)
//        mockDomain(ReceiverDeployment)
//        mockDomain(ReceiverRecovery)

        mockDomain(Sensor)
        mockDomain(Tag)
        
        mockConfig("tag { expectedLifeTime {gracePeriodDays = 182}}")
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    def receiverCodeName = "VRW1234"
    def codeMap = "A69-1303"
    def pingCode = "1234"
    def transmitterId = codeMap + "-" + pingCode
    def dateString = "2011-08-17 12:34:56" + " " + "UTC"

    def detectionParams
    def deviceStatusRetired
    
    def receiver
    def deployment
    
    def setupBasicEntities()
    {
        receiver = new Receiver(codeName:receiverCodeName,
                                         organisation:new Organisation(),
                                         model:new DeviceModel(),
                                         serialNumber:"1234",
                                         status:new DeviceStatus()).save()
        deployment = 
            new ReceiverDeployment(deploymentDateTime:new DateTime("2010-02-15T12:34:56+10:00"),
                                   receiver:receiver)
        receiver.addToDeployments(deployment)
        
        
        ReceiverRecovery recovery =
            new ReceiverRecovery(recoveryDateTime:new DateTime("2012-02-15T12:34:56+10:00"),
                                 deployment:deployment)
        deployment.recovery = recovery
        
        receiver.save()
        
        detectionParams = 
        [
            (DetectionFactoryService.DATE_AND_TIME_COLUMN):dateString,
            (DetectionFactoryService.RECEIVER_COLUMN):receiverCodeName,
            (DetectionFactoryService.TRANSMITTER_COLUMN):transmitterId,
            (DetectionFactoryService.TRANSMITTER_NAME_COLUMN):null,
            (DetectionFactoryService.TRANSMITTER_SERIAL_NUMBER_COLUMN):null,
            (DetectionFactoryService.SENSOR_VALUE_COLUMN):null,
            (DetectionFactoryService.SENSOR_UNIT_COLUMN):null,
            (DetectionFactoryService.STATION_NAME_COLUMN):null,
            (DetectionFactoryService.LATITUDE_COLUMN):null,
            (DetectionFactoryService.LONGITUDE_COLUMN):null
        ]
        
        deviceStatusRetired = new DeviceStatus(status:'RETIRED').save()
        
        return detectionParams
    }
    
    void checkDetection(detection, detectionParams)
    {
        assertNotNull(detection)
        assertEquals(new Date().parse(DetectionFactoryService.DATE_FORMAT, detectionParams[DetectionFactoryService.DATE_AND_TIME_COLUMN]), detection.timestamp)
        assertEquals(detectionParams[DetectionFactoryService.RECEIVER_COLUMN], detection.receiverName)
        assertEquals(detectionParams[DetectionFactoryService.TRANSMITTER_COLUMN], detection.transmitterId)
        assertEquals(detectionParams[DetectionFactoryService.TRANSMITTER_NAME_COLUMN], detection.transmitterName)
        assertEquals(detectionParams[DetectionFactoryService.TRANSMITTER_SERIAL_NUMBER_COLUMN], detection.transmitterSerialNumber)
        assertEquals(detectionParams[DetectionFactoryService.STATION_NAME_COLUMN], detection.stationName)
        
        if (detection instanceof SensorDetection)
        {
            assertEquals(Float.parseFloat(detectionParams[DetectionFactoryService.SENSOR_VALUE_COLUMN]), detection.uncalibratedValue)
            assertEquals(detectionParams[DetectionFactoryService.SENSOR_UNIT_COLUMN], detection.sensorUnit)
        }
    }
    
    void testNoMatchingSurgeries() 
    {
        def detectionParams = setupBasicEntities()
        Detection detection = service.newDetection(detectionParams)

        checkDetection(detection, detectionParams)
        assertTrue(detection.detectionSurgeries?.isEmpty())
    }
    
    def release1
    def release2
    
    def createTagsReleasesSurgeries()
    {
        // Create two tags/release/surgeries.
        Tag tag1 = new Tag(codeMap:codeMap,
                          pingCode:pingCode,
                          transmitterType:new TransmitterType(),
                          project:new Project(),
                          codeName:transmitterId,
                          model:new DeviceModel(),
                          serialNumber:"1234",
                          status:new DeviceStatus())
        assertNotNull(tag1)              
                      
        Tag tag2 = new Tag(codeMap:codeMap,
                          pingCode:pingCode + "123",
                          transmitterType:new TransmitterType(),
                          project:new Project(),
                          codeName:transmitterId + "123",
                          model:new DeviceModel(),
                          serialNumber:"1234",
                          status:new DeviceStatus())
                      
        def tagList = [tag1, tag2]
        mockDomain(Tag, tagList)
        tag1.save()
        assertFalse(tag1.hasErrors())
        tag2.save()
        
        release1 = 
            new AnimalRelease(project:new Project(),
                              animal:new Animal(),
                              captureLocality:"somewhere",
                              captureLocation:(Point)reader.read("POINT(30.1234 30.1234)"),
                              captureDateTime:new DateTime("2010-06-15T12:34:56+10:00"),
                              captureMethod:new CaptureMethod(),
                              releaseLocality:"somewhere",
                              releaseLocation:(Point)reader.read("POINT(30.1234 30.1234)"),
                              releaseDateTime:new DateTime("2010-06-15T12:34:56+10:00"))
                          
        release2 = 
            new AnimalRelease(project:new Project(),
                              animal:new Animal(),
                              captureLocality:"somewhere",
                              captureLocation:(Point)reader.read("POINT(30.1234 30.1234)"),
                              captureDateTime:new DateTime("2010-06-15T12:34:56+10:00"),
                              captureMethod:new CaptureMethod(),
                              releaseLocality:"somewhere",
                              releaseLocation:(Point)reader.read("POINT(30.1234 30.1234)"),
                              releaseDateTime:new DateTime("2010-06-15T12:34:56+10:00"))
                          
        mockDomain(AnimalRelease, [release1, release2])
        release1.save()
        release2.save()
        
        Surgery surgery1 =
            new Surgery(release:release1,
                        tag:tag1,
                        timestamp:new DateTime("2010-02-15T12:34:56+10:00"),
                        type:new SurgeryType(),
                        treatmentType:new SurgeryTreatmentType())
        tag1.addToSurgeries(surgery1)
        release1.addToSurgeries(surgery1)
                    
        Surgery surgery2 =
            new Surgery(release:release2,
                        tag:tag2,
                        timestamp:new DateTime("2010-02-15T12:34:56+10:00"),
                        type:new SurgeryType(),
                        treatmentType:new SurgeryTreatmentType())
        tag2.addToSurgeries(surgery2)
        release2.addToSurgeries(surgery2)
                    
        mockDomain(Surgery, [surgery1, surgery2])
        surgery1.save()
        surgery2.save()
        tag1.save()
        tag2.save()
        release1.save()
        release2.save()
        
        // Mock the findAllBy methods needed in the service.
        Tag.metaClass.'static'.findAllByCodeMapAndPingCode =
        {
            codeMap, pingCode ->
            
            return [tagList[0]]
        }
        
        Sensor.metaClass.'static'.findAllByCodeMapAndPingCode =
        {
            codeMap, pingCode ->
            
            return []
        }

        return tagList
    }
    
    void testOneMatchingSurgeries() 
    {
        def detectionParams = setupBasicEntities()
        def tagList = createTagsReleasesSurgeries()
              
        Detection detection = service.newDetection(detectionParams)

        checkDetection(detection, detectionParams)
        assertEquals(1, detection.detectionSurgeries?.size())
    }
    
    void testMultipleMatchingSurgeries() 
    {
        def detectionParams = setupBasicEntities()
        def tagList = createTagsReleasesSurgeries()
              
        // Mock the findAllBy methods needed in the service.
        Tag.metaClass.'static'.findAllByCodeMapAndPingCode =
        {
            codeMap, pingCode ->
            
            return [tagList[0], tagList[1]]
        }
        
        Detection detection = service.newDetection(detectionParams)

        checkDetection(detection, detectionParams)
        assertEquals(2, detection.detectionSurgeries?.size())
    }
    
    void testSensorDetection()
    {
        def detectionParams = setupBasicEntities()
        detectionParams[DetectionFactoryService.SENSOR_VALUE_COLUMN] = "23"
        detectionParams[DetectionFactoryService.SENSOR_UNIT_COLUMN] = "depth"
        
        def tagList = createTagsReleasesSurgeries()
        
        // Add a sensor to tag[0].
        Sensor sensor = 
            new Sensor(
                tag:tagList[0],
                unit:"depth",
                slope:1,
                intercept:23,
                codeMap:"A69-1500",
                pingCode:"789",
                transmitterType:new TransmitterType(),
                project:new Project(),
                codeName:transmitterId,
                model:new DeviceModel(),
                serialNumber:"1234",
                status:new DeviceStatus())
        tagList[0].addToSensors(sensor)
        tagList[0].save()
              
        // Mock the findAllBy methods needed in the service.
        Tag.metaClass.'static'.findAllByCodeMapAndPingCode =
        {
            codeMap, pingCode ->
            
            return []
        }
        
        Sensor.metaClass.'static'.findAllByCodeMapAndPingCode =
        {
            codeMap, pingCode ->
            
            return [sensor]
        }

        Detection detection = service.newDetection(detectionParams)
        assertTrue(detection instanceof SensorDetection)
        checkDetection(detection, detectionParams)
        assertEquals(1, detection.detectionSurgeries?.size())
        
        // Also check the detection's surgery tag points to sensor tag.
        assertEquals(sensor, detection.detectionSurgeries[0].tag)
    }
    
    void testNonSensorDetection()
    {
        def detectionParams = setupBasicEntities()
        
        def tagList = createTagsReleasesSurgeries()
        
        // Add a sensor to tag[0].
        Sensor sensor = 
            new Sensor(
                tag:tagList[0],
                unit:"depth",
                slope:1,
                intercept:23,
                codeMap:"A69-1500",
                pingCode:"789",
                transmitterType:new TransmitterType(),
                project:new Project(),
                codeName:transmitterId,
                model:new DeviceModel(),
                serialNumber:"1234",
                status:new DeviceStatus())
        tagList[0].addToSensors(sensor)
        tagList[0].save()
              
        Detection detection = service.newDetection(detectionParams)
        checkDetection(detection, detectionParams)
        assertFalse(detection instanceof SensorDetection)
        assertEquals(1, detection.detectionSurgeries?.size())
        
        // Also check the detection's surgery tag points to parent tag.
        assertEquals(tagList[0], detection.detectionSurgeries[0].tag)
    }

    void testDuplicateDetection()
    {
        def detectionParams = setupBasicEntities()
        Detection detection = service.newDetection(detectionParams)

        // The service should just return the same detection (i.e. IDs should
        // be equal).
        Detection secondDetection = service.newDetection(detectionParams)
        
        assertEquals(detection.id, secondDetection.id)
    }

    // Implicitly ested by above.
//    void testReleaseStatusCurrent()

    void testReleaseStatusNonCurrent()
    {
        def detectionParams = setupBasicEntities()
        def tagList = createTagsReleasesSurgeries()
              
        release1.status = AnimalReleaseStatus.FINISHED
        release1.save()

        Detection detection = service.newDetection(detectionParams)

        // Surgery won't match because the release's status is not CURRENT.
        assertTrue(detection.detectionSurgeries?.isEmpty())
    }
    
    void testTagRetired()
    {
        def detectionParams = setupBasicEntities()
        def tagList = createTagsReleasesSurgeries()
        tagList[0].status = deviceStatusRetired
              
        Detection detection = service.newDetection(detectionParams)

        // Surgery won't match because the release's status is not CURRENT.
        assertTrue(detection.detectionSurgeries?.isEmpty())
    }
    
    void testBeforeTagWindow()
    {
        def detectionParams = setupBasicEntities()
        def tagList = createTagsReleasesSurgeries()
        
        Detection detection = service.newDetection(detectionParams)
        assertEquals(1, detection.detectionSurgeries?.size())

        // Now delete the above detection (so that we don't have a duplicate),
        // set the detection date to 2009 in the parameters and try again.
        detection.delete()
        
        detectionParams[DetectionFactoryService.DATE_AND_TIME_COLUMN] = "2010-04-10 12:34:56" + " " + "UTC"
        detection = service.newDetection(detectionParams)
        
        // Should be no surgeries this time.
        assertTrue(detection.detectionSurgeries?.isEmpty())
    }
    
    void testAfterTagWindow()
    {
        def detectionParams = setupBasicEntities()
        def tagList = createTagsReleasesSurgeries()
        
        Detection detection = service.newDetection(detectionParams)
        assertEquals(1, detection.detectionSurgeries?.size())

        // Cleanup so that we don't have a duplicate),
        // set the tag expected life time to 10 days).
        detection.delete()
        tagList[0].expectedLifeTimeDays = 600
        detection = service.newDetection(detectionParams)
        assertEquals(1, detection.detectionSurgeries?.size())

        detection.delete()
        tagList[0].expectedLifeTimeDays = 10
        detection = service.newDetection(detectionParams)
        
        // Should be no surgeries this time.
        assertTrue(detection.detectionSurgeries?.isEmpty())
    }

    void testUnknownReceiver()
    {
        def detectionParams = setupBasicEntities()
        detectionParams[DetectionFactoryService.RECEIVER_COLUMN] = "asfads" + receiverCodeName

        try
        {
            Detection detection = service.newDetection(detectionParams)
            fail()  // shouldn't get here.
        }
        catch (FileProcessingException e)
        {
            String errMsg = "Unknown receiver name: " + detectionParams[DetectionFactoryService.RECEIVER_COLUMN]
            assertEquals(errMsg, e.getMessage())
        }
    }
    

    void testNoDeployment()
    {
        def detectionParams = setupBasicEntities()
        receiver.removeFromDeployments(deployment)
        receiver.save()
        
        try
        {
            Detection detection = service.newDetection(detectionParams)
            fail()  // shouldn't get here.
        }
        catch (FileProcessingException e)
        {
            String errMsg = "No deployments for receiver: " + String.valueOf(receiver) \
                            + ", detectionDate: " + String.valueOf(new Date().parse(DetectionFactoryService.DATE_FORMAT, dateString))
            assertEquals(errMsg, e.getMessage())
        }
    }
    
    void testOneDeploymentAfterDetectionDate()
    {
        def detectionParams = setupBasicEntities()
        Detection detection = service.newDetection(detectionParams)
        assertNotNull(detection)
        
        // Now alter the detection date to be before deployment.
        detection.delete()
        String earlyDateString = "2010-01-10 12:34:56" + " " + "UTC"
        detectionParams[DetectionFactoryService.DATE_AND_TIME_COLUMN] = earlyDateString
        
        try
        {
            detection = service.newDetection(detectionParams)
            fail()  // shouldn't get here.
        }
        catch (FileProcessingException e)
        {
            String errMsg = "No deployments for receiver: " + String.valueOf(receiver) \
                            + ", detectionDate: " + String.valueOf(new Date().parse(DetectionFactoryService.DATE_FORMAT, earlyDateString))
            assertEquals(errMsg, e.getMessage())
        }
    }
    
    void testOneRecoveryBeforeDetectionDate()
    {
        def detectionParams = setupBasicEntities()
        Detection detection = service.newDetection(detectionParams)
        assertNotNull(detection)
        
        // Now alter the detection date to be before deployment.
        detection.delete()
        String lateDateString = "2013-01-10 12:34:56" + " " + "UTC"
        detectionParams[DetectionFactoryService.DATE_AND_TIME_COLUMN] = lateDateString
        
        try
        {
            detection = service.newDetection(detectionParams)
            fail()  // shouldn't get here.
        }
        catch (FileProcessingException e)
        {
            String errMsg = "No deployments for receiver: " + String.valueOf(receiver) \
                            + ", detectionDate: " + String.valueOf(new Date().parse(DetectionFactoryService.DATE_FORMAT, lateDateString))
            assertEquals(errMsg, e.getMessage())
        }
    }
    
//    void testMultipleDeployments()    // not sure?
//    

}
