package au.org.emii.aatams

class Project 
{
    static hasMany = [organisationProjects:OrganisationProject, 
                      projectRoles:ProjectRole]
    
    String name
    String description
    
    static constraints = 
    {
        name(blank:false)
        description()
    }
    
    String toString()
    {
        return name
    }
}
