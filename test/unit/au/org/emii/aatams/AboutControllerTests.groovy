package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class AboutControllerTests extends AbstractControllerUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()

		permitted = true
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
