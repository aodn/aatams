package pages

class ReceiverCreatePage extends ReceiverCreateEditPage 
{
	static content =
	{
		createButton (to: [ReceiverShowPage]) { $("input", type: "submit") }
	}
}
