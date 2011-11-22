import pages.*
import module.*

import org.junit.Test

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys

class OrganisationTests extends GrailsCrudTest 
{
	@Test
	void testList()
	{
		listOrganisationsAsUnauthorised()
		listOrganisationsAsSysAdmin()
	}
	
	@Test
	void testShow()
	{
		showOrganisation()
	}
	
	@Test
	void testEdit()
	{
		editOrganisation()
	}
	
	@Test
	void testCreate()
	{
		createOrganisation()
	}
	
	private void listOrganisationsAsUnauthorised()
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
	
	private void listOrganisationsAsSysAdmin()
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
	
	private void showOrganisation()
	{
		to OrganisationListPage
		def csiroRow = organisationRows[0]
		csiroRow.showLink.click()
		
		assertShowPageDetails("CSIRO", "CMAR", "1234", "1234", "12 Smith Street, Hobart, TAS, Australia, 7000", "34 Queen Street, Melbourne, VIC, Australia, 3000", 3)
	}
	
	void editOrganisation()
	{
		loginAsSysAdmin()
		to OrganisationListPage
		def csiroRow = organisationRows[0]
		csiroRow.showLink.click()

		String currName = name
		String newName = "different name"
		assertNameUpdate(newName)
		
		// Cleanup.
		assertNameUpdate(currName)
	}
	
	private void assertNameUpdate(newName)
	{
		assert at(OrganisationShowPage)
		editButton.click()
		assert at(OrganisationEditPage)
		
		nameTextField.value(newName)

		// Workaround for: http://code.google.com/p/selenium/issues/detail?id=2700
		JavascriptExecutor js = (JavascriptExecutor) driver
		js.executeScript( "document.getElementsByName('_action_update')[0].click();" );
//		updateButton.click()

		assert at(OrganisationShowPage)
		assert name == newName
	}
	
	void createOrganisation()
	{
		loginAsSysAdmin()
		to OrganisationListPage
		newButton.click()
		
		assert at(OrganisationCreatePage)
		
		nameTextField.value("Some New Organisation")
		departmentTextField.value("Marine")
		phoneNumberTextField.value("1234")
		faxNumberTextField.value("4321")

		streetAddressStreetAddressTextField.value("12 Smith Street")
		streetAddressSuburbTownTextField.value("Hobart")
		streetAddressStateTextField.value("TAS")
		streetAddressPostcodeTextField.value("7000")
		streetAddressCountryTextField.value("Australia")

		postalAddressStreetAddressTextField.value("PO Box 1234")
		postalAddressSuburbTownTextField.value("Melbourne")
		postalAddressStateTextField.value("VIC")
		postalAddressPostcodeTextField.value("3000")
		postalAddressCountryTextField.value("Australia")

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
