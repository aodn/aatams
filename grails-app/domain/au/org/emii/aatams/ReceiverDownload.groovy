package au.org.emii.aatams

import org.joda.time.*
import org.joda.time.contrib.hibernate.*

/**
 * Data downloaded from receiver after recovery (hence associated with a 
 * ReceiverRecovery).
 */
class ReceiverDownload
{
    static belongsTo = [receiverRecovery: ReceiverRecovery]
    static hasMany = [downloadFiles: ReceiverDownloadFile]
    static mapping =
    {
        downloadDateTime type: PersistentDateTimeTZ,
        {
            column name: "downloadDateTime_timestamp"
            column name: "downloadDateTime_zone"
        }
        comments type: 'text'
    }
    
    DateTime downloadDateTime
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
        downloadDateTime(nullable:true)
        clockDrift(nullable:true)
        pingCount(nullable:true, min:0)
        detectionCount(nullable:true, min:0)
        comments(nullable:true, blank:true)
        downloader(nullable:true, )
        batteryVoltage(nullable:true, min:0F)
        batteryDays(nullable:true, min:0)
    }
}
