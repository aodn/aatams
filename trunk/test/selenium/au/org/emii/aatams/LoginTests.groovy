package au.org.emii.aatams

import com.thoughtworks.selenium.*
import java.util.regex.Pattern

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class LoginTests extends GroovyTestCase 
{
    void testLogin() throws Exception 
    {
        selenium.open("/aatams/")
        selenium.click("link=Login")
        selenium.waitForPageToLoad("30000")
        selenium.type("name=username", "jkburges")
        selenium.type("name=password", "password")
        selenium.click("css=input[type=\"submit\"]")
        selenium.waitForPageToLoad("30000")
//        verifyTrue(selenium.isTextPresent("Logged in as jkburges (logout)"))
        assertTrue(selenium.isTextPresent("Logged in as jkburges (logout)"))
    }
}
