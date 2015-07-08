package au.org.emii.aatams

import grails.test.*

class OrganisationTests extends GrailsUnitTestCase  {
    protected void setUp()  {
        super.setUp()
    }

    protected void tearDown()  {
        super.tearDown()
    }

    void testListActive() {
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

    void testNullPostalAddress() {
        Organisation org = new Organisation(name:"org",
                                            department:"asdf",
                                            phoneNumber:"1234",
                                            faxNumber:"234",
                                            streetAddress:new Address(),
                                            //postalAddress:null
                                            )

        mockForConstraintsTests(Organisation, [org])

        assertFalse(org.validate())
    }

    void testToString() {
        Organisation org = new Organisation(name: "CSIRO",
                                            department: "CMAR")

        assertEquals("CSIRO (CMAR)", String.valueOf(org))
    }

    void testSortListByName() {
        Organisation aaa = new Organisation(name: "aaa")
        Organisation bbb = new Organisation(name: "bbb")
        Organisation ccc = new Organisation(name: "ccc")

        // Save out-of-order.
        def orgList = [ccc, aaa, bbb]

        mockDomain(Organisation, orgList)
        orgList.each { it.save() }

        def retrievedOrgList = Organisation.list(sort:'name')
        assertEquals(aaa, retrievedOrgList[0])
        assertEquals(bbb, retrievedOrgList[1])
        assertEquals(ccc, retrievedOrgList[2])
    }

    void testTotalReceivers() {
        Organisation aaa = new Organisation(name: "aaa")
        Receiver r1 = new Receiver()
        Receiver r2 = new Receiver()
        def receiverList = [r1, r2]

        mockDomain(Organisation, [aaa])
        mockDomain(Receiver, receiverList)

        receiverList.each {
            aaa.addToReceivers(it)
        }

        aaa.save()

        assertEquals(2, aaa.getTotalReceivers())
    }
}
