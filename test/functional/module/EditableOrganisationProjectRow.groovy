package module

import geb.Module;
import pages.OrganisationShowPage

class EditableOrganisationProjectRow extends Module 
{
    static content =
    {
        cell { $("td", it) }
        cellText { cell(it).text() }
        
        showLink (to: OrganisationShowPage){ cell(0).find("a") }
        deleteLink (to: OrganisationShowPage){ cell(1).find("a") }
        orgName { cellText(2) }
    }
}
