package au.org.emii.aatams

import grails.test.*

class ReceiverTests extends GrailsUnitTestCase 
{
	Receiver rx1
	Receiver rx2
	
    protected void setUp() 
	{
        super.setUp()
		
		ReceiverDeviceModel model = new ReceiverDeviceModel(modelName: "VR2W")
		mockDomain(ReceiverDeviceModel, [model])
		model.save()
		
        rx1 = new Receiver(serialNumber:"1111",
                                    status:new DeviceStatus(),
                                    model:model,
                                    organisation:new Organisation())
        rx2 = new Receiver(serialNumber:"2222",
                                    status:new DeviceStatus(),
                                    model:model,
                                    organisation:new Organisation())
                                
        mockDomain(Receiver, [rx1, rx2])
        mockForConstraintsTests(Receiver, [rx1, rx2])
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testNonUniqueSerialNumber() 
    {
		rx2.serialNumber = rx1.serialNumber
        assertFalse(rx2.validate())
    }
    
    void testName()
    {
        ReceiverDeviceModel model = new ReceiverDeviceModel(modelName:"VR2W")
        mockDomain(ReceiverDeviceModel, [model])
        model.save()
		
		def rxr = new Receiver(serialNumber: "12345", model: model)
        
		assertEquals("VR2W-12345", rxr.name)
    }
	
	void testFindByName()
	{
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
			assertEquals("Invalid receiver name: VR2W111. Expression: (tokens.size() == 2)", e.getMessage())
		}
	}
}
