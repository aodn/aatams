package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

import org.joda.time.*

class ReceiverDeploymentControllerTests extends AbstractControllerUnitTestCase
{
    Receiver receiver
    Receiver csiroReceiver

    DeviceStatus newStatus
    DeviceStatus deployedStatus
    DeviceStatus recoveredStatus
    DeviceStatus retiredStatus

    def candidateEntitiesService

    InstallationStation station1
    InstallationStation station2
    InstallationStation newStation

    Receiver receiver1
    Receiver receiver2

    protected void setUp()
    {
        super.setUp()

        mockDomain(Person)
        newStatus = new DeviceStatus(status:"NEW")
        deployedStatus = new DeviceStatus(status:"DEPLOYED")
        recoveredStatus = new DeviceStatus(status:"RECOVERED")
        retiredStatus = new DeviceStatus(status:"RETIRED")

        def statusList = [newStatus, deployedStatus, recoveredStatus, retiredStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }

        def imos = new Organisation(name:"IMSO")
        def csiro = new Organisation(name:"CSIRO")
        def orgList = [imos, csiro]
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }

        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName: "VRW2")
        receiver = new Receiver(model: model, serialNumber:"111", organisation:imos)
        csiroReceiver = new Receiver(model: model, serialNumber:"444", organisation:csiro)
        receiver1 = new Receiver(model: model, serialNumber:"receiver1", organisation:imos)
        receiver2 = new Receiver(model: model, serialNumber:"receiver2", organisation:imos)

        def receiverList = [receiver, csiroReceiver, receiver1, receiver2]
        mockDomain(Receiver, receiverList)
        receiverList.each
        {
            imos.addToReceivers(it)
            it.save()
        }

        imos.save()

        mockDomain(ReceiverDeployment)
        mockDomain(ReceiverRecovery)

        station1 = new InstallationStation(name:'station1', receivers:new HashSet<Receiver>())
        station2 = new InstallationStation(name:'station2', receivers:new HashSet<Receiver>())
        newStation = new InstallationStation(name:'newStation', receivers:new HashSet<Receiver>())
        def stationList = [station1, station2, newStation]
        mockDomain(InstallationStation, stationList)
        stationList.each { it.save() }

        controller.metaClass.message = { LinkedHashMap args -> return args }
        controller.metaClass.runAsync = { Closure c -> }

        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.metaClass.stations =
        {
            return stationList - newStation
        }
        candidateEntitiesService.metaClass.receivers =
        {
            return [receiver1, receiver2]
        }

        controller.candidateEntitiesService = candidateEntitiesService
        controller.params.mooringType = new MooringType()
        controller.params.station = station1

        mockLogging(Receiver)
    }

    void testSaveNoReceiver()
    {
        controller.params.deploymentDateTime = new DateTime()
        controller.save()
        def model = controller.renderArgs.model
        assertEquals("nullable",  model.receiverDeploymentInstance.errors.getFieldError("receiver").getCode())
    }

    private void assertSuccessfulSaveDeployment(deploymentDateTime)
    {
        assertEquals(0, station1.numDeployments)

        boolean rescanCalled = false
        controller.metaClass.rescanDetections =
        {
            theDeployment ->

            rescanCalled = true
        }

        controller.params.deploymentDateTime = deploymentDateTime
        controller.params.receiver = receiver
        controller.save()

        assertEquals("show", controller.redirectArgs.action)
        assertEquals(1, station1.numDeployments)
        assertFalse(rescanCalled)

        def deployment = ReceiverDeployment.get(controller.redirectArgs.id)
        assertNotNull(deployment)
        assertEquals(1, deployment.deploymentNumber)
    }

    private void assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime)
    {
        controller.params.deploymentDateTime = deploymentDateTime
        controller.params.id = existingDeployment.id
        controller.update()

        assertEquals("show", controller.redirectArgs.action)

        def updatedDeployment = ReceiverDeployment.get(controller.redirectArgs.id)
        assertNotNull(updatedDeployment)
        assertEquals(deploymentDateTime, updatedDeployment.deploymentDateTime)
    }

    private void assertFailedDeployment(deploymentDateTime, field, fieldErrorCode)
    {
        assertEquals(0, station1.numDeployments)

        controller.params.deploymentDateTime = deploymentDateTime
        controller.params.receiver = receiver
        controller.save()

        def model = controller.renderArgs.model
        assertEquals(fieldErrorCode, model.receiverDeploymentInstance.errors.getFieldError(field).getCode())
    }

    void testSaveNoDeployment()
    {
        assertSuccessfulSaveDeployment(new DateTime())
    }

    private ReceiverDeployment createDeployment(dateTime)
    {
        ReceiverDeployment deployment =
            new ReceiverDeployment(deploymentDateTime: dateTime,
                                   receiver: receiver,
                                   mooringType: new MooringType(),
                                   station: new InstallationStation())
        deployment.save(failOnError: true)
        receiver.addToDeployments(deployment)

        return deployment
    }

    private ReceiverRecovery createRecovery(dateTime, deployment, status)
    {
        ReceiverRecovery recovery = new ReceiverRecovery(recoveryDateTime: dateTime, deployment: deployment, status: status)
        recovery.save()
        deployment.recovery = recovery

        return recovery
    }

    void testSaveFutureDeployment()
    {
        def deploymentDateTime = new DateTime()
        createDeployment(deploymentDateTime.plusDays(1))

        assertSuccessfulSaveDeployment(deploymentDateTime)
    }

    void testSaveCurrentDeployment()
    {
        def deploymentDateTime = new DateTime()
        def existingDeployment = createDeployment(deploymentDateTime.minusDays(1))

        assertFailedDeployment(deploymentDateTime, "receiver", "receiverDeployment.receiver.invalidStateAtDateTime")

        // Should work if we delete the existing deployment then try again.
        existingDeployment.delete()
        receiver.deployments.clear()

        assertSuccessfulSaveDeployment(deploymentDateTime)
    }

    void testSavePastRecoveryRecovered()
    {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment pastDeployment = createDeployment(deploymentDateTime.minusDays(2))
        ReceiverRecovery pastRecovery = createRecovery(deploymentDateTime.minusDays(1), pastDeployment, recoveredStatus)

        assertSuccessfulSaveDeployment(deploymentDateTime)
    }

    void testSavePastRecoveryRetired()
    {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment pastDeployment = createDeployment(deploymentDateTime.minusDays(2))
        ReceiverRecovery pastRecovery = createRecovery(deploymentDateTime.minusDays(1), pastDeployment, retiredStatus)

        assertFailedDeployment(deploymentDateTime, "receiver", "receiverDeployment.receiver.invalidStateAtDateTime")
    }

    void testSaveFutureRecoveryRecovered()
    {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment futureDeployment = createDeployment(deploymentDateTime.plusDays(2))
        ReceiverRecovery futureRecovery = createRecovery(deploymentDateTime.plusDays(3), futureDeployment, recoveredStatus)

        assertSuccessfulSaveDeployment(deploymentDateTime)
    }

    void testSaveFutureRecoveryRetired()
    {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment futureDeployment = createDeployment(deploymentDateTime.plusDays(2))
        ReceiverRecovery futureRecovery = createRecovery(deploymentDateTime.plusDays(3), futureDeployment, retiredStatus)

        assertSuccessfulSaveDeployment(deploymentDateTime)
    }

    void testUpdateExistingDeployment()
    {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment existingDeployment = createDeployment(deploymentDateTime)

        assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime.plusDays(2))
        assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime.minusDays(3))

        createRecovery(deploymentDateTime.plusDays(3), existingDeployment, recoveredStatus)
        assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime.minusDays(3))
    }

    void testSaveScheduledRecoveryDateBeforeDeploymentDate()
    {
        def deploymentDateTime = new DateTime()

        controller.params.deploymentDateTime = deploymentDateTime
        controller.params.receiver = receiver
        controller.params.recoveryDate = controller.params.deploymentDateTime.minusDays(1).toDate()

        controller.save()

        assertEquals("create", controller.renderArgs.view)
        def model = controller.renderArgs.model
        assertNotNull(model.receiverDeploymentInstance)
        assertEquals(3, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(receiver1))
        assertTrue(model.candidateReceivers.contains(receiver2))
        assertEquals(2, model.candidateStations.size())
        assertTrue(model.candidateStations.contains(station1))
        assertTrue(model.candidateStations.contains(station2))
    }

    void testCreate()
    {
        def model = controller.create()

        assertNull(model.receiverDeploymentInstance.recoveryDate)
        assertDefaultValues(model)
    }

    private assertDefaultValues(def model)
    {
        assertNotNull(model.receiverDeploymentInstance)
        assertEquals(2, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(receiver1))
        assertTrue(model.candidateReceivers.contains(receiver2))
        assertEquals(2, model.candidateStations.size())
        assertTrue(model.candidateStations.contains(station1))
        assertTrue(model.candidateStations.contains(station2))
    }

    void testSaveError()
    {
        controller.save()
        def model = controller.modelAndView.model

        assertDefaultValues(model)
    }

    void testSaveCheckStationsReceivers()
    {
        assertFalse(station1.receivers.contains(receiver))
        assertFalse(station1.receivers.contains(csiroReceiver))

        controller.params.receiver = receiver
        controller.save()

        assertTrue(station1.receivers.contains(receiver))
        assertFalse(station1.receivers.contains(csiroReceiver))

        controller.params.receiver = csiroReceiver
        controller.save()

        assertTrue(station1.receivers.contains(receiver))
        assertTrue(station1.receivers.contains(csiroReceiver))
    }

    void testUpdateCheckStationsReceivers()
    {
        assertFalse(station1.receivers.contains(receiver))
        assertFalse(station1.receivers.contains(csiroReceiver))

        controller.params.receiver = receiver
        controller.save()

        assertTrue(station1.receivers.contains(receiver))
        assertFalse(station1.receivers.contains(csiroReceiver))

        controller.params.clear()
        controller.params.id = controller.redirectArgs.id
        controller.params.receiver = csiroReceiver
        controller.update()

        assertFalse(station1.receivers.contains(receiver))
        assertTrue(station1.receivers.contains(csiroReceiver))
    }

    void testEditIncludesCurrentlyDeployedReceiverAndStation()
    {
        def model = controller.create()
        assertEquals(2, model.candidateReceivers.size())
        assertEquals(2, model.candidateStations.size())

        controller.params.receiver = receiver
        controller.params.station = newStation

        controller.save()

        controller.params.clear()
        controller.params.id = controller.redirectArgs.id
        model = controller.edit()
        assertEquals(3, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(receiver))
        assertEquals(3, model.candidateStations.size())
        assertTrue(model.candidateStations.contains(newStation))
    }
}
