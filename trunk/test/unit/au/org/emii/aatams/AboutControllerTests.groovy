package au.org.emii.aatams

import grails.test.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class AboutControllerTests extends ControllerUnitTestCase 
{
    boolean authenticated = true
    
    protected void setUp() 
    {
        super.setUp()

        def subject = [ getPrincipal: { "username" },
                        isAuthenticated: { authenticated },
                        hasRole: { true },
                        isPermitted:
                        {
                            return true
                        }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testUnauthenticated() 
    {
        authenticated = false
        
        controller.home()
        
        assertEquals("index", controller.renderArgs.view)
        assertTrue(controller.redirectArgs.isEmpty())
    }

    void testAuthenticated() 
    {
        authenticated = true
        
        controller.home()
        
        assertEquals("gettingStarted", controller.redirectArgs.controller)
        assertTrue(controller.renderArgs.isEmpty())
    }
}
