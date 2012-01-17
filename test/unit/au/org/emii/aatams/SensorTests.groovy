package au.org.emii.aatams

import grails.test.*

class SensorTests extends GrailsUnitTestCase 
{
	Tag tag1303
	Tag tag9002
	
    protected void setUp() 
    {
        super.setUp()
		
		CodeMap a69_1303 = new CodeMap(codeMap: "A69-1303")
		CodeMap a69_9002 = new CodeMap(codeMap: "A69-9002")
		def codeMapList = [a69_1303, a69_9002]
		mockDomain(CodeMap, codeMapList)
		codeMapList.each { it.save() }
		
		tag1303 = new Tag(codeMap: a69_1303,
						  model:new TagDeviceModel(),
						  serialNumber:"1111",
						  status:new DeviceStatus())
		
		tag9002 = new Tag(codeMap: a69_9002,
						  model:new TagDeviceModel(),
						  serialNumber:"2222",
						  status:new DeviceStatus())
		
		def tagList = [tag1303, tag9002]
		mockDomain(Tag, tagList)
		tagList.each { it.save() }
    }

    protected void tearDown() 
	{
        super.tearDown()
    }
	
	void testTransmitterIdInit()
	{
		Sensor sensor = new Sensor(tag: tag1303,
								   pingCode: 1234)
		
		assertEquals("A69-1303-1234", sensor.transmitterId)
	}
	
	void testSetPingCode()
	{
		Sensor sensor = new Sensor(tag: tag1303,
								   pingCode: 1234)
		
		sensor.pingCode = 5678
		
		assertEquals("A69-1303-5678", sensor.transmitterId)
	}
	
	void testSetTag()
	{
		Sensor sensor = new Sensor(tag: tag1303,
								   pingCode: 1234)
		
		sensor.tag = tag9002
		
		assertEquals("A69-9002-1234", sensor.transmitterId)
	}
}
