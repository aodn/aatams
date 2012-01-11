package au.org.emii.aatams

import grails.test.*

class TagFactoryServiceTests extends GrailsUnitTestCase 
{
    def tagService
    def params
	CodeMap codeMap
	
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(TagFactoryService)
        tagService = new TagFactoryService()
		
		mockDomain(Tag)
		
		codeMap = new CodeMap(codeMap:"A69-1303")
		mockDomain(CodeMap, [codeMap])
		codeMap.save()
		
        TagDeviceModel model = new TagDeviceModel()
        mockDomain(TagDeviceModel, [model])
        model.save()

		mockDomain(TransmitterType)
		
		params = 
		    [codeMap:[id:codeMap.id],
			pingCode:1234,
			serialNumber:"1111",
			model:[id:model.id],
			status:new DeviceStatus(),
			transmitterType:new TransmitterType()]
    }

    protected void tearDown() 
    {
        super.tearDown()
    }
	
	void testInvalidCodeMapId()
	{
		params.codeMap = [id:123]
		
		try
		{
			tagService.lookupOrCreate(params)
			fail("Expecting exception")
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Unknown code map ID: 123", e.getMessage())
		}
	}
	
	void testInvalidPingCode()
	{
		params.pingCode = "asdf"
		
		try
		{
			tagService.lookupOrCreate(params)
			fail("Expecting exception")
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Invalid ping code ID: asdf", e.getMessage())
		}
	}
	
	void testValidParamsNew()
	{
		assertEquals(0, Tag.count())
		lookupOrCreate()
		assertEquals(1, Tag.count())
	}

	void testValidParamsNewPingCodeAsString()
	{
		params.pingCode = "5678"
		assertEquals(0, Tag.count())
		lookupOrCreate()
		assertEquals(1, Tag.count())
	}

	void testValidParamsExisting()
	{
		TransmitterType pinger = new TransmitterType(transmitterTypeName: 'PINGER')
		mockDomain(TransmitterType, [pinger])
		pinger.save()
		
		Tag existingTag = new Tag(codeMap:codeMap, 
								  serialNumber:"1111",
								  model:new DeviceModel(),
								  status:new DeviceStatus())
		
		Sensor existingSensor = new Sensor(tag: existingTag,
								  		   transmitterType:pinger,
										   pingCode:1234)
		mockDomain(Sensor, [existingSensor])
		existingTag.addToSensors(existingSensor)
		
		mockDomain(Tag, [existingTag])
		codeMap.addToTags(existingTag)
		codeMap.save()
		
		existingTag.save()
		
		assertEquals(1, Tag.count())
		def retTag = lookupOrCreate()
		assertEquals(1, Tag.count())
		assertEquals(1, Sensor.count())
		assertEquals(existingSensor, retTag.sensors[0])
	}
	
	private Tag lookupOrCreate()
	{
        Tag tag = tagService.lookupOrCreate(params)

		assertFalse(tag.hasErrors())
		assertNotNull(Tag.get(tag.id))
		assertTrue(codeMap.tags*.id.contains(tag.id))
		assertTrue(CodeMap.get(tag.codeMap.id).tags*.id.contains(tag.id))
		
		return tag
	}
}
