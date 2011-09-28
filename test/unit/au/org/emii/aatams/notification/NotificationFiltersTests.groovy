package au.org.emii.aatams.notification

import au.org.emii.aatams.*

import grails.test.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig
import org.joda.time.*

class NotificationFiltersTests extends FiltersUnitTestCase 
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
        
        filters.notificationService = notificationService
        
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

    void testInsertNotifications()
    {
        def model = [:]
        
        FilterConfig filter = getFilter("insertActive")
        assertNotNull(filter)
        
        filter.after(model)
        assertNotNull(model)
        
        assertTrue(model.notifications.contains(gettingStarted))
        assertTrue(model.notifications.contains(receiverRecoveryCreate))
        assertFalse(model.notifications.contains(register))
        
        subject = [ getPrincipal: { null },
                        isAuthenticated: { false },
                        hasRole: { false },
                        isPermitted: { false }
                      ] as Subject

        model = [:]
        filter.after(model)
        assertNotNull(model)
        
        assertFalse(model.notifications.contains(gettingStarted))
        assertFalse(model.notifications.contains(receiverRecoveryCreate))
        assertTrue(model.notifications.contains(register))
    }
}
