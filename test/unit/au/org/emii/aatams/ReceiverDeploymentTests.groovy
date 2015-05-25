package au.org.emii.aatams

import java.util.Date
import java.util.Set

import org.joda.time.DateTime

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*

class ReceiverDeploymentTests extends GrailsUnitTestCase
{
    void testScheduledRecoveryDateBeforeDeploymentDate()
    {
        WKTReader reader = new WKTReader()

        ReceiverDeployment deployment =
            new ReceiverDeployment(station: new InstallationStation(),
                                   receiver: new Receiver(),
                                   deploymentNumber: 1,
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

        mockDomain(ReceiverDeployment, [deployment])
        deployment.save()

        assertTrue(deployment.hasErrors())
    }

    void testIsActive()
    {
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

    void testIsActiveAtTime()
    {
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
}
