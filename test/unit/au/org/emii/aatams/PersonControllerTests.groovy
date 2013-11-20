package au.org.emii.aatams

import grails.test.*
import au.org.emii.aatams.test.AbstractControllerUnitTestCase

import org.joda.time.DateTimeZone

import org.apache.shiro.crypto.hash.Sha256Hash

class PersonControllerTests extends AbstractControllerUnitTestCase
{
    def permissionUtilsService

    boolean mailSent

    def user

    protected void setUp()
    {
        super.setUp()

        mockDomain(Address)
        mockDomain(Person)
        mockDomain(Organisation)

        TestUtils.setupMessage(controller)
        initData()

        mockLogging(PermissionUtilsService)
        permissionUtilsService = new PermissionUtilsService()
        controller.permissionUtilsService = permissionUtilsService

        mailSent = false
        controller.metaClass.sendMail = { mailSent = true }

        user = new Person(username: "username")
        mockDomain(Person, [user])
        user.save()
    }

    protected void tearDown()
    {
        super.tearDown()
    }

	protected def getPrincipal()
	{
		return user?.id
	}

    static def createDataList()
    {
        Organisation csiro = Organisation.findByName('CSIRO')

        Person jonBurgess =
            new Person(username:'jkburges',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Jon Burgess',
                       organisation:csiro,
                       phoneNumber:'1234',
                       emailAddress:'jkburges@utas.edu.au',
                       status:EntityStatus.ACTIVE)

        Person joeBloggs =
            new Person(username:'jbloggs',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'Joe Bloggs',
                       organisation:csiro,
                       phoneNumber:'1234',
                       emailAddress:'jbloggs@blah.au',
                       status:EntityStatus.ACTIVE)

        Person johnCitizen =
            new Person(username:'jcitizen',
                       passwordHash:new Sha256Hash("password").toHex(),
                       name:'John Citizen',
                       organisation:csiro,
                       phoneNumber:'5678',
                       emailAddress:'jcitizen@blah.au',
                       status:EntityStatus.PENDING)

        def personList = [jonBurgess, joeBloggs, johnCitizen]
        return personList
    }

    void initData()
    {
        def orgList = OrganisationControllerTests.createDataList()
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }

        def personList = createDataList()
        mockDomain(Person, personList)
        personList.each { it.save() }
    }


    void testShow()
    {
		hasRole = true

        def address = new Address(id:1,
                                  streetAddress:'1 Smith Street',
                                  suburbTown:'Moonah',
                                  state:'TAS',
                                  postcode:'7000',
                                  country:'Australia')

        mockDomain(Address, [address])
        address.save()

        // Create organisation.
        def imos = new Organisation(id:2,
                                    name:'IMOS',
                                    department:'eMII',
                                    phoneNumber:'12345678',
                                    faxNumber:'12345678',
                                    streetAddress:address,
                                    postalAddress:address,
                                    status:EntityStatus.ACTIVE)
        mockDomain(Organisation, [imos])
        imos.save()

        def joeBloggs = new Person(organisation:new Organisation(),
                                   name:'Joe Bloggs',
                                   username:'jbloggs',
                                   emailAddress:'jbloggs@imos.org.au',
                                   phoneNumber:'12345678',
                                   passwordHash:'asdf',
                                   status:EntityStatus.ACTIVE)
        mockDomain(Person, [joeBloggs])
        joeBloggs.save()

        controller.params.id = joeBloggs.id
        def returnValue = controller.show()

        assertSame(returnValue.personInstance, joeBloggs)
    }

