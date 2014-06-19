package pages

class InstallationStationCreatePage extends InstallationStationCreateEditPage 
{
    static url = "installationStation/create"
    
    static at =
    {
        title == "Create Installation Station"
    }
    
    static content =
    {
        createButton (to: [InstallationStationShowPage]) { $("input", type: "submit") }
    }
}
