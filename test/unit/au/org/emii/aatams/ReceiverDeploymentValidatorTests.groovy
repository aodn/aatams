package au.org.emii.aatams

import org.joda.time.DateTime
import org.joda.time.Interval

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*

class ReceiverDeploymentValidatorTests extends GrailsUnitTestCase {
    static def now = new DateTime()

    void setUp() {
        super.setUp()

        JodaOverrides.apply()
        mockDomain(ReceiverDeployment)
    }

    void tearDown() {
        JodaOverrides.unmock()
        super.tearDown()
    }

    void testConflictingDeployments() {

        def otherDeployments = [
            [ undeployableInterval: new Interval(now, now.plusDays(2)), same: { false } ]
        ]
        def initialisationDateTime = now.plusDays(1)
        def deploymentDateTime = now.plusDays(1)

        def expectValid = false

        assertConflictingDeployment(otherDeployments, now.plusDays(1), now.plusDays(1), null, null, true)
        assertConflictingDeployment(otherDeployments, null, now.plusDays(1), null, null, true)
        assertConflictingDeployment(otherDeployments, now.minusDays(1), now.minusDays(1), now.plusDays(3), DeviceStatus.RECOVERED, true)

        assertConflictingDeployment(otherDeployments, now.minusDays(1), now.minusDays(1), null, null, false)
        assertConflictingDeployment(otherDeployments, now.plusDays(3), now.plusDays(3), null, null, false)
        assertConflictingDeployment(otherDeployments, now.plusDays(3), now.plusDays(3), now.plusDays(4), DeviceStatus.RECOVERED, false)

        assertConflictingDeployment(otherDeployments, now.minusDays(2), now.minusDays(2), now.minusDays(1), DeviceStatus.LOST, true)

        otherDeployments = [
            [ undeployableInterval: new OpenInterval(now), same: { false } ]
        ]

        assertConflictingDeployment(otherDeployments, now.plusDays(1), now.plusDays(1), null, null, true)
    }

    void assertConflictingDeployment(otherDeployments, initialisationDateTime, deploymentDateTime, recoveryDateTime, recoveryStatus, expectConflict) {

        def receiver = new Receiver()
        receiver.metaClass.deployments = otherDeployments

        def deployment = new ReceiverDeployment(
            receiver: receiver,
            station: new InstallationStation(),
            mooringType: new MooringType(),
            initialisationDateTime: initialisationDateTime,
            deploymentDateTime: deploymentDateTime
        )

        mockDomain(ReceiverRecovery)
        def recovery
        if (recoveryDateTime && recoveryStatus) {
            recovery = new ReceiverRecovery(
                deployment: deployment,
                recoveryDateTime: recoveryDateTime,
                status: recoveryStatus,
                location: new WKTReader().read('POINT(1 2)'),
                recoverer: new ProjectRole()
            )
            deployment.recovery = recovery
        }

        assertTrue(deployment.validate() != expectConflict)
        if (recovery) {
            recovery.validate()
            println recovery.errors
            assertTrue(recovery.validate() != expectConflict)
        }
    }
}
