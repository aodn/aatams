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
    
	static mapping =
	{
		cache true
	}
	
    boolean isValid()
    {
        return false
    }
    
    String toString()
    {
        StringBuilder buf = new StringBuilder(String.valueOf(reason))
        if (message)
        {
            buf.append(": ")
            buf.append(message)
        }
        
        return buf.toString()
    }
}
