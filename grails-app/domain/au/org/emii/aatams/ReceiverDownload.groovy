package au.org.emii.aatams

/**
 * Data downloaded from receiver after recovery (hence associated with a 
 * ReceiverRecovery).
 */
class ReceiverDownload 
{
    static belongsTo = [receiverRecovery: ReceiverRecovery]
    static hasMany = [downloadFiles: ReceiverDownloadFile]
    
    Date downloadDate
    Float clockDrift
    
    /**
     * Total number of pings recorded (this is different to the number of 
     * records imported).
     */
    Integer pingCount
    
    /**
     * Total number of detections recorded (this is different to the number of
     * records imported).
     */
    Integer detectionCount
    
    String comments
    
    /**
     * Person who downloaded the data.
     */
    Person downloader
    
    Float batteryVoltage
    Integer batteryDays
    
    static constraints = 
    {
        receiverRecovery()
        downloadDate(max:new Date())
        clockDrift()
        pingCount(min:0)
        detectionCount(min:0)
        comments(blank:true)
        downloader()
        batteryVoltage(min:0F)
        batteryDays(min:0)
    }
}
