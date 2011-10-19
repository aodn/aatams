package au.org.emii.aatams

import grails.test.*
import org.joda.time.DateTimeZone

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class PersonControllerTests extends ControllerUnitTestCase 
{
    def hasRole = true
    boolean mailSent
    String toAddress
    
    def user

    protected void setUp()
    {
        super.setUp()

        toAddress = null
        mailSent = false
        controller.metaClass.sendMail = 
        {
            mailSent = true
        }

        controller.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
        
        user = Person.findByUsername("jkburges")
        
        def subject = [ getPrincipal: { user?.username },
                        isAuthenticated: { true },
                        hasRole: { hasRole },
                        isPermitted: { false },
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testRegisterThenDelete() 
    {
        hasRole = false
        user = null
        
        def cmd = new PersonCreateCommand(
                      name: "John",
                      username: "John",
                      password: "password",
                      passwordConfirm: "password",
                      organisation:null,
                      unlistedOrganisationName:"unlisted",
                      phoneNumber:"1234",
                      emailAddress:"john@asdf.com",
                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))
                  
        assertTrue(cmd.validate())

        controller.save(cmd)
        
        assertNotNull(Person.findByName("John"))
        assertEquals("john", Person.findByName("John").username)
        assertEquals(EntityStatus.PENDING, Person.findByName("John").status)

        def unlistedOrg = Organisation.findByName("unlisted")
        assertNotNull(unlistedOrg)
        assertEquals(EntityStatus.PENDING, unlistedOrg.status)
        
        
        assertEquals(unlistedOrg, Person.findByName("John").organisation)
        assertEquals(Person.findByName("John").username, unlistedOrg.request.requester.username)
        
        assertEquals("show", controller.redirectArgs.action)
    
        assertTrue(mailSent)
        
        // Now delete the org (and check that the just registered user is deleted).
        try
        {
            hasRole = true
            def orgController = new OrganisationController()
            orgController.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
            orgController.params.id = unlistedOrg.id
            orgController.delete()
            assertNull(Organisation.findByName("unlisted"))
            assertNull(Person.findByName("John"))
//            assertEquals("list", orgController.redirectArgs.action)
        }
        catch (Throwable t)
        {
            println(t)
            fail()
        }
    }
    
    void testSaveUsernameToLowerCase()
    {
        Organisation org = 
            new Organisation(name:"org", 
                             department:"dep",
                             phoneNumber:"1234",
                             faxNumber:"1234",
                             streetAddress:Address.build(),
                             postalAddress:Address.build(),
                             status:EntityStatus.ACTIVE)
        org.save()
        
        def cmd = new PersonCreateCommand(
                      name: "John",
                      username: "John",
                      password: "password",
                      passwordConfirm: "password",
                      organisation:org,
                      phoneNumber:"1234",
                      emailAddress:"john@asdf.com",
                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))
                  
//        mockForConstraintsTests(PersonCreateCommand, [cmd])
        assertTrue(cmd.validate())

        controller.save(cmd)
        
        assertEquals("john", Person.findByName("John").username)
        assertEquals("show", controller.redirectArgs.action)
        
        // Try to save "john".
        cmd.name = "john"
        assertTrue(cmd.validate())
        controller.save(cmd)
        assertEquals("create", controller.renderArgs.view)
    }
    
    void testSaveExistingOrganisation()
    {
        hasRole = false
        
        Organisation org = 
            new Organisation(name:"org", 
                             department:"dep",
                             phoneNumber:"1234",
                             faxNumber:"1234",
                             streetAddress:Address.build(),
                             postalAddress:Address.build(),
                             status:EntityStatus.ACTIVE)
                         
        org.save()
        
        def cmd = new PersonCreateCommand(
                      name: "John",
                      username: "John",
                      password: "password",
                      passwordConfirm: "password",
                      organisation:org,
                      unlistedOrganisationName:null,
                      phoneNumber:"1234",
                      emailAddress:"john@asdf.com",
                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))
                  
//        mockForConstraintsTests(PersonCreateCommand, [cmd])
        assertTrue(cmd.validate())

        controller.save(cmd)
        
        assertEquals("john", Person.findByName("John").username)
        assertEquals("show", controller.redirectArgs.action)
        
        assertTrue(mailSent)
    }
    
    void testSaveUnlistedOrganisation()
    {
        hasRole = false
        user = null
        
        def cmd = new PersonCreateCommand(
                      name: "John",
                      username: "John",
                      password: "password",
                      passwordConfirm: "password",
                      organisation:null,
                      unlistedOrganisationName:"unlisted",
                      phoneNumber:"1234",
                      emailAddress:"john@asdf.com",
                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))
                  
//        mockForConstraintsTests(PersonCreateCommand, [cmd])
        assertTrue(cmd.validate())

        controller.save(cmd)
        
        assertNotNull(Person.findByName("John"))
        assertEquals("john", Person.findByName("John").username)
        assertEquals(EntityStatus.PENDING, Person.findByName("John").status)

        assertNotNull(Organisation.findByName("unlisted"))
        assertEquals(EntityStatus.PENDING, Organisation.findByName("unlisted").status)
        assertEquals(Organisation.findByName("unlisted"), Person.findByName("John").organisation)
        assertEquals(Person.findByName("John").username, Organisation.findByName("unlisted").request.requester.username)
        
        assertEquals("show", controller.redirectArgs.action)
    
        assertTrue(mailSent)
    }
}
