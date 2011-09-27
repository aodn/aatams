package au.org.emii.aatams

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

/**
 * Surgery is the process of attaching/implanting a tag to/in an animal (given
 * by the owning AnimalRelease).
 */
class Surgery 
{
    static belongsTo = [release: AnimalRelease, tag: Tag]
    static hasMany = [detectionSurgeries: DetectionSurgery]
    
    static mapping =
    {
        timestamp type: PersistentDateTimeTZ,
        {
            column name: "timestamp_timestamp"
            column name: "timestamp_zone"
        }
        
        comments type: 'text'
    }
    
    DateTime timestamp = new DateTime(Person.defaultTimeZone())
    SurgeryType type
    SurgeryTreatmentType treatmentType
    String comments
    
    static constraints =
    {
        timestamp()
        release()
        tag()
        treatmentType()
        comments(nullable:true, blank:true)
    }
    
    String toString()
    {
        return "Tag (" + String.valueOf(tag) + "): " + String.valueOf(type)
    }
}
