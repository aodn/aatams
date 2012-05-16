package au.org.emii.aatams.detection

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat;

import au.org.emii.aatams.*
import grails.test.*
import com.vividsolutions.jts.geom.*

import de.micromata.opengis.kml.v_2_2_0.Document
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.TimeStamp;
import de.micromata.opengis.kml.v_2_2_0.gx.Track;

class ValidDetectionTests extends GrailsUnitTestCase 
{
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    
	Project fish, crab
	Installation derwent, bondi
	InstallationStation der1, der2, bondi1
	Receiver rx1, rx2
	ReceiverDeployment rx1AtDer1, rx1AtDer2, rx2AtDer1, rx2AtBondi1
	ValidDetection det1Der1, det2Der1, det3Der1
	ValidDetection det1Der2
	ValidDetection det1Bondi1, det2Bondi1
		
    protected void setUp() 
    {
        super.setUp()
        
		fish = new Project(name: "fish")
		crab = new Project(name: "crab")
		mockDomain(Project, [fish, crab])
		[fish, crab].each { it.save() }
		
		derwent = new Installation(name: "derwent", project: fish)
		bondi = new Installation(name: "bondi", project: fish)
		mockDomain(Installation, [derwent, bondi])
		[derwent, bondi].each { it.save() }
		
		der1 = new InstallationStation(name: "der1", installation: derwent)
		der2 = new InstallationStation(name: "der2", installation: derwent)
		bondi1 = new InstallationStation(name: "bondi1", installation: bondi)
		mockDomain(InstallationStation, [der1, der2, bondi1])
		[der1, der2, bondi1].each { it.save() }
		
		ReceiverDeviceModel model = new ReceiverDeviceModel(modelName: "VR2W")
		mockDomain(ReceiverDeviceModel, [model])
		model.save()
		
        rx1 = new Receiver(serialNumber:"1111",
                           model:model,
                           organisation:new Organisation())
        rx2 = new Receiver(serialNumber:"2222",
                           model:model,
                           organisation:new Organisation())
		mockDomain(Receiver, [rx1, rx2])
		[rx1, rx2].each { it.save() }
		
		rx1AtDer1 = new ReceiverDeployment(receiver: rx1, 
										   station: der1, 
										   location: new GeometryFactory().createPoint(new Coordinate(145f, -42f)),
										   deploymentDateTime: new DateTime("2012-05-03T01:02:03+10:00"))
		rx1AtDer2 = new ReceiverDeployment(receiver: rx1, 
										   station: der2, 
										   location: new GeometryFactory().createPoint(new Coordinate(145f, -42f)),
										   deploymentDateTime: new DateTime("2012-08-03T01:02:03+10:00"))
		rx2AtDer1 = new ReceiverDeployment(receiver: rx2, 
										   station: der1, 
										   location: new GeometryFactory().createPoint(new Coordinate(145f, -42f)),
										   deploymentDateTime: new DateTime("2012-07-03T01:02:03+10:00"))
		rx2AtBondi1 = new ReceiverDeployment(receiver: rx2, 
										   station: bondi1, 
										   location: new GeometryFactory().createPoint(new Coordinate(145f, -35f)),
										   deploymentDateTime: new DateTime("2012-06-03T01:02:03+10:00"))
		mockDomain(ReceiverDeployment, [rx1AtDer1, rx2AtDer1, rx2AtBondi1])
		[rx1AtDer1, rx2AtDer1, rx2AtBondi1].each { it.save() }
				
		det1Der1 = new ValidDetection(receiverDeployment: rx1AtDer1, 
								  timestamp: new DateTime("2012-05-09T12:34:56+10:00").toDate(),
								  receiverName: rx1.getName(),
								  transmitterId: "A69-1303-1234")
		det1Der2 = new ValidDetection(receiverDeployment: rx1AtDer2,
			timestamp: new DateTime("2012-05-09T14:34:56+10:00").toDate(),
			receiverName: rx1.getName(),
			transmitterId: "A69-1303-1234")
		det2Der1 = new ValidDetection(receiverDeployment: rx1AtDer1, 
								  timestamp: new DateTime("2012-05-09T14:34:56+10:00").toDate(),
								  receiverName: rx1.getName(),
								  transmitterId: "A69-1303-1234")
		det3Der1 = new ValidDetection(
			receiverDeployment: rx2AtDer1,
			timestamp: new DateTime("2012-07-09T14:34:56+10:00").toDate(),
			receiverName: rx2.getName(),
			transmitterId: "A69-1303-1234")

		det1Bondi1 = new ValidDetection(receiverDeployment: rx2AtBondi1, 
								  timestamp: new DateTime("2012-05-10T12:34:56+10:00").toDate(),
								  receiverName: rx2.getName(),
								  transmitterId: "A69-1303-1234")
		det2Bondi1 = new ValidDetection(receiverDeployment: rx2AtBondi1, 
								  timestamp: new DateTime("2012-05-10T13:34:56+10:00").toDate(),
								  receiverName: rx2.getName(),
								  transmitterId: "A69-1303-1234")
        mockDomain(ValidDetection)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testIsDuplicateTrue() 
    {
        Point location = new GeometryFactory().createPoint(new Coordinate(45.1234f, -40.1234f))
        Date timestamp = new Date().parse(DATE_FORMAT, "2009-12-08 06:44:24 UTC")
        def params = 
                [receiverDownload:new ReceiverDownloadFile(),
                 timestamp:timestamp,
                 receiverName:"VR3UWM-354",
                 transmitterId:"A69-1303-62347",
                 transmitterName:"shark tag",
                 transmitterSerialNumber:"1234",
                 sensorValue:123,
                 sensorUnit:"ADC",
                 stationName:"Neptune SW 1",
                 location:location,
                 receiverDeployment:new ReceiverDeployment()]
             
        ValidDetection validDet = new ValidDetection(params)
        validDet.save()
        assertFalse(validDet.hasErrors())
        assertTrue(validDet.validate())
        
        assertTrue(ValidDetection.isDuplicate(params))
        assertTrue(validDet.isDuplicate(params))
        
        params.receiverName = "XYZ"
        assertFalse(ValidDetection.isDuplicate(params))
    }
	
	void testToKmlNoDetections()
	{
		Kml expectedKml = new Kml()
		Document doc = expectedKml.createAndSetDocument()
		Kml kml = ValidDetection.toKmlByProject(Collections.emptyList())
		
		assertEquals(expectedKml, kml)
	}
	
	void testToKmlOneDetection()
	{
		Kml kml = ValidDetection.toKmlByProject([det1Der1])
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Folder>
            <name>fish</name>
            <Folder>
                <name>derwent</name>
                <Folder>
                    <name>der1</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-05-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 12:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}

	void testToKmlTwoDets()
	{
		Kml kml = ValidDetection.toKmlByProject([det1Der1, det2Der1])
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Folder>
            <name>fish</name>
            <Folder>
                <name>derwent</name>
                <Folder>
                    <name>der1</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-05-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 12:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                        <Placemark>
                            <name>Wed May 09 14:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T14:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
    </Document>
</kml>
'''
 		assertKmlEquals(expectedKml, kml)
	}

	void testToKmlTwoDetsDifferentDeployment()
	{
		Kml kml = ValidDetection.toKmlByProject([det1Der1, det3Der1])
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Folder>
            <name>fish</name>
            <Folder>
                <name>derwent</name>
                <Folder>
                    <name>der1</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-05-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 12:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                    <Folder>
                        <name>VR2W-2222 - 2012-07-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Mon Jul 09 14:34:56 EST 2012 VR2W-2222</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-07-09T14:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}

	void testToKmlTwoDetsDifferentStation()
	{
		Kml kml = ValidDetection.toKmlByProject([det1Der1, det1Der2])
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Folder>
            <name>fish</name>
            <Folder>
                <name>derwent</name>
                <Folder>
                    <name>der1</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-05-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 12:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
                <Folder>
                    <name>der2</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-08-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 14:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T14:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	void testToKmlTwoDetsDifferentInstallation()
	{
		Kml kml = ValidDetection.toKmlByProject([det1Der1, det1Bondi1])
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Folder>
            <name>fish</name>
            <Folder>
                <name>bondi</name>
                <Folder>
                    <name>bondi1</name>
                    <Folder>
                        <name>VR2W-2222 - 2012-06-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Thu May 10 12:34:56 EST 2012 VR2W-2222</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-10T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-35.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
            <Folder>
                <name>derwent</name>
                <Folder>
                    <name>der1</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-05-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 12:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}
	
	void testToKmlTwoDetsDifferentProject()
	{
		det1Der1.receiverDeployment.station.installation.project = crab
		Kml kml = ValidDetection.toKmlByProject([det1Der1, det1Bondi1])
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Folder>
            <name>crab</name>
            <Folder>
                <name>derwent</name>
                <Folder>
                    <name>der1</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-05-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 12:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
        <Folder>
            <name>fish</name>
            <Folder>
                <name>bondi</name>
                <Folder>
                    <name>bondi1</name>
                    <Folder>
                        <name>VR2W-2222 - 2012-06-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Thu May 10 12:34:56 EST 2012 VR2W-2222</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-10T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-35.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
    </Document>
</kml>
'''
		assertKmlEquals(expectedKml, kml)
	}

	void testToKmlOneDetectionEmbargoApplied()
	{
		Kml kml = ValidDetection.toKmlByProject([det1Der1.applyEmbargo()])
		
		def expectedKml = '''<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kml xmlns="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0">
    <Document>
        <Folder>
            <name>fish</name>
            <Folder>
                <name>derwent</name>
                <Folder>
                    <name>der1</name>
                    <Folder>
                        <name>VR2W-1111 - 2012-05-03T01:02:03.000+10:00</name>
                        <Placemark>
                            <name>Wed May 09 12:34:56 EST 2012 VR2W-1111</name>
                            <open>1</open>
                            <TimeStamp>
                                <when>2012-05-09T12:34:56.000+10:00</when>
                            </TimeStamp>
                            <Point>
                                <coordinates>145.0,-42.0</coordinates>
                            </Point>
                        </Placemark>
                    </Folder>
                </Folder>
            </Folder>
        </Folder>
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
