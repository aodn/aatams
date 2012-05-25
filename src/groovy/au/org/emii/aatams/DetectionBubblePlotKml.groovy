package au.org.emii.aatams

import au.org.emii.aatams.detection.ValidDetection;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark;

class DetectionBubblePlotKml extends Kml 
{
	private final String serverURL
	
	private final TreeMap<InstallationStation, Long> detCountByStation
	
	public DetectionBubblePlotKml(detections, serverURL)
	{
		this.serverURL = serverURL
		
		this.detCountByStation = new TreeMap<InstallationStation, Long>([compare: {a, b -> a.name <=> b.name} ] as Comparator)
		
		loadDetections(detections)
		refresh()	
	}
	
	private void loadDetections(dets)
	{
		dets.each
		{
			det ->
			
			InstallationStation station = InstallationStation.get(det.station_id)
			def detCountForStation = detCountByStation[station]
			
			if (!detCountForStation)
			{
				detCountForStation = Long.valueOf(0)
				detCountByStation[station] = detCountForStation
			}
			
			detCountByStation[station] = detCountByStation[station] + 1
		}
	}
	
	private void refresh()
	{
		Document doc = createAndSetDocument()
		
		if (detCountByStation.isEmpty())
		{
			return
		}
		
		detCountByStation.each
		{
			station, count ->
			
			Placemark placemark = new Placemark()
			placemark.setName(station.name)
			placemark.setDescription(getDescription(count))
			placemark.createAndSetPoint().addToCoordinates(station.longitude, station.latitude)
			
			doc.getFeature().add(placemark)
		}
	}
	
	// TODO: refactor to GSP template.
	private String getDescription(count)
	{
		return '''<div>
                    <link rel="stylesheet" type="text/css" href="files/main.css" />
                    <div class="description">

                        <!--  "Header" data. -->
                        <div class="dialog">
                            <table>
                                <tbody>

                                    <tr class="prop">
                                        <td valign="top" class="name">Number of detections</td>
                                        <td valign="top" class="value">''' + count + '''</td>
                                    </tr>

                                    <tr class="prop">
                                        <td valign="top" class="name">Link to the Data</td>
                                        <td valign="top" class="value"><a href="''' + serverURL + '''/detection/list?filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in=$[name]">Detections for $[name]</a></td>
                                    </tr>

                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>'''
	}
}
