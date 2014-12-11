package au.org.emii.aatams

import grails.test.*
import grails.converters.JSON

class TagTests extends GrailsUnitTestCase
{
    CodeMap a69_1303
    CodeMap a69_9002

    Tag tag
    Sensor pingerSensor
    Sensor tempSensor
    Sensor pressureSensor

    protected void setUp()
    {
        super.setUp()

        a69_1303 = new CodeMap(codeMap: "A69-1303")
        a69_9002 = new CodeMap(codeMap: "A69-9002")
        def codeMapList = [a69_1303, a69_9002]
        mockDomain(CodeMap, codeMapList)
        codeMapList.each { it.save() }

        tag =
            new Tag(codeMap:a69_1303,
                    model:new TagDeviceModel(),
                    serialNumber:"1111",
                    status:new DeviceStatus())

        mockDomain(Tag, [tag])
        tag.save()

        TransmitterType pinger = new TransmitterType(transmitterTypeName: 'PINGER')
        TransmitterType temp = new TransmitterType(transmitterTypeName: 'TEMPERATURE')
        TransmitterType pressure = new TransmitterType(transmitterTypeName: 'PRESSURE')
        def transmitterTypeList = [pinger, temp, pressure]
        mockDomain(TransmitterType, transmitterTypeList)
        transmitterTypeList.each { it.save() }

        pingerSensor = new Sensor(tag: tag, transmitterType: pinger, pingCode: 1111)
        tempSensor = new Sensor(tag: tag, transmitterType: temp, pingCode: 2222)
        pressureSensor = new Sensor(tag: tag, transmitterType: pressure, pingCode: 3333)
        def sensorList = [pingerSensor, tempSensor, pressureSensor]
        mockDomain(Sensor, sensorList)

        sensorList.each
        {
            tag.addToSensors(it)
            it.save()
        }
        tag.save()
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    void testUniqueSerialNumbers()
    {
        mockDomain(Tag)

        TagDeviceModel model = new TagDeviceModel()

        Tag tag1 = new Tag(codeName:'A69-1303-1111',
                           codeMap:new CodeMap(codeMap:'A69-1303'),
                           pingCode:"1111",
                           model:model,
                           project:new Project(),
                           serialNumber:"1111",
                           status:new DeviceStatus(),
                           transmitterType:new TransmitterType())


        tag1.save(failOnError:true)

        try
        {
            Tag tag2 = new Tag(codeName:'A69-1303-2222',
                               codeMap:new CodeMap(codeMap:'A69-1303'),
                               pingCode:"2222",
                               model:model,
                               project:new Project(),
                               serialNumber:"1111",
                               status:new DeviceStatus(),
                               transmitterType:new TransmitterType())
            tag2.save(failOnError:true)

            fail()
        }
        catch (Throwable)
        {

        }
    }

    void testDeleteProject()
    {
        Project project = new Project(name:"some project")
        mockDomain(Project, [project])

        tag.project = project
        tag.save()

        project.addToTags(tag)
        project.save()

        assertTrue(project.tags.contains(tag))
        assertEquals(project, tag.project)


        project.delete(flush:true)

        project = Project.findByName("some project")
        assertNull(project)

        tag = Tag.get(1)
        assertNotNull(tag)
        assertNull(Project.get(tag.project.id))
    }

    void testNullProject()
    {
        tag.project = null
        tag.save()

        assertFalse(tag.hasErrors())
    }

    void testNonPingerSensors()
    {
        assertTrue(tag.getNonPingerSensors().contains(tempSensor))
        assertTrue(tag.getNonPingerSensors().contains(pressureSensor))
        assertFalse(tag.getNonPingerSensors().contains(pingerSensor))
    }

    void testGetPingCode()
    {
         assertEquals(pingerSensor.pingCode, tag.pingCode)
    }

    void testSetPingCodeExistingPinger()
    {
        assertEquals(3, tag.sensors.size())
        tag.setPingCode(1234)

        assertEquals(3, tag.sensors.size())
        assertEquals(1234, tag.pinger.pingCode)
    }

    void testSetPingCodeNoExistingPinger()
    {
        Tag newTag =
            new Tag(codeMap:a69_1303,
                    model:new TagDeviceModel(),
                    serialNumber:"1111",
                    status:new DeviceStatus())

        assertNull(newTag.sensors)
        newTag.setPingCode(1234)

        assertEquals(1, newTag.sensors.size())
        assertEquals(1234, newTag.pinger.pingCode)
    }

    void testSetCodeMapUpdatesSensorIds()
    {
        tag.sensors.each
        {
            assertTrue(it.transmitterId.startsWith("A69-1303"))
        }

        tag.setCodeMap(a69_9002)

        tag.sensors.each
        {
            assertTrue(it.transmitterId.startsWith("A69-9002"))
        }
    }

    // Test for #1863.
    void testTagAsJson()
    {
        Tag.registerObjectMarshaller()

        def tagAsJson = (tag as JSON)
        assertEquals("1111, 2222, 3333", JSON.parse(tagAsJson.toString()).pingCode)
    }
}
