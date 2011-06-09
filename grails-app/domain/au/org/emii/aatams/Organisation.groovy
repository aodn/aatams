package au.org.emii.aatams

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
        def retString = ""
        organisationProjects.each
        {
            retString += String.valueOf(it.project) + ", "
        }

        // Remove the trailing ','
        retString = retString.substring(0, retString.length() - 2)
        return retString
    }
    
    String getPeople()
    {
        def retString = ""
        organisationPeople.each
        {
            retString += String.valueOf(it.person) + ", "
        }

        // Remove the trailing ", ".
        retString = retString.substring(0, retString.length() - 2)
        return retString
    }
}
