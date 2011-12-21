package pages

class InstallationEditPage extends InstallationCreateEditPage 
{
	static url = "installation/edit"
	
	static at =
	{
		title == "Edit Installation"
	}
	
	static content =
	{
		updateButton (to: [InstallationShowPage]) { $("input", value:"Update") }
		deleteButton (to: [InstallationListPage]) { $("input", value:"Delete") }
	}
}
