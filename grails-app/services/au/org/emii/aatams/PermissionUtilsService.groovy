package au.org.emii.aatams

import org.apache.shiro.SecurityUtils

/**
 * Collection of security related utility methods.
 * 
 * @author jburgess
 */
class PermissionUtilsService
{
    // Cache the Principal Investigator role type to speed things up.
    def piRoleType
    
    private static final String NOT_PERMITTED = "notPermitted"
    private static final String PROJECT_READ_ANY = "projectReadAny"
    private static final String PROJECT_WRITE_ANY = "projectWriteAny"
    private static final String PERSON_WRITE_ANY = "personWriteAny"
    private static final String RECEIVER_CREATE = "receiverCreate"
    

    private static final Map<Long, String> PROJECT_READ = [:]
    private static final Map<Long, String> PROJECT_WRITE = [:]
    private static final Map<Long, String> PI = [:]
    private static final Map<Long, String> RECEIVER = [:]
    
    ProjectRoleType getPIRoleType()
    {
        if (piRoleType == null)
        {
            piRoleType = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        }
        
        assert(piRoleType): "piRoleType cannot be null"
        return piRoleType
    }
    
    Person setPermissions(ProjectRole projectRole)
    {
        log.debug("projectRole: " + String.valueOf(projectRole))
        
        // Cleanup existing permissions.
        Person user = removePermissions(projectRole)
//        Person user = Person.get(projectRole.person.id)

        if (!user)
        {
            log.error("Unknown user for role: " + projectRole)
            return null
        }

        CachedDbRealm.markCacheDirty()
        
        // Principal Investigators have special permissions.
        log.debug("Comparing roleTypes, person's roleType: " + projectRole.roleType + ", PI role type: " + getPIRoleType())
        
        if (   projectRole.roleType 
            == getPIRoleType())
        {
            log.debug("Adding PI permission to user: " + String.valueOf(projectRole.person) + ", project: " + String.valueOf(projectRole.project))
            
            user.addToPermissions(buildPersonWriteAnyPermission())
            user.addToPermissions(buildReceiverCreatePermission())
            String permission = buildPrincipalInvestigatorPermission(projectRole.project.id)
            user.addToPermissions(permission)
            log.debug("Added permission: " + permission)
            
            user.save()
        }
        
        // Read access for project (this just allows embargoes data to be 
        // views, compared to unauthenticated users).
        if (   projectRole.access == ProjectAccess.READ_ONLY
            || projectRole.access == ProjectAccess.READ_WRITE)
        {
            log.debug("Adding read permission to user: " + String.valueOf(projectRole.person) + ", project: " + String.valueOf(projectRole.project))
            String permission = buildProjectReadPermission(projectRole.project.id)
            user.addToPermissions(buildProjectReadAnyPermission())
            user.addToPermissions(permission).save()
            log.debug("Added permission: " + permission)
        }
        
        if (projectRole.access == ProjectAccess.READ_WRITE)
        {
            log.debug("Adding write permission to user: " + String.valueOf(projectRole.person) + ", project: " + String.valueOf(projectRole.project))
            String permission = buildProjectWritePermission(projectRole.project.id)
            user.addToPermissions(buildProjectWriteAnyPermission())
            user.addToPermissions(permission).save()
            log.debug("Added permission: " + permission)
        }
        
        return user
    }
    
    Person removePermissions(ProjectRole projectRole)
    {
        Person user = Person.get(projectRole.person.id)
        if (!user)
        {
            log.error("Unknown user for role: " + projectRole)
            return null
        }

        CachedDbRealm.markCacheDirty()
        
        if (   projectRole.roleType 
            == getPIRoleType())
        {
            user.removeFromPermissions(buildReceiverCreatePermission())
            user.removeFromPermissions(buildPersonWriteAnyPermission())
            user.removeFromPermissions(buildPrincipalInvestigatorPermission(projectRole.project.id))
        }
        
        // Read access for project (this just allows embargoes data to be 
        // views, compared to unauthenticated users).
        if (   projectRole.access == ProjectAccess.READ_ONLY
            || projectRole.access == ProjectAccess.READ_WRITE)
        {
            user.removeFromPermissions(buildProjectReadPermission(projectRole.project.id))
            user.removeFromPermissions(buildProjectReadAnyPermission())
        }
        
        if (projectRole.access == ProjectAccess.READ_WRITE)
        {
            user.removeFromPermissions(buildProjectWritePermission(projectRole.project.id))
            user.removeFromPermissions(buildProjectWriteAnyPermission())
        }
        
        user.save()
        
        return user
    }
    
    private String buildPermission(Long id, Map cache, String permFormat)
    {
        if (!id)
        {
            return NOT_PERMITTED
        }
        
        String perm = cache.get(id)
        if (!perm)
        {
            perm = String.format(permFormat, id)
            cache[id] = perm
        }
        
        assert(perm): "permission cannot be null"
        assert(perm != ""): "permission cannot be blank"
        
        return perm
    }
    
    String buildProjectReadPermission(projectId)
    {
        return buildPermission(projectId, PROJECT_READ, "project:%d:read")
    }
    
    /**
     * This applies where a user has read access to one or more projects. It is
     * different to "project:*:read" (which would imply read access to all 
     * projects).
     * 
     * This permission is checked in the view when determining whether to display
     * the "new" icon for various entities.
     */
    String buildProjectReadAnyPermission()
    {
        return PROJECT_READ_ANY
    }
    
    String buildProjectWritePermission(projectId)
    {
        return buildPermission(projectId, PROJECT_WRITE, "project:%d:write")
    }
    
    String buildProjectWriteAnyPermission()
    {
        return PROJECT_WRITE_ANY
    }
    
    String buildPrincipalInvestigatorPermission(projectId)
    {
        return buildPermission(projectId, PI, "principalInvestigator:%d")
    }

    String buildPersonWriteAnyPermission()
    {
        return PERSON_WRITE_ANY
    }
    
    String buildReceiverCreatePermission()
    {
        return RECEIVER_CREATE
    }
    
    String buildReceiverUpdatePermission(receiverId)
    {
        return buildPermission(receiverId, RECEIVER, "receiverUpdate:%d")
    }
    
    def principal()
    {
        if (!SecurityUtils.subject?.isAuthenticated())
        {
            return null
        }
        
        return Person.findByUsername(SecurityUtils.subject?.principal)
    }
    
    /**
     * This should be called by the ReceiverController whenever a receiver is
     * created.
     */
    def receiverCreated(receiverInstance)
    {
        CachedDbRealm.markCacheDirty()
        
        String permissionString = buildReceiverUpdatePermission(receiverInstance?.id)
        
//        log.debug("Adding permission \'" + permissionString + "\' to user:" + String.valueOf(principal()))
        principal().addToPermissions(permissionString)
        principal().save()
    }

    def receiverDeleted(receiverInstance)
    {
        CachedDbRealm.markCacheDirty()
        
        String permissionString = buildReceiverUpdatePermission(receiverInstance?.id)
     
        log.debug("Deleting permission \'" + permissionString + "\' from all users...")
        
        // TODO: this is potentially an expensive operation - ref second level cache:
        // http://stackoverflow.com/questions/2437446/grails-how-can-i-search-through-children-in-a-hasmany-relationship
        def people = Person.list().each{
            
            if (it.permissions.contains(permissionString))
            {
                it.removeFromPermissions(permissionString)
                it.save()
            }
        }
    }
}

