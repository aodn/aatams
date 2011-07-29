package au.org.emii.aatams

import grails.test.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class PersonControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
        TestUtils.setupMessage(controller)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testShow() 
    {
        TestUtils.loginSysAdmin()
        
        def address = new Address(id:1,
                                  streetAddress:'1 Smith Street',
                                  suburbTown:'Moonah',
                                  state:'TAS',
                                  postcode:'7000',
                                  country:'Australia')
        
        // Create organisation.
        def imos = new Organisation(id:2,
                                    name:'IMOS',
                                    department:'eMII',
                                    phoneNumber:'12345678',
                                    faxNumber:'12345678',
                                    streetAddress:address,
                                    postalAddress:address,
                                    status:EntityStatus.ACTIVE)
        
        def joeBloggs = new Person(id:3,
                                   organisation:imos,
                                   name:'Joe Bloggs',
                                   emailAddress:'jbloggs@imos.org.au',
                                   phoneNumber:'12345678',
                                   status:EntityStatus.ACTIVE)
        mockDomain(Person, [joeBloggs])                      
        
        controller.params.id = 3
        def returnValue = controller.show()
        println(returnValue)
        
        assertSame(returnValue.personInstance, joeBloggs)
    }
}
