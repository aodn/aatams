package pages

import module.*

class OrganisationListPage extends ListPage 
{
	static url = "organisation/list"
	
	static at =
	{
		title == "Organisation List"
	}
	
	static content =
	{
		organisationTable { $("div.list table", 0) }
		rowsAsTr(required: false) { organisationTable.find("tbody").find("tr") }
		organisationRows { rowsAsTr.collect { module OrganisationRow, it } }
		
		newButton (required: false, to: OrganisationCreatePage) { $("a", text: "New Organisation") }
	}
}
