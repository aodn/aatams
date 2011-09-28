package au.org.emii.aatams.notification

import grails.test.*

import au.org.emii.aatams.*

import org.joda.time.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class NotificationServiceTests extends GrailsUnitTestCase 
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

    void testActiveNotifications()
    {
        def notificationList = notificationService.listActive()
        
        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))
        assertFalse(notificationList.contains(register))
        
        subject = [ getPrincipal: { null },
                        isAuthenticated: { false },
                        hasRole: { false },
                        isPermitted: { false }
                      ] as Subject
                      
        notificationList = notificationService.listActive()
        
        assertFalse(notificationList.contains(gettingStarted))
        assertFalse(notificationList.contains(receiverRecoveryCreate))
        assertTrue(notificationList.contains(register))
    }
    
    void testAcknowledgeNotifications()
    {
        def notificationList = notificationService.listActive()
        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))
        
        notificationService.acknowledge(gettingStarted)
        notificationList = notificationService.listActive()
        assertFalse(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))
        
        subject =     [ getPrincipal: { otherPerson.username },
                        isAuthenticated: { true },
                        hasRole: { true },
                        isPermitted: { true }
                      ] as Subject

        notificationList = notificationService.listActive()
        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))

    }
}
