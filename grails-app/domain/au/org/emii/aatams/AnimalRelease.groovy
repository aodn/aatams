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
class AnimalRelease implements Embargoable
{
    static belongsTo = [project: Project, animal: Animal]
    static hasMany = [surgeries: Surgery, measurements: AnimalMeasurement]
    static transients = ['scrambledReleaseLocation', 'current', 'embargoed']

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
        
        comments type: 'text'
    }

    String captureLocality
    Point captureLocation
    DateTime captureDateTime = new DateTime(Person.defaultTimeZone())
    
    CaptureMethod captureMethod
    
    String releaseLocality
    Point releaseLocation
    DateTime releaseDateTime = new DateTime(Person.defaultTimeZone())

    String comments
    
    /**
     * Date when data from this release is no longer embargoed (may be null to
     * indicate that no embargo exists).
     */
    Date embargoDate
    
    /**
     * Status is used to model the case where an animal (with associated tag
     * and surgery) is recaptured at which point the surgery is no longer 
     * current.
     */
    AnimalReleaseStatus status = AnimalReleaseStatus.CURRENT

    static constraints =
    {
        project()
        animal()
        captureLocality()
        captureLocation(nullable:true)
        captureDateTime()
        captureMethod()
        releaseLocality()
        releaseLocation(nullable:true)
        releaseDateTime()
        comments(nullable:true)
        embargoDate(nullable:true)
    }
    
    String toString()
    {
        StringBuilder buf = new StringBuilder()
        if (project)
        {
            buf.append(String.valueOf(project))
            buf.append(" - ")
        }
        
        if (animal?.species)
        {
            buf.append(String.valueOf(animal?.species))
            buf.append(" - ")
        }
        
        buf.append(String.valueOf(releaseDateTime))
        
        return buf.toString()
    }
    
    /**
     * Non-authenticated users can only see scrambled locations.
     */

    Point getScrambledReleaseLocation()
    {
        return GeometryUtils.scrambleLocation(releaseLocation)
    }
    
    boolean isCurrent()
    {
        return (status == AnimalReleaseStatus.CURRENT)
    }
    
    boolean isEmbargoed()
    {
        return (embargoDate != null) && (embargoDate.compareTo(new Date()) > 0)
    }
    
    Embargoable applyEmbargo()
    {
        if (isEmbargoed())
        {
            log.debug("AnimalRelease is embargoed, id: " + id)
            return null
        }
        
        return this
    }
}
