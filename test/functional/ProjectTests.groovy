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
		
//		assert nestedRowsAsTr("Organisations").size() == organisations.size()
//		assert nestedRowsAsTr("People").size() == roles.size()
		
		assert projectRoleRows.size() == roles.size()
		roles.each
		{
			expectedRole ->
			
			def actualRole = projectRoleRows.find 
			{
				expectedRole.name == it.name
			}
			
			assert actualRole.name == expectedRole.name
			assert actualRole.projectRole == expectedRole.projectRole
			assert actualRole.access == expectedRole.access
		}
		
		assert organisationProjectRows.size() == organisations.size()
		assert organisations == organisationProjectRows*.orgName
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

		String currName = name
		String newName = "different name"
		assertNameUpdate(newName)
		
		// Cleanup.
		assertNameUpdate(currName)

	}

	private void assertNameUpdate(newName)
	{
		assert at(ProjectShowPage)
		editButton.click()
		assert at(ProjectEditPage)
		
		nameTextField.value(newName)

		updateButton.click()

		assert at(ProjectShowPage)
		assert name == newName
	}
}
