package au.org.emii.aatams

import com.vividsolutions.jts.geom.Point

/**
 * Receiver recovery is the process of retrieving a receiver from the field and
 * either (a) downloading data from the receiver and immediately redeploying it
 * or (b) returning the receiver to the office for downloading and storage for
 * future redeployment.
 */
class ReceiverRecovery 
{
    Date recoveryDate
    Point location
    DeviceStatus status
    ReceiverDownload download
    
    /**
     * Every recovery should have a (chronologically) preceding deployment.
     */
    ReceiverDeployment deployment
    
    /**
     * Battery life/voltage at the time the receiver was recovered.
     */
    Float batteryLife
    
    Float batteryVoltage

    static constraints =
    {
        recoveryDate(max:new Date())
        location()
        status()
        download(nullable:true)
        deployment()
        batteryLife(min:0F, nullable:true)
        batteryVoltage(min:0F, nullable:true)
    }
    
    String toString()
    {
        return String.valueOf(deployment.receiver) + " recovered on "
            + String.valueOf(recoveryDate)
    }
}
