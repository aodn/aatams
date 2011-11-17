package pages

import grails.plugins.selenium.pageobjects.UnexpectedPageException;

class GettingStartedPage extends LayoutPage 
{
	public GettingStartedPage() throws UnexpectedPageException 
	{
		super()
	}

	public GettingStartedPage(String uri) throws UnexpectedPageException 
	{
		super(uri)
	}

	@Override
	protected void verifyPage() throws UnexpectedPageException 
	{
//		pageTitleIs("Getting Started")
	}
	
	public String getBodyHeading()
	{
		return selenium.getText("css=h1")
	}
}
