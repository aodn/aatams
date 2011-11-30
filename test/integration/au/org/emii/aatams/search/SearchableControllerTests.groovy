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
        
        def tag = Tag.findByCodeName('A69-1303-62339')
        assert(tag)
        
        controller.params.q = "A69-1303-62339"
        def model = controller.index()
        
        assertFalse(model.searchResult.results*.id.contains(tag.id))
    }

    void testSearchEmbargoAuthenticatedNotPermitted()
    {
        authenticated = true
        permitted = false
        
        def tag = Tag.findByCodeName('A69-1303-62339')
        assert(tag)
        
        controller.params.q = "A69-1303-62339"
        def model = controller.index()
        
        assertFalse(model.searchResult.results*.id.contains(tag.id))
    }

    void testSearchEmbargoAuthenticatedAndPermitted()
    {
        authenticated = true
        permitted = true
        
        def tag = Tag.findByCodeName('A69-1303-62339')
        assert(tag)
        
        controller.params.q = "A69-1303-62339"
        def model = controller.index()
        
        assertTrue(model.searchResult.results*.id.contains(tag.id))
    }
	
	void testSearchReceiverCodeName()
	{
		assertSearchReceiver("VR2W-101336", ["VR2W-101336"])
	}
	
	void testSearchReceiverSerialNumber()
	{
		assertSearchReceiver("12345678", ["VR2W-101336"])
	}
	
	void testSearchReceiverCodeNameImplicitWildCard()
	{
		assertSearchReceiver("101336", ["VR2W-101336"])
	}
	
	void testSearchReceiverSerialNumberImplicitWildCard()
	{
		assertSearchReceiver("5678", ["VR2W-101336"])
	}
	
	private void assertSearchReceiver(searchTerm, expectedCodeNames)
	{
		controller.params.q = searchTerm
		def model = controller.index()
		
		def receiverResults = model.searchResult.results.grep
		{
			it instanceof Receiver
		}
		
		assertEquals(expectedCodeNames, receiverResults*.codeName)
	}
}
