package module

import geb.Module;

class ProjectRoleRow extends Module
{
	static content =
	{
		cell { $("td", it) }
		cellText { cell(it).text() }
		
		showLink { cell(0).find("a") }
		name { cellText(1) }
		projectRole { cellText(2) }
		access { cellText(3) }
	}
}
