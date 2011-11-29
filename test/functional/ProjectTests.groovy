import org.junit.Test

import pages.*

class ProjectTests extends GrailsCrudTest 
{
	def listPage = ProjectListPage
	def showPage = ProjectShowPage
	def createPage = ProjectCreatePage
	def editPage = ProjectEditPage
	
	@Test
	void testList()
	{
		doTestList(3,
				   [name:"Seal Count", organisations:"CSIRO (CMAR)", principalInvestigator:"Joe Bloggs"],
				   [],
				   [])
	}

	@Test
	void testShow()
	{
		doTestShow("Seal Count",  
				   [name:"Seal Count", 
					projectRoles:[[name:"John Citizen", projectRole:"Administrator", access:"Read Only"], 
								  [name:"Joe Bloggs", projectRole:"Principal Investigator", access:"Read/Write"]],
					organisations:[[name:"CSIRO (CMAR)"]]])
	}
	
	@Test
	void testCreate()
	{
		doTestCreate(
			[nameTextField:"Prawns",
			 organisationSelect:"5",
			 personSelect:"23"],
		 	[name: "Prawns"])
	}

	@Test
	void testEdit()
	{
		doTestEdit("Seal Count")

		navigateToEditPageFromShowPage()
		assertAddPerson()
	}

	private void assertAddPerson()
	{
		assert at(getEditPage())
/**		
		addPersonLink.click()
		
		assert addPersonDialog.rows.size() == 4
		assert addPersonDialog.projectLabel.text() == "Seal Count"
		addPersonDialog.personSelect.value("19")	// Joe Bloggs
		addPersonDialog.roleTypeSelect.value("18")	// Administrator
		addPersonDialog.accessSelect.value("READ_WRITE")

		try
		{
			addPersonDialog.createButton.click()
			
			assert at(getEditPage())
			updateButton.click()
			
			assertShowPageDetails(
				[projectRoles:[[name:"John Citizen", projectRole:"Administrator", access:"Read Only"],
				               [name:"Joe Bloggs", projectRole:"Principal Investigator", access:"Read/Write"],
							   [name:"Joe Bloggs", projectRole:"Administrator", access:"Read/Write"]]])
		}
		finally
		{
			navigateToEditPageFromShowPage()
			
			// Cleanup.
			def newRoleRow = findProjectRoleByNameAndRole(projectRoleRows, "Joe Bloggs", "Administrator")
			withConfirm { newRoleRow.deleteLink.click() }
			report("after delete")
		}
*/		
	}

	private def findProjectRoleByNameAndRole(roles, name, projectRole)
	{
		def actualRole = projectRoleRows.find 
		{
			(it.name == name) && (it.projectRole == projectRole)
		}
	}	
}
