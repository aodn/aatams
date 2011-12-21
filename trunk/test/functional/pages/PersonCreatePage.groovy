package pages

class PersonCreatePage extends PersonCreateEditPage
{
	static content =
	{
		passwordTextField { $("input", name: "password") }
		passwordConfirmTextField { $("input", name: "passwordConfirm") }

		createButton (to: [PersonShowPage]) { $("input", type: "submit") }
	}
}
