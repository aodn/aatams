package au.org.emii.aatams

class Organisation 
{
    static hasMany = [organisationProjects:OrganisationProject]
    
    String name
    String phoneNumber
    String faxNumber
    String postalAddress
    
    static constraints =
    {
        name(blank:false)
        phoneNumber()
        faxNumber()
        postalAddress()
    }
    
    String toString()
    {
        return name
    }
}