//    void testSaveUsernameToLowerCase()
//    {
//        mockDomain(Person)
//
//        Organisation org =
//            new Organisation(name:"org",
//                             department:"dep",
//                             phoneNumber:"1234",
//                             faxNumber:"1234",
//                             streetAddress:new Address(),
//                             postalAddress:new Address(),
//                             status:EntityStatus.ACTIVE)
//        mockDomain(Organisation, [org])
//        org.save()
//
//        def cmd = new PersonCreateCommand(
//                      name: "John",
//                      username: "John",
//                      password: "password",
//                      passwordConfirm: "password",
//                      organisation:org,
//                      phoneNumber:"1234",
//                      emailAddress:"john@asdf.com",
//                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))
//
//        mockForConstraintsTests(PersonCreateCommand, [cmd])
//        assertTrue(cmd.validate())
//
//        controller.save(cmd)
//
//        assertEquals("john", Person.findByName("John").username)
//        assertEquals("show", controller.redirectArgs.action)
//
//        // Try to save "john".
//        cmd.name = "john"
//        assertTrue(cmd.validate())
//        controller.save(cmd)
//        assertEquals("create", controller.renderArgs.view)
//    }

    void testUpdateUsernameToLowerCase()
    {
        Person person = new Person(name:"John",
                                   username:"john")

        mockDomain(Person, [person])
        person.save()

        controller.params.id = person.id
        controller.params.username = "Wayne"

        controller.update()

        def updatedPerson = Person.findByName("John")
        assertEquals("wayne", updatedPerson.username)
    }

//    void testSaveExistingOrganisation()
//    {
//        hasRole = false
//
//        Organisation org =
//            new Organisation(name:"org",
//                             department:"dep",
//                             phoneNumber:"1234",
//                             faxNumber:"1234",
//                             streetAddress:new Address(),
//                             postalAddress:new Address(),
//                             status:EntityStatus.ACTIVE)
//
//        mockDomain(Organisation, [org])
//        org.save()
//
//        def cmd = new PersonCreateCommand(
//                      name: "John",
//                      username: "John",
//                      password: "password",
//                      passwordConfirm: "password",
//                      organisation:org,
//                      unlistedOrganisationName:null,
//                      phoneNumber:"1234",
//                      emailAddress:"john@asdf.com",
//                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))
//
//        mockForConstraintsTests(PersonCreateCommand, [cmd])
//        assertTrue(cmd.validate())
//
//        controller.save(cmd)
//
//        assertEquals("john", Person.findByName("John").username)
//        assertEquals("show", controller.redirectArgs.action)
//
//        assertTrue(mailSent)
//    }
//
//    void testSaveUnlistedOrganisation()
//    {
//        hasRole = false
//        user = null
//
//        mockDomain(Organisation)
//
//        def cmd = new PersonCreateCommand(
//                      name: "John",
//                      username: "John",
//                      password: "password",
//                      passwordConfirm: "password",
//                      organisation:null,
//                      unlistedOrganisationName:"unlisted",
//                      phoneNumber:"1234",
//                      emailAddress:"john@asdf.com",
//                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))
//
//        mockForConstraintsTests(PersonCreateCommand, [cmd])
//        assertTrue(cmd.validate())
//
//        controller.save(cmd)
//
//        assertNotNull(Person.findByName("John"))
//        assertEquals("john", Person.findByName("John").username)
//        assertEquals(EntityStatus.PENDING, Person.findByName("John").status)
//
//        assertNotNull(Organisation.findByName("unlisted"))
//        assertEquals(EntityStatus.PENDING, Organisation.findByName("unlisted").status)
//        assertEquals(Organisation.findByName("unlisted"), Person.findByName("John").organisation)
//        assertEquals(Person.findByName("John").username, Organisation.findByName("unlisted").requestingUser.username)
//
//        assertEquals("show", controller.redirectArgs.action)
//
//        assertTrue(mailSent)
//    }

    void testSaveBothOrganisationAndOrgName()
    {
        hasRole = false

        Organisation org = new Organisation(name:"org", status:EntityStatus.ACTIVE)
        mockDomain(Organisation, [org])
        org.save()

        def cmd = new PersonCreateCommand(
                      name: "John",
                      username: "John",
                      password: "password",
                      passwordConfirm: "password",
                      organisation:org,
                      unlistedOrganisationName:"unlisted",
                      phoneNumber:"1234",
                      emailAddress:"john@asdf.com",
                      defaultTimeZone:DateTimeZone.forID("Australia/Melbourne"))

        mockForConstraintsTests(PersonCreateCommand, [cmd])
        assertFalse(cmd.validate())

        controller.save(cmd)

        assertEquals("create", controller.renderArgs.view)
        assertNull(Person.findByName("John"))
        assertNull(Organisation.findByName("unlisted"))
        assertFalse(mailSent)
    }
}
