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
    
    /**
     * Convenience method to return a collection of organisations not related
     * to this project.
     */
    def unrelatedOrganisations()
    {
        // We also want to get a list of organisations which aren't 
        // associated with this project (so that when "adding organisation",
        // those already associated with project aren't shown).
        def organisations = Organisation.list(sort:"name")

        // Relates this project to any oranisations...
        def organisationProjects = OrganisationProject.findAllByProject(this)
        organisationProjects.each 
        {
            organisations.remove(it.organisation)
        }

        log.debug("unrelated organisations: " + organisations)
        
        return organisations
    }

}
