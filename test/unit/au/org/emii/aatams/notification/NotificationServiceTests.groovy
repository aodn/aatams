package au.org.emii.aatams.notification

import grails.test.*

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import org.joda.time.*

class NotificationServiceTests extends AbstractGrailsUnitTestCase {
    def notificationService
    def person
    def otherPerson

    Notification gettingStarted
    Notification receiverRecoveryCreate
    Notification register

    def permissionUtilsService

    def authorisedPerson

    protected void setUp() {
        super.setUp()

        mockDomain(Notification)
        mockLogging(Notification, true)

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

        permitted = true
        authorisedPerson = person

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
                             htmlFragment:"Click here to register to use the IMOS Animal Tracking Database",
                             anchorSelector:"#userlogin > [href^='/aatams/person/create']",
                             unauthenticated:true)

        def notificationList = [gettingStarted, receiverRecoveryCreate, register]
        mockDomain(Notification, notificationList)
        notificationList.each { it.save() }
    }

    protected void tearDown() {
        super.tearDown()
    }

    protected def getPrincipal() {
        return authorisedPerson?.id
    }

    void testActiveNotifications() {
        def notificationList = notificationService.listActive()

        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))
        assertFalse(notificationList.contains(register))

        authenticated = false
        hasRole = false
        authorisedPerson = null
        permitted = false

        notificationList = notificationService.listActive()

        assertFalse(notificationList.contains(gettingStarted))
        assertFalse(notificationList.contains(receiverRecoveryCreate))
        assertTrue(notificationList.contains(register))
    }

    void testAcknowledgeNotifications() {
        def notificationList = notificationService.listActive()
        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))

        notificationService.acknowledge(gettingStarted)
        notificationList = notificationService.listActive()
        assertFalse(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))

        authorisedPerson = otherPerson
        authenticated = true
        hasRole = true
        permitted = true

        notificationList = notificationService.listActive()
        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))

    }

    void testAcknowledgeNotificationsByKey() {
        def notificationList = notificationService.listActive()
        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))

        notificationService.acknowledge("GETTING_STARTED")
        notificationList = notificationService.listActive()
        assertFalse(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))

        authorisedPerson = otherPerson
        authenticated = true
        hasRole = true
        permitted = true

        notificationList = notificationService.listActive()
        assertTrue(notificationList.contains(gettingStarted))
        assertTrue(notificationList.contains(receiverRecoveryCreate))

    }
}
