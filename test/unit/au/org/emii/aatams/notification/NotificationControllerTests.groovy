package au.org.emii.aatams.notification

import grails.test.*
import grails.converters.JSON

import au.org.emii.aatams.*

import org.joda.time.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class NotificationControllerTests extends ControllerUnitTestCase 
{
    def notificationService
    def person
    def subject
    def otherPerson
    
    Notification gettingStarted 
    Notification receiverRecoveryCreate
    Notification register
    
    def permissionUtilsService

    protected void setUp() 
    {
        super.setUp()
        
        mockDomain(Notification)
        
        mockLogging(NotificationService, true)
        notificationService = new NotificationService()
        
        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()
        notificationService.permissionUtilsService = permissionUtilsService
        
        controller.notificationService = notificationService
        
        person = new Person(name:"Joe Bloggs",
                            username:"person",
                            organisation:new Organisation(),
                            defaultTimeZone:DateTimeZone.forID("Australia/Darwin"))
                               
        otherPerson = new Person(name:"Other Person",
                             username:"otherPerson",
                             organisation:new Organisation())
        
        def personList = [person, otherPerson]
        mockDomain(Person, personList)
        personList.each { it.save() }
        
        subject =     [ getPrincipal: { person.username },
                        isAuthenticated: { true },
                        hasRole: { true },
                        isPermitted: { true }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
        
        
        gettingStarted = 
            new Notification(key:"GETTING_STARTED",
                             htmlFragment:"Click here to get started",
                             anchorSelector:"[href='/aatams/gettingStarted/index']")
    
        receiverRecoveryCreate =
            new Notification(key:"RECEIVER_RECOVERY_CREATE",
                             htmlFragment:"Click here to create a receiver recovery",
                             anchorSelector:"td.rowButton > [href^='/aatams/receiverRecovery/create']")

        register =
            new Notification(key:"REGISTER",
                             htmlFragment:"Click here to register to user AATAMS",
                             anchorSelector:"#userlogin > [href^='/aatams/person/create']",
                             unauthenticated:true)

        def notificationList = [gettingStarted, receiverRecoveryCreate, register]
        mockDomain(Notification, notificationList)
        notificationList.each { it.save() }
    }
    
    protected void tearDown() 
    {
        super.tearDown()
    }

    void testAcknowledgeNoKey() 
    {
        controller.acknowledge()
     
        def jsonResponse = JSON.parse(controller.response.contentAsString)
        println jsonResponse
        
        assertFalse(jsonResponse.result)
    }
    
    void testAcknowledgeValidKey() 
    {
        controller.params.key = "GETTING_STARTED"
        controller.acknowledge()
     
        def jsonResponse = JSON.parse(controller.response.contentAsString)
        println jsonResponse
        
        assertTrue(jsonResponse.result)
    }

    void testAcknowledgeInvalidKey() 
    {
        controller.params.key = "XYZ"
        controller.acknowledge()
     
        def jsonResponse = JSON.parse(controller.response.contentAsString)
        println jsonResponse
        
        assertFalse(jsonResponse.result)
    }
    
    void testAcknowledgeUnathenticated()
    {
        subject =     [ getPrincipal: { null },
                        isAuthenticated: { false },
                        hasRole: { false },
                        isPermitted: { false }
                      ] as Subject
        
        controller.params.key = "GETTING_STARTED"
        controller.acknowledge()
     
        def jsonResponse = JSON.parse(controller.response.contentAsString)
        println jsonResponse
        
        assertTrue(jsonResponse.result)
    }
}
