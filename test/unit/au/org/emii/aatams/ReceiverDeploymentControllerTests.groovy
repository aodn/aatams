package au.org.emii.aatams

import grails.test.*

import org.joda.time.*

class ReceiverDeploymentControllerTests extends ControllerUnitTestCase 
{
    Receiver newReceiver
    Receiver deployedReceiver
    Receiver recoveredReceiver
    Receiver retiredReceiver
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
        newReceiver = new Receiver(model: model, serialNumber:"111", status:newStatus, organisation:imos)
        deployedReceiver = new Receiver(model: model, serialNumber:"222", status:deployedStatus, organisation:imos)
        recoveredReceiver = new Receiver(model: model, serialNumber:"333", status:recoveredStatus, organisation:imos)
        retiredReceiver = new Receiver(model: model, serialNumber:"555", status:retiredStatus, organisation:imos)
        csiroReceiver = new Receiver(model: model, serialNumber:"444", status:recoveredStatus, organisation:csiro)
		receiver1 = new Receiver(model: model, serialNumber:"receiver1", status:newStatus, organisation:imos)
		receiver2 = new Receiver(model: model, serialNumber:"receiver2", status:newStatus, organisation:imos)
		
        def receiverList = [newReceiver, deployedReceiver, recoveredReceiver, retiredReceiver, csiroReceiver, receiver1, receiver2]
        mockDomain(Receiver, receiverList)
        receiverList.each 
        {
            imos.addToReceivers(it)
            it.save() 
        }
        
        imos.save()
        
        mockDomain(ReceiverDeployment)
        
		station1 = new InstallationStation(name:'station1', receivers:new HashSet<Receiver>())
		station2 = new InstallationStation(name:'station2', receivers:new HashSet<Receiver>())
		newStation = new InstallationStation(name:'newStation', receivers:new HashSet<Receiver>())
		def stationList = [station1, station2, newStation]
		mockDomain(InstallationStation, stationList)
		stationList.each { it.save() }
		
        controller.metaClass.message = { LinkedHashMap args -> return args }
        
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
        
        controller.params.deploymentDateTime = new DateTime()
        controller.params.mooringType = new MooringType()
        controller.params.station = station1
	}

    protected void tearDown() 
    {
        super.tearDown()
    }

	void testSaveNoReceiver()
	{
		controller.save()
		def model = controller.renderArgs.model
		assertEquals("nullable",  model.receiverDeploymentInstance.errors.getFieldError("receiver").getCode())
	}
	
    void testSaveNewReceiver() 
    {
		assertEquals(0, station1.numDeployments)
		
        controller.params.receiver = newReceiver
        controller.save()
        
        assertEquals("show", controller.redirectArgs.action)
        assertEquals(deployedStatus, newReceiver.status)
		
		assertEquals(1, station1.numDeployments)

		def deployment = ReceiverDeployment.get(controller.redirectArgs.id)
		assertNotNull(deployment)
		assertEquals(1, deployment.deploymentNumber)
    }

    void testSaveDeployedReceiver() 
    {
        controller.params.receiver = deployedReceiver
        controller.save()
        
        assertEquals("create", controller.renderArgs.view)
		assertDefaultValues(controller.renderArgs.model)
        assertNull(controller.renderArgs.model.receiverDeploymentInstance.receiver)
        
        def message = "${[code: 'default.invalidState.receiver', \
                       args: [deployedReceiver.toString(), \
                              deployedStatus.toString()]]}"
        assertEquals(message, controller.flash.message)
    }

    void testSaveRetiredReceiver() 
    {
        controller.params.receiver = retiredReceiver
        controller.save()
        
        assertEquals("create", controller.renderArgs.view)
		assertDefaultValues(controller.renderArgs.model)
        assertNull(controller.renderArgs.model.receiverDeploymentInstance.receiver)
        
        def message = "${[code: 'default.invalidState.receiver', \
                       args: [retiredReceiver.toString(), \
                              retiredStatus.toString()]]}"
        assertEquals(message, controller.flash.message)
    }
    
    void testSaveScheduledRecoveryDateBeforeDeploymentDate() 
    {
        controller.params.receiver = newReceiver
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
		assertFalse(station1.receivers.contains(newReceiver))		
		assertFalse(station1.receivers.contains(csiroReceiver))
		
		controller.params.receiver = newReceiver
		controller.save()

		assertTrue(station1.receivers.contains(newReceiver))		
		assertFalse(station1.receivers.contains(csiroReceiver))

		controller.params.receiver = csiroReceiver
		controller.save()

		assertTrue(station1.receivers.contains(newReceiver))		
		assertTrue(station1.receivers.contains(csiroReceiver))
	}

	void testUpdateCheckStationsReceivers()
	{
		assertFalse(station1.receivers.contains(newReceiver))		
		assertFalse(station1.receivers.contains(csiroReceiver))
		
		controller.params.receiver = newReceiver
		controller.save()

		assertTrue(station1.receivers.contains(newReceiver))		
		assertFalse(station1.receivers.contains(csiroReceiver))

		controller.params.clear()
		controller.params.id = controller.redirectArgs.id
		controller.params.receiver = csiroReceiver
		controller.update()

		assertFalse(station1.receivers.contains(newReceiver))		
		assertTrue(station1.receivers.contains(csiroReceiver))
	}
	
	void testEditIncludesCurrentlyDeployedReceiverAndStation()
	{
        def model = controller.create()
		assertEquals(2, model.candidateReceivers.size())
		assertEquals(2, model.candidateStations.size())
		
		controller.params.receiver = newReceiver
		controller.params.station = newStation
		
		controller.save()
		
		controller.params.clear()
		controller.params.id = controller.redirectArgs.id
		model = controller.edit()
		assertEquals(3, model.candidateReceivers.size())
		assertTrue(model.candidateReceivers.contains(newReceiver))
		assertEquals(3, model.candidateStations.size())
		assertTrue(model.candidateStations.contains(newStation))
		
	}
	
	void testReceiverStatusBackToNewOnUndeploy()
	{
		Receiver prevDeployed = new Receiver(serialNumber:"1111", status:deployedStatus)
		Receiver editDeployed = new Receiver(serialNumber:"2222", status:newStatus)
		
		def receiverList = [prevDeployed, editDeployed]
		mockDomain(Receiver, receiverList)
		receiverList.each { it.save() }
		
		ReceiverDeployment deployment = new ReceiverDeployment(receiver: prevDeployed, station:station1)
		mockDomain(ReceiverDeployment, [deployment])
		deployment.save()
		
		assertEquals(deployedStatus, prevDeployed.status)
		assertEquals(newStatus, editDeployed.status)
		
		controller.params.id = deployment.id
		controller.params.putAll(deployment.properties)
		controller.params.receiver = editDeployed
		controller.update()
		
		assertEquals(newStatus, prevDeployed.status)
		assertEquals(deployedStatus, editDeployed.status)
	}
}
