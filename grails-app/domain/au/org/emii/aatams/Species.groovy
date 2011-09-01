package au.org.emii.aatams

/**
 * Represents a particular species of animal.
 */
class Species 
{
    String name

    /**
     * Data for sensitive species may be embargoed.
     */
    Date embargoDate

    
    static constraints = 
    {
        name(nullable:true)
        embargoDate(nullable:true)
    }
    
    String toString()
    {
        return String.valueOf(name)
    }
}
