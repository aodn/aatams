package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class SensorControllerTests extends AbstractControllerUnitTestCase
{
	CodeMap a69_1303
	Tag owningTag
	TransmitterType pressure
	
    protected void setUp() 
	{
        super.setUp()
		
		a69_1303 = new CodeMap(codeMap: "A69-1303")
		mockDomain(CodeMap, [a69_1303])
		a69_1303.save()
		
		DeviceStatus newStatus = new DeviceStatus(status:"NEW")
		mockDomain(DeviceStatus, [newStatus])
		newStatus.save()
		
		TagDeviceModel model = new TagDeviceModel(modelName:"V16")
		mockDomain(TagDeviceModel, [model])
		
		owningTag = new Tag(codeMap:a69_1303, pingCode: 1111, codeName: "A69-1303-1111", model:model, status:newStatus)
		mockDomain(Tag, [owningTag])
		owningTag.save()
		
		pressure = new TransmitterType(transmitterTypeName:"PRESSURE")
		mockDomain(TransmitterType, [pressure])
		pressure.save()
		
		mockDomain(Sensor)
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testSaveValid() 
	{
		controller.params.tag = owningTag
		controller.params.transmitterType = pressure
		controller.params.pingCode = 2222
		controller.params.slope = "1"
		controller.params.intercept = "2"
		controller.params.unit = "ADC"
		
		controller.save()
		
		assertEquals(1, Sensor.count())
		assertEquals("A69-1303-2222", Sensor.list()[0].codeName)
		assertEquals([code:"default.updated.message", args:[[code:"sensor.label", default:"Sensor"], "A69-1303-2222"]], flashMsgParams)
    }
}
