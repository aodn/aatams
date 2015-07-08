package module

import geb.Module;
import pages.*

class PersonRow extends Module  {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }

        showLink (to: PersonShowPage) { cell(0).find("a") }
        name { cellText(1) }
        organisationLink { cell(2).find("a") }
        projects { cellText(3).tokenize(',').collect { it.trim() } }
    }
}
