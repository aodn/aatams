package pages

import module.PointEditDialog

class InstallationStationCreateEditPage extends LayoutPage
{
    static content =
    {
        nameTextField { $("input", name: "name") }
        arrayPositionTextField { $("input", name: "curtainPosition") }
        installationSelect { $("select", name: "installation.id") }
        locationTextField { $("input", name: "location_pointInputTextField") }
        locationDialog { module PointEditDialog, $("div", id: "location_dialog-form-edit-point").parent() }
    }
}
