package au.org.emii.aatams

class AnimalMeasurement implements Embargoable {
    static belongsTo = [release: AnimalRelease]
    
    AnimalMeasurementType type
    Float value
    MeasurementUnit unit
    
    /**
     * True if the measurement is only an estimate.
     */
    Boolean estimate
    
    String comments
    
    static constraints = {
        type()
        value()
        unit()
        estimate()
        comments(nullable:true)
    }

    static transients = ['project', 'embargoed']
    
    static mapping = {
        comments type: 'text'
    }
    
    String toString() {
        return String.valueOf(type) + ":" + value + " (" + unit + ")"
    }
    
    boolean isEmbargoed() {
        return release.embargoed
    }

    def applyEmbargo() {
        return embargoed ? null : this
    }

    def getProject() {
        return release.project
    }
}
