package au.org.emii.aatams

class Organisation 
{
    static hasMany = [organisationProjects:OrganisationProject]
    
    String name
    String phoneNumber
    String faxNumber
    String postalAddress
    EntityStatus status
    
    static constraints =
    {
        name(blank:false)
        phoneNumber()
        faxNumber()
        postalAddress()
        status()    // Default to PENDING
    }
    
    String toString()
    {
        return name
    }
}
