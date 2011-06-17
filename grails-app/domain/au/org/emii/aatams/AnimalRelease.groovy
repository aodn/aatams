package au.org.emii.aatams

import au.org.emii.aatams.util.ListUtils
import com.vividsolutions.jts.geom.Point

/**
 * Animal release is the process of capturing, tagging and releasing an animal,
 * generally within the proximity of previously deployed receivers in an 
 * installation, but may be release outside these areas to perform active
 * tracking of a continuous tag (following the animal around for a couple of 
 * hours or days) or where they may be expected to enter an area containing
 * receivers during future movements.
 */
class AnimalRelease 
{
    static belongsTo = [project: Project]
    static hasMany = [surgeries: Surgery, measurements: AnimalMeasurement]
    static transients = ['tags']
    /**
     * Animal that has been captured and released.
     */
    Animal animal
    
    String captureLocality
    Point captureLocation
    Date captureDateTime
    
    String releaseLocality
    Point releaseLocation
    Date releaseDateTime

    String comments
    
    static constraints =
    {
        project()
        animal()
        captureLocality()
        captureLocation()
        captureDateTime(max:new Date())
        releaseLocality()
        releaseLocation()
        releaseDateTime(max:new Date())
        comments(nullable:true)
    }
    
    String getTags()
    {
        return ListUtils.fold(surgeries.tag, "codeName")
    }
}
