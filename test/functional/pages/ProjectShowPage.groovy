package pages

import module.*

class ProjectShowPage extends ShowPage 
{
	static url = "project/show" // + "/" + id
	
	static at =
	{
		title == "Show Project"
	}
	
	static content =
	{
		editButton(to: ProjectEditPage) { $("input", value: "Edit") }
		deleteButton(to: ProjectListPage) { $("input", value: "Delete") }

		row { $("td.name", text: it).parent() }
		value { row(it).find("td.value").text() }
		
		name { value("Name") }
		
		nestedRowsAsTr { row(it).find("table.nested").find("tbody").find("tr") }
		organisationProjectRows { nestedRowsAsTr("Organisations").collect { module OrganisationProjectRow, it } }
		projectRoleRows { nestedRowsAsTr("People").collect { module ProjectRoleRow, it } }
	}
}
