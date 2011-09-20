package au.org.emii.aatams

import grails.test.*

class ReceiverControllerTests extends ControllerUnitTestCase 
{

    DeviceStatus newStatus
    DeviceStatus deployedStatus
    DeviceStatus recoveredStatus

    def imos
    
    def permissionUtilsService
    
    protected void setUp() 
    {
        super.setUp()
        
        newStatus = new DeviceStatus(status:"NEW")
        deployedStatus = new DeviceStatus(status:"DEPLOYED")
        recoveredStatus = new DeviceStatus(status:"RECOVERED")
        
        def statusList = [newStatus, deployedStatus, recoveredStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }
        
        imos = new Organisation(name:"IMSO")
        def csiro = new Organisation(name:"CSIRO")
        def orgList = [imos, csiro]
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }
        
        mockDomain(Receiver)
        
        mockLogging(PermissionUtilsService)
        permissionUtilsService = new PermissionUtilsService()
        permissionUtilsService.metaClass.receiverCreated =
        {
            // no-op.
        }
        
        controller.permissionUtilsService = permissionUtilsService
        controller.metaClass.message = {}
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testReceiverSave() 
    {
        controller.params.organisation = imos
        controller.params.model = new DeviceModel(modelName:"model")
        controller.params.serialNumber = "12345"
        controller.params.comment = ""
        
        controller.save()
        def receiver = Receiver.get(controller.redirectArgs.id)
        
        assertEquals("show", controller.redirectArgs.action)
        assertEquals("model-12345", receiver.codeName)
        assertEquals(newStatus, receiver.status)
    }
}
