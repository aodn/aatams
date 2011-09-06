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
        faxNumber(nullable:true)
        streetAddress()
        postalAddress(nullable:true)
        status()    // Default to PENDING
        organisationProjects()
        requestingUser(nullable:false)
    }
    
    static mapping =
    {
        sort "name"
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
