package pages

class ProjectCreatePage extends ProjectCreateEditPage  {
    static url = "project/create"
    
    static at = {
        title == "Create Project"
    }
    
    static content = {
        createButton (to: [ProjectShowPage]) { $("input", type: "submit") }
    }
}
