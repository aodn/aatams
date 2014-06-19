package au.org.emii.aatams

import au.org.emii.aatams.detection.ValidDetection;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Icon
import de.micromata.opengis.kml.v_2_2_0.IconStyle
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
        
        insertStyles(doc)
        
        def stationMarkersFolder = doc.createAndAddFolder().withName("station markers")
        
        detCountByStation.each
        {
            station, count ->
            
            Placemark placemark = new Placemark()
            placemark.setStyleUrl("#station")
            placemark.setName(station.name)
            placemark.setDescription(getDescription(count))
            placemark.createAndSetPoint().addToCoordinates(station.longitude, station.latitude)
            
            stationMarkersFolder.getFeature().add(placemark)
        }
        
        def bubblesFolder = doc.createAndAddFolder().withName("bubbles")
        
        detCountByStation.each
        {
            station, count ->
            
            Placemark placemark = new Placemark()
            placemark.setStyleUrl("#bubble" + DetectionBubblePlotKml.calculateRelativeSize(count, detCountByStation.values()))
            placemark.createAndSetPoint().addToCoordinates(station.longitude, station.latitude)
            
            bubblesFolder.getFeature().add(placemark)
        }
    }
    
    private void insertStyles(Document doc)
    {
        def scaleLookup = [0: 0.5, 1: 1.0, 2: 1.5, 3: 2.0, 4: 2.5, 5: 3.0, 6: 3.5, 7: 4.0, 8: 4.5, 9: 5.0]
        
        10.times
        {
            doc.createAndAddStyle()
                .withId("bubble" + it)
                .withIconStyle(new IconStyle().withScale(scaleLookup[it]).withIcon(new Icon().withHref("files/circle.png")))
        }
        
        doc.createAndAddStyle()
            .withId("station")
            .withIconStyle(new IconStyle().withScale(1.0).withIcon(new Icon().withHref("files/station.png")))
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
    
    public static Integer calculateRelativeSize(value, allValues)
    {
        assert(value > 0)
        assert(allValues.size() >= 1)
        assert(allValues.contains(value))
        
        def scaleFactor = 9
        
        if (allValues.size() == 1)
        {
            return Integer.valueOf(scaleFactor)
        }
        
        return Math.round(Math.log10(value) / (allValues.collect { Math.log10(it) }).max() * scaleFactor)
    }
}
