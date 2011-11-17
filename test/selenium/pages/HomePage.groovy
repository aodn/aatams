package pages

import java.rmi.UnexpectedException;

import grails.plugins.selenium.pageobjects.UnexpectedPageException

class HomePage extends AboutPage 
{
	static HomePage open()
	{
		return new HomePage("/aatams/about")
//		return new HomePage(URL)
	}
	
	public HomePage() throws UnexpectedPageException
	{
		super()
	}
	
	public HomePage(String uri) throws UnexpectedPageException
	{
		super(uri)
	}
}
