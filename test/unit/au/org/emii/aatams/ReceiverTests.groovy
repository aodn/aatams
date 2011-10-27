package au.org.emii.aatams

import grails.test.*

class ReceiverTests extends GrailsUnitTestCase 
{
	Receiver rx1
	Receiver rx2
	
    protected void setUp() 
	{
        super.setUp()
        rx1 = new Receiver(codeName: "VRW2-1111",
                                    serialNumber:"1111",
                                    status:new DeviceStatus(),
                                    model:new DeviceModel(),
                                    organisation:new Organisation())
        rx2 = new Receiver(codeName: "VRW2-2222",
                                    serialNumber:"2222",
                                    status:new DeviceStatus(),
                                    model:new DeviceModel(),
                                    organisation:new Organisation())
                                
        mockDomain(Receiver, [rx1, rx2])
        mockForConstraintsTests(Receiver, [rx1, rx2])
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testNonUniqueCodeName() 
    {
		rx2.codeName = rx1.codeName
        assertFalse(rx2.validate())
		println(rx2.errors)
    }
    
    void testNonUniqueSerialNumber() 
    {
		rx2.serialNumber = rx1.serialNumber
        assertFalse(rx2.validate())
    }
    
    void testConstructCodeName()
    {
        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName:"VR2W")
        mockDomain(ReceiverDeviceModel, [model])
        model.save()
        
        def params = [serialNumber: "12345",
                      model:[id:model.id],
                      "model.id":model.id]
        
        assertEquals("VR2W-12345", Receiver.constructCodeName(params))
    }
}
