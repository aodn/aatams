package module

import geb.Module
import pages.PersonEditPage

class ChangePasswordDialog extends Module  {
    static content = {
        rows { $("tr") }

        value { it.find("td.value").children()[0] }

        personLabel { value(rows[0]) }
        passwordTextField { value(rows[1]) }
        passwordConfirmTextField { value(rows[1]) }

        updateButton (to: PersonEditPage) { $("span", text: "Update") }
    }
}
