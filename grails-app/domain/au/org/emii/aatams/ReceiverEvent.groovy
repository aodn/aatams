package au.org.emii.aatams

/**
 * Receivers (or more correctly deployments) have a number of (usually daily) 
 * events associated with them.
 *
 * e.g. initialisation time (once off), daily battery level, daily pings etc.
 */
class ReceiverEvent 
{
    /**
     * UTC timestamp.
     */
    Date timestamp
    
    static belongsTo = [receiverDeployment: ReceiverDeployment, receiverDownload:ReceiverDownloadFile]
    
    String description
    
    String data
    
    String units
    
    static searchable =
    {
//        root:false
        receiverDeployment component:true
    }
    
    static constraints = 
    {
        timestamp()
        description()
        data(nullable:true, blank:true)
        units(nullable:true, blank:true)
    }
}
