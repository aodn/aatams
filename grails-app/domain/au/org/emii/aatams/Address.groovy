package au.org.emii.aatams

/**
 * Models an Address (street or postal).
 */
class Address 
{
    String streetAddress // e.g. 12 Smith Street, PO Box 2345
    String suburbTown
    String state        // enum?
    Integer postcode
    String country      // needed?
    
    static belongsTo = { organisation: Organisation }
    static constraints = 
    {
        streetAddress()
        suburbTown()
        state()
        postcode()
        country(nullable:true)  // Assume Australia if null?
    }
    
    String toString()
    {
        return streetAddress + ", " + suburbTown + ", " + state + " " + postcode
    }
}
