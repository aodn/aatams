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
        name(blank:false)
        embargoDate(nullable:true)
    }
    
    String toString()
    {
        return name
    }
}
