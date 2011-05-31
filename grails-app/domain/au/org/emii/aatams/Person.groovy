package au.org.emii.aatams

class Person 
{
    static hasMany = [projectRoles:ProjectRole, systemRoles:SystemRole]
    
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
}
