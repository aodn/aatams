package pages

import module.AddPersonToProjectDialog
import module.EditableOrganisationProjectRow
import module.EditableProjectRoleRow

class ProjectEditPage extends ProjectCreateEditPage  {
    static url = "project/edit"

    static at = {
        title == "Edit Project"
    }

    static content = {
        updateButton (to: [ProjectShowPage]) { $("input", value:"Update") }
        deleteButton (to: [ProjectListPage]) { $("input", value:"Delete") }

        row { $("label", text: it).parent().parent() }

        nestedRowsAsTr { row(it).find("table.nested").find("tbody").find("tr") }
        projectRoleRows  {
            def retVal = nestedRowsAsTr("People").collect { module EditableProjectRoleRow, it }
            retVal.pop()
            retVal.pop()
            return retVal
        }

        projectRoles  {
            projectRoleRows.collect {
                [name: it.name, projectRole: it.projectRole, access: it.access]
            }
        }

        organisationProjectRows {
            def retVal = nestedRowsAsTr("Organisations").collect { module EditableOrganisationProjectRow, it }
            retVal.pop()
            retVal.pop()

            return retVal
        }


        addPersonLink { $("a", id:"add_person_to_project") }
        addPersonDialog { module AddPersonToProjectDialog, $("div", id:"dialog-form-add-person").parent() }
    }
}
