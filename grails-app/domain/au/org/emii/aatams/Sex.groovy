package au.org.emii.aatams

/**
 * e.g. MALE, FEMALE, UNKNOWN.
 */
class Sex  {
    String sex
    
    static constraints = {
       sex(blank:false, unique:true)
    }
    
    String toString() {
        return sex
    }
}
