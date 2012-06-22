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
		
		def expectedKml = wrapInKml("<Document/>")
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
		
		def expectedKml = wrapInKml('''
    <Document>
        <Placemark>
            <name>BL1</name>''' + getDescription(1) + '''
			<Point>
                <coordinates>-122.0,37.0</coordinates>
            </Point>
        </Placemark>
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
		
		def expectedKml = wrapInKml('''<Document>
        <Placemark>
            <name>BL1</name>''' + getDescription(2) + '''
             <Point>
                <coordinates>-122.0,37.0</coordinates>
            </Point>
        </Placemark>
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
		
		def expectedKml = wrapInKml('''<Document>
        <Placemark>
            <name>BL1</name>''' + getDescription(2) + '''
            <Point>
                <coordinates>-122.0,37.0</coordinates>
            </Point>
        </Placemark>
        <Placemark>
            <name>BL2</name>''' + getDescription(1) + '''
            <Point>
                <coordinates>12.0,34.0</coordinates>
            </Point>
        </Placemark>
    </Document>''')

		assertKmlEquals(expectedKml, kml)
	}
	
	private String getDescription(numDetections)
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
	
	private String wrapInKml(String doc)
	{
		return '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
''' + doc + '''
</kml>
'''
	}
}
