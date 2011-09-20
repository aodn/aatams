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

    protected void setUp() 
    {
        super.setUp()

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

        newReceiver = new Receiver(codeName:"VRW2-111", status:newStatus, organisation:imos)
        deployedReceiver = new Receiver(codeName:"VRW2-222", status:deployedStatus, organisation:imos)
        recoveredReceiver = new Receiver(codeName:"VRW2-333", status:recoveredStatus, organisation:imos)
        retiredReceiver = new Receiver(codeName:"VRW2-555", status:retiredStatus, organisation:imos)
        csiroReceiver = new Receiver(codeName:"VRW2-444", status:recoveredStatus, organisation:csiro)

        def receiverList = [newReceiver, deployedReceiver, recoveredReceiver]
        mockDomain(Receiver, receiverList)
        receiverList.each 
        {
            imos.addToReceivers(it)
            it.save() 
        }
        
        imos.save()
        
        mockDomain(ReceiverDeployment)
        
        controller.metaClass.message = { LinkedHashMap args -> return args }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testSaveNewReceiver() 
    {
        controller.params.receiver = newReceiver
        controller.params.deploymentDateTime = new DateTime()
        controller.params.mooringType = new MooringType()
        controller.params.station = new InstallationStation()
        
        controller.save()
        
        assertEquals("show", controller.redirectArgs.action)
        assertEquals(deployedStatus, newReceiver.status)
    }

    void testSaveDeployedReceiver() 
    {
        controller.params.receiver = deployedReceiver
        controller.params.deploymentDateTime = new DateTime()
        controller.params.mooringType = new MooringType()
        controller.params.station = new InstallationStation()
        
        controller.save()
        
        assertEquals("create", controller.renderArgs.view)
        assertNotNull(controller.renderArgs.model.receiverDeploymentInstance)
        assertNull(controller.renderArgs.model.receiverDeploymentInstance.receiver)
        
        def message = "${[code: 'default.invalidState.receiver', \
                       args: [deployedReceiver.toString(), \
                              deployedStatus.toString()]]}"
        assertEquals(message, controller.flash.message)
    }

    void testSaveRetiredReceiver() 
    {
        controller.params.receiver = retiredReceiver
        controller.params.deploymentDateTime = new DateTime()
        controller.params.mooringType = new MooringType()
        controller.params.station = new InstallationStation()
        
        controller.save()
        
        assertEquals("create", controller.renderArgs.view)
        assertNotNull(controller.renderArgs.model.receiverDeploymentInstance)
        assertNull(controller.renderArgs.model.receiverDeploymentInstance.receiver)
        
        def message = "${[code: 'default.invalidState.receiver', \
                       args: [retiredReceiver.toString(), \
                              retiredStatus.toString()]]}"
        assertEquals(message, controller.flash.message)
    }
    
    void testSaveScheduledRecoveryDateBeforeDeploymentDate() 
    {
        controller.params.receiver = newReceiver
        controller.params.deploymentDateTime = new DateTime()
        controller.params.mooringType = new MooringType()
        controller.params.station = new InstallationStation()
        controller.params.recoveryDate = controller.params.deploymentDateTime.minusDays(1).toDate()
        
        controller.save()
        
        assertEquals("create", controller.renderArgs.view)
        assertNotNull(controller.renderArgs.model.receiverDeploymentInstance)
    }
}
