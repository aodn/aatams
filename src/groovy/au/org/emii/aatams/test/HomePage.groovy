package au.org.emii.aatams.test

import grails.plugins.selenium.pageobjects.Page
import grails.plugins.selenium.pageobjects.UnexpectedPageException

class HomePage extends Page 
{
	private static final String URL = "/aatams"
	
	static HomePage open()
	{
		return new HomePage(URL)
	}
	
	
	public HomePage(String uri) throws UnexpectedPageException
	{
		super(uri)
	}


	@Override
	protected void verifyPage() throws UnexpectedPageException 
	{
		pageTitleIs("About AATAMS")
	}
	
	public AboutPage getAboutPage()
	{
		return new AboutPage()
	}

	public BannerPage getBannerPage()
	{
		return new BannerPage()
	}	
	
	public List<String> getNotifications()
	{
		List<String> notifications = new ArrayList()
		notifications.add(selenium.getText("css=div.qtip-content.qtip-content"))
		
		return notifications
	}
	
	public String getLoginText()
	{
		return selenium.getText("link=Login")
	}
}
