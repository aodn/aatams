package au.org.emii.aatams

import java.util.Map;

import au.org.emii.aatams.detection.ValidDetection
import au.org.emii.aatams.util.SqlUtils


/**
 * Models the relationship between a detection and a surgery.
 *
 * From a user's point of view, they are really only interested in the 
 * relationship between a particular animal and a detection.
 *
 * However, the following points need to be considered:
 *
 * - a tag may be re-used on a different animal, therefore the association is
 *   with a surgery (or 'tagging' as it is known to users) and detection
 * - the transmitter ID uploaded as part of the detection may match multiple
 *   tag (and therefore surgeries), although it is expected that this will be a
 *   rare occurence.
 *
 * Therefore, the relationship is modelled as many-to-many between Detection and
 * Surgery.
 * 
 * Note: additionally store the tag reference so we can differentiate between
 * pings sent out from tags with multiple sensors on the one physical tag (and
 * hence only one surgery).
 */
class DetectionSurgery implements Embargoable
{
    static belongsTo = [surgery:Surgery, detection:ValidDetection, sensor:Sensor]
    
	static mapping =
	{
		id generator:'sequence', params:[sequence:'detection_surgery_sequence']
	}
    
    static transients = ['project']

    static DetectionSurgery newSavedInstance(surgery, detection, sensor)
    {
        DetectionSurgery detectionSurgery =
            new DetectionSurgery(surgery:surgery, 
                                 sensor:sensor, 
                                 detection:detection)

        detection.addToDetectionSurgeries(detectionSurgery)
        surgery.addToDetectionSurgeries(detectionSurgery)
        sensor.addToDetectionSurgeries(detectionSurgery)

        detection.save()
        surgery.save()
        sensor.save()
        
        detectionSurgery.save()
        return detectionSurgery
    }
    
	static PreparedStatement prepareInsertStatement(connection)
	{
		String sql = "INSERT INTO DETECTION_SURGERY (ID, VERSION, DETECTION_ID, SURGERY_ID, SENSOR_ID) VALUE (nextval('hibernate_sequence'), ?, ?, ?, ?)"
		return connection.prepareStatement(sql);
	}

	static void addToPreparedStatement(
		preparedStatement,
		Map detSurgery,
		boolean useHibernateSeqForDetectionId)
	{
		preparedStatement.setInt     (1, 0)

		if (useHibernateSeqForDetectionId)
		{
			# TODO this will not parse well!!
			preparedStatement.setInt (2, "currval('hibernate_sequence'),")
		}
		else
		{
			assert(detSurgery.detectionId)
			preparedStatement.setInt (2, detSurgery.detectionId)
		}

		preparedStatement.setInt     (3, detSurgery.surgeryId)
		preparedStatement.setInt     (4, detSurgery.sensorId)

		preparedStatement.addBatch()
	}

    String toString()
    {
        return String.valueOf(sensor) + "-" + String.valueOf(surgery) + "-" + String.valueOf(detection)
    }

    def applyEmbargo()
	{
        if (surgery.isEmbargoed()) {
            return null
        }

        return this
	}
    
    def getProject()
    {
        return surgery.project
    }
}
