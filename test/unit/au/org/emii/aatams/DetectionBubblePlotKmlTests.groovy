package au.org.emii.aatams

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import de.micromata.opengis.kml.v_2_2_0.Kml
import grails.test.*
import org.joda.time.DateTime

import au.org.emii.aatams.test.AbstractKmlTest;

class DetectionBubblePlotKmlTests extends AbstractKmlTest 
{
    InstallationStation bl1, bl2
    
    protected void setUp() 
    {
        super.setUp()

        bl1 = new InstallationStation(name: "BL1", location: new GeometryFactory().createPoint(new Coordinate(-122f, 37f)))
        bl2 = new InstallationStation(name: "BL2", location: new GeometryFactory().createPoint(new Coordinate(12f, 34f)))
        
        mockDomain(InstallationStation, [bl1, bl2])
        [bl1, bl2].each { it.save() }
    }

    protected void tearDown()
    {
        super.tearDown()
    }
    
    void testNoDetections() 
    {
        Kml kml = new DetectionBubblePlotKml([], "http://localhost:8090/aatams")
        
        def expectedKml = wrapInKmlElement("<Document/>")
        assertKmlEquals(expectedKml, kml)
    }
    
    void testOneDetection()
    {
        Kml kml = 
            new DetectionBubblePlotKml(
                [
                    [transmitter_id: 'A69-1303-5566', 
                     timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
                     station_id: bl1.id]
                ],
                "http://localhost:8090/aatams")
        
        def expectedKml = wrapInKmlElement('''
    <Document>
        ''' + getStyleElements() + '''

        <Folder>
            <name>station markers</name>
            <Placemark>
                <styleUrl>#station</styleUrl>
                <name>BL1</name>''' + getDescriptionElement(1) + '''
                <Point>
                    <coordinates>-122.0,37.0</coordinates>
                </Point>
            </Placemark>
        </Folder>
        <Folder>
            <name>bubbles</name>
            <Placemark>
                <styleUrl>#bubble9</styleUrl>
                <Point>
                    <coordinates>-122.0,37.0</coordinates>
                </Point>
            </Placemark>
        </Folder>
    </Document>''')

        assertKmlEquals(expectedKml, kml)
    }
    
    void testTwoDetectionsSameStation()
    {
        Kml kml = 
            new DetectionBubblePlotKml(
                [
                    [transmitter_id: 'A69-1303-5566', 
                     timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
                     station_id: bl1.id],
                    [transmitter_id: 'A69-1303-5566', 
                     timestamp: new DateTime("2010-05-28T06:02:09+10:00").toDate(),
                     station_id: bl1.id]
                ],
                "http://localhost:8090/aatams")
        
        def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Folder>
            <name>station markers</name>
            <Placemark>
                <styleUrl>#station</styleUrl>
                <name>BL1</name>''' + getDescriptionElement(2) + '''
                 <Point>
                    <coordinates>-122.0,37.0</coordinates>
                </Point>
            </Placemark>
        </Folder>
        <Folder>
            <name>bubbles</name>
            <Placemark>
                <styleUrl>#bubble9</styleUrl>
                <Point>
                    <coordinates>-122.0,37.0</coordinates>
                </Point>
            </Placemark>
        </Folder>
    </Document>''')

        assertKmlEquals(expectedKml, kml)
    }

    void testThreeDetectionsDifferentStations()
    {
        Kml kml = 
            new DetectionBubblePlotKml(
                [
                    [transmitter_id: 'A69-1303-5566', 
                     timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
                     station_id: bl1.id],
                    [transmitter_id: 'A69-1303-5566', 
                     timestamp: new DateTime("2010-05-28T06:02:09+10:00").toDate(),
                     station_id: bl2.id],
                    [transmitter_id: 'A69-1303-5566', 
                     timestamp: new DateTime("2010-05-28T06:02:09+10:00").toDate(),
                     station_id: bl1.id]
                ],
                "http://localhost:8090/aatams")
        
        def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Folder>
            <name>station markers</name>
            <Placemark>
                <styleUrl>#station</styleUrl>
                <name>BL1</name>''' + getDescriptionElement(2) + '''
                <Point>
                    <coordinates>-122.0,37.0</coordinates>
                </Point>
            </Placemark>
            <Placemark>
                <styleUrl>#station</styleUrl>
                <name>BL2</name>''' + getDescriptionElement(1) + '''
                <Point>
                    <coordinates>12.0,34.0</coordinates>
                </Point>
            </Placemark>
        </Folder>
        <Folder>
            <name>bubbles</name>
            <Placemark>
                <styleUrl>#bubble9</styleUrl>
                <Point>
                    <coordinates>-122.0,37.0</coordinates>
                </Point>
            </Placemark>
            <Placemark>
                <styleUrl>#bubble0</styleUrl>
                <Point>
                    <coordinates>12.0,34.0</coordinates>
                </Point>
            </Placemark>
        </Folder>
    </Document>''')

        assertKmlEquals(expectedKml, kml)
    }
    
    void testCalcRelativeSize()
    {
        assertEquals(9, DetectionBubblePlotKml.calculateRelativeSize(1, [1]))
        assertEquals(9, DetectionBubblePlotKml.calculateRelativeSize(3, [3]))
        
        assertEquals(9, DetectionBubblePlotKml.calculateRelativeSize(2, [2, 1]))
        assertEquals(0, DetectionBubblePlotKml.calculateRelativeSize(1, [2, 1]))
        
        assertEquals(0, DetectionBubblePlotKml.calculateRelativeSize(1, [1, 10, 100]))
        assertEquals(5, DetectionBubblePlotKml.calculateRelativeSize(10, [1, 10, 100]))
        assertEquals(9, DetectionBubblePlotKml.calculateRelativeSize(100, [1, 10, 100]))
    }
    
    // testFor0detections
    
    private String getDescriptionElement(numDetections)
    {
        return '''<description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;

                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Number of detections&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;''' + numDetections + '''&lt;/td&gt;
                                    &lt;/tr&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.receiverDeployment.station.in=name&amp;filter.receiverDeployment.station.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;

                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;

                    &lt;/div&gt;
                &lt;/div&gt;</description>'''
    }
    
    private String getStyleElements()
    {
        StringBuilder buff = new StringBuilder()
    
        def scaleLookup = [0: 0.5, 1: 1.0, 2: 1.5, 3: 2.0, 4: 2.5, 5: 3.0, 6: 3.5, 7: 4.0, 8: 4.5, 9: 5.0]
        
        10.times
        {
            buff.append('''<Style id="bubble''' + it + '''">
            <IconStyle>
                <scale>''' + scaleLookup[it] + '''</scale>
                <heading>0.0</heading>
                <Icon>
                    <href>files/circle.png</href>
                    <refreshInterval>0.0</refreshInterval>
                    <viewRefreshTime>0.0</viewRefreshTime>
                    <viewBoundScale>0.0</viewBoundScale>
                </Icon>
            </IconStyle>
        </Style>''')
            buff.append('\n')
        }
        
        buff.append('''<Style id="station">
            <IconStyle>
                <scale>1.0</scale>
                <heading>0.0</heading>
                <Icon>
                    <href>files/station.png</href>
                    <refreshInterval>0.0</refreshInterval>
                    <viewRefreshTime>0.0</viewRefreshTime>
                    <viewBoundScale>0.0</viewBoundScale>
                </Icon>
            </IconStyle>
        </Style>''')
        
        return buff.toString()
    }
}
