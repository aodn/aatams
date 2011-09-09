import grails.test.*

class CachedDbRealmTests extends GrailsUnitTestCase 
{
    def realm
    
    protected void setUp() 
    {
        super.setUp()

        realm = new CachedDbRealm()
        
        SecUser user = new SecUser(username:"user", passwordHash:"asdf")
        SecUser userDiff = new SecUser(username:"userDiff", passwordHash:"asdf")
        def userList = [user, userDiff]
        mockDomain(SecUser, userList)
        userList.each{ it.save() }
        
        SecUser.metaClass.static.executeQuery =
        {
            query ->
            return null
        }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testCacheMiss() 
    {
        assertFalse(realm.getCacheHit())
        assertFalse(realm.isPermitted("user", "write"))
        assertFalse(realm.getCacheHit())
    }

    void testCacheHit() 
    {
        // First time will be a miss
        assertFalse(realm.isPermitted("user", "write"))
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        assertFalse(realm.isPermitted("user", "write"))
        assertTrue(realm.getCacheHit())
        
        // Different user (miss)
        assertFalse(realm.isPermitted("userDiff", "write"))
        assertFalse(realm.getCacheHit())
        
        // Different permission (miss)
        assertFalse(realm.isPermitted("user", "writeDiff"))
        assertFalse(realm.getCacheHit())
        
        
        // Different user and permission (miss)
        assertFalse(realm.isPermitted("userDiff", "writeDiff"))
        assertFalse(realm.getCacheHit())
        
        // Same user and permission (hit)
        assertFalse(realm.isPermitted("user", "write"))
        assertTrue(realm.getCacheHit())
    }
    
    void testCacheHitThenDiffUser() 
    {
        // First time will be a miss
        assertFalse(realm.isPermitted("user", "write"))
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        assertFalse(realm.isPermitted("user", "write"))
        assertTrue(realm.getCacheHit())
        
        // Different user (miss)
        assertFalse(realm.isPermitted("userDiff", "write"))
        assertFalse(realm.getCacheHit())
    }
    
    void testCacheHitThenDiffPerm() 
    {
        // First time will be a miss
        assertFalse(realm.isPermitted("user", "write"))
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        assertFalse(realm.isPermitted("user", "write"))
        assertTrue(realm.getCacheHit())
        
        // Different permission (miss)
        assertFalse(realm.isPermitted("user", "writeDiff"))
        assertFalse(realm.getCacheHit())
    }
    
    void testCacheHitThenDiffUserAndPerm() 
    {
        // First time will be a miss
        assertFalse(realm.isPermitted("user", "write"))
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        assertFalse(realm.isPermitted("user", "write"))
        assertTrue(realm.getCacheHit())
        
        // Different user and permission (miss)
        assertFalse(realm.isPermitted("userDiff", "writeDiff"))
        assertFalse(realm.getCacheHit())
    }
    
    void testCacheHitTwice() 
    {
        // First time will be a miss
        assertFalse(realm.isPermitted("user", "write"))
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        assertFalse(realm.isPermitted("user", "write"))
        assertTrue(realm.getCacheHit())
        
        // Same user and permission (hit)
        assertFalse(realm.isPermitted("user", "write"))
        assertTrue(realm.getCacheHit())
    }
}
