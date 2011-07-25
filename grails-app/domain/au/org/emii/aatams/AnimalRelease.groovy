package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils
import au.org.emii.aatams.util.ListUtils

import com.vividsolutions.jts.geom.Point
import org.joda.time.*
import org.joda.time.contrib.hibernate.*

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
    static transients = ['scrambledReleaseLocation']
    static mapping =
    {
        captureDateTime type: PersistentDateTimeTZ,
        {
            column name: "captureDateTime_timestamp"
            column name: "captureDateTime_zone"
        }
        
        releaseDateTime type: PersistentDateTimeTZ,
        {
            column name: "releaseDateTime_timestamp"
            column name: "releaseDateTime_zone"
        }
    }

    /**
     * Animal that has been captured and released.
     */
    Animal animal
    
    String captureLocality
    Point captureLocation
    DateTime captureDateTime
    
    CaptureMethod captureMethod
    
    String releaseLocality
    Point releaseLocation
    DateTime releaseDateTime

    String comments
    
    /**
     * Date when data from this release is no longer embargoed (may be null to
     * indicate that no embargo exists).
     */
    Date embargoDate

    static constraints =
    {
        project()
        animal()
        captureLocality()
        captureLocation()
        captureDateTime()
        captureMethod()
        releaseLocality()
        releaseLocation()
        releaseDateTime()
        comments(nullable:true)
        embargoDate(nullable:true)
    }
    
    String toString()
    {
        return String.valueOf(project) + " - " + String.valueOf(animal?.species) + " - " + String.valueOf(releaseDateTime)
    }
    
    /**
     * Non-authenticated users can only see scrambled locations.
     */

    Point getScrambledReleaseLocation()
    {
        return GeometryUtils.scrambleLocation(releaseLocation)
    }
}
