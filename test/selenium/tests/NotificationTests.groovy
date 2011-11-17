package tests

import pages.*

import com.thoughtworks.selenium.*
import java.util.regex.Pattern

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class NotificationTests extends GroovyTestCase
{
	HomePage homePage
	
	void setUp() 
	{
		homePage = HomePage.open()
	}

	void testAcknowledgeRegisterNotification()
	{
		homePage.undoAcknowledgeRegisterNotification()
		homePage.refresh()
		assertTrue(homePage.isRegisterNotificationPresent())
		assertTrue(homePage.getNotificationMessages().contains("Click here to register to use AATAMS"))

		homePage.acknowledgeRegisterNotification()
		homePage.refresh()
		assertFalse(homePage.isRegisterNotificationPresent())
		assertFalse(homePage.getNotificationMessages().contains("Click here to register to use AATAMS"))
	}
}
