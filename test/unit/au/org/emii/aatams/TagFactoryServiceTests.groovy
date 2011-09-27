package au.org.emii.aatams

import grails.test.*

class TagFactoryServiceTests extends GrailsUnitTestCase 
{
    def tagService
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(TagFactoryService)
        tagService = new TagFactoryService()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testInvalidCodeName()
    {
        def listInvalidCodeNames =
            ["A69-1303", "A69", "A69-1303 12345", "1303-12345", "A69 1303-234", "A69-1303-sadds"]
            
        DeviceModel model = new DeviceModel()
        mockDomain(DeviceModel, [model])
        model.save()
        
        DeviceStatus status = new DeviceStatus()
        mockDomain(DeviceStatus, [status])
        status.save()
        
        TransmitterType transType = new TransmitterType()
        mockDomain(TransmitterType, [transType])
        transType.save()
        
        mockDomain(Tag)
        
        listInvalidCodeNames.each
        {
            try
            {
                Tag tag = tagService.lookupOrCreate(
                    [codeName:it,
                     serialNumber:"1111",
                     model:[id:model.id],
                     status:status,
                     transmitterType:transType])
             
                fail()
            }
            catch (IllegalArgumentException e)
            {
                
            }
        }
        
    }
}
