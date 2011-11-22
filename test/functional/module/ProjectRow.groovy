package module

import geb.Module
import pages.ProjectShowPage

class ProjectRow extends Module 
{
	static content =
	{
		cell { $("td", it) }
		cellText { cell(it).text() }
		
		showLink (to: ProjectShowPage) { cell(0).find("a") }
		name { cellText(1) }
		organisations { cellText(2) }
		principalInvestigator { cellText(3) }
		people{ cellText(4) }
	}
}
