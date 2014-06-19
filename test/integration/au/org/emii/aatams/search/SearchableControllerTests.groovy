package au.org.emii.aatams.search

import grails.test.*
import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractControllerUnitTestCase;

class SearchableControllerTests extends AbstractControllerUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testSearchEmbargoNotAuthenticated()
    {
        authenticated = false
        permitted = false
        
        def tag = Tag.findBySerialNumber('62339')
        assert(tag)
        
        controller.params.q = "62339"
        def model = controller.index()
        
        assertFalse(model.searchResult.results*.id.contains(tag.id))
    }

    void testSearchEmbargoAuthenticatedNotPermitted()
    {
        authenticated = true
        permitted = false
        
        def tag = Tag.findBySerialNumber('62339')
        assert(tag)
        
        controller.params.q = "62339"
        def model = controller.index()
        
        assertFalse(model.searchResult.results*.id.contains(tag.id))
    }

    void testSearchEmbargoAuthenticatedAndPermitted()
    {
        authenticated = true
        permitted = true
        
        def tag = Tag.findBySerialNumber('62339')
        assert(tag)
        
        controller.params.q = "62339"
        def model = controller.index()
        
        assertTrue(model.searchResult.results*.id.contains(tag.id))
    }
    
    void testSearchReceiverName()
    {
        assertSearchReceiver("VR2W-101336", ["VR2W-101336"])
    }
    
    void testSearchReceiverSerialNumber()
    {
        assertSearchReceiver("101336", ["VR2W-101336"])
    }
    
    void testSearchReceiverNameImplicitWildCard()
    {
        assertSearchReceiver("101336", ["VR2W-101336"])
    }
    
    void testSearchReceiverSerialNumberImplicitWildCard()
    {
        assertSearchReceiver("1336", ["VR2W-101336"])
    }
    
    private void assertSearchReceiver(searchTerm, expectedNames)
    {
        controller.params.q = searchTerm
        def model = controller.index()
        
        def receiverResults = model.searchResult.results.grep
        {
            it instanceof Receiver
        }
        
        assertEquals(expectedNames, receiverResults*.name)
    }
}
