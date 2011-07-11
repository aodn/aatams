package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils

class Person extends SecUser
{
    static hasMany = [projectRoles:ProjectRole, 
                      organisationPeople:OrganisationPerson]
    
    static transients = ['organisations', 'projects']
    
    String name;
    String emailAddress;
    String phoneNumber;
    
    static constraints = 
    {
        name(blank:false)
        phoneNumber(blank:true)
        emailAddress(email:true)
    }
    
    String toString()
    {
        return name
    }
    
    String getOrganisations()
    {
        return ListUtils.fold(organisationPeople, "organisation")
    }
    
    String getProjects()
    {
        return ListUtils.fold(projectRoles, "project")
    }
}
