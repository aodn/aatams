package au.org.emii.aatams

import grails.test.*

class ReceiverEventControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreate() 
    {
        controller.create()
        
        assertEquals("receiverDownloadFile", controller.redirectArgs.controller)
        assertEquals("createEvents", controller.redirectArgs.action)
    }
}
