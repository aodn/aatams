package au.org.emii.aatams

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import grails.test.*

class ReceiverDownloadFileTests extends GrailsUnitTestCase
{
    Tag tag111, tag222, tag333
    Sensor sensor111, sensor222, sensor333

    protected void setUp()
    {
        super.setUp()

        CodeMap a69_1303 = new CodeMap(codeMap: "A69-1303")
        mockDomain(CodeMap, [a69_1303])
        a69_1303.save()

        tag111 = new Tag(codeMap: a69_1303,
                          model:new TagDeviceModel(),
                          serialNumber:"111",
                          status:new DeviceStatus())

        tag222 = new Tag(codeMap: a69_1303,
            model:new TagDeviceModel(),
            serialNumber:"222",
            status:new DeviceStatus())

        tag333 = new Tag(codeMap: a69_1303,
            model:new TagDeviceModel(),
            serialNumber:"333",
            status:new DeviceStatus())

        def tagList = [tag111, tag222, tag333]
        mockDomain(Tag, tagList)
        tagList.each { it.save() }

        sensor111 = new Sensor(tag: tag111, pingCode: 111)
        sensor222 = new Sensor(tag: tag222, pingCode: 222)
        sensor333 = new Sensor(tag: tag333, pingCode: 333)

        def sensorList = [sensor111, sensor222, sensor333]
        mockDomain(Sensor, sensorList)
        sensorList.each { it.save() }

        tag111.addToSensors(sensor111)
        tag222.addToSensors(sensor222)
        tag333.addToSensors(sensor333)
4
        mockDomain(ReceiverDownloadFile)
    }

    void testGetKnownSensorIDsOneValid()
    {
        assertGetKnownSensors([sensor111.transmitterId], [sensor111])
    }

    void testGetKnownSensorIDsTwoValidSameTag()
    {
        assertGetKnownSensors([sensor111.transmitterId, sensor111.transmitterId], [sensor111])
    }

    void testGetKnownSensorIDsTwoValidDifferentSensor()
    {
        assertGetKnownSensors([sensor111.transmitterId, sensor222.transmitterId], [sensor111, sensor222])
    }

    void testGetKnownSensorIDsTwoValidDifferentSensorOneUnknown()
    {
        assertGetKnownSensors([sensor111.transmitterId, sensor222.transmitterId, "A69-1303-444"], [sensor111, sensor222])
    }

    private void assertGetKnownSensors(transmitterIds, expectedSensors)
    {
        ReceiverDownloadFile.metaClass.getUniqueTransmitterIds =
        {
            transmitterIds.unique().sort()
        }

        ReceiverDownloadFile download = new ReceiverDownloadFile()
        assertEquals(expectedSensors, download.getKnownSensors())
    }
}
