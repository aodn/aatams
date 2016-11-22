package au.org.emii.aatams

import org.joda.time.DateTime
import org.joda.time.Interval

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*

class ReceiverDeploymentTests extends GrailsUnitTestCase {
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

    void testScheduledRecoveryDateBeforeDeploymentDate() {
        WKTReader reader = new WKTReader()

        ReceiverDeployment deployment =
            new ReceiverDeployment(station: new InstallationStation(),
                                   receiver: new Receiver(),
                                   deploymentDateTime:new DateTime(),
                                   recoveryDate:new DateTime().minusHours(1).toDate(),
                                   acousticReleaseID:"1234",
                                   mooringType:new MooringType(),
                                   mooringDescriptor:"concrete",
                                   bottomDepthM:23.3f,
                                   depthBelowSurfaceM:12.3f,
                                   receiverOrientation:ReceiverOrientation.UP,
                                   location:(Point)reader.read("POINT(30.1234 30.1234)"),
                                   batteryLifeDays:60,
                                   comments:"some comment")

        deployment.save()

        assertTrue(deployment.hasErrors())
    }

    void testIsActive() {
        def deploymentDateTime = new DateTime().plusDays(1)
        ReceiverDeployment deployment = new ReceiverDeployment(deploymentDateTime:deploymentDateTime)
        assertFalse(deployment.isActive())

        deployment.deploymentDateTime = new DateTime().minusDays(1)
        assertTrue(deployment.isActive())

        def recoveryDateTime = new DateTime().plusDays(3)
        ReceiverRecovery recovery = new ReceiverRecovery(deployment: deployment, recoveryDateTime: recoveryDateTime)
        deployment.recovery = recovery
        assertTrue(deployment.isActive())

        deployment.deploymentDateTime = new DateTime().plusDays(1)
        assertFalse(deployment.isActive())
    }

    void testIsActiveAtTime() {
        def deploymentDateTime = new DateTime().plusDays(1)
        ReceiverDeployment deployment = new ReceiverDeployment(deploymentDateTime:deploymentDateTime)
        assertFalse(deployment.isActive())
        assertFalse(deployment.isActive(deploymentDateTime.minusDays(1)))
        assertTrue(deployment.isActive(deploymentDateTime))
        assertTrue(deployment.isActive(deploymentDateTime.plusDays(1)))

        deploymentDateTime = new DateTime().minusDays(1)
        deployment.deploymentDateTime = deploymentDateTime
        assertTrue(deployment.isActive())
        assertFalse(deployment.isActive(deploymentDateTime.minusDays(1)))
        assertTrue(deployment.isActive(deploymentDateTime))
        assertTrue(deployment.isActive(deploymentDateTime.plusDays(1)))

        def recoveryDateTime = new DateTime().plusDays(3)
        ReceiverRecovery recovery = new ReceiverRecovery(deployment: deployment, recoveryDateTime: recoveryDateTime)
        deployment.recovery = recovery
        assertTrue(deployment.isActive())
        assertFalse(deployment.isActive(deploymentDateTime.minusDays(1)))
        assertTrue(deployment.isActive(deploymentDateTime))
        assertTrue(deployment.isActive(deploymentDateTime.plusDays(1)))
        assertTrue(deployment.isActive(recoveryDateTime.minusDays(1)))
        assertFalse(deployment.isActive(recoveryDateTime))
        assertFalse(deployment.isActive(recoveryDateTime.plusDays(1)))

        deploymentDateTime = new DateTime().plusDays(1)
        deployment.deploymentDateTime = deploymentDateTime
        assertFalse(deployment.isActive())
        assertFalse(deployment.isActive(deploymentDateTime.minusDays(1)))
        assertTrue(deployment.isActive(deploymentDateTime))
        assertTrue(deployment.isActive(deploymentDateTime.plusDays(1)))
        assertTrue(deployment.isActive(recoveryDateTime.minusDays(1)))
        assertFalse(deployment.isActive(recoveryDateTime))
        assertFalse(deployment.isActive(recoveryDateTime.plusDays(1)))
    }

    void testToString() {
        def deployment = [
            receiver: [ toString: { 'VR2W-1234' } ] as Receiver,
                          station: [ toString: { 'BL1' } ] as InstallationStation,
                          deploymentDateTime: new DateTime('2014-06-01T12:34:56+10:00')
        ] as ReceiverDeployment

        assertEquals('VR2W-1234 @ BL1, deployed 2014-06-01T12:34:56+10:00', deployment.toString())
    }

    void testInitAndDeployDateTimeValidation() {
        assertDateTimeValidation(null, now, true)
        assertDateTimeValidation(now, now, true)
        assertDateTimeValidation(now, now.plusDays(1), true)

        assertDateTimeValidation(now.plusDays(1), now, false)
    }

    def assertDateTimeValidation(initialisationDateTime, deploymentDateTime, expectValid) {
       def deployment = new ReceiverDeployment(
           mooringType: new MooringType(),
           receiver: new Receiver(),
           station: new InstallationStation(),
           initialisationDateTime: initialisationDateTime,
           deploymentDateTime: deploymentDateTime
       )

       assertTrue(expectValid == deployment.validate())
    }
}
