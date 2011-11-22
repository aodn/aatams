package module

import geb.Module;

class OrganisationProjectRow extends Module 
{
	static content =
	{
		cell { $("td", it) }
		cellText { cell(it).text() }
		
		showLink { cell(0).find("a") }
		orgName { cellText(1) }
	}
}
