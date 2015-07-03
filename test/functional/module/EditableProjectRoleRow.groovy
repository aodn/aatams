package module

import geb.Module

import pages.ProjectEditPage
import pages.ProjectShowPage

class EditableProjectRoleRow extends Module  {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }

        showLink (to: ProjectShowPage) { cell(0).find("a") }
        deleteLink (to: ProjectEditPage) { cell(1).find("a") }
        name { cellText(2) }
        projectRole { cellText(3) }
        access { cellText(4) }
    }
}
