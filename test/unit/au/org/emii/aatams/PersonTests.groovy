package au.org.emii.aatams

import grails.test.*

import org.joda.time.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class PersonTests extends GrailsUnitTestCase 
{
    def perthTZ = DateTimeZone.forID("Australia/Perth")
    
    protected void setUp() 
    {
        super.setUp()

        mockDomain(Person)
        def person = new Person(username:"person",
                                organisation:new Organisation(),
                                defaultTimeZone:perthTZ)
                               
        mockDomain(Person, [person])
        person.save()
        
        def subject = [ getPrincipal: { person.username },
                        isAuthenticated: { true },
                        hasRole: { true },
                        isPermitted: { true }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testDefaultTimeZone() 
    {
        assertEquals(perthTZ, Person.defaultTimeZone())
    }
}
