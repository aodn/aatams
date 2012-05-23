package au.org.emii.aatams

import de.micromata.opengis.kml.v_2_2_0.Kml
import grails.test.*
import org.joda.time.DateTime

import au.org.emii.aatams.test.AbstractKmlTest;

class DetectionBubblePlotKmlTests extends AbstractKmlTest 
{
    protected void setUp() 
	{
        super.setUp()
    }

    void testNoDetections() 
	{
		Kml kml = new DetectionBubblePlotKml([], "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document/>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	void testOneDetection()
	{
		def deployment = [station: [name: "BL1", longitude: -122f, latitude: 37f]]
			
		Kml kml = new DetectionBubblePlotKml([[transmitterId: 'A69-1303-5566', 
									   timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
									   receiverDeployment: deployment]], "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Placemark>
            <name>BL1</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;

                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Number of detections&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;1&lt;/td&gt;
                                    &lt;/tr&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.receiverDeployment.station.in=name&amp;filter.receiverDeployment.station.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;

                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;

                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <Point>
                <coordinates>-122.0,37.0</coordinates>
            </Point>
        </Placemark>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTwoDetectionsSameStation()
	{
		def deployment = [station: [name: "BL1", longitude: -122f, latitude: 37f]]
			
		Kml kml = 
			new DetectionBubblePlotKml([[transmitterId: 'A69-1303-5566', 
									     timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
									     receiverDeployment: deployment],
									 	[transmitterId: 'A69-1303-5566', 
									     timestamp: new DateTime("2010-05-28T06:02:09+10:00").toDate(),
									     receiverDeployment: deployment]], 
									 
									 "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Placemark>
            <name>BL1</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;

                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Number of detections&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;2&lt;/td&gt;
                                    &lt;/tr&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.receiverDeployment.station.in=name&amp;filter.receiverDeployment.station.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;

                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;

                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <Point>
                <coordinates>-122.0,37.0</coordinates>
            </Point>
        </Placemark>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}

	void testThreeDetectionsDifferentStations()
	{
		def deployment1 = [station: [name: "BL1", longitude: -122f, latitude: 37f]]
		def deployment2 = [station: [name: "BL2", longitude: 12f, latitude: 34f]]
		
		Kml kml = 
			new DetectionBubblePlotKml([[transmitterId: 'A69-1303-5566', 
									     timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
									     receiverDeployment: deployment1],
									 	[transmitterId: 'A69-1303-5566', 
									     timestamp: new DateTime("2010-05-28T06:02:09+10:00").toDate(),
									     receiverDeployment: deployment2], 
									 	[transmitterId: 'A69-1303-5566',
										 timestamp: new DateTime("2010-05-28T06:02:09+10:00").toDate(),
										 receiverDeployment: deployment1]],

									 "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Placemark>
            <name>BL1</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;

                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Number of detections&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;2&lt;/td&gt;
                                    &lt;/tr&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.receiverDeployment.station.in=name&amp;filter.receiverDeployment.station.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;

                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;

                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <Point>
                <coordinates>-122.0,37.0</coordinates>
            </Point>
        </Placemark>
        <Placemark>
            <name>BL2</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;

                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Number of detections&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;1&lt;/td&gt;
                                    &lt;/tr&gt;

                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.receiverDeployment.station.in=name&amp;filter.receiverDeployment.station.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;

                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;

                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <Point>
                <coordinates>12.0,34.0</coordinates>
            </Point>
        </Placemark>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
}
