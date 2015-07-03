package shiro

import grails.test.*

class SecUserTests extends GrailsUnitTestCase  {
    protected void setUp()  {
        super.setUp()
        
        mockDomain(SecUser)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testNullUsername()  {
        SecUser user = new SecUser(passwordHash:"asdf")
        mockForConstraintsTests(SecUser, [user])
        
        assertFalse(user.validate())
    }
    
    void testBlankUsername() {
        SecUser user = new SecUser(username:"", passwordHash:"asdf")
        mockForConstraintsTests(SecUser, [user])
        
        assertFalse(user.validate())
    }

    void testNonUniqueUsername() {
        SecUser user1 = new SecUser(username:"aaa", passwordHash:"asdf")
        mockForConstraintsTests(SecUser, [user1])
        
        assertTrue(user1.validate())

        SecUser user2 = new SecUser(username:"aaa", passwordHash:"asdf")
        mockForConstraintsTests(SecUser, [user1, user2])
        
        assertFalse(user2.validate())
    }
 
    void testNullPassword() {
        SecUser user = new SecUser(username:"")
        mockForConstraintsTests(SecUser, [user])
        
        assertFalse(user.validate())
    }
    
    void testBlankPassword() {
        SecUser user = new SecUser(username:"", passwordHash:"")
        mockForConstraintsTests(SecUser, [user])
        
        assertFalse(user.validate())
    }
}
