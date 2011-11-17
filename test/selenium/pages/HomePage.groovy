package pages

import grails.plugins.selenium.pageobjects.UnexpectedPageException

class HomePage extends AboutPage 
{
	static HomePage open()
	{
		return new HomePage(URL)
	}
	
	public HomePage(String uri) throws UnexpectedPageException
	{
		super(uri)
	}
}
