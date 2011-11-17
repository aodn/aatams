package pages

import pages.GettingStartedPage;
import grails.plugins.selenium.pageobjects.Page;
import grails.plugins.selenium.pageobjects.UnexpectedPageException;

class LoginPage extends Page 
{
	private static final String URL = "/aatams/auth/login"
	
	public LoginPage() throws UnexpectedPageException 
	{
		super()
	}

	public LoginPage(String uri) throws UnexpectedPageException 
	{
		super(uri)
	}

	@Override
	protected void verifyPage() throws UnexpectedPageException 
	{
		pageTitleIs("Login")
	}
	
	public void typeUsername(String username)
	{
		selenium.type("name=username", username)
	}
	
	public void typePassword(String password)
	{
		selenium.type("name=password", password)
	}
	
	public GettingStartedPage clickSignIn()
	{
		selenium.click("css=input[type=\"submit\"]")
		selenium.waitForPageToLoad("30000")
		
		return new GettingStartedPage()
	}
}
