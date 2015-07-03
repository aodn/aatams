package module

import geb.Module;
import pages.*

class InstallationStationRow extends Module  {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }
        
        showLink (to: InstallationStationShowPage) { cell(0).find("a") }
        name { cellText(1) }
        arrayPosition { cellText(2) }
        location { cellText(3) }
        installationLink (to: InstallationShowPage) { cell(4).find("a") }
        projectLink (to: ProjectShowPage) { cell(5).find("a") }
        active { cellText(6) }
    }
}
