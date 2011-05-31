package au.org.emii.aatams

class Project 
{
    static hasMany = [organisationProjects:OrganisationProject, 
                      projectRoles:ProjectRole,
                      devices:Device]
    
    String name
    String description

    /**
     * Each project must have one and only one principal investigator.
     */
    ProjectRole principalInvestator
    
    static constraints = 
    {
        name(blank:false, unique:true)
        description(blank:true)
        principalInvestator(nullable:true)
    }
    
    String toString()
    {
        return name
    }
}
