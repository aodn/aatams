import org.junit.Test

import au.org.emii.aatams.test.BaseTests

import pages.*

class AuthenticationTests extends BaseTests
{
	@Test
	void signInUnknownUsername()
	{
		assertFailedLogin("asdf", "password")
	}
	
	@Test
	void signInIncorrectPassword()
	{
		assertFailedLogin("jkburges", "xyz")
	}
	
	@Test
	void signInPendingUser()
	{
		assertFailedLogin("pending", "pending")
	}
	
	private void assertFailedLogin(username, password)
	{
		login(username, password)
		assert at(LoginPage)
		assert message == "Invalid username and/or password"
		assert usernameTextField.value() == username
		assert passwordTextField.value() == ""
	}
	
	@Test
	void signInAsSysAdmin()
	{
		loginAsSysAdmin()
		assert at(GettingStartedPage)
	}
}
