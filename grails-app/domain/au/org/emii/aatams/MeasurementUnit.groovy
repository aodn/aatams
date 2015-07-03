package au.org.emii.aatams

/**
 * e.g. gm, mm.
 */
class MeasurementUnit  {
    String unit

    static constraints = {
        unit(blank:false, unique:true)
    }

    String toString() {
        return unit
    }
}
