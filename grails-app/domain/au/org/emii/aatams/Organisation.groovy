package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils

class Organisation 
{
    static hasMany = [organisationProjects:OrganisationProject,
                      receivers:Receiver,
                      people:Person]
                  
    static transients = ['projects']
    
    String name
    String department
    String phoneNumber
    String faxNumber
    Address streetAddress
    Address postalAddress
    EntityStatus status = EntityStatus.PENDING
    
    // The person requesting creation of Organisation.
    Person requestingUser
    
    static constraints =
    {
        name(blank:false)
        department(blank:false)
        phoneNumber(blank:false)
        faxNumber()
        streetAddress()
        postalAddress(nullable:true)
        status()    // Default to PENDING
        organisationProjects()
        requestingUser(nullable:true)
    }
    
    String toString()
    {
        return name
    }
    
    String getProjects()
    {
        return ListUtils.fold(organisationProjects, "project")
    }
}
