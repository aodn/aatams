package au.org.emii.aatams

class AnimalMeasurement 
{
    static belongsTo = [release: AnimalRelease]
    
    AnimalMeasurementType type
    Float value
    MeasurementUnit unit
    
    /**
     * True if the measurement is only an estimate.
     */
    Boolean estimate
    
    String comments
    
    static constraints =
    {
        type()
        value()
        unit()
        estimate()
        comments(nullable:true)
    }
}
