import org.junit.Test

import au.org.emii.aatams.test.BaseTests

import pages.*

class AuthenticationTests extends BaseTests
{
	@Test
	void signInUnknownUsername()
	{
		String unknownUsername = "asdf"
		signInWithUsernameAndPassword(unknownUsername, "password")
		assert at(LoginPage)
		assert message == "Invalid username and/or password"
//		assert usernameTextField.text() == unknownUsername
		assert passwordTextField.text() == ""
	}
	
	@Test
	void signInAsSysAdmin()
	{
		signInWithUsernameAndPassword("jkburges", "password")
		assert at(GettingStartedPage)
	}
	
	private void signInWithUsernameAndPassword(username, password)
	{
		to HomePage
		
		assert heading.text() == "Australian Animal Tagging and Monitoring System (AATAMS)"
		loginLink.click()
		
		assert at(LoginPage)
		usernameTextField << username
		passwordTextField << password

		signInButton.click()
	}
}
