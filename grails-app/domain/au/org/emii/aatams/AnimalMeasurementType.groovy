package au.org.emii.aatams

/**
 * e.g. LENGTH, WEIGHT.
 */
class AnimalMeasurementType  {
    String type

    static constraints =  {
        type(blank:false, unique:true)
    }

    String toString() {
        return type
    }
}
