import org.junit.Test

import pages.*

class ProjectTests extends GrailsCrudTest 
{
	@Test
	void testList()
	{
		to ProjectListPage
		
		assert projectRows.size() == 3
		
		def sealCountRow = findRowByName(projectRows, "Seal Count")
		assert sealCountRow
		assert sealCountRow.name == "Seal Count"
		assert sealCountRow.organisations == "CSIRO (CMAR)"
		assert sealCountRow.principalInvestigator == "Joe Bloggs"
		
		assert !newButton
		
		loginAsSysAdmin()
		to ProjectListPage
		assert newButton
	}

	@Test
	void testShow()
	{
		to ProjectListPage
		
		def sealCountRow = findRowByName(projectRows, "Seal Count")
		sealCountRow.showLink.click()
		
		assertShowPageDetails("Seal Count", ["CSIRO (CMAR)"], [[name:"John Citizen", projectRole:"Administrator", access:"Read Only"], 
															   [name:"Joe Bloggs", projectRole:"Principal Investigator", access:"Read/Write"]])
	}

	private def findRowByName(rows, name)
	{
		def retRow = rows.find
		{
			it.name == name
		}
		
		return retRow
	}
	
	private void assertShowPageDetails(name, organisations, roles)
	{
		assert at(ProjectShowPage)
		assert name == name
		
		assert projectRoleRows.size() == roles.size()
		roles.each
		{
			expectedRole ->
			
			def actualRole = findProjectRoleByName(projectRoleRows, expectedRole.name)
			
			assert actualRole.name == expectedRole.name
			assert actualRole.projectRole == expectedRole.projectRole
			assert actualRole.access == expectedRole.access
		}
		
		assert organisationProjectRows.size() == organisations.size()
		assert organisations == organisationProjectRows*.orgName
	}
	
	private def findProjectRoleByName(roles, name)
	{
		def actualRole = projectRoleRows.find 
		{
			it.name == name
		}
	}	
	
	@Test
	void testCreate()
	{
		loginAsSysAdmin()
		to ProjectListPage
		newButton.click()
		
		assert at(ProjectCreatePage)
		
		nameTextField.value("Prawns")
		organisationSelect.value("5")
		personSelect.value("23")
		createButton.click()
		
		assert at(ProjectShowPage)
		
		// Clean up.
		withConfirm { deleteButton.click() }
	}

	@Test
	void testEdit()
	{
		loginAsSysAdmin()
		to ProjectListPage
		def sealCountRow = findRowByName(projectRows, "Seal Count")
		sealCountRow.showLink.click()

		navigateToEditPageFromShowPage()
		assertNameChange()

		navigateToEditPageFromShowPage()
		assertAddPerson()
	}

	private navigateToEditPageFromShowPage() 
	{
		assert at(ProjectShowPage)
		editButton.click()
		assert at(ProjectEditPage)
	}

	private void assertNameChange() 
	{
		assert at(ProjectEditPage)
		
		String currName = name
		String newName = "different name"
		assertNameUpdate(newName)

		// Cleanup.
		navigateToEditPageFromShowPage()
		assertNameUpdate(currName)
	}

	private void assertAddPerson()
	{
		assert at(ProjectEditPage)
		
		addPersonLink.click()
		
		assert addPersonDialog.rows.size() == 4
		assert addPersonDialog.projectLabel.text() == "Seal Count"
		addPersonDialog.personSelect.value("19")	// Joe Bloggs
		addPersonDialog.roleTypeSelect.value("18")	// Administrator
		addPersonDialog.accessSelect.value("READ_WRITE")
		
//		addPersonDialog.createButton.click()
//		
//		assertShowPageDetails("Seal Count", ["CSIRO (CMAR)"], 
//							  [[name:"John Citizen", projectRole:"Administrator", access:"Read Only"],
//							   [name:"Joe Bloggs", projectRole:"Principal Investigator", access:"Read/Write"],
//							   [name:"Joe Bloggs", projectRole:"Administrator", access:"Read/Write"]])

		// Cleanup.
									   
	}
	
	private void assertNameUpdate(newName)
	{
		nameTextField.value(newName)

		updateButton.click()

		assert at(ProjectShowPage)
		assert name == newName
	}
}
