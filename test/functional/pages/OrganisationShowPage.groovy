package pages

class OrganisationShowPage extends ShowPage
{
	static url = "organisation/show" // + "/" + id
	
	static at =
	{
		title == "Show Organisation"
	}
	
	static content =
	{
//		editButton(to: EditPage) { $("input", value: "Edit") }
//		deleteButton(to: ListPage) { $("input", value: "Delete") }
		
		row { $("td.name", text: it).parent() }
		value { row(it).find("td.value").text() }
		
		name { value("Name") }
		department { value("Department") }
		phoneNumber { value("Phone Number") }
		faxNumber{ value("Fax Number") }
		streetAddress { value("Street Address") }
		postalAddress { value("Postal Address") }
		peopleLinks { row("People").find("a") }
	}
}
