package pages

import geb.Page;

class GettingStartedPage extends Page 
{
	static url = "gettingStarted"
	
	static at =
	{
		heading.text() ==~ /Getting Started/
	}
	
	static content =
	{
		
	}
}
