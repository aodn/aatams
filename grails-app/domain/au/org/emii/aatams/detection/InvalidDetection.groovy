package au.org.emii.aatams.detection

class InvalidDetection extends RawDetection
{
    InvalidDetectionReason reason
    String message
    
    static constraints = 
    {
        reason(nullable:false)
        message(nullable:true, blank:true)
    }
    
    boolean isValid()
    {
        return false
    }
}
