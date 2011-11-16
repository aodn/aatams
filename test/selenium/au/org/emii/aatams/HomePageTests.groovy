package au.org.emii.aatams

import au.org.emii.aatams.test.*

import com.thoughtworks.selenium.*
import java.util.regex.Pattern

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class HomePageTests extends GroovyTestCase
{
	void testOpenHomePage() throws Exception
	{
		HomePage homePage = HomePage.open()
		
		assertTrue(homePage.getNotifications().contains("Click here to register to use AATAMS"))
		
		// TODO: acknowledge notification
		// test it's not there
		
		assertEquals("Australian Animal Tagging and Monitoring System (AATAMS)", homePage.getAboutPage().getBodyHeading())
		assertEquals("Login", homePage.getBannerPage().getLoginText())
	}
	
	void testLoginAsSysadmin()
	{
		HomePage homePage = HomePage.open()
		
		LoginPage loginPage = homePage.getBannerPage().login()
		loginPage.typeUsername("jkburges")
		loginPage.typePassword("password")
		
		GettingStartedPage gettingStartedPage = (GettingStartedPage)loginPage.clickSignIn()
		assertEquals("Getting Started", gettingStartedPage.getBodyHeading())
		
		homePage.getBannerPage().logout()
	}
}
