package au.org.emii.aatams

/**
 * The type of project role, e.g. Principal Investigator, Co-Investigator,
 * Research Assistant, Technical Assistant, Administrator, Student.
 */
class ProjectRoleType 
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
