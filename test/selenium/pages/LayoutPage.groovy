package pages

import java.util.List;

import grails.plugins.selenium.pageobjects.Page;
import grails.plugins.selenium.pageobjects.UnexpectedPageException;

abstract class LayoutPage extends Page
{
	static final String REGISTER_ELEMENT_LOCATOR = "css=div.qtip-content.qtip-content"
	

	public LayoutPage() throws UnexpectedPageException 
	{
		super()
	}

	public LayoutPage(String uri) throws UnexpectedPageException 
	{
		super(uri)
	}
	
	public void refresh()
	{
		selenium.refresh()
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
		giveJavaScriptTimeToExecute()
		
		return selenium.isElementPresent(REGISTER_ELEMENT_LOCATOR)
	}

	private giveJavaScriptTimeToExecute() 
	{
		// TODO: the dodgy sleeps are to give client-side javascript time to execute
		// Need to revisit and find a better (faster, more reliable) way to do this.
		Thread.sleep(500)
	}
	
	public String getLoginText()
	{
		return selenium.getText("link=Login")
	}
	
	public void acknowledgeRegisterNotification()
	{
		selenium.click(REGISTER_ELEMENT_LOCATOR)
		giveJavaScriptTimeToExecute()
	}
	
	public void undoAcknowledgeRegisterNotification()
	{
		selenium.deleteCookie("REGISTER", "path=/, domain=localhost")
//		selenium.deleteAllVisibleCookies()
		giveJavaScriptTimeToExecute()
	}
	
	public List<String> getCreateLinks()
	{
		String[] allLinks = selenium.getAllLinks()
		println(allLinks)
		
		List<String> createLinks = allLinks.grep 
		{
			it.contains("create")
		}
		
		return createLinks
	}
}
