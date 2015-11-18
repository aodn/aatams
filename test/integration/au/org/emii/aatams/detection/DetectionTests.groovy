package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
import org.joda.time.DateTime

class DetectionTests extends GroovyTestCase {

    def dataSource

    def now = new DateTime()
    def today = now
    def yesterday = now.minusDays(1)
    def dayBeforeYesterday = yesterday.minusDays(1)
    def tomorrow = now.plusDays(1)
    def dayAfterTomorrow = tomorrow.plusDays(1)

    void testInvalidReasonNoReceiver() {
        def detection = createDetection()

        assertInvalidReason(InvalidDetectionReason.UNKNOWN_RECEIVER, detection)
    }

    void testInvalidReasonNoDeployment() {
        createReceiver()
        def detection = createDetection()

        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)
    }

    void testInvalidReasonNoRecovery() {
        def receiver = createReceiver()
        createDeployment(receiver: receiver, initDateTime: now)
        def detection = createDetection()

        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)
    }

    void testInvalidReasonRecoveryInitDateAfter() {
        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: tomorrow)
        createRecovery(deployment: deployment, recoveryDateTime: dayAfterTomorrow)
        def detection = createDetection()

        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)
    }

    void testInvalidReasonRecoveryRecoveryDateBefore() {
        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: dayBeforeYesterday)
        createRecovery(deployment: deployment, recoveryDateTime: yesterday)
        def detection = createDetection()

        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)
    }

    void testInvalidReasonDuplicate() {
        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: dayBeforeYesterday)
        createRecovery(deployment: deployment, recoveryDateTime: tomorrow)
        def detection = createDetection()

        def duplicateDetection = createDetection()

        assertInvalidReason(null, detection)
        assertInvalidReason(InvalidDetectionReason.DUPLICATE, duplicateDetection)
    }

    void testValid() {
        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: dayBeforeYesterday)
        createRecovery(deployment: deployment, recoveryDateTime: tomorrow)
        def detection = createDetection()

        assertInvalidReason(null, detection)
    }

    void testInvalidReasonChangesDynamically() {
        // Test as the detection progressively satisfies more of the validity criteria...
        def detection = createDetection()
        assertInvalidReason(InvalidDetectionReason.UNKNOWN_RECEIVER, detection)

        def receiver = createReceiver()
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)

        def deployment = createDeployment(receiver: receiver, initDateTime: dayBeforeYesterday)
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)

        def recovery = createRecovery(deployment: deployment, recoveryDateTime: yesterday)
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)

        recovery.recoveryDateTime = tomorrow
        recovery.save(flush: true)
        detection.refresh(dataSource)
        assertInvalidReason(null, detection)

        // ... now go backwards.
        deployment.recovery = null
        recovery.recoverer.recoveries.remove(recovery)
        recovery.delete(flush: true)
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)

        receiver.deployments.remove(deployment)
        deployment.station.deployments.remove(deployment)
        deployment.delete(flush: true)
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)

        receiver.organisation.receivers.remove(receiver)
        receiver.delete(flush: true)
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.UNKNOWN_RECEIVER, detection)
    }

    def testNoSurgery() {
        def detection1 = createDetection(yesterday)
        def detection2 = createDetection(tomorrow)

        def d1 = DetectionView.get(detection1.id, dataSource)
        assertEquals('', d1.getSpeciesName())

        def d2 = DetectionView.get(detection2.id, dataSource)
        assertEquals('', d2.getSpeciesName())
    }

    def testWithSurgery() {

        def detectionHasExpectedFields = { detection, expectedSpecies ->
            assertEquals(expectedSpecies, detection.getSpeciesName())
            assertNotNull(detection.embargoDate)
            assertNotNull(detection.releaseId)
            assertNotNull(detection.releaseProjectId)
            assertNotNull(detection.surgeryId)
        }

        def detectionHasNullFields = { detection ->
            assertEquals('', detection.getSpeciesName())
            assertNull(detection.embargoDate)
            assertNull(detection.releaseId)
            assertNull(detection.releaseProjectId)
            assertNull(detection.surgeryId)
        }

        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: dayBeforeYesterday)
        createRecovery(deployment: deployment, recoveryDateTime: dayAfterTomorrow)

        def whiteShark= Animal.findBySpecies( Species.findByNameLike('%Carcharodon carcharias%'))
        def southernBluefinTuna= Animal.findBySpecies( Species.findByNameLike('%Southern Bluefin Tuna%'))

        def detection1 = createDetection(dayBeforeYesterday)
        // create surgery 1
        createSurgery(yesterday, whiteShark)
        def detection2 = createDetection(today)
        // surgery 2 will be created here
        def detection3 = createDetection(dayAfterTomorrow)

        // test detection d1 before surgery has null fields
        def d1 = DetectionView.get(detection1.id, dataSource)
        detectionHasNullFields(d1)

        // test detection 2 after surgery has expected fields
        def d2 = DetectionView.get(detection2.id, dataSource)
        detectionHasExpectedFields(d2, '37010003 - Carcharodon carcharias (White Shark)')

        // test detection 3 after surgery also has expected fields
        def d3 = DetectionView.get(detection3.id, dataSource)
        detectionHasExpectedFields(d3, '37010003 - Carcharodon carcharias (White Shark)')
        
        // create surgery 2
        createSurgery(tomorrow, southernBluefinTuna)
        d2.refresh(dataSource)
        d3.refresh(dataSource)

        // d2 should be unchanged and refer to surgery 1
        detectionHasExpectedFields(d2, '37010003 - Carcharodon carcharias (White Shark)')

        // d3 should refer to second surgery
        detectionHasExpectedFields(d3, '37441004 - Thunnus maccoyii (Southern Bluefin Tuna)')
    }

    // TODO
    // done - test to make sure the detection is associated with the correct surgery in time
    // done - make sure that all the required fields are null, when detection occurs before the surgery
    // performance
    // done - test multiple case of multiple surgeries, that the result is between

    def newPoint() {
        return new GeometryFactory().createPoint(new Coordinate(1, 2))
    }

    def createSurgery(timestamp, animal) {

        def project = Project.list()[0]

        def method = CaptureMethod.list()[0]

        AnimalRelease release = new AnimalRelease(
                project: project,
                surgeries: [],
                measurements: [],
                embargoDate: new Date(2050 - 1900, 1, 1),
                animal:animal,
                captureLocality: 'Neptune Islands',
                captureLocation: newPoint(),
                captureDateTime: new DateTime("2010-02-14T15:10:00"),
                captureMethod: method,
                releaseLocality: 'Neptune Islands',
                releaseLocation: newPoint(),
                releaseDateTime: new DateTime("2010-02-14T14:15:00")).save(failOnError: true)

        def pinger = TransmitterType.findByTransmitterTypeName('PINGER')

        def sensor = Sensor.buildLazy( pingCode: 6789, transmitterType: pinger).save(flush: true)

        Tag tag = Tag.buildLazy(serialNumber: '1000001', codeMap: CodeMap.findByCodeMap('A69-1303')).save(flush: true)

        tag.addToSensors(sensor)
        tag.save(flush: true, failOnError: true)

        def type = SurgeryType.list()[0]
        def treatmentType = SurgeryTreatmentType.list()[0]

        Surgery surgery = new Surgery(
                release: release,
                tag: tag,
                timestamp: timestamp,   // the important bit
                type: type,
                treatmentType: treatmentType)

        tag.addToSurgeries(surgery).save(failOnError: true)
        release.addToSurgeries(surgery).save(failOnError: true)

        return surgery.save(flush: true, failOnError: true)
    }
    def createDetection(timestamp = now) {

        // println "here1";

        def download = ReceiverDownloadFile.buildLazy().save(flush: true)
        return new Detection(timestamp: timestamp,
                             receiverName: 'VR2W-7654',
                             transmitterId: 'A69-1303-6789',
                             receiverDownloadId: download.id).save(dataSource)


    }




    def createReceiver() {
        return Receiver.buildLazy(model: ReceiverDeviceModel.buildLazy(modelName: 'VR2W'),
                                  serialNumber: '7654').save(flush: true)
    }

    def createDeployment(p) {
        return ReceiverDeployment.buildLazy(
            receiver: p.receiver,
            initialisationDateTime: p.initDateTime,
            deploymentDateTime: p.initDateTime
        ).save(flush: true)
    }

    def createRecovery(p) {
        return ReceiverRecovery.buildLazy(deployment: p.deployment, recoveryDateTime: p.recoveryDateTime).save(flush: true)
    }

    void assertInvalidReason(expectedReason, detection) {
        assertEquals(expectedReason ? expectedReason.toString() : expectedReason, detection.invalidReason)
    }
}
