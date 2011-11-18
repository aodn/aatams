package pages

import geb.Page

class LoginPage extends Page 
{
	static url = "/auth/login"
	
	static at =
	{
//		title == "Login"
		true
	}
	
	static content =
	{
		usernameTextField { $("input[@name='username']") }
		passwordTextField { $("input[@name='password']") }
		signInButton(to: GettingStartedPage) { "Sign In"() }
	}
}
