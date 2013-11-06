package shiro

import grails.test.*
import au.org.emii.aatams.*

import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.web.util.WebUtils

class AuthControllerTests extends ControllerUnitTestCase
{
    protected void setUp()
    {
        super.setUp()

        WebUtils.metaClass.static.getSavedRequest = { javax.servlet.ServletRequest request -> null }
        controller.metaClass.checkForPendingUser = { Map params -> }
        controller.metaClass.doLogin = { UsernamePasswordToken token -> }

        controller.embargoService = new EmbargoService()
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    void testLoginFromReportExtract()
    {
        String targetUri = "/report/extract?name=detection&formats=CSV&formats=KML"
        controller.params.username = "jbloggs"
        controller.params.password = "password"
        controller.params.targetUri = targetUri

        controller.signIn()

        assertEquals(targetUri, controller.redirectArgs.uri)
    }

    void testLoginWithUpperCaseName()
    {
        controller.params.username = "FredSmith"
        controller.params.password = "paSSwoRD"

        controller.metaClass.doLogin =
        {
            UsernamePasswordToken token ->

            assertEquals("fredsmith", token.username)
            assertEquals("paSSwoRD".toCharArray(), token.password)
        }

        controller.signIn()
    }

    void testRedirectToTargetUrlSlash()
    {
        def targetUri = '/'
        controller.redirectToTargetUrl(targetUri)

        assertEquals(targetUri, controller.redirectArgs.uri)
        assertNull(controller.redirectArgs.url)
    }

    void testRedirectToTargetUrlStartsWithHttp()
    {
        def targetUri = 'http://something'
        controller.redirectToTargetUrl(targetUri)

        assertNull(controller.redirectArgs.uri)
        assertEquals(targetUri, controller.redirectArgs.url)
    }

    void testRedirectToTargetUrlStartsWithHttps()
    {
        def targetUri = 'https://something'
        controller.redirectToTargetUrl(targetUri)

        assertNull(controller.redirectArgs.uri)
        assertEquals(targetUri, controller.redirectArgs.url)
    }

    void testRedirectToTargetUrlStartsWithAnimalRelease()
    {
        def targetUri = 'animalRelease'
        controller.redirectToTargetUrl(targetUri)

        assertEquals(targetUri, controller.redirectArgs.uri)
        assertNull(controller.redirectArgs.url)
    }
}
