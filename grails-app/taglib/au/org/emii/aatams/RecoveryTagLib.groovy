package au.org.emii.aatams

class RecoveryTagLib  {
    def recoveryList = {
        attrs, body ->

        out << render(template: "/receiverRecovery/recoveryList",
                      model: attrs)
    }

    def column = {
        attrs, body ->

        if (attrs.sortable) {
            out << g.sortableColumn(property: attrs.property, title: attrs.title, params: attrs.params)
        }
        else {
            out << "<th>" << attrs.title << "</th>"
        }
    }
}
