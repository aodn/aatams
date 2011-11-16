package au.org.emii.aatams.test

import grails.plugins.selenium.pageobjects.Page;
import grails.plugins.selenium.pageobjects.UnexpectedPageException;

class BannerPage extends Page 
{
	public BannerPage() throws UnexpectedPageException 
	{
		super()
	}

	public BannerPage(String uri) throws UnexpectedPageException 
	{
		super(uri)
	}

	@Override
	protected void verifyPage() throws UnexpectedPageException 
	{
	}
	
	public LoginPage login()
	{
		selenium.click("link=Login")
		selenium.waitForPageToLoad("30000")
		
		return new LoginPage()
	}
	
	public HomePage logout()
	{
		selenium.click("link=logout")
		selenium.waitForPageToLoad("30000")
		
		return HomePage.open()
	}
	
	public String getLoginText()
	{
		return selenium.getText("link=Login")
	}
}
