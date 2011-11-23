import org.junit.Test;

import pages.*

class InstallationTests extends GrailsCrudTest 
{
	@Test
	void testList()
	{
		to InstallationListPage
		
		assert installationRows.size() == 3
		
		def bondiLineRow =  findRowByName(installationRows, "Bondi Line")
		
		assert bondiLineRow.name == "Bondi Line"
		assert bondiLineRow.configuration == "CURTAIN"
		assert bondiLineRow.project == "Seal Count"
		assert bondiLineRow.numStations == "3"
		assert bondiLineRow.stations.size() == 3
		assert bondiLineRow.stations.containsAll(["Bondi SW1", "Bondi SW3", "Bondi SW2"])
	}
	
	private def findRowByName(rows, name)
	{
		rows.find
		{
			it.name == name
		}
	}
	
	@Test
	void testShow()
	{
		to InstallationListPage
		
		def bondiLineRow =  findRowByName(installationRows, "Bondi Line")
		bondiLineRow.showLink.click()
		
		assertShowPage("Bondi Line", "CURTAIN", "Seal Count")
	}
	
	private void assertShowPage(installationName, configuration, projectName)
	{
		assert at(InstallationShowPage)
		assert name == installationName
		assert configurationLink.text() == configuration
		assert projectLink.text() == projectName 
	}
	
	@Test
	void testCreate()
	{
		loginAsSysAdmin()
		to InstallationListPage
		newButton.click()
		
		assert at(InstallationCreatePage)
		
		nameTextField.value("Derwent Array")
		configurationSelect.value("60")	// ARRAY
		projectSelect.value("13")	// Tuna
		createButton.click()
		
		assertShowPage("Derwent Array", "ARRAY", "Tuna")
		
		withConfirm { deleteButton.click() }
	}
	
	@Test
	void testEdit()
	{
		loginAsSysAdmin()
		to InstallationListPage
		def bondiLineRow =  findRowByName(installationRows, "Bondi Line")
		bondiLineRow.showLink.click()

		navigateToEditPageFromShowPage()
		assertNameChange()
	}

	private navigateToEditPageFromShowPage() 
	{
		assert at(InstallationShowPage)
		editButton.click()
		assert at(InstallationEditPage)
	}

	private void assertNameChange() 
	{
		assert at(InstallationEditPage)
		
		String currName = name
		String newName = "different name"
		assertNameUpdate(newName)

		// Cleanup.
		navigateToEditPageFromShowPage()
		assertNameUpdate(currName)
	}
	
	private void assertNameUpdate(newName)
	{
		nameTextField.value(newName)

		updateButton.click()

		assert at(InstallationShowPage)
		assert name == newName
	}
}
