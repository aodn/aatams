package au.org.emii.aatams

import grails.test.*

class DeviceModelTests extends GrailsUnitTestCase 
{
    DeviceModel vemcoVR2
    DeviceModel vemcoVR2W
    DeviceModel vemcoVR3UWM
    
    DeviceModel vemcoV6180
    DeviceModel vemcoV7
    DeviceModel vemcoV8
    DeviceModel vemcoV9
    DeviceModel vemcoV9AP
    DeviceModel vemcoV13
    DeviceModel vemcoV16
    
    protected void setUp() 
    {
        super.setUp()
        
        DeviceManufacturer vemco = 
            new DeviceManufacturer(manufacturerName:'Vemco')
        mockDomain(DeviceManufacturer, [vemco])    
        

        // Receiver models.
        vemcoVR2 =
            new ReceiverDeviceModel(modelName:'VR2', manufacturer:vemco)
        vemcoVR2W =
            new ReceiverDeviceModel(modelName:'VR2W', manufacturer:vemco)
        vemcoVR3UWM =
            new ReceiverDeviceModel(modelName:'VR3-UWM', manufacturer:vemco)
        def receiverModels = [vemcoVR2, vemcoVR2W, vemcoVR3UWM]
        mockDomain(DeviceModel, receiverModels)
        receiverModels.each { it.save() }
            
        // Tag models.
        vemcoV6180 =
            new TagDeviceModel(modelName:'V6-180kHz', manufacturer:vemco)
        vemcoV7 =
            new TagDeviceModel(modelName:'V7', manufacturer:vemco)
        vemcoV8 =
            new TagDeviceModel(modelName:'V8', manufacturer:vemco)
        vemcoV9 =
            new TagDeviceModel(modelName:'V9', manufacturer:vemco)
        vemcoV9AP =
            new TagDeviceModel(modelName:'V9AP', manufacturer:vemco)
        vemcoV13 =
            new TagDeviceModel(modelName:'V13', manufacturer:vemco)
        vemcoV16 =
            new TagDeviceModel(modelName:'V16', manufacturer:vemco)
        def tagModels = [vemcoV6180, vemcoV7, vemcoV8, vemcoV9, vemcoV9AP, vemcoV13, vemcoV16]
        mockDomain(TagDeviceModel, tagModels)
        tagModels.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testTagDeviceModelList() 
    {
        def tagList = TagDeviceModel.list()
        
        assertEquals(7, tagList.size())
        
        assertTrue(tagList.contains(vemcoV6180))
        assertTrue(tagList.contains(vemcoV7))
        assertTrue(tagList.contains(vemcoV8))
        assertTrue(tagList.contains(vemcoV9))
        assertTrue(tagList.contains(vemcoV9AP))
        assertTrue(tagList.contains(vemcoV13))
        assertTrue(tagList.contains(vemcoV16))
    }

    void testReceiverDeviceModelList() 
    {
        def receiverList = ReceiverDeviceModel.list()
        
        assertEquals(3, receiverList.size())
        
        assertTrue(receiverList.contains(vemcoVR2))
        assertTrue(receiverList.contains(vemcoVR2W))
        assertTrue(receiverList.contains(vemcoVR3UWM))
    }
}
