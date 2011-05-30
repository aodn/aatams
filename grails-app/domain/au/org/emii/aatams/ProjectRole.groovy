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
    
    ProjectRoleType roleType;
}
