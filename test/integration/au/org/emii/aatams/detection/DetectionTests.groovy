package au.org.emii.aatams.detection

import au.org.emii.aatams.*

import grails.test.*
import groovy.sql.Sql
import org.joda.time.DateTime

class DetectionTests extends GroovyTestCase {

    def dataSource
    def now = new DateTime()

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
        def deployment = createDeployment(receiver: receiver, initDateTime: now.plusDays(1))
        def recovery = createRecovery(deployment: deployment, recoveryDateTime: now.plusDays(2))
        def detection = createDetection()

        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)
    }

    void testInvalidReasonRecoveryRecoveryDateBefore() {
        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: now.minusDays(2))
        def recovery = createRecovery(deployment: deployment, recoveryDateTime: now.minusDays(1))
        def detection = createDetection()

        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)
    }

    void testInvalidReasonDuplicate() {
        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: now.minusDays(2))
        def recovery = createRecovery(deployment: deployment, recoveryDateTime: now.plusDays(1))
        def detection = createDetection()

        def duplicateDetection = createDetection()

        assertInvalidReason(null, detection)
        assertInvalidReason(InvalidDetectionReason.DUPLICATE, duplicateDetection)
    }

    void testValid() {
        def receiver = createReceiver()
        def deployment = createDeployment(receiver: receiver, initDateTime: now.minusDays(2))
        def recovery = createRecovery(deployment: deployment, recoveryDateTime: now.plusDays(1))
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

        def deployment = createDeployment(receiver: receiver, initDateTime: now.minusDays(2))
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)

        def recovery = createRecovery(deployment: deployment, recoveryDateTime: now.minusDays(1))
        detection.refresh(dataSource)
        assertInvalidReason(InvalidDetectionReason.NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME, detection)

        recovery.recoveryDateTime = now.plusDays(1)
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

    def createDetection() {
        def download = ReceiverDownloadFile.buildLazy().save(flush: true)
        return new Detection(timestamp: now,
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
