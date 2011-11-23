import org.junit.Test;

import pages.*

class PersonTests extends GrailsCrudTest 
{
	@Test
	void testList()
	{
		to PersonListPage
		
		assert peopleRows.size() == 3
		
		def joeBloggsRow = peopleRows[0]
		assert joeBloggsRow.name == "Joe Bloggs"
		assert joeBloggsRow.organisationLink.text() == "CSIRO (CMAR)"
		assert joeBloggsRow.projects.contains("Seal Count")
		assert joeBloggsRow.projects.contains("Tuna")
	}
	
	@Test
	void testShow()
	{
		to PersonListPage
		
		def joeBloggsRow = peopleRows[0]
		joeBloggsRow.showLink.click()
		
		def personName = "Joe Bloggs"
		def organisationName = "CSIRO (CMAR)"
		def sealCount = "Seal Count"
		def tuna = "Tuna"
		def timezone = "Australia/Perth"

		assertShowPage(personName, organisationName, [sealCount, tuna], timezone)
	}
	
	private void assertShowPage(personName, organisationName, projectNames, timezone)
	{
		assert at(PersonShowPage)
		assert name == personName
		assert organisationLink.text() == organisationName
		
		projectNames.each 
		{
			assert projectLinks*.text().contains(it)
		}
		assert defaultTimeZone == timezone
	}

	private void assertShowPage(personName, organisationName, projectNames, timezone, expectedUsername, expectedPhoneNumber, expectedEmailAddress, expectedStatus)
	{
		assertShowPage(personName, organisationName, projectNames, timezone)
		
		assert username == expectedUsername
		assert phoneNumber == expectedPhoneNumber
		assert emailAddress == expectedEmailAddress
		assert status == expectedStatus
	}
	
	@Test
	void testCreate()
	{
		loginAsSysAdmin()
		to PersonListPage
		newButton.click()
		
		assert at(PersonCreatePage)
		
		def personName = "Ricky Ponting"
		def theusername = "rponting"
		def thephoneNumber = "1234 4321"
		def theemailAddress = "rponting@acb.org.au"
		def thetimezone = "Australia/Hobart"
		
		nameTextField.value(personName)
		usernameTextField.value(theusername)
		passwordTextField.value("batsman")
		passwordConfirmTextField.value("batsman")
		organisationSelect.value("5")	// CSIRO
		phoneNumberTextField.value(thephoneNumber)
		emailAddressTextField.value(theemailAddress)
		defaultTimeZoneSelect.value(thetimezone)
		createButton.click()
		
		assertShowPage(personName, "CSIRO (CMAR)", [], "Australia/Hobart", theusername, phoneNumber, emailAddress, "ACTIVE")
		
		// Cleanup.
		withConfirm { deleteButton.click() }
	}

	@Test
	void testEdit()
	{
		loginAsSysAdmin()
		to PersonListPage
		def joeBloggsRow = peopleRows[0]
		joeBloggsRow.showLink.click()
		
		navigateToEditPageFromShowPage()
		assertNameChange()

		navigateToEditPageFromShowPage()
		assertPasswordChange()
	}
	
	private void assertNameChange()
	{
		assert at(PersonEditPage)
		
		String currName = name
		String newName = "different name"
		assertNameUpdate(newName)
		
		navigateToEditPageFromShowPage()
		assertNameUpdate(currName)	
	}
	
	private void assertPasswordChange()
	{
		changePassword("new password")
		changePassword("password")
	}
	
	private void changePassword(password)
	{
		assert at(PersonEditPage)
		changePasswordLink.click()
		
		assert changePasswordDialog.personLabel.text() == "Joe Bloggs"
		changePasswordDialog.passwordTextField.value(password)
		changePasswordDialog.passwordConfirmTextField.value(password)
		
		changePasswordDialog.updateButton.click()
	}
	
	private void assertNameUpdate(newName)
	{
		nameTextField.value(newName)

		updateButton.click()

		assert at(PersonShowPage)
		assert name == newName
	}
	
	private void navigateToEditPageFromShowPage()
	{
		assert at(PersonShowPage)
		editButton.click()
		assert at(PersonEditPage)
	}
}
