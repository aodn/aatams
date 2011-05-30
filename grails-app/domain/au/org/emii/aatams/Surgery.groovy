package au.org.emii.aatams

/**
 * Surgery is the process of attaching/implanting a tag to/in an animal (given
 * by the owning AnimalRelease).
 */
class Surgery 
{
    static belongsTo = [release: AnimalRelease]
    
    Tag tag
    Date timestamp
    SurgeryType type
    Boolean sutures
    SurgeryTreatmentType treatmentType
    Person surgeon
    String comments
    
    String toString()
    {
        String.valueOf(type) " performed by " + String.valueOf(surgeon)
    }
}
