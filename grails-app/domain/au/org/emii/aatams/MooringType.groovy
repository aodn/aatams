package au.org.emii.aatams

/**
 * e.g. CAR TYRE, CONCRETE BLOCK, DEEP WATER.
 */
class MooringType  {
    String type
    
    static constraints = {
        type(blank:false, unique:true)
    }
     
    String toString() {
        return type
    }
}
