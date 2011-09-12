import grails.test.*

import au.org.emii.aatams.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import shiro.*

class CachedDbRealmTests extends GrailsUnitTestCase 
{
    def realm
    
    PermissionUtilsService service
    ProjectRoleType piRoleType
    Person user
    Project project
    ProjectRoleType nonPiType
    
    protected void setUp() 
    {
        super.setUp()

        realm = new CachedDbRealm()
        
        user = new Person(username:"user", passwordHash:"asdf", name:Person)
        Person userDiff = new Person(username:"userDiff", passwordHash:"asdf")
        def userList = [user, userDiff]
        mockDomain(Person, userList)
        
        mockDomain(SecUser, userList)
        userList.each{ it.save() }
        
        SecUser.metaClass.static.executeQuery =
        {
            query ->
            return null
        }
        
        mockLogging(PermissionUtilsService, true)
        service = new PermissionUtilsService()
        piRoleType = new ProjectRoleType(displayName:ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        nonPiType = new ProjectRoleType(displayName:"Non PI")
        def roleTypeList = [piRoleType, nonPiType]
        mockDomain(ProjectRoleType, roleTypeList)
        roleTypeList.each{it.save()}
        
        mockDomain(ProjectRole)
        
        project = new Project(name:"project")
        mockDomain(Project, [project])
        project.save()
        
        def subject = [ getPrincipal: { user.username },
                        isAuthenticated: { true },
                        hasRole: { true } 
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
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
    
    void testSetPermissionClearsCache()
    {
        String permission = "project:" + project.id + ":read"

        // First time will be a miss
        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        realm.isPermitted(user.username, permission)
        assertTrue(realm.getCacheHit())
        
        // Now we make a call to permission util service, which should result
        // in the cache being dirtied.
        ProjectRole nonPiRead = 
            new ProjectRole(project:project,
                            person:user,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_ONLY)
                        
        service.setPermissions(nonPiRead)
        user.permissions = null // Do this so that SecDbRealm doesn't throw NPE

        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())
    }
    
    void testRemovePermissionClearsCache()
    {
        String permission = "project:" + project.id + ":read"

        // First time will be a miss
        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        realm.isPermitted(user.username, permission)
        assertTrue(realm.getCacheHit())
        
        // Now we make a call to permission util service, which should result
        // in the cache being dirtied.
        ProjectRole nonPiRead = 
            new ProjectRole(project:project,
                            person:user,
                            roleType:nonPiType,
                            access:ProjectAccess.READ_ONLY)
                        
        service.removePermissions(nonPiRead)
        
        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())
    }
    
    void testReceiverCreatedClearsCache()
    {
        Receiver rx = new Receiver(id:12)
        String permission = service.buildReceiverUpdatePermission(rx.id)
        
        // First time will be a miss
        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        realm.isPermitted(user.username, permission)
        assertTrue(realm.getCacheHit())
        
        // Now we make a call to permission util service, which should result
        // in the cache being dirtied.
        service.receiverCreated(rx)
        user.permissions = null // Do this so that SecDbRealm doesn't throw NPE

        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())
    }
    
    void testReceiverDeletedClearsCache()
    {
        Receiver rx = new Receiver(id:12)
        String permission = service.buildReceiverUpdatePermission(rx.id)
        
        // First time will be a miss
        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())

        // Second time will be a hit
        realm.isPermitted(user.username, permission)
        assertTrue(realm.getCacheHit())
        
        // Now we make a call to permission util service, which should result
        // in the cache being dirtied.
        service.receiverCreated(rx)
        user.permissions = null // Do this so that SecDbRealm doesn't throw NPE

        realm.isPermitted(user.username, permission)
        assertFalse(realm.getCacheHit())
    }
}
