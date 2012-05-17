package au.org.emii.aatams.detection

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import au.org.emii.aatams.*
import au.org.emii.aatams.util.StringUtils

import com.vividsolutions.jts.geom.Point

import de.micromata.opengis.kml.v_2_2_0.Feature
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark
import de.micromata.opengis.kml.v_2_2_0.TimeStamp


class ValidDetection extends RawDetection implements Embargoable
{
	static belongsTo = [receiverDeployment: ReceiverDeployment]
    static transients = ['project', 'firstDetectionSurgery', 'sensorIds', 'speciesNames', 'placemark']
    
    /**
     * This is modelled as a many-to-many relationship, due to the fact that tags
     * transmit only code map and ping ID which is not guaranteed to be unique
     * between manufacturers, although in reality the relationship will *usually*
     * be one-to-one.
     * 
     * Additionally, the relationship is modelled via surgery, due to the fact
     * that a tag could potentially be reused on several animals.
     */
    // Note: initialise with empty set so that detectionSurgeries.isEmpty()
    // returns true (I thought that the initialisation should happen when save()
    // is called but apparently not.
//    List<DetectionSurgery> detectionSurgeries = new ArrayList<DetectionSurgery>()
    Set<DetectionSurgery> detectionSurgeries = new HashSet<DetectionSurgery>()
    static hasMany = [detectionSurgeries:DetectionSurgery]
    
	static mapping =
	{
		cache true
		detectionSurgeries cache:true
	}
	
    static boolean isDuplicate(other)
    {
        boolean duplicate = false
        ValidDetection.findAllByTimestamp(other.timestamp).each
        {
            if (it.duplicate(other))
            {
                duplicate = true
                return
            }
			return false
        }
        
        return duplicate
    }
    
	private boolean duplicateProperty(property, other)
	{
		// null property is considered equal to empty string.
		(this[property] == other[property]) ||
		((this[property] == null) && (other[property] == "")) ||
		((this[property] == "") && (other[property] == null))
	}
	
    private boolean duplicate(other)
    {
		 return (
			 this.timestamp == other.timestamp
		  && duplicateProperty("receiverName", other)
		  && duplicateProperty("transmitterId", other)
		  && duplicateProperty("transmitterName", other)
		  && duplicateProperty("transmitterSerialNumber", other)
		  && duplicateProperty("sensorUnit", other)
		  && this.location == other.location
		  && this.sensorValue == other.sensorValue)
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
                Species species = new Species(name:"")
                Animal animal = new Animal(species:species)
                AnimalRelease release = new AnimalRelease(animal:animal)
                Surgery surgery = new Surgery(release:release)

                def sensor = new Sensor(transmitterId:"")

                NULL_DETECTION_SURGERY = new DetectionSurgery(surgery:surgery, sensor:sensor)
            }

            return NULL_DETECTION_SURGERY
        }

        return new ArrayList(detectionSurgeries)[0]
    }
	
	String getSensorIds()
	{
		return getSensorIds(detectionSurgeries)
	}
	
	private String getSensorIds(theDetectionSurgeries)
	{
		return StringUtils.removeSurroundingBrackets(theDetectionSurgeries*.sensor.transmitterId)
	}
	
	String getSpeciesNames()
	{
		return getSpeciesNames(detectionSurgeries)
	}
   
	private String getSpeciesNames(theDetectionSurgeries)
	{
		return StringUtils.removeSurroundingBrackets(theDetectionSurgeries*.surgery.release.animal.species.name)
	}
   
    def applyEmbargo()
    {
        // Return a temporary detection, with embargoed surgeries removed.
		def detectionProperties = new HashMap(this.properties)
		detectionProperties["detectionSurgeries"] = new HashSet<DetectionSurgery>()

        detectionSurgeries.each
        {
            if (!it.surgery.release.isEmbargoed())
            {
				detectionProperties["detectionSurgeries"].add(it)
            }
        }
		
		detectionProperties["sensorIds"] = getSensorIds(detectionProperties["detectionSurgeries"])
		detectionProperties["speciesNames"] = getSpeciesNames(detectionProperties["detectionSurgeries"])
		
		return detectionProperties
    }
	
	static Kml toKml(List<ValidDetection> detections, serverURL)
	{
		return new SensorTrackKml(detections, serverURL)
	}
}
