package pages

class OrganisationEditPage extends OrganisationCreateEditBasePage {
    static url = "organisation/edit"
    
    static at = {
        title == "Edit Organisation"
    }
    
    static content = {
        updateButton (to: [OrganisationShowPage]) { $("input", value:"Update") }
        deleteButton (to: [OrganisationListPage]) { $("input", value:"Delete") }
        
        nameTextField { $("input", name: "name") }
        departmentTextField { $("input", name: "department") }
        phoneNumberTextField { $("input", name: "phoneNumber") }
        faxNumberTextField { $("input", name: "faxNumber") }
    }
}
