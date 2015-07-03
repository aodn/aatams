package module

import geb.Module;
import pages.InstallationShowPage

class InstallationRow extends Module  {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }
        
        showLink (to: InstallationShowPage) { cell(0).find("a") }
        name { cellText(1) }
        configuration{ cellText(2) }
        project { cellText(3) }
        numStations { cellText(4) }
        stations {
             String stationsAsString = cellText(5)
             stationsAsString = stationsAsString.substring(1, stationsAsString.length() - 1)
             def stationArray = stationsAsString.split(",").collect {
                 it.trim()
             }
             
             return stationArray
        }
    }
}
