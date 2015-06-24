package au.org.emii.aatams

import org.joda.time.DateTime

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*

class ReceiverTests extends GrailsUnitTestCase
{
    Receiver rx1
    Receiver rx2

    protected void setUp() {
        super.setUp()

        JodaOverrides.mock()

        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName: "VR2W")
        mockDomain(ReceiverDeviceModel, [model])

        rx1 = new Receiver(serialNumber:"1111",
                           model:model,
                           organisation:new Organisation())
        rx2 = new Receiver(serialNumber:"2222",
                           model:model,
                           organisation:new Organisation())

        mockDomain(Receiver, [rx1, rx2])
        mockForConstraintsTests(Receiver, [rx1, rx2])


        mockDomain(ReceiverDeployment)
        mockDomain(ReceiverRecovery)
        mockLogging(Receiver)
    }

    protected void tearDown() {
        JodaOverrides.unmock()
        super.tearDown()
    }

    void testNonUniqueSerialNumber() {
        rx2.serialNumber = rx1.serialNumber
        assertFalse(rx2.validate())
    }

    void testName() {
        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName:"VR2W")
        mockDomain(ReceiverDeviceModel, [model])
        model.save()

        def rxr = new Receiver(serialNumber: "12345", model: model)

        assertEquals("VR2W-12345", rxr.name)
    }

    void testFindByName() {
        assertNotNull(Receiver.findByName("VR2W-1111"))
        assertNotNull(Receiver.findByName("VR2W-1111", [cache:true]))
        assertNull(Receiver.findByName("VR2W-3333"))

        try
        {
            Receiver.findByName("VR2W111")
            fail()
        }
        catch (AssertionError e)
        {
            assertEquals("Invalid receiver name: VR2W111. Expression: (tokens.size() >= 2)", e.getMessage())
        }
    }

    void testFindByNameWhereModelIncludesHyphen() {
        ReceiverDeviceModel vr4UwmModel = new ReceiverDeviceModel(modelName: "VR4-UWM")
        mockDomain(ReceiverDeviceModel, [vr4UwmModel])
        vr4UwmModel.save()

        Receiver vr4uwmRxr = new Receiver(model: vr4UwmModel, serialNumber: '1234')
        mockDomain(Receiver, [vr4uwmRxr])
        vr4uwmRxr.save()

        assertEquals(vr4uwmRxr, Receiver.findByName('VR4-UWM-1234'))
    }

    void testStatusNoDeployments() {
        Receiver newReceiver = new Receiver()
        newReceiver.save()

        assertStatus(DeviceStatus.NEW, newReceiver)
    }

    void testStatusWithDeployment() {
        Receiver receiver = new Receiver()
        receiver.save()
        assertStatus(DeviceStatus.NEW, receiver)

        ReceiverDeployment deployment = new ReceiverDeployment(receiver: receiver, deploymentDateTime: new DateTime().minusDays(1))
        deployment.save()
        receiver.addToDeployments(deployment)
        receiver.save()

        assertStatus(DeviceStatus.DEPLOYED, receiver)
    }

    void testStatusWithDeploymentAndRecovery() {
        Receiver receiver = new Receiver()
        receiver.save()
        assertStatus(DeviceStatus.NEW, receiver)

        ReceiverDeployment deployment = new ReceiverDeployment(
            receiver: receiver,
            deploymentDateTime: new DateTime().minusDays(2),
            initialisationDateTime: new DateTime().minusDays(2)
        )
        deployment.save()
        receiver.addToDeployments(deployment)
        receiver.save()
        assertStatus(DeviceStatus.DEPLOYED, receiver)

        ReceiverRecovery recovery = new ReceiverRecovery(deployment: deployment, recoveryDateTime: new DateTime().minusDays(1), status: DeviceStatus.RECOVERED)
        recovery.save()
        deployment.recovery = recovery
        deployment.save()

        assertStatus(DeviceStatus.RECOVERED, receiver)
    }

    void testStatusWithDeploymentAndRecoveryAtTime() {
        Receiver receiver = new Receiver()
        receiver.save()

        assertStatus(DeviceStatus.NEW, receiver)
        assertStatus(DeviceStatus.NEW, receiver, new DateTime().minusDays(1))
        assertStatus(DeviceStatus.NEW, receiver, new DateTime().plusDays(1))

        def deploymentDate = new DateTime().minusDays(5)
        ReceiverDeployment deployment = new ReceiverDeployment(
            receiver: receiver,
            deploymentDateTime: deploymentDate,
            initialisationDateTime: deploymentDate
        )
        deployment.save()
        receiver.addToDeployments(deployment)
        receiver.save()

        assertStatus(DeviceStatus.DEPLOYED, receiver)
        assertStatus(DeviceStatus.NEW, receiver, deploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, deploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, deploymentDate.plusDays(1))

        def recoveryDate = new DateTime().minusDays(2)
        ReceiverRecovery recovery = new ReceiverRecovery(deployment: deployment, recoveryDateTime: recoveryDate, status: DeviceStatus.RECOVERED)
        recovery.save()
        deployment.recovery = recovery
        deployment.save()

        assertStatus(DeviceStatus.RECOVERED, receiver)
        assertStatus(DeviceStatus.NEW, receiver, deploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, deploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, deploymentDate.plusDays(1))

        assertStatus(DeviceStatus.DEPLOYED, receiver, recoveryDate.minusDays(1))
        assertStatus(DeviceStatus.RECOVERED, receiver, recoveryDate)
        assertStatus(DeviceStatus.RECOVERED, receiver, recoveryDate.plusDays(1))

        // add second deployment and recovery
        def secondDeploymentDate = new DateTime().plusDays(2)
        ReceiverDeployment secondDeployment = new ReceiverDeployment(receiver: receiver, deploymentDateTime: secondDeploymentDate)
        secondDeployment.save()
        receiver.addToDeployments(secondDeployment)
        receiver.save()

        assertStatus(DeviceStatus.RECOVERED, receiver)
        assertStatus(DeviceStatus.RECOVERED, receiver, secondDeploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate.plusDays(1))

        def secondRecoveryDate = new DateTime().plusDays(5)
        ReceiverRecovery secondRecovery = new ReceiverRecovery(deployment: secondDeployment, recoveryDateTime: secondRecoveryDate, status: DeviceStatus.RECOVERED)
        secondRecovery.save()
        secondDeployment.recovery = secondRecovery
        secondDeployment.save()

        assertStatus(DeviceStatus.RECOVERED, receiver)
        assertStatus(DeviceStatus.RECOVERED, receiver, secondDeploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate.plusDays(1))

        assertStatus(DeviceStatus.DEPLOYED, receiver, secondRecoveryDate.minusDays(1))
        assertStatus(DeviceStatus.RECOVERED, receiver, secondRecoveryDate)
        assertStatus(DeviceStatus.RECOVERED, receiver, secondRecoveryDate.plusDays(1))
    }

    void testStatusWithTwoUnrecoveredDeployments() {
        Receiver receiver = new Receiver()
        receiver.save()

        def firstDeploymentDate = new DateTime().minusDays(5)
        ReceiverDeployment firstDeployment = new ReceiverDeployment(receiver: receiver, deploymentDateTime: firstDeploymentDate)
        firstDeployment.save()
        receiver.addToDeployments(firstDeployment)

        def secondDeploymentDate = new DateTime().minusDays(3)
        ReceiverDeployment secondDeployment = new ReceiverDeployment(receiver: receiver, deploymentDateTime: secondDeploymentDate)
        secondDeployment.save()
        receiver.addToDeployments(secondDeployment)

        receiver.save()

        assertStatus(DeviceStatus.NEW, receiver, firstDeploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, firstDeploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, firstDeploymentDate.plusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate.plusDays(1))
    }

    void testStatusWithOneUnrecoveredBeforeOneRecoveredDeployment() {
        Receiver receiver = new Receiver()
        receiver.save()

        def firstDeploymentDate = new DateTime().minusDays(5)
        ReceiverDeployment firstDeployment =
            new ReceiverDeployment(receiver: receiver, deploymentDateTime: firstDeploymentDate, initialisationDateTime: firstDeploymentDate)
        firstDeployment.save()
        receiver.addToDeployments(firstDeployment)

        def secondDeploymentDate = new DateTime().minusDays(3)
        ReceiverDeployment secondDeployment =
            new ReceiverDeployment(receiver: receiver, deploymentDateTime: secondDeploymentDate, initialisationDateTime: secondDeploymentDate)
        secondDeployment.save()
        receiver.addToDeployments(secondDeployment)

        def recoveryDate = new DateTime().minusDays(1)
        ReceiverRecovery recovery = new ReceiverRecovery(deployment: secondDeployment, recoveryDateTime: recoveryDate, status: DeviceStatus.RECOVERED)
        recovery.save()
        secondDeployment.recovery = recovery
        secondDeployment.save()

        receiver.save()

        assertStatus(DeviceStatus.NEW, receiver, firstDeploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, firstDeploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, firstDeploymentDate.plusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate.minusDays(1))
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate)
        assertStatus(DeviceStatus.DEPLOYED, receiver, secondDeploymentDate.plusDays(1))
        assertStatus(DeviceStatus.RECOVERED, receiver, recoveryDate)
        assertStatus(DeviceStatus.RECOVERED, receiver, recoveryDate.plusDays(1))
    }

    // TODO: this can go?
    void testCanDeploy() {
        Receiver receiver = new Receiver()
        receiver.save()

        def deploymentDateTime = new DateTime()
        ReceiverDeployment existingDeployment = new ReceiverDeployment(receiver: receiver, deploymentDateTime: deploymentDateTime)
        existingDeployment.save()
        receiver.addToDeployments(existingDeployment)

        ReceiverRecovery existingRecovery = new ReceiverRecovery(deployment: existingDeployment,
                                                                 recoveryDateTime: deploymentDateTime.plusDays(1),
                                                                 status: DeviceStatus.RETIRED)
        existingRecovery.save()
        existingDeployment.recovery = existingRecovery

        // Create second deployment before existing.
        def secondDeploymentDateTime = deploymentDateTime.minusDays(5)
        ReceiverDeployment secondDeployment = new ReceiverDeployment(receiver: receiver, deploymentDateTime: secondDeploymentDateTime)
        receiver.addToDeployments(secondDeployment)
        assertTrue(receiver.canDeploy(secondDeployment))
        secondDeployment.save()

        // Update date/time to be future
        secondDeploymentDateTime = deploymentDateTime.plusDays(2)
        secondDeployment.deploymentDateTime = secondDeploymentDateTime
        assertFalse(receiver.canDeploy(secondDeployment))
    }

    private void assertStatus(expectedStatus, receiver) {
        assertNotNull(receiver.status)
        assertEquals(expectedStatus, receiver.status)
    }

    private void assertStatus(expectedStatus, receiver, dateTime) {
        assert(receiver)
        assert(dateTime)

        assertNotNull(receiver.getStatus(dateTime))
        assertEquals(expectedStatus, receiver.getStatus(dateTime))
    }
}
