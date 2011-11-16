package au.org.emii.aatams

import au.org.emii.aatams.test.*

import com.thoughtworks.selenium.*
import java.util.regex.Pattern

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class HomePageTests extends GroovyTestCase
{
	HomePage homePage
	
	void setUp() 
	{
		homePage = HomePage.open()
	}
	
	void testOpenHomePage() throws Exception
	{
		assertEquals("Australian Animal Tagging and Monitoring System (AATAMS)", homePage.getAboutPage().getBodyHeading())
		assertEquals("Login", homePage.getBannerPage().getLoginText())
	}

//	void testAcknowledgeRegisterNotification()
//	{
//		homePage.undoAcknowledgeRegisterNotification()
//		homePage.refresh()
//		homePage.waitForRegisterNotification()
//		assertTrue(homePage.isRegisterNotificationPresent())
//		assertTrue(homePage.getNotificationMessages().contains("Click here to register to use AATAMS"))
//
//		homePage.acknowledgeRegisterNotification()
//		homePage.refresh()
//		assertFalse(homePage.isRegisterNotificationPresent())
//		assertFalse(homePage.getNotificationMessages().contains("Click here to register to use AATAMS"))
//	}
	
	void testLoginAsSysadmin()
	{
		LoginPage loginPage = homePage.getBannerPage().login()
		loginPage.typeUsername("jkburges")
		loginPage.typePassword("password")
		
		GettingStartedPage gettingStartedPage = (GettingStartedPage)loginPage.clickSignIn()
		assertEquals("Getting Started", gettingStartedPage.getBodyHeading())
		
		homePage = homePage.getBannerPage().logout()
	}
}
