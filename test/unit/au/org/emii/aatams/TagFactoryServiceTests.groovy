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
		    [codeMap:codeMap,
			pingCode:1234,
			serialNumber:"1111",
			model:model,
			status:new DeviceStatus(),
			transmitterType:new TransmitterType()]
    }

    protected void tearDown() 
    {
        super.tearDown()
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
		assertEquals(existingSensor, retTag.pinger)
	}
	
	private Tag lookupOrCreate()
	{
        Tag tag = tagService.lookupOrCreate(params)

		assertFalse(tag.hasErrors())
		assertNotNull(Tag.get(tag.id))
		assertEquals(codeMap, tag.codeMap)
		
		return tag
	}
}
