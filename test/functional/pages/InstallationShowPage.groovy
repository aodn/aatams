package pages

class InstallationShowPage extends ShowPage 
{
    static url = "installation/show"
    
    static at =
    {
        title == "Show Installation"
    }
    
    static content =
    {
        editButton(to: InstallationEditPage) { $("input", value: "Edit") }
        deleteButton(to: InstallationListPage) { $("input", value: "Delete") }

        row { $("td.name", text: it).parent() }
        value { row(it).find("td.value").text() }
        
        name { value("Name") }
        configurationLink { row("Configuration").find("a") }
        configuration { configurationLink.text() }
        
        projectLink { row("Project").find("a") }
        project { projectLink.text() }
    }
}
