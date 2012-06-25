package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.test.AbstractKmlTest;

import de.micromata.opengis.kml.v_2_2_0.Kml
import grails.test.*
import org.joda.time.DateTime

class SensorTrackKmlTests extends AbstractKmlTest
{
    protected void setUp() 
	{
        super.setUp()
		
		SensorTrackKml.metaClass.grailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
		
		mockDomain(Tag)
		mockDomain(Sensor)
    }

	protected void tearDown()
	{
		super.tearDown()
	}
	
	void testTrackNoDetections()
	{
		Kml kml = new SensorTrackKml([], "http://localhost:8090/aatams")
		
		def expectedKml = wrapInKmlElement("<Document/>")
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTrackOneDetection()
	{
		Kml kml = 
			new SensorTrackKml(
				[
					[transmitter_id: 'A69-1303-5566', 
					 timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
					 latitude: 37f,
					 longitude: -122f]
				],
				"http://localhost:8090/aatams")
		
		def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Placemark>
            <name>A69-1303-5566</name>
			''' + getDescriptionElement() + ''' 
			<styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>''')
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTrackOneDetectionWithTag()
	{
		Tag tag = new Tag(codeMap: new CodeMap(codeMap: "A69-1303"), model: new TagDeviceModel(), serialNumber: "1234", status: new DeviceStatus())
		tag.save(validate: false)
		assertNotNull(tag.id)
		
		Sensor sensor = new Sensor(tag: tag, pingCode: 5566, transmitterType: new TransmitterType())
		sensor.save(validate: false, failOnError: true)
		assertNotNull(sensor.id)
		
		Kml kml = 
			new SensorTrackKml(
				[
					[transmitter_id: 'A69-1303-5566', 
					 timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
					 latitude: 37f,
					 longitude: -122f]
				],
				"http://localhost:8090/aatams")
		
		def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Placemark>
            <name>A69-1303-5566</name>
			''' + getDescriptionElement(tag.id) + ''' 
			<styleUrl>#defaultDetectionStyle</styleUrl>
			<ExtendedData>
                <Data name="tagId">
                    <value>''' + tag.id + '''</value>
		        </Data>
		    </ExtendedData>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>''')
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTrackTwoDetections()
	{
		Kml kml =
			new SensorTrackKml(
				[
					[transmitter_id: 'A69-1303-5566',
					 timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
					 latitude: 37f,
					 longitude: -122f],
					[transmitter_id: 'A69-1303-5566',
					 timestamp: new DateTime("2010-05-28T02:05:13+10:00").toDate(),
					 latitude: -42f,
					 longitude: -145f]
				],
				"http://localhost:8090/aatams")

		def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Placemark>
            <name>A69-1303-5566</name>
			''' + getDescriptionElement() + ''' 
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <when>2010-05-28T02:05:13.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
                <gx:coord>-145.0 -42.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>''')
		
		assertKmlEquals(expectedKml, kml)
	}
	
	void testTrackTwoDetectionsReverseOrder()
	{
		Kml kml =
			new SensorTrackKml(
				[
					[transmitter_id: 'A69-1303-5566',
					 timestamp: new DateTime("2010-05-28T02:05:13+10:00").toDate(),
					 latitude: -42f,
					 longitude: -145f],
					[transmitter_id: 'A69-1303-5566',
					 timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
					 latitude: 37f,
					 longitude: -122f]
				],
				"http://localhost:8090/aatams")
		
		def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Placemark>
            <name>A69-1303-5566</name>
			''' + getDescriptionElement() + ''' 
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:02:09.000+10:00</when>
                <when>2010-05-28T02:05:13.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
                <gx:coord>-145.0 -42.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>''')

		assertKmlEquals(expectedKml, kml)
	}

	void testTrackThreeDetectionsTwoSensors()
	{
		Kml kml =
			new SensorTrackKml(
				[
					[transmitter_id: 'A69-1303-5566',
					 timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
					 latitude: 37f,
					 longitude: -122f],
					[transmitter_id: 'A69-1303-7788',
					 timestamp: new DateTime("2010-05-28T02:08:13+10:00").toDate(),
					 latitude: 37f,
					 longitude: -122f],
					[transmitter_id: 'A69-1303-5566',
					 timestamp: new DateTime("2010-05-28T02:05:13+10:00").toDate(),
					 latitude: -42f,
					 longitude: -145f]
				],
				"http://localhost:8090/aatams")

		def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Placemark>
            <name>A69-1303-5566</name>
			''' + getDescriptionElement() + ''' 
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
			''' + getDescriptionElement() + ''' 
            <styleUrl>#defaultDetectionStyle</styleUrl>
            <gx:Track>
                <gx:altitudeMode>clampToGround</gx:altitudeMode>
                <when>2010-05-28T02:08:13.000+10:00</when>
                <gx:coord>-122.0 37.0</gx:coord>
            </gx:Track>
        </Placemark>
    </Document>''')

		assertKmlEquals(expectedKml, kml)
	}
	
	private String getDescriptionElement()
	{
		return getDescriptionElement(null, null)
	}
	
	private String getDescriptionElement(tagId)
	{
		return getDescriptionElement(tagId, null)
	}
	
	private String getDescriptionElement(def tagId, def releaseId)
	{
		StringBuffer desc = new StringBuffer('''<description>&lt;div&gt;
                    &lt;link rel="stylesheet" type="text/css" href="files/main.css" /&gt;
                    &lt;div class="description"&gt;
                    
                        &lt;!--  "Header" data. --&gt;
                        &lt;div class="dialog"&gt;
                            &lt;table&gt;
                                &lt;tbody&gt;
''')
		
		if (tagId)
		{
			desc.append('''&lt;tr class="prop"&gt;
    &lt;td valign="top" class="name"&gt;Tag&lt;/td&gt;
    &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/tag/show/$[tagId]"&gt;$[name]&lt;/a&gt;&lt;/td&gt;
&lt;/tr&gt;''')
		}
		desc.append('''                                
                                    &lt;tr class="prop"&gt;
                                        &lt;td valign="top" class="name"&gt;Link to the Data&lt;/td&gt;
                                        &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/detection/list?filter.in=transmitterId&amp;filter.in=$[name]"&gt;Detections for $[name]&lt;/a&gt;&lt;/td&gt;
                                    &lt;/tr&gt;
                                    
                                &lt;/tbody&gt;
                            &lt;/table&gt;
                        &lt;/div&gt;
                       
                    &lt;/div&gt;
                &lt;/div&gt;</description>''')
		
		return desc
	}
	
	private String getStyleElements()
	{
		return '''        <Style id="defaultDetectionStyle">
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
'''
	}
}
