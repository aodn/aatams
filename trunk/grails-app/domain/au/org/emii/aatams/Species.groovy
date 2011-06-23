package au.org.emii.aatams

/**
 * Represents a particular species of animal.
 */
class Species 
{
    String name
    
    static constraints = 
    {
        name(blank:false)
    }
    
    String toString()
    {
        return name
    }
}
