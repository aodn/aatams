package au.org.emii.aatams.detection

import au.org.emii.aatams.*

class ValidDetection extends RawDetection implements Embargoable
{
    static belongsTo = [receiverDeployment: ReceiverDeployment]
    static transients = ['project', 'firstDetectionSurgery']
    
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
    
    static searchable =
    {
        root(false)
        receiverDeployment(component:true)
        detectionSurgeries(component:true)
    }
    
    ValidDetection()
    {
        
    }
    
    private ValidDetection(ValidDetection other)
    {
        timestamp = other.timestamp
        receiverDeployment = other.receiverDeployment
        detectionSurgeries = other.detectionSurgeries
        receiverName = other.receiverName
        stationName = other.stationName
        transmitterId = other.transmitterId
        transmitterName = other.transmitterName
        transmitterSerialNumber = other.transmitterSerialNumber
        location = other.location
    }
    
    static boolean isDuplicate(other)
    {
        boolean duplicate = false
        ValidDetection.findByTimestamp(other.timestamp, [cache:true]).each
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

    /**
     * Null object used where a detection has no associated surgeries.
     */
    static DetectionSurgery NULL_DETECTION_SURGERY = null

    DetectionSurgery getFirstDetectionSurgery()
    {
        if (detectionSurgeries.isEmpty())
        {
            if (!NULL_DETECTION_SURGERY)
            {
                // Employ the null object pattern so that we don't have to have
                // conditionals in the extract report.
                Species species = new Species(name:" ")
                Animal animal = new Animal(species:species)
                AnimalRelease release = new AnimalRelease(animal:animal)
                Surgery surgery = new Surgery(release:release)

                Tag tag = new Tag(codeName:" ")

                NULL_DETECTION_SURGERY = new DetectionSurgery(surgery:surgery, tag:tag)
            }

            return NULL_DETECTION_SURGERY
        }

        return detectionSurgeries[0]
    }
   
    Embargoable applyEmbargo()
    {
        // Return a temporary detection, with embargoed surgeries removed.
        ValidDetection retDetection = new ValidDetection(this)
        retDetection.detectionSurgeries = []

        detectionSurgeries.each
        {
            if (!it.surgery.release.isEmbargoed())
            {
                retDetection.addToDetectionSurgeries(it)
            }
        }

        return retDetection
    }
}
