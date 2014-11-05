package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import grails.test.*
import groovy.sql.Sql
import org.joda.time.DateTime

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

class DetectionFactoryServiceIntegrationTests extends GroovyTestCase {

    def dataSource
    def detectionFactoryService

    // Test for #1751
    void testRescanForDeployment()
    {
        def codeMap = CodeMap.build(codeMap: 'A69-1303')
        def releasedTag = Tag.build(codeMap: codeMap, serialNumber: '46601')
        def pinger46601 = Sensor.build(tag: releasedTag, pingCode: 46601)
        def release = AnimalRelease.build(releaseDateTime: new DateTime("2011-05-15T14:15:00"))
        def surgery = Surgery.build(release: release,
                                    tag: releasedTag)

        def receiverDownload = ReceiverDownloadFile.build()

        def sql = new Sql(dataSource)
        ValidDetection.metaClass.static.count = {
            return sql.firstRow('select count(*) from valid_detection').count
        }

        assertEquals(0, DetectionSurgery.findAllBySensor(pinger46601).size())
        assertEquals(0, ValidDetection.count())
        assertEquals(0, InvalidDetection.count())

        // 1) invaliddetection - inside time range - no deployment
        InvalidDetection insideTimeRangeUnknownReceiver = createInvalidDetection(reason: InvalidDetectionReason.UNKNOWN_RECEIVER)
        InvalidDetection insideTimeRangeNoDeployment = createInvalidDetection(reason: InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME)
        InvalidDetection insideTimeRangeNoRecovery = createInvalidDetection(reason: InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME)

        // 2) invaliddetection - outside time range
        InvalidDetection beforeTimeRange = createInvalidDetection(timestamp: new DateTime("2009-01-01T00:00:00").toDate(), reason: InvalidDetectionReason.UNKNOWN_RECEIVER)
        InvalidDetection afterTimeRange = createInvalidDetection(timestamp: new DateTime("2025-01-01T00:00:00").toDate(), reason: InvalidDetectionReason.UNKNOWN_RECEIVER)

        // 3) invaliddetection - other reason
        InvalidDetection insideTimeRangeDuplicate = createInvalidDetection(reason: InvalidDetectionReason.DUPLICATE)
        InvalidDetection insideTimeRangeDifferentReceiver = createInvalidDetection(receiverName: "VR2W-5678", reason: InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME)

        assertEquals(0, ValidDetection.count())
        assertEquals(7, InvalidDetection.count())

        def rx1 = Receiver.build(model: ReceiverDeviceModel.build(modelName: 'VR2W'), serialNumber: '101336')
        def deploymentBondi1 =
            ReceiverDeployment.build(receiver: rx1,
                                     initialisationDateTime: new DateTime("2010-02-15T00:34:56+10:00"),
                                     deploymentDateTime: new DateTime("2010-02-15T12:34:56+10:00"))

        def recovery =
            ReceiverRecovery.build(recoveryDateTime: new DateTime("2020-01-01T00:00:00+11:00"),
                                   deployment: deploymentBondi1)
        deploymentBondi1.recovery = recovery

        detectionFactoryService.rescanForDeployment(deploymentBondi1)

        assertEquals(3, ValidDetection.count())
        assertEquals(4, InvalidDetection.count())

        // assert that DetectionSurgeries have been created.
        assertEquals(3, DetectionSurgery.findAllBySensor(pinger46601).size())

        // These should all still be invalid.
        [beforeTimeRange, afterTimeRange, insideTimeRangeDuplicate, insideTimeRangeDifferentReceiver].each {
            assertNotNull(InvalidDetection.get(it.id))
        }

        // TODO: cleanup
    }


    private def createInvalidDetection(params)
    {
        def timestamp = params.timestamp ?: new DateTime("2012-01-01T00:00:00").toDate()
        def receiverName = params.receiverName ?: "VR2W-101336"

        def det =
            new InvalidDetection(
                timestamp: timestamp,
                receiverName: receiverName,
                reason: params.reason,
                message: "some message",
                transmitterId: "A69-1303-46601",
                receiverDownload: ReceiverDownloadFile.list()[0])

        det.save(failOnError: true)

        return det
    }
}
