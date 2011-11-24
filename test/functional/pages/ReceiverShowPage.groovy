package pages

class ReceiverShowPage extends ShowPage 
{
	static url = "receiver/show"
	
	static at =
	{
		title == "Show Receiver"
	}
	
	static content =
	{
		row { $("td.name", text: it).parent() }
		value { row(it).find("td.value").text() }
		
		id { value("ID") }
		name { id }
		organisationLink { row("Organisation").find("a") }
		organisation { organisationLink.text() }
		model { value("Model") }
		serialNumber { value("Serial Number") }
		status { value("Status") }
		comment { value("Comment") }
	}
}
