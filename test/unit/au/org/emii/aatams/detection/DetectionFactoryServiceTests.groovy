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

    void testValid()
    {
        def validDetection = newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertEquals("2009-12-08 06:44:24", validDetection.formattedTimestamp)

        assertNotNull(validDetection)
        assertTrue(validDetection instanceof ValidDetection)
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

        assertTrue(detection.surgeries.isEmpty())
    }

    void testOneMatchingPingerSurgery()
    {
        setupData()

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        def matchingSurgery = detection.mostRecentSurgery
        assertNotNull(matchingSurgery)
        assertEquals(surgery, matchingSurgery)
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

        def matchingSurgery = detection.mostRecentSurgery
        assertNotNull(matchingSurgery)
        assertEquals(surgery, matchingSurgery)
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

        def surgeries = detection.surgeries
        assertEquals(surgery1, surgeries[0])
        assertEquals(surgery2, surgeries[1])
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

        assertTrue(detection.surgeries.isEmpty())
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

        assertEquals(1, detection.surgeries.size())
        assertEquals(surgery2, detection.surgeries[0])
        assertTrue(detection.surgeries[0].tag.sensors.contains(sensor2))
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

        def surgeries = detection.surgeries

        assertEquals(1, surgeries.size())
        assertEquals(surgery2, surgeries[0])
        assertTrue(surgeries[0].tag.sensors.contains(sensor2))
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

        def detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        assertNotNull(detection)
        assertTrue(detection instanceof ValidDetection)
        assertNotNull(ValidDetection.findByTimestamp(detection.timestamp))

        def surgeries = detection.surgeries
        assertEquals(1, surgeries.size())
        assertEquals(surgery2, surgeries[0])

        // So we don't get a duplicate.
        detection.setTimestamp(new Date())
        detection.save()

        // Move the release time so that detection is now within the window.
        release1.releaseDateTime = new DateTime("2009-11-19T06:44:24")
        release1.save()

        // "2009-12-08 06:44:24"
        detection =
            newDetection(new ReceiverDownloadFile(type: ReceiverDownloadFileType.DETECTIONS_CSV), standardParams)

        surgeries = detection.surgeries
        assertEquals(2, surgeries.size())
        assertEquals(surgery1, surgeries[0])
        assertEquals(surgery2, surgeries[1])
    }

    void testBuildRescanDeploymentSql()
    {
        def sql = '''insert into valid_detection (id, version, location, receiver_deployment_id, receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number)
(select id, version, location, 123, receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number from invalid_detection
where reason <> 'DUPLICATE' and receiver_name = 'VR2W-102026' and timestamp between '2009-05-19T10:18:00+10:00' AND '2011-05-19T10:18:00+10:00'
group by receiver_name, transmitter_id, timestamp, id, version, location, message, reason, receiver_download_id, sensor_unit, sensor_value, station_name, transmitter_name, transmitter_serial_number
);
delete from invalid_detection
where reason <> 'DUPLICATE' and receiver_name = 'VR2W-102026' and timestamp between '2009-05-19T10:18:00+10:00' AND '2011-05-19T10:18:00+10:00';'''

        def deployment = [id: 123, receiver: [name: 'VR2W-102026'], deploymentDateTime: new DateTime("2009-05-19T10:18:00"), recovery: [recoveryDateTime: new DateTime("2011-05-19T10:18:00")]]
        assertEquals(StringUtils.deleteWhitespace(sql), StringUtils.deleteWhitespace(detectionFactoryService.buildRescanDeploymentSql(deployment)))
    }
}
