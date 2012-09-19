package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import au.org.emii.aatams.detection.ValidDetection
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Data
import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark
import de.micromata.opengis.kml.v_2_2_0.Point
import de.micromata.opengis.kml.v_2_2_0.gx.Track

/**
 * @author jburgess
 *
 */
class SensorTrackKml extends Kml
{
	private final TreeMap<String, TreeSet<ValidDetection>> detsBySensor
	private final String serverURL
	
	private static DateTimeFormatter fmt = ISODateTimeFormat.dateTime()
	

	public SensorTrackKml(detections, serverURL)
	{
		this.serverURL = serverURL
		this.detsBySensor = new TreeMap<String, TreeSet<ValidDetection>>()
		loadDetections(detections)
		refresh()
	}
	
	private void loadDetections(dets)
	{
		dets.each
		{
			det ->
			
			def detsForSingleSensor = detsBySensor[det.transmitter_id]
			
			if (!detsForSingleSensor)
			{
				detsForSingleSensor = new TreeSet<ValidDetection>([compare: {a, b -> a.timestamp <=> b.timestamp} ] as Comparator)
				detsBySensor[det.transmitter_id] = detsForSingleSensor
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
		
		insertDefaultDetectionStyle(doc)
		insertDefaultReleaseStyle(doc)
		
		Folder releasesFolder = doc.createAndAddFolder().withName("Releases")	
		Folder detectionsFolder = doc.createAndAddFolder().withName("Detections")	
		
		detsBySensor.each 
		{
			transmitterId, detsForSingleSensor ->
			
			assert(!detsForSingleSensor.isEmpty()): "No detections for sensor"
			
			AnimalRelease release = DetectionSurgery.findByDetection(ValidDetection.get(detsForSingleSensor.iterator().next().detection_id))?.surgery?.release
			Sensor sensor = Sensor.findByTransmitterId(transmitterId, [cache: true])
			
			createAndAddReleasePlacemark(transmitterId, release, sensor, detsForSingleSensor, releasesFolder)
			createAndAddDetectionPlacemark(transmitterId, release, sensor, detsForSingleSensor, detectionsFolder)
		}
	}

	private void createAndAddReleasePlacemark(String transmitterId, AnimalRelease release, Sensor sensor, def detsForSingleSensor, Folder releasesFolder) 
	{
		if (release && release.releaseLocation)
		{
			Placemark releasePlacemark = createBasePlacemark(transmitterId, release, sensor)
			releasePlacemark.setStyleUrl("#defaultReleaseStyle")
			addReleaseGeometry(releasePlacemark, release, detsForSingleSensor.iterator().next())
			releasesFolder.getFeature().add(releasePlacemark)
		}
	}

	private void createAndAddDetectionPlacemark(String transmitterId, AnimalRelease release, Sensor sensor, def detsForSingleSensor, Folder detectionsFolder) 
	{
		Placemark detectionPlacemark = createBasePlacemark(transmitterId, release, sensor)
		detectionPlacemark.setStyleUrl("#defaultDetectionStyle")
		addTrack(detectionPlacemark, detsForSingleSensor)
		detectionsFolder.getFeature().add(detectionPlacemark)
	}

	private void addReleaseGeometry(Placemark releasePlacemark, AnimalRelease release, def firstDetection)
	{
		Point releasePoint = new Point()
		releasePoint.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
		releasePoint.addToCoordinates(release.scrambledReleaseLocation.x, release.scrambledReleaseLocation.y)
		
		LineString releaseToFirstDetLine = new LineString()
		releaseToFirstDetLine.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
		releaseToFirstDetLine.addToCoordinates(release.scrambledReleaseLocation.x, release.scrambledReleaseLocation.y)
		releaseToFirstDetLine.addToCoordinates(GeometryUtils.scrambleCoordinate(firstDetection.longitude), GeometryUtils.scrambleCoordinate(firstDetection.latitude))
		
		MultiGeometry geometry = new MultiGeometry()
		geometry.addToGeometry(releasePoint)
		geometry.addToGeometry(releaseToFirstDetLine)

		releasePlacemark.setGeometry(geometry)	
	}
	
	private void addTrack(Placemark detectionPlacemark, def detsForSingleSensor) 
	{
		Track track = new Track()
		track.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)

		track.setWhen(detsForSingleSensor.collect {

			DateTime dt = new DateTime(it.timestamp)
			return fmt.print(dt)
		})

		track.setCoord(detsForSingleSensor.collect {

			return GeometryUtils.scrambleCoordinate(it.longitude) + " " + GeometryUtils.scrambleCoordinate(it.latitude)
		})

		detectionPlacemark.setGeometry(track)
	}

	private Placemark createBasePlacemark(def transmitterId, AnimalRelease release, Sensor sensor) 
	{
		Placemark placemark = new Placemark()
		placemark.setName(transmitterId)

		if (release)
		{
			addReleaseData(release, placemark)
		}

		if (sensor)
		{
			addSensorData(sensor, placemark)
		}

		placemark.setDescription(getDescription(sensor?.tag, release))
		return placemark
	}

	private Placemark createBasePlacemark
	private void addReleaseData(AnimalRelease release, Placemark placemark)
	{
		ExtendedData extData = placemark.getExtendedData()?: new ExtendedData()
		extData.addToData(new Data(String.valueOf(release.id)).withName("releaseId"))
		extData.addToData(new Data(String.valueOf(release?.animal?.species?.name)).withName("releaseSpecies"))
		placemark.setExtendedData(extData)
	}
	
	private void addSensorData(Sensor sensor, Placemark placemark) 
	{
		ExtendedData extData = placemark.getExtendedData()?: new ExtendedData()
		extData.addToData(new Data(String.valueOf(sensor.tag.id)).withName("tagId"))
		placemark.setExtendedData(extData)
	}
	
	private void insertDefaultDetectionStyle(Document doc)
	{
		doc.createAndAddStyle()
			.withId("defaultDetectionStyle")
			.withIconStyle(new IconStyle().withScale(1.0).withHeading(0.0).withIcon(new Icon().withHref("files/fish.png")))
			.withLineStyle(new LineStyle().withColor("ffface87").withWidth(2))
	}
	
	private void insertDefaultReleaseStyle(Document doc)
	{
		doc.createAndAddStyle()
			.withId("defaultReleaseStyle")
			.withIconStyle(new IconStyle().withScale(1.0).withHeading(0.0).withIcon(new Icon().withHref("files/red_fish.png")))
			.withLineStyle(new LineStyle().withColor("aa0000ff").withWidth(2))
	}
	
	// TODO: refactor to GSP template.
	private String getDescription(tag, release)
	{
		StringBuilder desc = new StringBuilder('''<div>
                    <link rel="stylesheet" type="text/css" href="files/main.css" />
                    <div class="description">
                    
                        <!--  "Header" data. -->
                        <div class="dialog">
                            <table>
                                <tbody>
''')

		if (release)
		{
			desc.append('''<tr class="prop">
    <td valign="top" class="name">Release</td>
    <td valign="top" class="value"><a href="''' + serverURL + '''/animalRelease/show/$[releaseId]">$[releaseSpecies]</a></td>
</tr>''')
		}	
		else
		{
			desc.append('''<tr class="prop">
    <td valign="top" class="name">Release</td>
    <td valign="top" class="value">unknown release</td>
</tr>''')
		}

		if (tag)
		{
			desc.append('''<tr class="prop">
    <td valign="top" class="name">Tag</td>
    <td valign="top" class="value"><a href="''' + serverURL + '''/tag/show/$[tagId]">$[name]</a></td>
</tr>''')
		}	
		else
		{
			desc.append('''<tr class="prop">
    <td valign="top" class="name">Tag</td>
    <td valign="top" class="value">unknown tag</td>
</tr>''')
		}

		desc.append('''                                
                                    <tr class="prop">
                                        <td valign="top" class="name">Link to the Data</td>
                                        <td valign="top" class="value"><a href="''' + serverURL + '''/detection/list?filter.in=transmitterId&filter.in=$[name]">Detections for $[name]</a></td>
                                    </tr>
                                    
                                </tbody>
                            </table>
                        </div>
                       
                    </div>
                </div>''')

		return desc.toString()
	}
}
