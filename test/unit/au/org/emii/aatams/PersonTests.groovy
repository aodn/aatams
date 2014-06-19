package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractGrailsUnitTestCase
import grails.test.*

import org.joda.time.*

class PersonTests extends AbstractGrailsUnitTestCase
{
    def perthTZ = DateTimeZone.forID("Australia/Perth")
    def person

    protected void setUp()
    {
        super.setUp()

        person = new Person(username:"person",
                                organisation:new Organisation(),
                                defaultTimeZone:perthTZ)

        mockDomain(Person, [person])
        person.save()

        permitted = true
        hasRole = true
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    protected def getPrincipal()
    {
        return person?.id
    }

    void testDefaultTimeZone()
    {
//        assertEquals(perthTZ, Person.defaultTimeZone())
    }
}
