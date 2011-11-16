package au.org.emii.aatams.test

import grails.plugins.selenium.pageobjects.Page;
import grails.plugins.selenium.pageobjects.UnexpectedPageException;

class GettingStartedPage extends Page 
{
	public GettingStartedPage() throws UnexpectedPageException 
	{
	}

	public GettingStartedPage(String uri) throws UnexpectedPageException 
	{
		super(uri)
	}

	@Override
	protected void verifyPage() throws UnexpectedPageException 
	{
		pageTitleIs("Getting Started")
	}
	
	public String getBodyHeading()
	{
		return selenium.getText("css=h1")
	}
}
