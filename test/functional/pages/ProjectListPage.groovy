package pages

import module.ProjectRow

class ProjectListPage extends ListPage  {
    static url = "project/list"

    static at = {
        title == "Project List"
    }

    static content = {
        projectTable { $("div.list table", 0) }
        rowsAsTr(required: false) { projectTable.find("tbody").find("tr") }
        detailRows { rowsAsTr.collect { module ProjectRow, it } }

        newButton (required: false, to: ProjectCreatePage) { $("a", text: "New Project") }
    }
}
