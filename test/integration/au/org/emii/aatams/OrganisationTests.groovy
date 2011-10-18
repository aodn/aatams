package au.org.emii.aatams

import grails.test.*
import org.joda.time.DateTimeZone

class OrganisationTests extends GroovyTestCase 
{
    protected void setUp() 
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testDelete() 
    {
        Person person = 
            new Person(username:'name',
                       passwordHash:'asdf',
                       name:'name',
                       emailAddress:'person@asdf.com',
                       phoneNumber:'1234',
                       defaultTimeZone:DateTimeZone.forID("Australia/Hobart"),
                       status:EntityStatus.ACTIVE)
                   
        Organisation org1 = 
            new Organisation(name:'org1', 
                             department:'dep',
                             phoneNumber:'1234',
                             faxNumber:'1234',
                             streetAddress:Address.build(),
                             postalAddress:Address.build(),
                             requestingUser:person)
                             
        
        Organisation org2 = 
            new Organisation(name:'org2', 
                             department:'dep',
                             phoneNumber:'1234',
                             faxNumber:'1234',
                             streetAddress:Address.build(),
                             postalAddress:Address.build(),
                             requestingUser:person)
             
        def orgList = [org1, org2]
        orgList.each { it.save() }
                             
        println(org1.errors)
        assertFalse(org1.hasErrors())
        
        try
        {
            org1.delete()
            assertNull(Organisation.findByName('org1'))
        }
        catch (Throwable t)
        {
            println(t)
            fail()
        }
    }
}
