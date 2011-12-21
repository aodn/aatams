package pages

import geb.Page

class OrganisationCreatePage extends OrganisationCreateEditBasePage 
{
	static url = "organisation/create"
	
	static at =
	{
		title == "Create Organisation"
	}
	
	static content =
	{
		createButton (to: [OrganisationShowPage]) { $("input", type: "submit") }
	}
}
