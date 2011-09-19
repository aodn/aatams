package au.org.emii.aatams

import grails.test.*

class OrganisationTests extends GrailsUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testListActive()
    {
        Organisation activeOrg1 = new Organisation(name:"active 1", status:EntityStatus.ACTIVE)
        Organisation pendingOrg1 = new Organisation(name:"pending 1", status:EntityStatus.PENDING)
        Organisation activeOrg2 = new Organisation(name:"active 2", status:EntityStatus.ACTIVE)
        
        def orgList = [activeOrg1, pendingOrg1, activeOrg2]
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }
        
        def activeOrgList = Organisation.listActive()
        assertEquals(2, activeOrgList.size())
        assertTrue(activeOrgList.contains(activeOrg1))
        assertTrue(activeOrgList.contains(activeOrg2))
    }
}
