package au.org.emii.aatams

class Person 
{
    static hasMany = [projectRoles:ProjectRole, systemRoles:SystemRole]
    
    String firstName;
    String lastName;
    String emailAddress;    // TODO: validate
    
    static constraints = 
    {
        lastName(blank:false)
        firstName()
        emailAddress(blank:false)
    }
    
    String toString()
    {
        return lastName + ", " + firstName
    }
}
