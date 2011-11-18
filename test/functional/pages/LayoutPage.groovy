package pages

import geb.Page

class LayoutPage extends Page 
{
	static content =
	{
		heading { $("h1") }
//		loginLink(to: LoginPage) { $("#userlogin > a") }
		loginLink(to: LoginPage) { $("a", text: "Login") }
//		loginLink(to: LoginPage) { $("a[id=loginLink]") }
	}
}
