package pages

class ReceiverEditPage extends ReceiverCreateEditPage 
{
	static url = "receiver/edit"
	
	static at =
	{
		title == "Edit Receiver"
	}
	
	static content =
	{
		updateButton (to: [ReceiverShowPage]) { $("input", value:"Update") }
		deleteButton (to: [ReceiverListPage]) { $("input", value:"Delete") }
	}
}
