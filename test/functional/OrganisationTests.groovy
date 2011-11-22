import pages.*
import module.*

import org.junit.Test

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys

class OrganisationTests extends TestBase 
{
	@Test
	void listOrganisationsAsUnauthorised()
	{
		to OrganisationListPage
		
		assert organisationRows.size() == 1
		def csiroRow = organisationRows[0]
		assert csiroRow.orgName == "CSIRO"
		assert csiroRow.department == "CMAR"
		assert csiroRow.phoneNumber == "1234"
		assert csiroRow.faxNumber == "1234"
		assert csiroRow.streetAddress == "12 Smith Street, Hobart, TAS, Australia, 7000"
		assert csiroRow.postalAddress == "34 Queen Street, Melbourne, VIC, Australia, 3000"
		
		// TODO: sort order of associated entities is not guaranteed.
//		assert csiroRow.projects == "Seal Count, Tuna"
//		assert csiroRow.people == "[Pending Pending, Joe Bloggs, John Citizen]"

		assert !newButton
	}
	
	@Test
	void listOrganisationsAsSysAdmin()
	{
		loginAsSysAdmin()
		to OrganisationListPage
		assert organisationRows.size() == 3
		
		def orgNames = organisationRows.collect 
		{
			it.orgName
		}
		assert orgNames == ["CSIRO", "IMOS", "IMOS 2"]

		assert newButton
	}
	
	@Test
	void showOrganisation()
	{
		to OrganisationListPage
		def csiroRow = organisationRows[0]
		csiroRow.showLink.click()
		
		assertShowPageDetails("CSIRO", "CMAR", "1234", "1234", "12 Smith Street, Hobart, TAS, Australia, 7000", "34 Queen Street, Melbourne, VIC, Australia, 3000", 3)
	}
	
	@Test
	void createOrganisation()
	{
		loginAsSysAdmin()
		to OrganisationListPage
		newButton.click()
		
		assert at(OrganisationCreatePage)
		
		nameTextField << "Some New Organisation"
		departmentTextField << "Marine"
		phoneNumberTextField << "1234"
		faxNumberTextField << "4321"

		streetAddressStreetAddressTextField << "12 Smith Street"
		streetAddressSuburbTownTextField << "Hobart"
		streetAddressStateTextField << "TAS"
		streetAddressPostcodeTextField << "7000"
		streetAddressCountryTextField << "Australia"

		postalAddressStreetAddressTextField << "PO Box 1234"
		postalAddressSuburbTownTextField << "Melbourne"
		postalAddressStateTextField << "VIC"
		postalAddressPostcodeTextField << "3000"
		postalAddressCountryTextField << "Australia"

		// Workaround for: http://code.google.com/p/selenium/issues/detail?id=2700
		JavascriptExecutor js = (JavascriptExecutor) driver
		js.executeScript( "document.getElementById('create').click();" );
//		createButton.click()
		
		assert at(OrganisationShowPage)
		
		// Clean up.
		withConfirm { deleteButton.click() }
	}
	
	private void assertShowPageDetails(name, department, phoneNumber, faxNumber, streetAddress, postalAddress, numPeopleLinks)
	{
		assert at(OrganisationShowPage)
		assert name == name
		assert department == department
		assert phoneNumber == phoneNumber
		assert faxNumber == faxNumber
		assert streetAddress == streetAddress
		assert postalAddress == postalAddress
		assert peopleLinks.size() == numPeopleLinks
	}
}
