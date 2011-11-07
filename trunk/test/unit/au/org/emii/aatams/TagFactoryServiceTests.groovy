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
		Tag existingTag = new Tag(codeName:"A69-1303-1234",
								  codeMap:codeMap, 
								  serialNumber:"12345",
								  model:new DeviceModel(),
								  status:new DeviceStatus(),
								  transmitterType:new TransmitterType(),
								  pingCode:1234)
		mockDomain(Tag, [existingTag])
		codeMap.addToTags(existingTag)
		codeMap.save()
		
		assertEquals(1, Tag.count())
		lookupOrCreate()
		assertEquals(1, Tag.count())
	}
	
	private void lookupOrCreate()
	{
        Tag tag = tagService.lookupOrCreate(params)

		assertFalse(tag.hasErrors())
		assertNotNull(Tag.get(tag.id))
		assertTrue(codeMap.tags*.id.contains(tag.id))
		assertTrue(CodeMap.get(tag.codeMap.id).tags*.id.contains(tag.id))
	}
}
