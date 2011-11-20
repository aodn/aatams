package pages

class LoginPage extends LayoutPage 
{
	static url = "auth/login"
	
	static at =
	{
		title == "Login"
	}
	
	static content =
	{
		usernameTextField { $("input", name: "username") }
		passwordTextField { $("input", name: "password") }
		
		signInButton (to: [GettingStartedPage, LoginPage]) { $("input", type: "submit") }
	}
}
