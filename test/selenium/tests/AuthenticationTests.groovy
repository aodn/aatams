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
	}

	void testLoginAsSysadmin()
	{
		LoginPage loginPage = homePage.login()
		loginPage.typeUsername("jkburges")
		loginPage.typePassword("password")
		
		GettingStartedPage gettingStartedPage = loginPage.clickSignIn()
		assertEquals("Getting Started", gettingStartedPage.getBodyHeading())
		
		homePage = homePage.logout()
	}
}
