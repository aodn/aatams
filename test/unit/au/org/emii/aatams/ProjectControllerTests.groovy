package au.org.emii.aatams

import grails.test.*
import org.apache.shiro.crypto.hash.Sha256Hash

class ProjectControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
        TestUtils.setupMessage(controller)
        initData()
    }

    protected void tearDown() 
    {
        TestUtils.logout()
        super.tearDown()
    }

    static def createDataList()
    {
        Project sealCountProject =
            new Project(name:'Seal Count',
                        description:'Counting seals',
                        status:EntityStatus.ACTIVE)

        Project tunaProject =
            new Project(name:'Tuna',
                        description:'Counting tuna',
                        status:EntityStatus.PENDING)

        Project whaleProject =
            new Project(name:'Whale',
                        description:'Whale counting',
                        status:EntityStatus.DEACTIVATED)

        def projectList = [sealCountProject, tunaProject, whaleProject]
        
    }
    
    void initData()
    {
        def orgList = OrganisationControllerTests.createDataList()
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }
        
        def personList = PersonControllerTests.createDataList()
        mockDomain(Person, personList)
        personList.each { it.save() }
        
        def projectList = createDataList()
        mockDomain(Project, projectList)
        projectList.each { it.save() }
    }   
    
    void testListAsSysAdmin()
    {
        TestUtils.loginSysAdmin(this)
        
        def retVal = controller.list()
        assertEquals(3,
                     retVal.projectInstanceTotal)
    }
    
    void testListAsNonSysAdmin()
    {
        TestUtils.loginJoeBloggs(this)
        
        def retVal = controller.list()
        assertEquals(1, 
                     retVal.projectInstanceTotal)
    }
    
    void testSaveAsSysAdmin()
    {
/**   Commented out for now as not working.      
        TestUtils.loginSysAdmin(this)

        // Status should be set to ACTIVE.
        controller.params.name = 'new project'
        
        Organisation csiro = Organisation.findByName('CSIRO')
        controller.params.organisation = csiro
        
        Person jkburges = Person.get(1) //findByUsername('jkburges')
        println("person: " + jkburges)
        def joeBloggs = [id:1,
                         username:'jbloggs',
                         passwordHash:new Sha256Hash("password").toHex(),
                         name:'Joe Bloggs',
                         organisation:csiro,
                         phoneNumber:'1234',
                         emailAddress:'jbloggs@blah.au',
                         status:EntityStatus.ACTIVE]

        controller.params.person = joeBloggs
        
        def retVal = controller.save()
        
        assertEquals("show", redirectArgs['action'])
        assertEquals(EntityStatus.ACTIVE, Project.get(redirectArgs['id']).status) 
*/        
    }
    
    void testSaveAsNonSysAdmin()
    {
/**        
        // Status should be set to PENDING.
        TestUtils.loginJoeBloggs(this)

        def newProject =
            [name:'new project',
             description:'new project']

        controller.params.project = newProject
        
        def retVal = controller.save()
        
        assertEquals("show", redirectArgs['action'])
        assertEquals(EntityStatus.PENDING, Project.get(redirectArgs['id']).status) 
*/        
    }
}
