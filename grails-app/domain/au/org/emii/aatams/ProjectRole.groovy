package au.org.emii.aatams

/**
 * The role of a person within a project (c.f. "Role" in user spec).
 * 
 * Each project must have one and only one Project Investigator (who is 
 * implicitly the main contact person for the project).
 */
class ProjectRole 
{
    static belongsTo = [project:Project, person:Person]
    static transients = ['projectAndRole']
    
    ProjectRoleType roleType
    
    ProjectAccess access
    
    String toString()
    {
        return getProjectAndRole() + ": " + String.valueOf(person)
    }
    
    String getProjectAndRole()
    {
        return String.valueOf(project) + " - " + String.valueOf(roleType)
    }
    
    def beforeInsert =
    {
        // Add permissions based on role type and access.
        PermissionUtils.setPermissions(this)
    }
    
    def beforeDelete =
    {
        PermissionUtils.removePermissions(this)
    }
}
