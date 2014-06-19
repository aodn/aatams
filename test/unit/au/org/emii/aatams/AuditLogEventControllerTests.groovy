package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class AuditLogEventControllerTests extends AbstractControllerUnitTestCase
{
    def eventSomeone
    def eventJkburges

    protected void setUp()
    {
        super.setUp()

        mockDomain(Person)

        Person.metaClass.static.get =
        {
            new Person(username: 'jkburges')
        }

        mockDomain(AuditLogEvent)

        eventSomeone = new AuditLogEvent(actor: "someone", dateCreated: new Date(1))
        eventJkburges = new AuditLogEvent(actor: "jkburges", dateCreated: new Date(2))
        [eventSomeone, eventJkburges].each {
            it.save()
        }
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    void testAllVisibleToSysAdmin()
    {
        hasRole = true

        controller.params.max = 10
        def eventList = controller.list()

        assertEquals(2, eventList.auditLogEventInstanceList.size())
        assertEquals(2, eventList.auditLogEventInstanceTotal)
        assertTrue(eventList.auditLogEventInstanceList.contains(eventSomeone))
        assertTrue(eventList.auditLogEventInstanceList.contains(eventJkburges))
    }

    void testOnlyOwnVisibleToNonSysAdmin()
    {
        hasRole = false

        controller.params.max = 10
        def eventList = controller.list()

        assertEquals(1, eventList.auditLogEventInstanceList.size())
        assertEquals(1, eventList.auditLogEventInstanceTotal)
        assertFalse(eventList.auditLogEventInstanceList.contains(eventSomeone))
        assertTrue(eventList.auditLogEventInstanceList.contains(eventJkburges))
    }
}
