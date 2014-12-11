package au.org.emii.aatams

import grails.test.*

class SensorTests extends GrailsUnitTestCase
{
    Tag tag1303
    Tag tag9002

    Person adam
    Person bruce
    Person charlie

    def sensorList
    Sensor a69_1303_1111
    Sensor a69_1303_2222
    Sensor a69_1303_3333
    Sensor a69_9002_1111
    Sensor a69_9002_2222
    Sensor a69_9002_3333

    protected void setUp()
    {
        super.setUp()

        adam = new Person(name: "adam", emailAddress: "adam@aatams")
        bruce = new Person(name: "bruce", emailAddress: "bruce@aatams")
        charlie = new Person(name: "charlie", emailAddress: "charlie@aatams")
        def personList = [adam, bruce, charlie]
        mockDomain(Person, personList)
        personList.each { it.save() }

        Project sealProject = new Project(name: "Seals")
        Project tunaProject = new Project(name: "Tuna")
        def projectList = [sealProject, tunaProject]
        mockDomain(Project, projectList)
        projectList.each { it.save() }

        ProjectRoleType pi = new ProjectRoleType(displayName: ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        ProjectRoleType student = new ProjectRoleType(displayName: "student")
        def roleList = [pi, student]
        mockDomain(ProjectRoleType, roleList)
        roleList.each { it.save() }

        ProjectRole adamPiOnSeal = new ProjectRole(project: sealProject, person: adam, roleType: pi, access: ProjectAccess.READ_WRITE)
        ProjectRole brucePiOnSeal = new ProjectRole(project: sealProject, person: bruce, roleType: pi, access: ProjectAccess.READ_WRITE)
        ProjectRole charlieStudentOnSeal = new ProjectRole(project: sealProject, person: charlie, roleType: student, access: ProjectAccess.READ_WRITE)
        ProjectRole adamPiOnTuna = new ProjectRole(project: tunaProject, person: adam, roleType: pi, access: ProjectAccess.READ_WRITE)
        ProjectRole bruceStudentOnTuna = new ProjectRole(project: tunaProject, person: bruce, roleType: student, access: ProjectAccess.READ_WRITE)
        def projectRoleList = [adamPiOnSeal, brucePiOnSeal, charlieStudentOnSeal, adamPiOnTuna, bruceStudentOnTuna]
        mockDomain(ProjectRole, projectRoleList)
        projectRoleList.each { it.save() }

        adam.addToProjectRoles(adamPiOnSeal)
        adam.addToProjectRoles(adamPiOnTuna)
        bruce.addToProjectRoles(brucePiOnSeal)
        bruce.addToProjectRoles(bruceStudentOnTuna)
        charlie.addToProjectRoles(charlieStudentOnSeal)

        sealProject.addToProjectRoles(adamPiOnSeal)
        sealProject.addToProjectRoles(brucePiOnSeal)
        sealProject.addToProjectRoles(charlieStudentOnSeal)
        tunaProject.addToProjectRoles(adamPiOnTuna)
        tunaProject.addToProjectRoles(bruceStudentOnTuna)

        CodeMap a69_1303 = new CodeMap(codeMap: "A69-1303")
        CodeMap a69_9002 = new CodeMap(codeMap: "A69-9002")
        def codeMapList = [a69_1303, a69_9002]
        mockDomain(CodeMap, codeMapList)
        codeMapList.each { it.save() }

        tag1303 = new Tag(codeMap: a69_1303,
                          model:new TagDeviceModel(),
                          serialNumber:"1111",
                          status:new DeviceStatus(),
                          project: sealProject)

        tag9002 = new Tag(codeMap: a69_9002,
                          model:new TagDeviceModel(),
                          serialNumber:"2222",
                          status:new DeviceStatus(),
                          project: tunaProject)

        def tagList = [tag1303, tag9002]
        mockDomain(Tag, tagList)
        tagList.each { it.save() }

        a69_1303_1111 = new Sensor(tag: tag1303, pingCode: 1111)
        a69_1303_2222 = new Sensor(tag: tag1303, pingCode: 2222)
        a69_1303_3333 = new Sensor(tag: tag1303, pingCode: 3333)
        a69_9002_1111 = new Sensor(tag: tag9002, pingCode: 1111)
        a69_9002_2222 = new Sensor(tag: tag9002, pingCode: 2222)
        a69_9002_3333 = new Sensor(tag: tag9002, pingCode: 3333)

        sensorList = [a69_1303_1111, a69_1303_2222, a69_1303_3333, a69_9002_1111, a69_9002_2222, a69_9002_3333]
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    void testTransmitterIdInit()
    {
        Sensor sensor = new Sensor(tag: tag1303,
                                   pingCode: 1234)

        assertEquals("A69-1303-1234", sensor.transmitterId)
    }

    void testSetPingCode()
    {
        Sensor sensor = new Sensor(tag: tag1303,
                                   pingCode: 1234)

        sensor.pingCode = 5678

        assertEquals("A69-1303-5678", sensor.transmitterId)
    }

    void testSetTag()
    {
        Sensor sensor = new Sensor(tag: tag1303,
                                   pingCode: 1234)

        sensor.tag = tag9002

        assertEquals("A69-9002-1234", sensor.transmitterId)
    }

    void testGetOwningPIs()
    {
        assertContainsAll([adam, bruce], a69_1303_1111.getOwningPIs())
        assertContainsAll([adam, bruce], a69_1303_2222.getOwningPIs())
        assertContainsAll([adam, bruce], a69_1303_3333.getOwningPIs())
        assertContainsAll([adam], a69_9002_1111.getOwningPIs())
        assertContainsAll([adam], a69_9002_2222.getOwningPIs())
        assertContainsAll([adam], a69_9002_3333.getOwningPIs())
    }

    void testGroupByOwningPI()
    {
        def sensorsGroupedByPI = Sensor.groupByOwningPI(sensorList)

        assertNotNull(sensorsGroupedByPI[adam])
        assertNotNull(sensorsGroupedByPI[bruce])
        assertNull(sensorsGroupedByPI[charlie])
    }

    private assertContainsAll(listA, listB)
    {
        assertEquals(listA.size(), listB.size())
        assertTrue(listA.containsAll(listB))
        assertTrue(listB.containsAll(listA))
    }
}
