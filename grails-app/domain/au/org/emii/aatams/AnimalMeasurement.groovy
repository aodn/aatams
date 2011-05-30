package au.org.emii.aatams

class AnimalMeasurement 
{
    AnimalMeasurementType type
    Float value
    MeasurementUnit unit
    
    /**
     * True if the measurement is only an estimate.
     */
    Boolean estimate
    
    String comments
}
