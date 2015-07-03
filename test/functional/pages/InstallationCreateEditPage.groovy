package pages

class InstallationCreateEditPage extends LayoutPage  {
    static content = {
        nameTextField { $("input", name: "name") }
        configurationSelect { $("select", name: "configuration.id") }
        projectSelect { $("select", name: "project.id") }
    }
}
