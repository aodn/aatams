package au.org.emii.aatams

class SystemRoleType 
{
    String displayName
    
    static constraints = 
    {
        displayName(blank:false, unique:true)
    }
    
    String toString()
    {
        return displayName
    }
}
