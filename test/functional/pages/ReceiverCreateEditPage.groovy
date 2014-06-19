package pages

class ReceiverCreateEditPage extends LayoutPage 
{
    static content =
    {
//        idTextField { TODO }
        organisationSelect { $("select", name: "organisation.id") }
        modelSelect { $("select", name: "model.id") }
        serialNumberTextField { $("input", name: "serialNumber") }
        statusSelect { $("select", name: "status.id") }
        commentTextField { $("input", name: "comment") }
    }
}
