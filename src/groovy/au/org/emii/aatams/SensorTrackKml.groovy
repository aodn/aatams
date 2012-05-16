package au.org.emii.aatams

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import au.org.emii.aatams.detection.ValidDetection
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark
import de.micromata.opengis.kml.v_2_2_0.gx.Track

/**
 * @author jburgess
 *
 */
class SensorTrackKml extends Kml
{
	private final TreeMap<String, TreeSet<ValidDetection>> detsBySensor
	
	public SensorTrackKml(detections)
	{
		this.detsBySensor = new TreeMap<String, TreeSet<ValidDetection>>()
		loadDetections(detections)
		refresh()
	}
	
	private void loadDetections(dets)
	{
		dets.each
		{
			det ->
			
			def detsForSingleSensor = detsBySensor[det.transmitterId]
			
			if (!detsForSingleSensor)
			{
				detsForSingleSensor = new TreeSet<ValidDetection>([compare: {a, b -> a.timestamp <=> b.timestamp} ] as Comparator)
				detsBySensor[det.transmitterId] = detsForSingleSensor
			}
			
			detsForSingleSensor.add(det)
		}
	}
	
	private void refresh()
	{
		Document doc = createAndSetDocument()
		
		if (detsBySensor.isEmpty())
		{
			return
		}
		
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime()
			
		detsBySensor.each 
		{
			transmitterId, detsForSingleSensor ->
			
			Placemark placemark = new Placemark()
			placemark.setName(transmitterId)
	
			Track track = new Track()
			track.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
	
			track.setWhen(detsForSingleSensor.collect {
				
				DateTime dt = new DateTime(it.timestamp)
				return fmt.print(dt)
			})

			track.setCoord(detsForSingleSensor.collect {
				
				def station = it.receiverDeployment.station
				return String.valueOf(station.longitude) + " " + String.valueOf(station.latitude)
			})

			placemark.setGeometry(track)
			doc.getFeature().add(placemark)
		}
	}
}
