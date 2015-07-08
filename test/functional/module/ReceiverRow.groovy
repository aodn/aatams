package module

import geb.Module
import pages.*

class ReceiverRow extends Module  {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }

        showLink (to: ReceiverShowPage) { cell(0).find("a") }
        id { cellText(1) }
        name { id }    // ID, but called name to keep it consistent with other page objects.
        model { cellText(2) }
        serialNumber { cellText(3) }
        organisationLink { cell(4).find("a") }
        organisation { organisationLink.text() }
        status { cellText(5) }
    }
}
