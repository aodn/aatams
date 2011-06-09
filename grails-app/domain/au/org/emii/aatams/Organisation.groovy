package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils

class Organisation 
{
    static hasMany = [organisationProjects:OrganisationProject,
                      organisationPeople:OrganisationPerson]
    
    static transients = ['projects', 'people']
    
    String name
    String phoneNumber
    String faxNumber
    String postalAddress
    EntityStatus status = EntityStatus.PENDING
    
    static constraints =
    {
        name(blank:false, unique:true)
        phoneNumber(blank:false)
        faxNumber()
        postalAddress(blank:false)
        status()    // Default to PENDING
        organisationProjects()
        organisationPeople()
    }
    
    String toString()
    {
        return name
    }
    
    String getProjects()
    {
        return ListUtils.fold(organisationProjects, "project")
    }
    
    String getPeople()
    {
        return ListUtils.fold(organisationPeople, "person")
    }
}
