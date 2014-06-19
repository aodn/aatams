package pages

class PersonCreateEditPage extends LayoutPage 
{
    static content =
    {
        nameTextField { $("input", name: "name") }
        usernameTextField { $("input", name: "username") }
        organisationSelect { $("select", name: "organisation.id") }
        phoneNumberTextField { $("input", name: "phoneNumber") }
        emailAddressTextField { $("input", name: "emailAddress") }
        defaultTimeZoneSelect { $("select", name: "defaultTimeZone") }
    }
}
