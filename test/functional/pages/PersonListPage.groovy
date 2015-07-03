package pages

import module.PersonRow

class PersonListPage extends ListPage  {
    static url = "person/list"
    
    static at = {
        title == "Person List"
    }
    
    static content = {
        peopleTable { $("div.list table", 0) }
        rowsAsTr(required: false) { peopleTable.find("tbody").find("tr") }
        detailRows { rowsAsTr.collect { module PersonRow, it } }
        
        newButton (required: false, to: PersonCreatePage) { $("a", text: "New Person") }
    }
}
