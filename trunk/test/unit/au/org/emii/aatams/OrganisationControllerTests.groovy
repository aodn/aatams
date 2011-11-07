package au.org.emii.aatams

import grails.test.*

class OrganisationControllerTests extends ControllerUnitTestCase 
{
    String toAddress
    boolean mailSent
    
    protected void setUp() 
    {
        super.setUp()

        mockDomain(Request)
        
        TestUtils.setupMessage(controller)
        initData()
        
        mockConfig("grails.gorm.default.list.max = 10")
        controller.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
        
        toAddress = null
        mailSent = false
        controller.metaClass.sendMail = 
        {
            it ->
            
            it.call()
            
            mailSent = true
        }
        
        controller.metaClass.to = 
        {
            toAddress = it
        }

        controller.metaClass.bcc = {}
        controller.metaClass.from = {}
        controller.metaClass.subject = {}
        controller.metaClass.body = {}
        controller.metaClass.createLink = {}
    }
    
    protected void tearDown() 
    {
        TestUtils.logout()
        super.tearDown()
    }

    static def createDataList()
    {
        Address address =
            new Address(streetAddress:'12 Smith Street',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7000')
        
        Person somePerson = new Person()

        //
        // Organisations.
        //
        Organisation activeOrg = 
            new Organisation(name:'CSIRO', 
                             department:'CMAR',
                             phoneNumber:'1234',
                             faxNumber:'1234',
                             streetAddress:address,
                             postalAddress:address,
                             status:EntityStatus.ACTIVE,
                             request:new Request(requester:somePerson))

        Organisation pendingOrg = 
            new Organisation(name:'IMOS', 
                             department:'CMAR',
                             phoneNumber:'1234',
                             faxNumber:'1234',
                             streetAddress:address,
                             postalAddress:address,
                             status:EntityStatus.PENDING,
                             request:new Request(requester:somePerson))

        Organisation deactivatedOrg = 
            new Organisation(name:'SIMS', 
                             department:'CMAR',
                             phoneNumber:'1234',
                             faxNumber:'1234',
                             streetAddress:address,
                             postalAddress:address,
                             status:EntityStatus.DEACTIVATED,
                             request:new Request(requester:somePerson))
                         
        def orgList = [activeOrg, pendingOrg, deactivatedOrg]
        
        return orgList
    }
    
    void initData()
    {
        mockDomain(Address)
        
        def orgList = createDataList()
        
        mockDomain(Organisation, orgList)
        
        // Save each of the domain object so that the IDs are set properly.
        orgList.each
        {
            it.save(flush:true, failOnError:true)
        }
    }
    
    void testListAsSysAdmin()
    {
        TestUtils.loginSysAdmin(this)
        
        // There list of organisations should include non-active.
        def retVal = controller.list()
        assertEquals(3,
                     retVal.organisationInstanceTotal)
        assertEquals(3, retVal.organisationInstanceList.size())
    }
    
    void testListAsNonSysAdmin()
    {
        TestUtils.loginJoeBloggs(this)
        
        // There list of organisations should include non-active.
        def retVal = controller.list()
        assertEquals(1, 
                     retVal.organisationInstanceTotal)
        assertEquals(1, retVal.organisationInstanceList.size())
    }
    
    void testListAsNoone()
    {
        // There list of organisations should include non-active.
        def retVal = controller.list()
        assertEquals(1, retVal.organisationInstanceTotal)
        assertEquals(1, retVal.organisationInstanceList.size())
    }
    
    void testSaveAsSysAdmin()
    {
        TestUtils.loginSysAdmin(this)

        // Status should be set to ACTIVE.
        def address = 
                    [streetAddress:'12 Smith Street',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7000']

        controller.params.streetAddress = address
        controller.params.postalAddress = address
        
        def organisation = [name:'SIMS', 
                            department:'CMAR',
                            phoneNumber:'1234',
                            faxNumber:'1234']
                        
        controller.params.organisation = organisation
        
        def retVal = controller.save()
        
        assertEquals("show", redirectArgs['action'])
        assertEquals(EntityStatus.ACTIVE, Organisation.get(redirectArgs['id']).status) 
        assertFalse(mailSent)
    }
    
    void testSaveAsNonSysAdmin()
    {
        // Status should be set to PENDING.
        TestUtils.loginJoeBloggs(this)

        def address = 
                    [streetAddress:'12 Smith Street',
                        suburbTown:'Hobart',
                        state:'TAS',
                        country:'Australia',
                        postcode:'7000']

        controller.params.streetAddress = address
        controller.params.postalAddress = address
        
        def organisation = [name:'SIMS 2', 
                            department:'CMAR',
                            phoneNumber:'1234',
                            faxNumber:'1234']
                        
        controller.params.organisation = organisation
        
        def retVal = controller.save()
        
        assertEquals("show", redirectArgs['action'])
        assertEquals(EntityStatus.PENDING, Organisation.get(redirectArgs['id'])?.status) 
        assertTrue(mailSent)
        assertEquals("jbloggs@test.com", toAddress)
    }
}
