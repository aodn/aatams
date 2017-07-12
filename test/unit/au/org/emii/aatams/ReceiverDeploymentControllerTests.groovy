package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

import org.joda.time.*

class ReceiverDeploymentControllerTests extends AbstractControllerUnitTestCase {
    Receiver receiver
    Receiver csiroReceiver


    def candidateEntitiesService

    InstallationStation station1
    InstallationStation station2
    InstallationStation newStation

    Receiver receiver1
    Receiver receiver2

    protected void setUp() {
        super.setUp()

        JodaOverrides.mock()
        mockDomain(Person)


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
        receiverList.each {
            imos.addToReceivers(it)
            it.save()
        }

        imos.save()

        mockDomain(ReceiverDeployment)
        mockDomain(ReceiverRecovery)

        station1 = new InstallationStation(name:'station1')
        station2 = new InstallationStation(name:'station2')
        newStation = new InstallationStation(name:'newStation')
        def stationList = [station1, station2, newStation]
        mockDomain(InstallationStation, stationList)
        stationList.each { it.save() }

        controller.metaClass.message = { LinkedHashMap args -> return args }
        controller.metaClass.runAsync = { Closure c -> }

        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.metaClass.stations = {
            return stationList - newStation
        }
        candidateEntitiesService.metaClass.receivers = {
            return [receiver1, receiver2]
        }

        controller.candidateEntitiesService = candidateEntitiesService
        controller.params.mooringType = new MooringType()
        controller.params.station = station1

        mockLogging(Receiver)
    }

    protected void tearDown() {
        JodaOverrides.unmock()
        super.tearDown()
    }

    void testSaveNoReceiver() {
        controller.params.deploymentDateTime = new DateTime()
        controller.save()
        def model = controller.renderArgs.model
        assertEquals("nullable",  model.receiverDeploymentInstance.errors.getFieldError("receiver").getCode())
    }

    private void assertSuccessfulSaveDeployment(deploymentDateTime) {
        controller.params.deploymentDateTime = deploymentDateTime
        controller.params.receiver = receiver
        controller.save()

        assertEquals("show", controller.redirectArgs.action)

        def deployment = ReceiverDeployment.get(controller.redirectArgs.id)
        assertNotNull(deployment)
    }

    private void assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime) {
        controller.params.deploymentDateTime = deploymentDateTime
        controller.params.id = existingDeployment.id
        controller.update()

        assertEquals("show", controller.redirectArgs.action)

        def updatedDeployment = ReceiverDeployment.get(controller.redirectArgs.id)
        assertNotNull(updatedDeployment)
        assertEquals(deploymentDateTime, updatedDeployment.deploymentDateTime)
    }

    void testSaveNoDeployment() {
        assertSuccessfulSaveDeployment(new DateTime())
    }

    private ReceiverDeployment createDeployment(dateTime) {
        ReceiverDeployment deployment =
            new ReceiverDeployment(deploymentDateTime: dateTime,
                                   initialisationDateTime: dateTime,
                                   receiver: receiver,
                                   mooringType: new MooringType(),
                                   station: new InstallationStation())
        deployment.save(failOnError: true)
        receiver.addToDeployments(deployment)

        return deployment
    }

    private ReceiverRecovery createRecovery(dateTime, deployment, status) {
        ReceiverRecovery recovery = new ReceiverRecovery(recoveryDateTime: dateTime, deployment: deployment, status: status)
        recovery.save()
        deployment.recovery = recovery

        return recovery
    }

    void testSaveFutureDeploymentNotRecovered() {
        def deploymentDateTime = new DateTime()
        createDeployment(deploymentDateTime.plusDays(1))

        controller.params.deploymentDateTime = deploymentDateTime
        controller.params.receiver = receiver
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
        def errors = model.receiverDeploymentInstance.errors
        assertEquals(1, errors.getErrorCount())
        def error = errors.getFieldError('deploymentDateTime')
        assertTrue(error.getDefaultMessage().contains("does not pass custom validation"))

    }

    void testSaveFutureRecoveryRecovered() {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment futureDeployment = createDeployment(deploymentDateTime.plusDays(2))
        ReceiverRecovery futureRecovery = createRecovery(deploymentDateTime.plusDays(3), futureDeployment, DeviceStatus.RECOVERED)

        assertSuccessfulSaveDeployment(deploymentDateTime)
    }

    void testSaveFutureRecoveryRetired() {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment futureDeployment = createDeployment(deploymentDateTime.plusDays(2))
        ReceiverRecovery futureRecovery = createRecovery(deploymentDateTime.plusDays(3), futureDeployment, DeviceStatus.RETIRED)

        assertSuccessfulSaveDeployment(deploymentDateTime)
    }

    void testUpdateExistingDeployment() {
        def deploymentDateTime = new DateTime()
        ReceiverDeployment existingDeployment = createDeployment(deploymentDateTime)

        assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime.plusDays(2))
        assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime.minusDays(3))

        createRecovery(deploymentDateTime.plusDays(3), existingDeployment, DeviceStatus.RECOVERED)
        assertSuccessfulUpdateDeployment(existingDeployment, deploymentDateTime.minusDays(3))
    }

    void testSaveScheduledRecoveryDateBeforeDeploymentDate() {
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

    void testCreate() {
        def model = controller.create()

        assertNull(model.receiverDeploymentInstance.recoveryDate)
        assertDefaultValues(model)
    }

    private assertDefaultValues(def model) {
        assertNotNull(model.receiverDeploymentInstance)
        assertEquals(2, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(receiver1))
        assertTrue(model.candidateReceivers.contains(receiver2))
        assertEquals(2, model.candidateStations.size())
        assertTrue(model.candidateStations.contains(station1))
        assertTrue(model.candidateStations.contains(station2))
    }

    void testSaveError() {
        controller.save()
        def model = controller.modelAndView.model

        assertDefaultValues(model)
    }

    void testEditIncludesCurrentlyDeployedReceiverAndStation() {
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
