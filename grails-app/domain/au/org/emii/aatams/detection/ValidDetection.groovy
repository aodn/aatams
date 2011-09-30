package au.org.emii.aatams.detection

import au.org.emii.aatams.*

class ValidDetection extends RawDetection
{
    static belongsTo = [receiverDeployment: ReceiverDeployment]
    static transients = ['project']
    
    /**
     * This is modelled as a many-to-many relationship, due to the fact that tags
     * transmit only code map and ping ID which is not guaranteed to be unique
     * between manufacturers, although in reality the relationship will *usually*
     * be one-to-one.
     * 
     * Additionally, the relationship is modelled via surgery, due to the fact
     * that a tag could potentially be reused on several animals.
     */
    // Note: initialise with empty list so that detectionSurgeries.isEmpty()
    // returns true (I thought that the initialisation should happen when save()
    // is called but apparently not.
    List<DetectionSurgery> detectionSurgeries = new ArrayList<DetectionSurgery>()
    static hasMany = [detectionSurgeries:DetectionSurgery]
    
    static constraints = 
    {
    }
    
    static boolean isDuplicate(other)
    {
        boolean duplicate = false
        ValidDetection.findByTimestamp(other.timestamp).each
        {
            if (it.duplicate(other))
            {
                duplicate = true
                return
            }
        }
        
        return duplicate
    }
    
    private boolean duplicate(other)
    {
        return (
            this.timestamp == other.timestamp
         && this.receiverName == other.receiverName
         && this.stationName == other.stationName
         && this.transmitterId == other.transmitterId
         && this.transmitterName == other.transmitterName
         && this.transmitterSerialNumber == other.transmitterSerialNumber
         && this.location == other.location
         && this.sensorValue == other.sensorValue
         && this.sensorUnit == other.sensorUnit)
    }
    
    String toString()
    {
        return timestamp.toString() + " " + String.valueOf(receiverDeployment?.receiver)
    }
    
    // Convenience method.
    Project getProject()
    {
        return receiverDeployment?.station?.installation?.project
    }
}
