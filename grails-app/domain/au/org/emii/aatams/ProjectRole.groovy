package au.org.emii.aatams

/**
 * The role of a person within a project.
 */
class ProjectRole 
{
    static belongsTo = [project:Project, person:Person]
    
    ProjectRoleType roleType;
}
