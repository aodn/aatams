package au.org.emii.aatams.search

import grails.test.*
import au.org.emii.aatams.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class SearchableControllerTests extends ControllerUnitTestCase 
{
    boolean authenticated = false
    boolean permitted = false
    
    protected void setUp() 
    {
        super.setUp()

        def subject = [ getPrincipal: { "username" },
                        isAuthenticated: { authenticated },
                        hasRole: { true },
                        isPermitted: { permitted }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
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
}
