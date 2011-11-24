package pages

class InstallationStationShowPage extends ShowPage 
{
	static url = "installationStation/show"
	
	static at =
	{
		title == "Show Installation Station"
	}
	
	static content =
	{
		editButton(to: InstallationStationEditPage) { $("input", value: "Edit") }
		deleteButton(to: InstallationStationListPage) { $("input", value: "Delete") }

		row { $("td.name", text: it).parent() }
		value { row(it).find("td.value").text() }
		
		name { value("Name") }
		arrayPosition { value("Array Position") }
		location { value("Location") }
		installationLink { row("Installation").find("a") }
		installation { installationLink.text() }
		projectLink { row("Project").find("a") }
		project { projectLink.text() }
	}
}
