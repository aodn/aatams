package shiro

import grails.test.*
import au.org.emii.aatams.*

import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.web.util.WebUtils

class AuthControllerTests extends ControllerUnitTestCase
{
    def loginSuccess

    protected void setUp()
    {
        super.setUp()

        loginSuccess = true

        WebUtils.metaClass.static.getSavedRequest = { javax.servlet.ServletRequest request -> null }

        controller.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
        controller.metaClass.checkForPendingUser = { Map params -> }
        controller.metaClass.doLogin = { UsernamePasswordToken token -> 
            if (!loginSuccess) 
            {
                throw new AuthenticationException()
            }
        }

        controller.embargoService = new EmbargoService()
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

    void testSignInBadAuthNoFormat()
    {
        loginSuccess = false

        controller.params.username = "bad"
        controller.params.password = "stuff"

        controller.signIn()

        assertEquals('login', controller.redirectArgs.action)
        assertEquals(200, controller.response.status)
    }

    void testSignInBadAuthXmlFormat()
    {
        loginSuccess = false

        controller.params.username = "bad"
        controller.params.password = "stuff"
        controller.request.format = "xml"

        controller.signIn()

        assertEquals('unauthorized', controller.redirectArgs.action)
    }
}
