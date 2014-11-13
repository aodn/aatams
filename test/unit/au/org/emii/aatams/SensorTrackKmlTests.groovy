package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.test.AbstractKmlTest;

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Point
import de.micromata.opengis.kml.v_2_2_0.Kml
import grails.test.*
import org.joda.time.DateTime

class SensorTrackKmlTests extends AbstractKmlTest
{
    protected void setUp()
    {
        super.setUp()

        SensorTrackKml.metaClass.grailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}

        mockDomain(Sensor)
        mockDomain(Tag)
        mockDomain(ValidDetection)
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
        <Folder>
            <name>Releases</name>
        </Folder>
        <Folder>
            <name>Detections</name>
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
        </Folder>
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
        <Folder>
            <name>Releases</name>
        </Folder>
        <Folder>
            <name>Detections</name>
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
        </Folder>
    </Document>''')
        assertKmlEquals(expectedKml, kml)
    }

    void testTrackOneDetectionWithTagAndRelease()
    {
        mockDomain(DeviceStatus)
        Tag tag = new Tag(codeMap: new CodeMap(codeMap: "A69-1303"), model: new TagDeviceModel(), serialNumber: "1234", status: new DeviceStatus())
        tag.save(validate: false)
        assertNotNull(tag.id)

        Sensor sensor = new Sensor(tag: tag, pingCode: 5566, transmitterType: new TransmitterType())
        sensor.save(validate: false, failOnError: true)
        assertNotNull(sensor.id)

        Species flathead = new Species(name: "flathead")
        mockDomain(Species, [flathead])
        flathead.save()

        Animal fredTheFlathead = new Animal(species: flathead, sex: new Sex())
        mockDomain(Animal, [fredTheFlathead])
        fredTheFlathead.save()

        Point releaseLocation = new GeometryFactory().createPoint(new Coordinate(12f, 34f))

        AnimalRelease release =
            new AnimalRelease(
                project: new Project(),
                animal: fredTheFlathead,
                releaseLocation: releaseLocation,
                releaseDateTime: new DateTime("2010-05-26T02:02:09+10:00"))
        mockDomain(AnimalRelease, [release])
        release.save()

        assertNotNull(release.id)

        ValidDetection detection =
            new ValidDetection(
                receiverDeployment: new ReceiverDeployment(),
                receiverDownload: new ReceiverDownloadFile(),
                receiverName: "VR2W-1234",
                timestamp: new Date(),
                transmitterId: 'A69-1303-5566')

        detection.save(validate: false, failOnError: true)
        assertNotNull(detection.id)

        Surgery surgery = new Surgery(tag: tag, release: release)
        mockDomain(Surgery, [surgery])
        surgery.save()

        Kml kml =
            new SensorTrackKml(
                [
                    [detection_id: detection.id,
                     transmitter_id: 'A69-1303-5566',
                     timestamp: new DateTime("2010-05-28T02:02:09+10:00").toDate(),
                     latitude: 37f,
                     longitude: -122f]
                ],
                "http://localhost:8090/aatams")

        def expectedKml = wrapInKmlElement('''<Document>
        ''' + getStyleElements() + '''
        <Folder>
            <name>Releases</name>
            <Placemark>
                <name>A69-1303-5566</name>
                ''' + getDescriptionElement(tag.id, release.id) + '''
                <styleUrl>#defaultReleaseStyle</styleUrl>
                <ExtendedData>
                    <Data name="releaseId">
                        <value>''' + release.id + '''</value>
                    </Data>
                    <Data name="releaseSpecies">
                        <value>flathead</value>
                    </Data>
                    <Data name="tagId">
                        <value>''' + tag.id + '''</value>
                    </Data>
                </ExtendedData>
                <MultiGeometry>

                    <Point>
                        <altitudeMode>clampToGround</altitudeMode>
                        <coordinates>
                            12.0,34.0
                        </coordinates>
                    </Point>

                    <LineString>
                        <altitudeMode>clampToGround</altitudeMode>
                        <coordinates>
                            12.0,34.0 -122.0,37.0
                        </coordinates>
                    </LineString>
                </MultiGeometry>
            </Placemark>
        </Folder>
        <Folder>
            <name>Detections</name>
            <Placemark>
                <name>A69-1303-5566</name>
                ''' + getDescriptionElement(tag.id, release.id) + '''
                <styleUrl>#defaultDetectionStyle</styleUrl>
                <ExtendedData>
                    <Data name="releaseId">
                        <value>''' + release.id + '''</value>
                    </Data>
                    <Data name="releaseSpecies">
                        <value>flathead</value>
                    </Data>
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
        </Folder>
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
        <Folder>
            <name>Releases</name>
        </Folder>
        <Folder>
            <name>Detections</name>
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
        </Folder>
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
        <Folder>
            <name>Releases</name>
        </Folder>
        <Folder>
            <name>Detections</name>
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
        </Folder>
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
        <Folder>
            <name>Releases</name>
        </Folder>
        <Folder>
            <name>Detections</name>
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
        </Folder>
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

        if (releaseId)
        {
            desc.append('''&lt;tr class="prop"&gt;
    &lt;td valign="top" class="name"&gt;Release&lt;/td&gt;
    &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/animalRelease/show/$[releaseId]"&gt;$[releaseSpecies]&lt;/a&gt;&lt;/td&gt;
&lt;/tr&gt;''')
        }
        else
        {
            desc.append('''&lt;tr class="prop"&gt;
    &lt;td valign="top" class="name"&gt;Release&lt;/td&gt;
    &lt;td valign="top" class="value"&gt;unknown release&lt;/td&gt;
&lt;/tr&gt;''')
        }

        if (tagId)
        {
            desc.append('''&lt;tr class="prop"&gt;
    &lt;td valign="top" class="name"&gt;Tag&lt;/td&gt;
    &lt;td valign="top" class="value"&gt;&lt;a href="http://localhost:8090/aatams/tag/show/$[tagId]"&gt;$[name]&lt;/a&gt;&lt;/td&gt;
&lt;/tr&gt;''')
        }
        else
        {
            desc.append('''&lt;tr class="prop"&gt;
    &lt;td valign="top" class="name"&gt;Tag&lt;/td&gt;
    &lt;td valign="top" class="value"&gt;unknown tag&lt;/td&gt;
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
                <width>2.0</width>
            </LineStyle>
        </Style>
        <Style id="defaultReleaseStyle">
            <IconStyle>
                <scale>1.0</scale>
                <heading>0.0</heading>
                <Icon>
                    <href>files/red_fish.png</href>
                    <refreshInterval>0.0</refreshInterval>
                    <viewRefreshTime>0.0</viewRefreshTime>
                    <viewBoundScale>0.0</viewBoundScale>
                </Icon>
            </IconStyle>
            <LineStyle>
                <color>aa0000ff</color>
                <width>2.0</width>
            </LineStyle>
        </Style>
'''
    }
}
