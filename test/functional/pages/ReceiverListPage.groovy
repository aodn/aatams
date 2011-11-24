package pages

import module.ReceiverRow

class ReceiverListPage extends ListPage 
{
	static url = "receiver/list"
	
	static at =
	{
		title == "Receiver List"
	}
	
	static content =
	{
		detailTable { $("div.list table", 0) }
		rowsAsTr(required: false) { detailTable.find("tbody").find("tr") }
		detailRows { rowsAsTr.collect { module ReceiverRow, it } }
		
		newButton (required: false, to: ReceiverCreatePage) { $("a", text: "New Receiver") }
	}
}
