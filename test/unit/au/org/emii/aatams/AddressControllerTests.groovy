package au.org.emii.aatams

import grails.test.*

class AddressControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() 
    {
        controller.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
        
        def address = new Address(id:1,
                                  streetAddress:'1 Smith Street',
                                  suburbTown:'Moonah',
                                  state:'TAS',
                                  postcode:'7000',
                                  country:'Australia')
        mockDomain(Address, [address])
        
        controller.params.id = 1
        def returnValue = controller.show()
        assertSame(returnValue.addressInstance, address)
 
    }
}
