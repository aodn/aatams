package au.org.emii.aatams

import grails.test.*

class TagTests extends GrailsUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testUniquePingCodes() 
    {
        mockDomain(Tag)
        
        Tag tag1 = new Tag(codeName:'A69-1303-1111',
                           codeMap:'A69-1303', 
                           pingCode:"1111",
                           model:new TagDeviceModel(),
                           project:new Project(),
                           serialNumber:"1111",
                           status:new DeviceStatus(),
                           transmitterType:new TransmitterType())
                               
                           
        tag1.save(failOnError:true)
        
        try
        {
            Tag tag2 = new Tag(codeName:'A69-1303-1111',
                               codeMap:'A69-1303', 
                               pingCode:"1111",
                               model:new TagDeviceModel(),
                               project:new Project(),
                               serialNumber:"1111",
                               status:new DeviceStatus(),
                               transmitterType:new TransmitterType())
            tag2.save(failOnError:true)
            
            fail()
        }
        catch (Throwable)
        {
            
        }
    }

    void testUniqueSerialNumbers() 
    {
        mockDomain(Tag)
        
        Tag tag1 = new Tag(codeName:'A69-1303-1111',
                           codeMap:'A69-1303', 
                           pingCode:"1111",
                           model:new TagDeviceModel(),
                           project:new Project(),
                           serialNumber:"1111",
                           status:new DeviceStatus(),
                           transmitterType:new TransmitterType())
                               
                           
        tag1.save(failOnError:true)
        
        try
        {
            Tag tag2 = new Tag(codeName:'A69-1303-2222',
                               codeMap:'A69-1303', 
                               pingCode:"2222",
                               model:new TagDeviceModel(),
                               project:new Project(),
                               serialNumber:"1111",
                               status:new DeviceStatus(),
                               transmitterType:new TransmitterType())
            tag2.save(failOnError:true)
            
            fail()
        }
        catch (Throwable)
        {
            
        }
    }
}
