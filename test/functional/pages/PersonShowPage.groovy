package pages

class PersonShowPage extends ShowPage 
{
	static url = "person/show"
	
	static at =
	{
		title == "Show Person"
	}
	
	static content =
	{
		row { $("td.name", text: it).parent() }
		value { row(it).find("td.value").text() }
		
		name { value("Name") }
		organisationLink { row("Organisation").find("a") }
		projectLinks { row("Projects").find("a") }
		defaultTimeZone { value("Default Time Zone") }
		
		// These elements should only be visible to sys admin.
		username { value("Username") }
		phoneNumber { value("Phone Number") }
		emailAddress { value("Email Address") }
		status { value("Status") }

		editButton(to: PersonEditPage) { $("input", value: "Edit") }
		deleteButton(to: PersonListPage) { $("input", value: "Delete") }
		
	}
}
