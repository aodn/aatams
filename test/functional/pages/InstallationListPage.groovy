package pages

import module.InstallationRow

class InstallationListPage extends ListPage {
    static url = "installation/list"

    static at = {
        title == "Installation List"
    }

    static content = {
        installationTable { $("div.list table", 0) }
        rowsAsTr(required: false) { installationTable.find("tbody").find("tr") }
        detailRows { rowsAsTr.collect { module InstallationRow, it } }

        newButton (required: false, to: InstallationCreatePage) { $("a", text: "New Installation") }
    }
}
