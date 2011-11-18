package pages

class LoginPage extends LayoutPage 
{
	static url = "auth/login"
	
	static at =
	{
		title == "Login"
		true
	}
	
	static content =
	{
		usernameTextField { $("input", name: "username") }
		passwordTextField { $("input", name: "password") }
		
		signInButton { $("input", type: "submit") }
	}
}
