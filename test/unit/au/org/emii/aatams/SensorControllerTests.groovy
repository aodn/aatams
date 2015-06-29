package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.converters.JSON

class SensorControllerTests extends AbstractControllerUnitTestCase
{
    CodeMap a69_1303
    Tag owningTag
    Project project

    TransmitterType pinger
    TransmitterType temp
    TransmitterType pressure

    TagDeviceModel model

    protected void setUp()
    {
        super.setUp()

        mockLogging(TagFactoryService)
        controller.tagFactoryService = new TagFactoryService()

        a69_1303 = new CodeMap(codeMap: "A69-1303")
        mockDomain(CodeMap, [a69_1303])
        a69_1303.save()


        model = new TagDeviceModel(modelName:"V16")
        mockDomain(TagDeviceModel, [model])

        owningTag = new Tag(codeMap:a69_1303, model:model, status:DeviceStatus.NEW)
        mockDomain(Tag, [owningTag])
        owningTag.save()

        mockDomain(Sensor)

        project = new Project(name: "Hammerheads")
        mockDomain(Project, [project])

        pinger = new TransmitterType(transmitterTypeName: 'PINGER')
        temp = new TransmitterType(transmitterTypeName: 'TEMP')
        pressure = new TransmitterType(transmitterTypeName: 'PRESSURE')
        def transmitterTypeList = [pinger, temp, pressure]
        mockDomain(TransmitterType, transmitterTypeList)
        transmitterTypeList.each { it.save() }

        controller.candidateEntitiesService = new CandidateEntitiesService()
    }

    void testSavePingerNoMatchingSerialNumber()
    {
        assertSaveTag(1111, pinger)
    }

    void testSaveSensorExistingSerialNumber()
    {
        createTag("12345")

        assertSaveTag(2222, pressure)
    }

    void testSaveSensorExistingSerialNumberSameSensorType()
    {
        // #1728
        // Property [transmitterType] of class [class au.org.emii.aatams.Sensor] with value [PINGER] must be unique
        def serialNumber = "12345"
        createTag(serialNumber)

        def saveParams = [tag: [serialNumber:serialNumber,
                    project:project,
                    model:model,
                    codeMap:a69_1303,
                    expectedLifeTimeDays:100,
                    status:DeviceStatus.NEW],
              transmitterType:pinger,
              pingCode:2222]

        controller.params.putAll(saveParams)
        controller.save()
        controller.save()

        def renderModel = controller.renderArgs.model

        assertNotNull(renderModel.sensorInstance)
        assertEquals("unique", renderModel.sensorInstance.errors.getFieldError('transmitterType').getCode())
    }

    private void createTag(serialNumber)
    {
        Tag tag = new Tag(serialNumber:serialNumber,
                project:project,
                model:model,
                codeMap:a69_1303,
                expectedLifeTimeDays:100,
                status:DeviceStatus.NEW)
        tag.save()
        assertFalse(tag.hasErrors())
    }

    private assertSaveTag(pingCode, transmitterType)
    {
        assertEquals(0, Sensor.count())

        def saveParams = [tag: [serialNumber:"12345",
                                project:project,
                                model:model,
                                codeMap:a69_1303,
                                expectedLifeTimeDays:100,
                                status:DeviceStatus.NEW],
                          transmitterType:transmitterType,
                          pingCode:pingCode]

        controller.params.putAll(saveParams)

        controller.save()

        assertEquals("tag", controller.redirectArgs.controller)
        assertEquals("show", controller.redirectArgs.action)

        def tagId = controller.redirectArgs.id
        def tag = Tag.get(tagId)
        assertNotNull(tag)

        assertEquals("12345", tag.serialNumber)

        assertEquals(1, Sensor.count())
        Sensor sensor = Sensor.findByPingCode(pingCode)
        assertNotNull(sensor)
        assertEquals(tag, sensor.tag)
    }

    void testUpdateWithNullPingCode()
    {
        // should remove PINGER sensor from tag.
    }

    void testLookupByTransmitterId()
    {
        Sensor sensor = new Sensor(tag: owningTag, pingCode: 123, transmitterType: pinger)
        sensor.save()

        owningTag.addToSensors(sensor)

        assertLookupWithTerm(0, "A7")
        assertLookupWithTerm(1, "A69")
        assertLookupWithTerm(1, "A69-1303-123")
        assertLookupWithTerm(1, "123")
        assertLookupWithTerm(1, "23")
        assertLookupWithTerm(0, "21")
    }

    private void assertLookupWithTerm(expectedNumResults, term)
    {
        controller.params.term = term
        controller.lookupByTransmitterId()

        def jsonResponse = JSON.parse(controller.response.contentAsString)
        assertEquals(expectedNumResults, jsonResponse.size())

        // Need to reset the response so that this method can be called multiple times within a single test case.
        // Also requires workaround to avoid exception, see: http://jira.grails.org/browse/GRAILS-6483
        mockResponse?.committed = false // Current workaround
        reset()
    }
}
