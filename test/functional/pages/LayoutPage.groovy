package pages

import geb.Page

class LayoutPage extends Page 
{
	static content =
	{
		message { $("div.message").text() }
		heading { $("h1") }
		loginLink(to: LoginPage) { $("a", text: "Login") }
	}
}
