package au.org.emii.aatams

import grails.test.*

class ReceiverControllerTests extends ControllerUnitTestCase  {


    def imos
    
    def permissionUtilsService
    
    protected void setUp()  {
        super.setUp()
        
        
        
        imos = new Organisation(name:"IMSO")
        def csiro = new Organisation(name:"CSIRO")
        def orgList = [imos, csiro]
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }
        
        mockDomain(Receiver)
        
        mockLogging(PermissionUtilsService)
        permissionUtilsService = new PermissionUtilsService()
        permissionUtilsService.metaClass.receiverCreated = {
            // no-op.
        }
        
        controller.permissionUtilsService = permissionUtilsService
        controller.metaClass.message = {}
    }

    protected void tearDown()  {
        super.tearDown()
    }

    void testReceiverSave()  {
        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName:"model")
        mockDomain(ReceiverDeviceModel, [model])
        model.save()
        
        controller.params.organisation = imos
        controller.params.model = model
        controller.params.serialNumber = "12345"
        controller.params.comment = ""
        
        controller.save()
        def receiver = Receiver.get(controller.redirectArgs.id)
        
        assertEquals("show", controller.redirectArgs.action)
        assertEquals("model-12345", receiver.name)
        assertEquals(DeviceStatus.NEW, receiver.status)
    }
    
    void testDefaultModelIsVR2W() {
        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName:"VR2W")
        mockDomain(ReceiverDeviceModel, [model])
        model.save()
        
        def controllerModel = controller.create()
        assertEquals(model.id, controllerModel.receiverInstance.model.id)
    }
}
