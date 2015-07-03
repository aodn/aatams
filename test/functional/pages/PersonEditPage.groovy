package pages

import module.ChangePasswordDialog

class PersonEditPage extends PersonCreateEditPage  {
    static url = "person/edit"
    
    static at = {
        title == "Edit Person"
    }
    
    static content = {
        updateButton (to: [PersonShowPage]) { $("input", value:"Update") }
        deleteButton (to: [PersonListPage]) { $("input", value:"Delete") }
        
        changePasswordLink { $("a", id: "change_password") }
        changePasswordDialog { module ChangePasswordDialog, $("div", id: "dialog-form-change-password").parent() }
    }
}
