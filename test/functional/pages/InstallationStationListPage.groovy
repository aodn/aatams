package pages

import module.InstallationStationRow

class InstallationStationListPage extends ListPage  {
    static url = "installationStation/list"
    
    static at = {
        title == "Installation Station List"
    }
    
    static content = {
        stationTable { $("div.list table", 0) }
        rowsAsTr(required: false) { stationTable.find("tbody").find("tr") }
        detailRows { rowsAsTr.collect { module InstallationStationRow, it } }
        
        newButton (required: false, to: InstallationStationCreatePage) { $("a", text: "New Installation Station") }
    }

}
