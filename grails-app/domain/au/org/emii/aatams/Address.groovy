package au.org.emii.aatams

/**
 * Models an Address (street or postal).
 */
class Address 
{
    String streetAddress // e.g. 12 Smith Street, PO Box 2345
    String suburbTown
    String state        // enum?
    String postcode     // handle alpha-numeric (e.g. USA zipcodes).
    String country      // needed?
    
    static belongsTo = [organisation: Organisation]
    static constraints = 
    {
        streetAddress()
        suburbTown()
        state()
        postcode()
        country()
    }
    
    String toString()
    {
        return streetAddress + ", " + suburbTown + ", " + state + ", " + country + ", " + postcode
    }
}
