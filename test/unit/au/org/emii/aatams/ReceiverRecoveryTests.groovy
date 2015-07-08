package au.org.emii.aatams

import org.joda.time.DateTime
import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*

class ReceiverRecoveryTests extends GrailsUnitTestCase {

    def now = new DateTime()

    void setUp() {
        super.setUp()

        mockDomain(ReceiverDeployment)
        mockDomain(ReceiverRecovery)
    }

    void testRecoveryDateTimeValidation() {
        assertRecoveryDateTimeValidation(now.minusDays(1), now, true)
        assertRecoveryDateTimeValidation(now, now, false)
        assertRecoveryDateTimeValidation(now, now.minusDays(1), false)
    }

    def assertRecoveryDateTimeValidation(deploymentDateTime, recoveryDateTime, expectValid) {
        def deployment = new ReceiverDeployment(deploymentDateTime: deploymentDateTime)
        def recovery = new ReceiverRecovery(
            location: new WKTReader().read("POINT(30.1234 30.1234)"),
            recoverer: new ProjectRole(),
            status: DeviceStatus.RECOVERED,
            recoveryDateTime: recoveryDateTime,
            deployment: deployment
        )

        assertTrue(expectValid == recovery.validate())
    }
}
