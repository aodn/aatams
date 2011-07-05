package au.org.emii.aatams

/**
 * Collection of security related utility methods.
 * 
 * @author jburgess
 */
class SecurityUtils 
{
    static Person setPermissions(ProjectRole projectRole)
    {
        // Cleanup existing permissions.
        Person user = removePermissions(projectRole)
        if (!user)
        {
            return null
        }

        
        // Principal Investigators have special permissions.
        if (   projectRole.roleType 
            == ProjectRoleType.findByDisplayName(ProjectRoleType.PRINICIPAL_INVESTIGATOR))
        {
            user.addToPermissions(buildPrincipalInvestigatorPermission(projectRole.project.id))
        }
        
        // Read access for project (this just allows embargoes data to be 
        // views, compared to unauthenticated users).
        if (   projectRole.access == ProjectAccess.READ_ONLY
            || projectRole.access == ProjectAccess.READ_WRITE)
        {
            user.addToPermissions(buildProjectReadPermission(projectRole.project.id))
        }
        
        if (projectRole.access == ProjectAccess.READ_WRITE)
        {
            user.addToPermissions(buildProjectWritePermission(projectRole.project.id))
        }
        
        return user
    }
    
    static Person removePermissions(ProjectRole projectRole)
    {
        Person user = Person.get(projectRole.person.id)
        if (!user)
        {
            return null
        }

        if (   projectRole.roleType 
            == ProjectRoleType.findByDisplayName(ProjectRoleType.PRINICIPAL_INVESTIGATOR))
        {
            user.removeFromPermissions(buildPrincipalInvestigatorPermission(projectRole.project.id))
        }
        
        // Read access for project (this just allows embargoes data to be 
        // views, compared to unauthenticated users).
        if (   projectRole.access == ProjectAccess.READ_ONLY
            || projectRole.access == ProjectAccess.READ_WRITE)
        {
            user.removeFromPermissions(buildProjectReadPermission(projectRole.project.id))
        }
        
        if (projectRole.access == ProjectAccess.READ_WRITE)
        {
            user.removeFromPermissions(buildProjectWritePermission(projectRole.project.id))
        }
        
        return user
    }
    
    static String buildProjectReadPermission(projectId)
    {
        return "project:" + projectId + ":read"
    }
    
    static String buildProjectWritePermission(projectId)
    {
        return "project:" + projectId + ":write"
    }
    
    static String buildPrincipalInvestigatorPermission(projectId)
    {
        return "principalInvestigator:" + projectId
    }
}

