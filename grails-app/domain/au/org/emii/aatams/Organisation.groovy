package au.org.emii.aatams

class Organisation 
{
    static hasMany = [organisationProjects:OrganisationProject]
    
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
    }
    
    String toString()
    {
        return name
    }
}
