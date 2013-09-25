package au.org.emii.aatams.detection

import java.util.Map;
import java.sql.PreparedStatement;

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import au.org.emii.aatams.*
import au.org.emii.aatams.util.StringUtils
import au.org.emii.aatams.util.SqlUtils

import com.vividsolutions.jts.geom.Point

import de.micromata.opengis.kml.v_2_2_0.Feature
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark
import de.micromata.opengis.kml.v_2_2_0.TimeStamp

class ValidDetection extends RawDetection implements Embargoable
{
	static belongsTo = [receiverDownload:ReceiverDownloadFile, receiverDeployment: ReceiverDeployment]
    static transients = RawDetection.transients + ['project', 'firstDetectionSurgery', 'sensorIds', 'speciesNames', 'placemark']

    /**
     * This is a part of an optimisation for #2239.  All new detections are marked provisional.  Subsequently,
     * the set of provisional detections are used to update the 'detection_extract_view_mv' materialized view,
     * the 'detection_count_per_station_mv' materialized view and also the Statistics table.  After this, they
     * are no longer considered provisional.
     */
    boolean provisional = true

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
    Set<DetectionSurgery> detectionSurgeries = new HashSet<DetectionSurgery>()
    static hasMany = [detectionSurgeries:DetectionSurgery]

	static constraints = RawDetection.constraints

	static mapping =
	{
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
		return firstDetectionSurgery?.surgery?.release?.project
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

	static PreparedStatement prepareInsertStatement(connection)
	{
		String sql = "INSERT INTO VALID_DETECTION (ID, VERSION, TIMESTAMP, RECEIVER_DOWNLOAD_ID, RECEIVER_NAME, SENSOR_UNIT, SENSOR_VALUE, STATION_NAME, TRANSMITTER_ID, TRANSMITTER_NAME, TRANSMITTER_SERIAL_NUMBER, RECEIVER_DEPLOYMENT_ID, PROVISIONAL) VALUES (nextval('hibernate_sequence'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
		return connection.prepareStatement(sql);
	}

	static void addToPreparedStatement(preparedStatement, detection)
	{
		preparedStatement.setInt     (1,  0)
		preparedStatement.setString  (2,  detection.timestamp.getTime())
		preparedStatement.setInt     (3,  detection.receiverDownloadId)
		preparedStatement.setString  (4,  detection.receiverName)
		preparedStatement.setString  (5,  detection.sensorUnit)
		preparedStatement.setString  (6,  detection.sensorValue)
		preparedStatement.setString  (7,  detection.stationName)
		preparedStatement.setString  (8,  detection.transmitterId)
		preparedStatement.setString  (9,  detection.transmitterName)
		preparedStatement.setString  (10, detection.transmitterSerialNumber)
		preparedStatement.setInt     (11, detection.receiverDeploymentId)
		preparedStatement.setBoolean (12, detection.provisional)
		preparedStatement.addBatch()
	}

	def applyEmbargo()
	{
		boolean isEmbargoed = false

		detectionSurgeries.each
		{
			if (it.surgery.release.isEmbargoed())
			{
				isEmbargoed = true
			}
		}

		return isEmbargoed ? null : this
	}

	static Kml toKml(List<ValidDetection> detections, serverURL)
	{
		return new SensorTrackKml(detections, serverURL)
	}
}
