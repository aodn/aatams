package au.org.emii.aatams

import au.org.emii.aatams.detection.InvalidDetection
import au.org.emii.aatams.detection.InvalidDetectionReason
import au.org.emii.aatams.detection.RawDetection

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
    static hasMany = [detections:RawDetection]
    
    static constraints =
    {
        type()
        path()
    }
    
    static mapping =
    {
        errMsg type: 'text'
    }
    
    String toString()
    {
        return String.valueOf(path)
    }
    
    Number numDuplicates()
    {
        println("detections: " + detections)
        
        def invalidDetections = detections.grep
        {
            !it.isValid()
        }
        
        return invalidDetections*.reason.count(InvalidDetectionReason.DUPLICATE)
    }
}
