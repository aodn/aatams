package au.org.emii.aatams

class InstallationConfiguration  {
    /**
     * Type, e.g. ARRAY, CURTAIN
     */
    String type
    
    static constraints = {
        type(blank:false, unique:true)
    }
    
    String toString() {
        return type
    }
}
