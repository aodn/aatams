package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import grails.test.*
import com.vividsolutions.jts.geom.*

import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import org.apache.commons.lang.StringUtils

class DetectionFactoryServiceTests extends AbstractDetectionFactoryServiceTests
{
    def download

    protected void setUp()
    {
        super.setUp()

        mockLogging(DetectionFactoryService, true)
        detectionFactoryService = new DetectionFactoryService()

        mockLogging(DetectionValidator, true)

        download = new ReceiverDownloadFile()
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    void testValid()
    {
        def validDetection = newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertEquals("2009-12-08 06:44:24", validDetection.formattedTimestamp)

        assertNotNull(validDetection)
        assertTrue(validDetection instanceof ValidDetection)
        assertTrue(validDetection.isProvisional())
        assertNotNull(ValidDetection.findByTimestamp(validDetection.timestamp))

        assertEquals(deployment.id, validDetection.receiverDeployment.id)
        assertEquals(receiver.id, validDetection.receiverDeployment.receiver.id)
    }

    void testValidSensor()
    {
        def validDetection = newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV),
                standardParams + [(VueDetectionFormat.SENSOR_VALUE_COLUMN):123, (VueDetectionFormat.SENSOR_UNIT_COLUMN):"ADC"])

        assertNotNull(validDetection)
        assertTrue(validDetection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(validDetection.timestamp))

        assertEquals(deployment.id, validDetection.receiverDeployment.id)
        assertEquals(receiver.id, validDetection.receiverDeployment.receiver.id)
    }

    void testUnknownReceiver()
    {
        standardParams[(VueDetectionFormat.RECEIVER_COLUMN)] = "VR2W-1234"
        def unknownReceiverDetection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(unknownReceiverDetection)
        assertTrue(unknownReceiverDetection instanceof InvalidDetection)
        assertEquals(InvalidDetectionReason.UNKNOWN_RECEIVER, unknownReceiverDetection.reason)
        assertEquals("Unknown receiver code name VR2W-1234", unknownReceiverDetection.message)

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
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

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
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

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
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertTrue(detection.detectionSurgeries?.isEmpty())
    }

    void testOneMatchingPingerSurgery()
    {
        setupData()

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        def detectionSurgery = DetectionSurgery.list()[0]
        assertEquals(1, DetectionSurgery.count())
        assertEquals(sensor0, detectionSurgery.sensor)
        assertEquals(surgery, detectionSurgery.surgery)
    }

    void testOneMatchingSensorSurgery()
    {
        setupData()
        standardParams[(VueDetectionFormat.TRANSMITTER_COLUMN)] = "A69-1303-12345"

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        def detectionSurgery = DetectionSurgery.list()[0]
        assertEquals(1, DetectionSurgery.count())
        assertEquals(sensor, detectionSurgery.sensor)
        assertEquals(surgery, detectionSurgery.surgery)
    }

    void testMultipleMatchingSurgeries()
    {
        setupData()
        standardParams[(VueDetectionFormat.TRANSMITTER_COLUMN)] = "A69-1303-1111"

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertEquals(2, DetectionSurgery.count())

        def detectionSurgery1 = DetectionSurgery.list()[0]
        assertEquals(sensor1, detectionSurgery1.sensor)
        assertEquals(surgery1, detectionSurgery1.surgery)

        def detectionSurgery2 = DetectionSurgery.list()[1]
        assertEquals(sensor2, detectionSurgery2.sensor)
        assertEquals(surgery2, detectionSurgery2.surgery)
    }

    void testReleaseStatusNonCurrent()
    {
        setupData()

        release.status = AnimalReleaseStatus.FINISHED

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertTrue(detection.detectionSurgeries.isEmpty())
    }

    void testTagRetired()
    {
        setupData()
        standardParams[(VueDetectionFormat.TRANSMITTER_COLUMN)] = "A69-1303-1111"

        DeviceStatus retiredStatus = new DeviceStatus(status:DeviceStatus.RETIRED)
        mockDomain(DeviceStatus, [retiredStatus])

        tag1.setStatus(retiredStatus)
        tag1.save()

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertEquals(1, DetectionSurgery.count())
        def detectionSurgery = DetectionSurgery.list()[0]
        assertEquals(sensor2, detectionSurgery.sensor)
        assertEquals(surgery2, detectionSurgery.surgery)
    }

    void testBeforeTagWindow()
    {
        setupData()
        standardParams[(VueDetectionFormat.TRANSMITTER_COLUMN)] = "A69-1303-1111"

        release1.releaseDateTime = new DateTime("2009-12-09T06:44:24")
        release1.save()

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

           assertEquals(1, DetectionSurgery.count())
        def detectionSurgery = DetectionSurgery.list()[0]
        assertEquals(sensor2, detectionSurgery.sensor)
        assertEquals(surgery2, detectionSurgery.surgery)
    }

    void testAfterTagWindow()
    {
        setupData()
        standardParams[(VueDetectionFormat.TRANSMITTER_COLUMN)] = "A69-1303-1111"

        surgery1.grailsApplication = [config:[tag:[expectedLifeTime:[gracePeriodDays:10]]]]
        surgery2.grailsApplication = [config:[tag:[expectedLifeTime:[gracePeriodDays:10]]]]

        tag1.expectedLifeTimeDays = 10
        tag1.save()

        release1.releaseDateTime = new DateTime("2009-11-18T06:44:24")
        release1.save()

        assertEquals(0, DetectionSurgery.count())

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        assertEquals(1, DetectionSurgery.count())
        def detectionSurgery = DetectionSurgery.list()[0]
        assertEquals(sensor2, detectionSurgery.sensor)
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
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertEquals(3, DetectionSurgery.count())
        def detectionSurgery1 = DetectionSurgery.list()[1]
        assertEquals(sensor1, detectionSurgery1.sensor)
        assertEquals(surgery1, detectionSurgery1.surgery)
        def detectionSurgery2 = DetectionSurgery.list()[2]
        assertEquals(sensor2, detectionSurgery2.sensor)
        assertEquals(surgery2, detectionSurgery2.surgery)
    }

    void testRescanForSurgery()
    {
        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

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
        assertEquals(sensor0, detectionSurgery.sensor)
        assertEquals(surgery, detectionSurgery.surgery)
    }

    void testRescanForDeploymentBug69()
    {

    }

    void testBuildRescanDeploymentSql()
    {
        def sql = '''insert into valid_detection (id, version, location, receiver_deployment_id, receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number, provisional)
(select id, version, location, 123, receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number, true as provisional from invalid_detection
where reason <> 'DUPLICATE' and receiver_name = 'VR2W-102026' and timestamp between '2009-05-19T10:18:00+10:00' AND '2011-05-19T10:18:00+10:00'
group by receiver_name, transmitter_id, timestamp, id, version, location, message, reason, receiver_download_id, sensor_unit, sensor_value, station_name, transmitter_name, transmitter_serial_number
);
delete from invalid_detection
where reason <> 'DUPLICATE' and receiver_name = 'VR2W-102026' and timestamp between '2009-05-19T10:18:00+10:00' AND '2011-05-19T10:18:00+10:00';'''

        def deployment = [id: 123, receiver: [name: 'VR2W-102026'], deploymentDateTime: new DateTime("2009-05-19T10:18:00"), recovery: [recoveryDateTime: new DateTime("2011-05-19T10:18:00")]]
        assertEquals(StringUtils.deleteWhitespace(sql), StringUtils.deleteWhitespace(detectionFactoryService.buildRescanDeploymentSql(deployment)))
    }
}
