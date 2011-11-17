package tests

import pages.*

import com.thoughtworks.selenium.*
import java.util.regex.Pattern

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class AuthenticationTests extends GroovyTestCase
{
	HomePage homePage
	
	void setUp() 
	{
		homePage = HomePage.open()
	}
	
	void testOpenHomePage() throws Exception
	{
		assertEquals("Australian Animal Tagging and Monitoring System (AATAMS)", homePage.getBodyHeading())
		assertEquals("Login", homePage.getLoginText())

		assertTrue(homePage.getCreateLinks().isEmpty())
	}

	void testLoginAsSysadmin()
	{
		try
		{
			LoginPage loginPage = homePage.login()
			loginPage.typeUsername("jkburges")
			loginPage.typePassword("password")
			
			GettingStartedPage gettingStartedPage = loginPage.clickSignIn()
//			assertEquals("Getting Started", gettingStartedPage.getBodyHeading())
	
//			assertTrue(gettingStartedPage.getCreateLinks().containsAll(["/aatams/organisation/create", 
//																		"/aatams/project/create", 
//																		"/aatams/person/create", 
//																		"/aatams/installation/create", 
//																		"/aatams/installationStation/create", 
//																		"/aatams/receiver/create", 
//																		"/aatams/tag/create", 
//																		"/aatams/animalRelease/create", 
//																		"/aatams/detection/create", 
//																		"/aatams/receiverDeployment/create", 
//																		"/aatams/receiverEvent/create"]))
		}
		finally
		{
			homePage = homePage.logout()
		}
	}
}
