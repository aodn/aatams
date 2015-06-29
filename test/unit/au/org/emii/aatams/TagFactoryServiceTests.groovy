package au.org.emii.aatams

import grails.test.*

class TagFactoryServiceTests extends GrailsUnitTestCase
{
    def tagService
    def params
    CodeMap codeMap
    TransmitterType pinger
    Project sealProject
    Project tunaProject

    protected void setUp()
    {
        super.setUp()

        mockLogging(TagFactoryService)
        tagService = new TagFactoryService()

        mockDomain(Tag)

        codeMap = new CodeMap(codeMap:"A69-1303")
        mockDomain(CodeMap, [codeMap])
        codeMap.save()

        TagDeviceModel model = new TagDeviceModel()
        mockDomain(TagDeviceModel, [model])
        model.save()

        mockDomain(TransmitterType)

        pinger = new TransmitterType(transmitterTypeName: 'PINGER')
        mockDomain(TransmitterType, [pinger])
        pinger.save()

        sealProject = new Project(name: "Seal Project")
        tunaProject = new Project(name: "Tuna Project")
        def projectList = [sealProject, tunaProject]
        mockDomain(Project, projectList)
        projectList.each { it.save() }

        params =
            [codeMap:codeMap,
            pingCode:1234,
            serialNumber:"1111",
            model:model,
            status:DeviceStatus.NEW,
            transmitterType:new TransmitterType()]
    }

    void testValidParamsNew()
    {
        assertEquals(0, Tag.count())
        lookupOrCreate()
        assertEquals(1, Tag.count())
    }

    void testValidParamsNewPingCodeAsString()
    {
        params.pingCode = "5678"
        assertEquals(0, Tag.count())
        lookupOrCreate()
        assertEquals(1, Tag.count())
    }

    void testValidParamsExisting()
    {
        def existingTag = createExistingTag()

        assertEquals(1, Tag.count())
        def retTag = lookupOrCreate()
        assertEquals(1, Tag.count())
        assertEquals(1, Sensor.count())

        def existingSensor = Sensor.findByTag(existingTag)
        assertEquals(existingSensor, retTag.pinger)
    }

    void testTagProjectSetToReleasesProject()
    {
        def existingTag = createExistingTag()
        assertEquals(sealProject, existingTag.project)

        params += [project: tunaProject]
        def foundTag = lookupOrCreate()

        assertEquals(sealProject, foundTag.project)
    }

    private Tag createExistingTag()
    {
        Tag existingTag =
            new Tag(codeMap:codeMap,
                    serialNumber:"1111",
                    model:new DeviceModel(),
                    status:DeviceStatus.NEW,
                    project: sealProject)

        Sensor existingSensor = new Sensor(tag: existingTag,
        transmitterType:pinger,
        pingCode:1234)
        mockDomain(Sensor, [existingSensor])
        existingTag.addToSensors(existingSensor)

        mockDomain(Tag, [existingTag])
        codeMap.addToTags(existingTag)
        codeMap.save()

        existingTag.save()

        return existingTag
    }

    private Tag lookupOrCreate()
    {
        Tag tag = tagService.lookupOrCreate(params)

        assertFalse(tag.hasErrors())
        assertNotNull(Tag.get(tag.id))
        assertEquals(codeMap, tag.codeMap)

        return tag
    }
}
