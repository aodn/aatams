package au.org.emii.aatams

import grails.test.*

class TagTests extends GrailsUnitTestCase 
{
	CodeMap a69_1303
	CodeMap a69_9002
	
    protected void setUp() 
    {
        super.setUp()
		
		a69_1303 = new CodeMap(codeMap: "A69-1303")
		a69_9002 = new CodeMap(codeMap: "A69-9002")
		def codeMapList = [a69_1303, a69_9002]
		mockDomain(CodeMap, codeMapList)
		codeMapList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testUniqueSerialNumbers() 
    {
        mockDomain(Tag)
        
        Tag tag1 = new Tag(codeName:'A69-1303-1111',
                           codeMap:new CodeMap(codeMap:'A69-1303'), 
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
							   codeMap:new CodeMap(codeMap:'A69-1303'), 
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
    
    void testDeleteProject()
    {
        Project project = new Project(name:"some project")
        mockDomain(Project, [project])
        
        Tag tag = new Tag(codeMap:new CodeMap(codeMap:'A69-1303'), 
                          model:new TagDeviceModel(),
                          project:project,
                          serialNumber:"1111",
                          status:new DeviceStatus())
        mockDomain(Tag, [tag])
        tag.save()
        
        project.addToTags(tag)
        project.save()

        assertTrue(project.tags.contains(tag))
        assertEquals(project, tag.project)
        
        
        project.delete(flush:true)
        
        project = Project.findByName("some project")
        assertNull(project)
        
        tag = Tag.get(1)
        assertNotNull(tag)
        assertNull(Project.get(tag.project.id))
    }
    
    void testNullProject()
    {
        Tag tag = new Tag(codeMap:new CodeMap(codeMap:'A69-1303'), 
                          model:new TagDeviceModel(),
                          serialNumber:"1111",
                          status:new DeviceStatus())
        mockDomain(Tag, [tag])
        tag.save()
        
        assertFalse(tag.hasErrors())
    }
}
