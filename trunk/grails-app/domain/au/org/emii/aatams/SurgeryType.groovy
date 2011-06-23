package au.org.emii.aatams

/**
 * e.g. INTERNAL, EXTERNAL.
 */
class SurgeryType 
{
    String type
    
    static constraints = 
    {
        type(blank:false, unique:true)
    }
    
    String toString()
    {
        return type
    }
}
