package pages

class InstallationCreatePage extends InstallationCreateEditPage  {
    static url = "installation/create"
    
    static at = {
        title == "Create Installation"
    }
    
    static content = {
        createButton (to: [InstallationShowPage]) { $("input", type: "submit") }
    }
}
