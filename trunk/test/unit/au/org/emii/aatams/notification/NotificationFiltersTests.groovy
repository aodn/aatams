package au.org.emii.aatams.notification

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractFiltersUnitTestCase

import grails.test.*

import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig
import org.joda.time.*

class NotificationFiltersTests extends AbstractFiltersUnitTestCase 
{
    def notificationService
    def person
    def otherPerson
    
    Notification gettingStarted 
    Notification receiverRecoveryCreate
    Notification register
    
    def permissionUtilsService

    protected void setUp() 
    {
        super.setUp()
        
        mockDomain(Notification)
        mockLogging(Notification)
        
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
        
		permitted = true
        
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

	protected def getPrincipal()
	{
		return person.username
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
        
		authenticated = false
		hasRole = false
		permitted = false

        model = [:]
        filter.after(model)
        assertNotNull(model)
        
        assertFalse(model.notifications.contains(gettingStarted))
        assertFalse(model.notifications.contains(receiverRecoveryCreate))
        assertTrue(model.notifications.contains(register))
    }
}
