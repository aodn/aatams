package module

import geb.Module
import pages.*

class OrganisationRow extends Module 
{
	static content =
	{
		cell { $("td", it) }
		cellText { cell(it).text() }
		
		showLink (to: OrganisationShowPage) { cell(0).find("a") }
		orgName { cellText(1) }
		department { cellText(2) }
		phoneNumber { cellText(3) }
		faxNumber{ cellText(4) }
		streetAddress { cellText(5) }
		postalAddress { cellText(6) }
		projects { cellText(7) }
		people { cellText(8) }
	}
}
