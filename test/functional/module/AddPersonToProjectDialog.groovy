package module

import geb.Module
import pages.ProjectEditPage

class AddPersonToProjectDialog extends Module 
{
    static content =
    {
        rows { $("tr") }
        
        value { it.find("td.value").children()[0] }
        
        projectLabel { value(rows[0]) }
        personSelect{ value(rows[1]) }
        roleTypeSelect { value(rows[2]) }
        accessSelect { value(rows[3]) }
        
        createButton (to: ProjectEditPage) { $("span", text: "Create") }
    }
}
