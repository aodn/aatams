package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils

class Project 
{
    static hasMany = [organisationProjects:OrganisationProject, 
                      projectRoles:ProjectRole,
                      devices:Device]
                  
    static transients = ['organisations', 'people']
    
    String name
    String description

    /**
     * Each project must have one and only one principal investigator.
     */
    ProjectRole principalInvestigator
    
    static constraints = 
    {
        name(blank:false, unique:true)
        description(blank:true)
        principalInvestigator(nullable:true)
    }
    
    String toString()
    {
        return name
    }
    
    String getOrganisations()
    {
        return ListUtils.fold(organisationProjects, "organisation")
    }
    
    String getPeople()
    {
        return ListUtils.fold(projectRoles, "person")
    }
}
