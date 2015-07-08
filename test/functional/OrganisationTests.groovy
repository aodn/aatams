import pages.*
import module.*

import org.junit.Test

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys

class OrganisationTests extends GrailsCrudTest  {
    def listPage = OrganisationListPage
    def showPage = OrganisationShowPage
    def createPage = OrganisationCreatePage
    def editPage = OrganisationEditPage

    @Test
    void testList() {
        listOrganisationsAsUnauthorised()
        listOrganisationsAsSysAdmin()
    }

    @Test
    void testShow() {
        doTestShow("CSIRO",
                   [name:"CSIRO",
                    department:"CMAR",
                    phoneNumber:"1234",
                    faxNumber:"1234",
                    streetAddress:"12 Smith Street, Hobart, TAS, Australia, 7000",
                    postalAddress:"34 Queen Street, Melbourne, VIC, Australia, 3000",
                    people:["Pending Pending", "Joe Bloggs", "John Citizen"]])
    }

    @Test
    void testEdit() {
        doTestEdit("CSIRO")
    }

    @Test
    void testCreate() {
        doTestCreate(
            [nameTextField:"Some New Organisation",
             departmentTextField:"Marine",
             phoneNumberTextField:"1234",
             faxNumberTextField:"4321",
             streetAddressStreetAddressTextField:"12 Smith Street",
             streetAddressSuburbTownTextField:"Hobart",
             streetAddressStateTextField:"TAS",
             streetAddressPostcodeTextField:"7000",
             streetAddressCountryTextField:"Australia",
             postalAddressStreetAddressTextField:"PO Box 1234",
             postalAddressSuburbTownTextField:"Melbourne",
             postalAddressStateTextField:"VIC",
             postalAddressPostcodeTextField:"3000",
             postalAddressCountryTextField:"Australia"],
             [])
    }

    private void listOrganisationsAsUnauthorised() {
        doTestList(
            1,
            [name:"CSIRO",
             department:"CMAR",
             phoneNumber:"1234",
             faxNumber:"1234",
             streetAddress:"12 Smith Street, Hobart, TAS, Australia, 7000",
             postalAddress:"34 Queen Street, Melbourne, VIC, Australia, 3000"],
            [],
            [])
    }

    private void listOrganisationsAsSysAdmin() {
        loginAsSysAdmin()
        doTestList(3, [name:"CSIRO"], [], [])
        doTestList(3, [name:"IMOS"], [], [])
        doTestList(3, [name:"IMOS 2"], [], [])
    }
}
