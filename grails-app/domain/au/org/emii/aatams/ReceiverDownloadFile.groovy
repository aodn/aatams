package au.org.emii.aatams

import au.org.emii.aatams.detection.InvalidDetection
import au.org.emii.aatams.detection.InvalidDetectionReason
import au.org.emii.aatams.detection.RawDetection
import au.org.emii.aatams.detection.ValidDetection

/**
 * Index (and meta-data) to a file which has been downloaded from a receiver as
 * part of the recovery process.
 */
class ReceiverDownloadFile 
{
    ReceiverDownloadFileType type
    
    /**
     * Path (including filename) to file.
     */
    String path
    
    String name
    Date importDate
    
    FileProcessingStatus status
    
    String errMsg
    
    Person requestingUser
    
    Set<RawDetection> detections = new HashSet<RawDetection>()
    Set<ReceiverEvent> events = new HashSet<ReceiverEvent>()
    
    static hasMany = [detections:RawDetection, events:ReceiverEvent]
    
    static constraints =
    {
        type()
        path()
    }
    
    static mapping =
    {
        errMsg type: 'text'
    }
    
    static searchable = 
    {
        detections component:true
        events component:true
    }
    
    String toString()
    {
        return String.valueOf(path)
    }
    
    Collection<ValidDetection> validDetections()
    {
        def validDetections = detections.grep
        {
            it.isValid()
        }
        
        return validDetections
    }
    
    Collection<InvalidDetection> invalidDetections()
    {
        def invalidDetections = detections.grep
        {
            !it.isValid()
        }
        
        return invalidDetections
    }

    Collection<InvalidDetection> invalidDetections(reason)
    {
        def invalidWithReason = invalidDetections().grep
        {
            it.reason == reason
        }
        
        return invalidWithReason
    }
}
