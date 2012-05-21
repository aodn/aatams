package au.org.emii.aatams

import au.org.emii.aatams.detection.*

import de.micromata.opengis.kml.v_2_2_0.Kml
import grails.test.*
import org.joda.time.DateTime

class SensorTrackKmlTests extends GrailsUnitTestCase 
{
    protected void setUp() 
	{
        super.setUp()
		
		mockConfig('''grails
					{
						serverURL = "http://localhost:8090/aatams"
					}''')
		SensorTrackKml.metaClass.grailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
    }

	void testTrackNoDetections()
	{
		Kml kml = new SensorTrackKml([], "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document/>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTrackOneDetection()
	{
		def deployment = [station: [longitude: -122f, latitude: 37f]]
			
		Kml kml = new SensorTrackKml([[transmitterId: 'A69-1303-5566', 
									   timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
									   receiverDeployment: deployment]], "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Style id="defaultDetectionStyle">
            <IconStyle>
                <scale>1.0</scale>
                <heading>0.0</heading>
                <Icon>
                    <href>files/fish.png</href>
                    <refreshInterval>0.0</refreshInterval>
                    <viewRefreshTime>0.0</viewRefreshTime>
                    <viewBoundScale>0.0</viewBoundScale>
                </Icon>
            </IconStyle>
            <LineStyle>
                <color>ffface87</color>
                <width>4.0</width>
            </LineStyle>
        </Style>
        <Placemark>
            <name>A69-1303-5566</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;
                    
                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;
                                
                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.in=transmitterId&amp;filter.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;
                                    
                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;
                       
                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTrackTwoDetections()
	{
		def deployment1 = [station: [longitude: -122f, latitude: 37f]]
		def deployment2 = [station: [longitude: -145f, latitude: -42f]]
		
		Kml kml = new SensorTrackKml([[transmitterId: 'A69-1303-5566', 
									   timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
									   receiverDeployment: deployment1],
									  [transmitterId: 'A69-1303-5566', 
									   timestamp: new DateTime("2010-05-28T02:05:13+10:00").toDate(),
									   receiverDeployment: deployment2]], "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Style id="defaultDetectionStyle">
            <IconStyle>
                <scale>1.0</scale>
                <heading>0.0</heading>
                <Icon>
                    <href>files/fish.png</href>
                    <refreshInterval>0.0</refreshInterval>
                    <viewRefreshTime>0.0</viewRefreshTime>
                    <viewBoundScale>0.0</viewBoundScale>
                </Icon>
            </IconStyle>
            <LineStyle>
                <color>ffface87</color>
                <width>4.0</width>
            </LineStyle>
        </Style>
        <Placemark>
            <name>A69-1303-5566</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;
                    
                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;
                                
                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.in=transmitterId&amp;filter.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;
                                    
                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;
                       
                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <when>2010-05-28T02:05:13.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
                <gx:coord>-145.0 -42.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTrackTwoDetectionsReverseOrder()
	{
		def deployment1 = [station: [longitude: -122f, latitude: 37f]]
		def deployment2 = [station: [longitude: -145f, latitude: -42f]]
		
		Kml kml = new SensorTrackKml([[transmitterId: 'A69-1303-5566',
									   timestamp: new DateTime("2010-05-28T02:05:13+10:00").toDate(),
									   receiverDeployment: deployment2],
									  [transmitterId: 'A69-1303-5566',
									   timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
									   receiverDeployment: deployment1]], "http://localhost:8090/aatams")
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Style id="defaultDetectionStyle">
            <IconStyle>
                <scale>1.0</scale>
                <heading>0.0</heading>
                <Icon>
                    <href>files/fish.png</href>
                    <refreshInterval>0.0</refreshInterval>
                    <viewRefreshTime>0.0</viewRefreshTime>
                    <viewBoundScale>0.0</viewBoundScale>
                </Icon>
            </IconStyle>
            <LineStyle>
                <color>ffface87</color>
                <width>4.0</width>
            </LineStyle>
        </Style>
        <Placemark>
            <name>A69-1303-5566</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;
                    
                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;
                                
                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.in=transmitterId&amp;filter.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;
                                    
                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;
                       
                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <when>2010-05-28T02:05:13.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
                <gx:coord>-145.0 -42.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}

	void testTrackThreeDetectionsTwoSensors()
	{
		def deployment1 = [station: [longitude: -122f, latitude: 37f]]
		def deployment2 = [station: [longitude: -145f, latitude: -42f]]
		
		Kml kml = new SensorTrackKml([[transmitterId: 'A69-1303-5566', 
									   timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
									   receiverDeployment: deployment1],
									  [transmitterId: 'A69-1303-7788',
									   timestamp: new DateTime("2010-05-28T02:08:13+10:00").toDate(),
									   receiverDeployment: deployment1],
								      [transmitterId: 'A69-1303-5566', 
									   timestamp: new DateTime("2010-05-28T02:05:13+10:00").toDate(),
									   receiverDeployment: deployment2]], "http://localhost:8090/aatams")

		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Style id="defaultDetectionStyle">
            <IconStyle>
                <scale>1.0</scale>
                <heading>0.0</heading>
                <Icon>
                    <href>files/fish.png</href>
                    <refreshInterval>0.0</refreshInterval>
                    <viewRefreshTime>0.0</viewRefreshTime>
                    <viewBoundScale>0.0</viewBoundScale>
                </Icon>
            </IconStyle>
            <LineStyle>
                <color>ffface87</color>
                <width>4.0</width>
            </LineStyle>
        </Style>
        <Placemark>
            <name>A69-1303-5566</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;
                    
                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;
                                
                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.in=transmitterId&amp;filter.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;
                                    
                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;
                       
                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <when>2010-05-28T02:05:13.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
                <gx:coord>-145.0 -42.0</gx:coord>
            </gx:Track>
        </Placemark>
        <Placemark>
            <name>A69-1303-7788</name>
            <description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;
                    
                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;
                                
                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.in=transmitterId&amp;filter.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;
                                    
                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;
                       
                    &lt;/div&gt;
                &lt;/div&gt;</description>
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:08:13.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	private void assertKmlEquals(String expectedKmlAsString, actualKml)
	{
		StringWriter writer = new StringWriter()
		actualKml.marshal(writer)
		String actualKmlAsString = writer.toString()
		
		if (expectedKmlAsString != actualKmlAsString)
		{
			println "expected:\n" + expectedKmlAsString + "end"
			println "\n\nactual:\n" + actualKmlAsString + "end"
		}
		
		assertEquals(expectedKmlAsString, actualKmlAsString)
	}
}
