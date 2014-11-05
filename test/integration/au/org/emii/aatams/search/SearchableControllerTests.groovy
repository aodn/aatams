package au.org.emii.aatams.search

import grails.test.*
import au.org.emii.aatams.*

class SearchableControllerTests extends ControllerUnitTestCase
{
    // void testSearchEmbargoNotAuthenticated()
    // {
    //     authenticated = false
    //     permitted = false

    //     controller.params.q = "62339"
    //     def model = controller.index()

    //     def tag = Tag.findBySerialNumber('62339')
    //     assertFalse(model.searchResult.results*.id.contains(tag.id))
    // }

    // void testSearchEmbargoAuthenticatedNotPermitted()
    // {
    //     authenticated = true
    //     permitted = false

    //     controller.params.q = "62339"
    //     def model = controller.index()

    //     def tag = Tag.findBySerialNumber('62339')
    //     assertFalse(model.searchResult.results*.id.contains(tag.id))
    // }

    // void testSearchEmbargoAuthenticatedAndPermitted()
    // {
    //     authenticated = true
    //     permitted = true

    //     controller.params.q = "62339"
    //     def model = controller.index()

    //     def tag = Tag.findBySerialNumber('62339')
    //     assertTrue(model.searchResult.results*.id.contains(tag.id))
    // }

    void testSearchReceiverName()
    {
        TestUtils.buildReceiver('VR2W-101336').index()
        assertSearchReceiver("VR2W-101336", ["VR2W-101336"])
    }

    void testSearchReceiverSerialNumber()
    {
        TestUtils.buildReceiver('VR2W-101336').index()
        assertSearchReceiver("101336", ["VR2W-101336"])
    }

    void testSearchReceiverSerialNumberImplicitWildCard()
    {
        TestUtils.buildReceiver('VR2W-101336').index()
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
