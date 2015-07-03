package pages

class ProjectCreateEditPage extends LayoutPage {
    static content = {
        nameTextField { $("input", name: "name") }
        organisationSelect { $("select", name: "organisation.id") }
        personSelect { $("select", name: "person.id") }
        
        
    }
}
