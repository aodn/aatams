package au.org.emii.aatams

/**
 * Collection of security related utility methods.
 * 
 * @author jburgess
 */
class PermissionUtilsService
{
    Person setPermissions(ProjectRole projectRole)
    {
        // Cleanup existing permissions.
        Person user = removePermissions(projectRole)
//        Person user = Person.get(projectRole.person.id)
        if (!user)
        {
            return null
        }

        
        // Principal Investigators have special permissions.
        if (   projectRole.roleType 
            == ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR))
        {
            log.debug("Adding PI permission to user: " + String.valueOf(projectRole.person) + ", project: " + String.valueOf(projectRole.project))
            user.addToPermissions(buildPersonWriteAnyPermission())
            String permission = buildPrincipalInvestigatorPermission(projectRole.project.id)
            user.addToPermissions(permission)
            log.debug("Added permission: " + permission)
            
            user.save(flush:true)
        }
        
        // Read access for project (this just allows embargoes data to be 
        // views, compared to unauthenticated users).
        if (   projectRole.access == ProjectAccess.READ_ONLY
            || projectRole.access == ProjectAccess.READ_WRITE)
        {
            log.debug("Adding read permission to user: " + String.valueOf(projectRole.person) + ", project: " + String.valueOf(projectRole.project))
            String permission = buildProjectReadPermission(projectRole.project.id)
            user.addToPermissions(buildProjectReadAnyPermission())
            user.addToPermissions(permission).save(flush:true)
            log.debug("Added permission: " + permission)
        }
        
        if (projectRole.access == ProjectAccess.READ_WRITE)
        {
            log.debug("Adding write permission to user: " + String.valueOf(projectRole.person) + ", project: " + String.valueOf(projectRole.project))
            String permission = buildProjectWritePermission(projectRole.project.id)
            user.addToPermissions(buildProjectWriteAnyPermission())
            user.addToPermissions(permission).save(flush:true)
            log.debug("Added permission: " + permission)
        }
        
        return user
    }
    
    Person removePermissions(ProjectRole projectRole)
    {
        Person user = Person.get(projectRole.person.id)
        if (!user)
        {
            return null
        }

        if (   projectRole.roleType 
            == ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR))
        {
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
        
        user.save(flush:true)
        
        return user
    }
    
    String buildProjectReadPermission(projectId)
    {
        return "project:" + projectId + ":read"
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
        return "projectReadAny"
    }
    
    String buildProjectWritePermission(projectId)
    {
        return "project:" + projectId + ":write"
    }
    
    String buildProjectWriteAnyPermission()
    {
        return "projectWriteAny"
    }
    
    String buildPrincipalInvestigatorPermission(projectId)
    {
        return "principalInvestigator:" + projectId
    }

    String buildPersonWriteAnyPermission()
    {
        return "personWriteAny"
    }
    

}

