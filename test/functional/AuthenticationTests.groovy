import org.junit.Test

import pages.*

class AuthenticationTests extends BaseTests
{
	@Test
	void signInAsSysAdmin()
	{
		to AboutPage
		
		report("AboutPage")
		
		assert heading.text() == "Australian Animal Tagging and Monitoring System (AATAMS)"
		loginLink.click()
		
		assert at(LoginPage)
	}
}
