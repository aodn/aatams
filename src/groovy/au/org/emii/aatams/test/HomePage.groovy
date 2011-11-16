package au.org.emii.aatams.test

import grails.plugins.selenium.pageobjects.Page
import grails.plugins.selenium.pageobjects.UnexpectedPageException

class HomePage extends Page 
{
	private static final String URL = "/aatams"

	static final String REGISTER_ELEMENT_LOCATOR = "css=div.qtip-content.qtip-content"
	
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
	
	public void refresh()
	{
		selenium.refresh()
	}
	
	public List<String> getNotificationMessages()
	{
		List<String> notifications = new ArrayList()
		
		if (selenium.isElementPresent(REGISTER_ELEMENT_LOCATOR))
		{
			notifications.add(selenium.getText(REGISTER_ELEMENT_LOCATOR))
		}
		
		return notifications
	}

	public boolean isRegisterNotificationPresent()
	{
		return selenium.isElementPresent(REGISTER_ELEMENT_LOCATOR)
	}	
	
	public String getLoginText()
	{
		return selenium.getText("link=Login")
	}
	
	public void acknowledgeRegisterNotification()
	{
		selenium.click(REGISTER_ELEMENT_LOCATOR)
	}
	
	public void waitForRegisterNotification()
	{
		selenium.waitForElementPresent(REGISTER_ELEMENT_LOCATOR)
	}
	
	public void undoAcknowledgeRegisterNotification()
	{
		selenium.deleteAllVisibleCookies()
//		selenium.deleteCookie("REGISTER", "path=/, domain=localhost")
	}
}
