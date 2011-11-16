package au.org.emii.aatams.test

import grails.plugins.selenium.pageobjects.Page;
import grails.plugins.selenium.pageobjects.UnexpectedPageException;

class AboutPage extends Page 
{
	private static final String URL = "/aatams/about"
	
	
	public AboutPage() throws UnexpectedPageException 
	{
		super()
	}

	public AboutPage(String uri) throws UnexpectedPageException 
	{
		super(uri)
	}

	@Override
	protected void verifyPage() throws UnexpectedPageException 
	{
		pageTitleIs("About AATAMS")
	}
	
	public String getBodyHeading()
	{
		return selenium.getText("css=h1")
	}
}
