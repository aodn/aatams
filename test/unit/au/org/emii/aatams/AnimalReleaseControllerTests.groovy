package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader;

import org.joda.time.*

import java.text.DateFormat
import java.text.SimpleDateFormat

class AnimalReleaseControllerTests extends AbstractControllerUnitTestCase {
    WKTReader reader = new WKTReader()
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")

    Project project1
    Project project2

    def animalReleaseService
    def candidateEntitiesService

    def animalFactoryService
    def tagFactoryService

    TransmitterType pinger

    CodeMap codeMap

    def species
    def person

    protected void setUp() {
        super.setUp()

        mockLogging(AnimalReleaseController)

        // See http://jira.grails.org/browse/GRAILS-5926
        controller.metaClass.message = { Map map -> return "error message" }

        mockLogging(PermissionUtilsService)
        def permService = new PermissionUtilsService()

        mockLogging(CandidateEntitiesService)
        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.permissionUtilsService = permService
        controller.candidateEntitiesService = candidateEntitiesService

        mockLogging(AnimalFactoryService, true)
        animalFactoryService = new AnimalFactoryService()

        mockLogging(TagFactoryService, true)
        tagFactoryService = new TagFactoryService()

        mockLogging(AnimalReleaseService, true)
        animalReleaseService = new AnimalReleaseService()
        animalReleaseService.animalFactoryService = animalFactoryService
        animalReleaseService.tagFactoryService = tagFactoryService
        animalReleaseService.metaClass.runAsync = { Closure c -> }

        controller.animalReleaseService = animalReleaseService

        mockDomain(Person)
        mockDomain(AnimalRelease)
        mockDomain(ValidCodeMapTransmitterType)

        mockLogging(AnimalFactoryService)
        def animalFactoryService = new AnimalFactoryService()
        controller.animalFactoryService = animalFactoryService

        mockLogging(DetectionFactoryService)
        def detectionFactoryService = new DetectionFactoryService()
        controller.detectionFactoryService = detectionFactoryService

        mockLogging(TagFactoryService)
        def tagFactoryService = new TagFactoryService()
        controller.tagFactoryService = tagFactoryService


        pinger = new TransmitterType(transmitterTypeName:'PINGER')
        mockDomain(TransmitterType, [pinger])
        pinger.save()

        // Common request parameters.
        controller.params.project = new Project()
        controller.params.captureLocality = "Perth"
        controller.params.captureLocation = (Point)reader.read("POINT(30.1234 30.1234)")
        controller.params.captureDateTime = new DateTime("2011-05-15T14:12:00+10:00")
        controller.params.captureMethod = new CaptureMethod()

        controller.params.releaseLocality = "Perth"
        controller.params.releaseLocation = (Point)reader.read("POINT(30.1234 30.1234)")
        controller.params.releaseDateTime = new DateTime("2011-05-15T14:12:00+10:00")

        person = new Person(username:"person",
                            organisation:new Organisation())

        mockDomain(Person, [person])
        person.save()

        project1 = new Project(name:"project 1", status:EntityStatus.ACTIVE)
        project2 = new Project(name:"project 2", status:EntityStatus.ACTIVE)
        def projectList = [project1, project2]
        mockDomain(Project, projectList)
        projectList.each { it.save() }

        mockDomain(Animal)
        mockDomain(Sex)

        species = new CaabSpecies(name: "test")
        mockDomain(CaabSpecies, [species])

        codeMap = new CodeMap(codeMap:"A69-1303")
        mockDomain(CodeMap, [codeMap])
        codeMap.save()

        permitted = true
    }

    protected void tearDown() {
        super.tearDown()
    }

    protected boolean isPermitted() {
        return true
    }

    protected def getPrincipal() {
        return person?.id
    }

    void testAddSurgeryNoExistingRelease() {
        Project project = new Project()
        mockDomain(Project, [project])
        project.save()

        controller.params.projectId = project.id

        def model = controller.addSurgery()
        assertNull(model.animalReleaseInstance.id)
        assertEquals(project, model.animalReleaseInstance.project)
    }

    void testAddSurgeryExistingRelease() {
        AnimalRelease release = new AnimalRelease()
        mockDomain(AnimalRelease, [release])
        release.save()

        controller.params.id = release.id
        def model = controller.addSurgery()

        assertEquals(release.id, model.animalReleaseInstance.id)
    }

    void testSaveNoAnimalOrSpecies() {
        addOneSurgeryToParams()
        def model = controller.save()

        assertNull(model.animalReleaseInstance.animal)
        assertEquals("animalRelease.noSpeciesOrAnimal", model.animalReleaseInstance.errors.getGlobalError().getCode())
    }

    void testSaveWithAnimalNoSurgery() {
        Animal animal = new Animal(species:species,
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()

        // animal.id
        controller.params.animal = [id:animal.id]

        def model = controller.save()

        assertEquals("animalRelease.noTaggings", model.animalReleaseInstance.errors.getGlobalError().getCode())
    }

    void testSaveWithAnimal() {
        Animal animal = new Animal(species:species,
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()

        // animal.id
        controller.params.animal = [id:animal.id]
        addOneSurgeryToParams()

        def model = controller.save()

        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)
    }

    void testSaveWithSpeciesNoSexNoSurgery() {
        CaabSpecies species = new CaabSpecies(name:"white shark")
        mockDomain(CaabSpecies, [species])

        mockDomain(Animal)
        mockDomain(AnimalRelease)
        mockDomain(Sex)

        // speciesId
        controller.params.speciesId = species.id
        controller.params.speciesName = species.name

        def model = controller.save()

        assertEquals("animalRelease.noTaggings", model.animalReleaseInstance.errors.getGlobalError().getCode())
        assertEquals(species.id, model.species.id)
        assertEquals(species.name, model.species.name)
    }

    void testSaveWithSpeciesNoSex() {
        CaabSpecies species = new CaabSpecies(name:"white shark")
        mockDomain(CaabSpecies, [species])
        mockDomain(Species, [species]) // not included above

        mockDomain(Animal)
        mockDomain(AnimalRelease)
        mockDomain(Sex)

        // speciesId
        controller.params.speciesId = species.id
        addOneSurgeryToParams()

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertEquals(species, release.animal.species)
        assertNull(release.animal.sex)
    }

    void testSaveWithSpeciesAndSex() {
        CaabSpecies species = new CaabSpecies(name:"flathead")
        mockDomain(CaabSpecies, [species])
        mockDomain(Species, [species]) // not included above

        Sex sex = new Sex(sex:'MALE')
        mockDomain(Sex, [sex])
        sex.save()

        mockDomain(Animal)
        mockDomain(AnimalRelease)

        // speciesId
        controller.params.speciesId = species.id
        controller.params.sex = [id:sex.id]
        addOneSurgeryToParams()

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertEquals(species, release.animal.species)
        assertEquals(sex, release.animal.sex)
    }

    def tag
    def surgeryType
    def surgeryTreatmentType
    TagDeviceModel deviceModel

    private void initSurgeryObjects() {
        mockDomain(AnimalRelease)

        deviceModel = new TagDeviceModel()
        mockDomain(TagDeviceModel, [deviceModel])
        deviceModel.save()


        tag = new Tag(codeMap:codeMap,
                      serialNumber:"11111",
                      model:deviceModel,
                      status:DeviceStatus.NEW,
                      expectedLifeTimeDays:1203)
        mockDomain(Tag, [tag])

        def pinger = new Sensor(pingCode:11111,
                                  transmitterType:pinger)

        mockDomain(Sensor, [pinger])
        tag.addToSensors(pinger)

        tag.save()

        surgeryType = new SurgeryType(type:"type")
        mockDomain(SurgeryType, [surgeryType])
        surgeryType.save()

        surgeryTreatmentType = new SurgeryTreatmentType(type:"type")
        mockDomain(SurgeryTreatmentType, [surgeryTreatmentType])
        surgeryTreatmentType.save()

        mockDomain(Surgery)
        mockForConstraintsTests(Surgery)
    }

    void testSaveWithOneSurgery() {
        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        Project project = new Project(name:"test project")
        mockDomain(Project, [project])
        project.save()
        controller.params.project = project     // Overrides setup()

        addOneSurgeryToParams()

        controller.params.species = animal.species

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)

        def surgeries = Surgery.findAllByRelease(release)
        assertEquals(1, surgeries.size())

        // tag should now be deployed
        surgeries.each( {
            assertEquals(DeviceStatus.DEPLOYED, it.tag.status)
        })

        assertEquals(project, tag.project)
    }

    private addOneSurgeryToParams() {
        initSurgeryObjects()

        // Surgery 0.
        def surgery0 = [
                    timestamp_day: 1,
                    timestamp_month: 6,
                    timestamp_year: 2009,
                    timestamp_hour: 12,
                    timestamp_minute: 34,
                    timestamp_zone: 45,
                    type: [id:surgeryType.id],
                    treatmentType : [id:surgeryTreatmentType.id],
                    comments: "",
                    tag:[codeMap: tag.codeMap, pingCode: tag.pinger.pingCode, serialNumber: tag.serialNumber, model:[id: 1], expectedLifeTimeDays: 1203]]

        controller.params.surgery = ['0':surgery0]
    }

    void testSaveTagInDifferentProject() {
        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        Project releaseProject = new Project(name:"release project")
        Project tagProject = new Project(name:"tag project")
        mockDomain(Project, [releaseProject, tagProject])
        [releaseProject, tagProject].each { it.save() }

        initSurgeryObjects()
        tag.project = tagProject
        tag.save()

        controller.params.project = releaseProject     // Overrides setup()

        def surgery0 = [
            timestamp_day: 1,
            timestamp_month: 6,
            timestamp_year: 2009,
            timestamp_hour: 12,
            timestamp_minute: 34,
            timestamp_zone: 45,
            type: [id:surgeryType.id],
            treatmentType : [id:surgeryTreatmentType.id],
            comments: "",
            tag:[codeMap: tag.codeMap, pingCode: tag.pinger.pingCode, serialNumber: tag.serialNumber, model:[id: 1], expectedLifeTimeDays: 1203]]

        controller.params.surgery = ['0':surgery0]

        controller.params.species = animal.species

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)

        def surgeries = Surgery.findAllByRelease(release)
        assertEquals(1, surgeries.size())

        // tag should now be deployed
        surgeries.each( {
            assertEquals(DeviceStatus.DEPLOYED, it.tag.status)
        })

        assertEquals(tagProject, tag.project)
    }

    void testSaveWithMultipleSurgeries() {
        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        Project project = new Project()
        mockDomain(Project, [project])
        project.save()
        controller.params.project = project     // Overrides setup()

        mockDomain(AnimalRelease)

        mockDomain(Surgery)
        mockForConstraintsTests(Surgery)

        def surgeryMap = [:]
        def tags = []

        initSurgeryObjects()

        def numSurgeries = 3
        numSurgeries.times( {
            Tag newTag = new Tag(codeMap:codeMap,
                                 pingCode:it,
                                 codeName:"A69-1303-" + it,
                                 serialNumber:String.valueOf(it),
                                 transmitterType:pinger,
                                 model:deviceModel,
                                 status:DeviceStatus.NEW,
                                 expectedLifeTimeDays:1203)
            tags.add(newTag)

            def surgery = [
                timestamp_day: 1,
                timestamp_month: 6,
                timestamp_year: 2009,
                timestamp_hour: 12,
                timestamp_minute: 34,
                timestamp_zone: 45,
                type: [id:1],
                treatmentType : [id:1],
                comments: "",
                tag:[codeMap: tag.codeMap, pingCode: tag.pinger.pingCode, serialNumber: newTag.serialNumber, model:[id: 1], expectedLifeTimeDays: 1203]]

            surgeryMap.put(String.valueOf(it), surgery)
        })
        mockDomain(Tag, tags)
        tags.each({ it.save() })

        controller.params.surgery = surgeryMap
        controller.params.species = animal.species

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)

        def surgeries = Surgery.findAllByRelease(release)
        assertEquals(numSurgeries, surgeries.size())

        // tag should now be deployed
        surgeries.each( {
            assertEquals(DeviceStatus.DEPLOYED, it.tag.status)
        })
    }

//    void testSaveWithOneMeasurement()
//    void testSaveWithMultipleMeasurements()
//    void testSaveWithSurgeriesAndMeasurements()

    void testSaveOneSurgeryNewTag() {
        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        Project project = new Project()
        mockDomain(Project, [project])
        project.save()
        controller.params.project = project     // Overrides setup()

        mockDomain(AnimalRelease)

        def codeName = "A69-1303-12345"
        def serialNum = "12345"

        initSurgeryObjects()

        // Surgery 0.
        def surgery0 = [
            timestamp_day: 1,
            timestamp_month: 6,
            timestamp_year: 2009,
            timestamp_hour: 12,
            timestamp_minute: 34,
            timestamp_zone: 45,
            type: [id:1],
            treatmentType : [id:1],
            comments: "",
            tag:[codeMap: codeMap, pingCode: "12345", serialNumber: serialNum, model:[id: deviceModel.id], expectedLifeTimeDays: 1203]]

        controller.params.surgery = ['0':surgery0]

        controller.params.species = animal.species

        def model = controller.save()

        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        def surgeries = Surgery.findAllByRelease(release)
        assertEquals(1, surgeries.size())

        // tag should be created
        surgeries.each( {
            Tag tag = it.tag
            assertNotNull(tag)
            assertEquals(DeviceStatus.DEPLOYED, tag.status)
            assertEquals(project, tag.project)
        })
    }


    void testSaveNoProject() {
        CaabSpecies species = new CaabSpecies()
        mockDomain(CaabSpecies, [species])

        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])

        animal.save()
        controller.params.animal = [id:animal.id]

        controller.params.project = null   // Overrides setup()
        def model = controller.save()

        assertNull(controller.redirectArgs.action)
        assertNotNull(model.animalReleaseInstance)
        assertTrue(model.animalReleaseInstance.hasErrors())
    }

    /**
     * Tests that status is set correctly when an animal is re-released.
     */
    void testSaveStatusCurrentPreviousFinished() {
        CaabSpecies species = new CaabSpecies(name:"flathead")
        mockDomain(CaabSpecies, [species])

        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        controller.params.species = animal.species
        addOneSurgeryToParams()

        mockDomain(AnimalRelease)

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertEquals(AnimalReleaseStatus.CURRENT, release.status)

        // Now create another release for the same animal...
        model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release2 = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release2)
        assertEquals(AnimalReleaseStatus.CURRENT, release2.status)

        // Importantly, the previous release's status should now be FINISHED.
        assertEquals(AnimalReleaseStatus.FINISHED, release.status)
    }

    void testSaveNoEmbargo() {
        Animal animal = new Animal(species:species,
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        addOneSurgeryToParams()

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)

        // Embargo date must be null.
        assertNull(release.embargoDate)
    }

    void testSave3MonthsEmbargo() {
        Animal animal = new Animal(species:species,
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        // controller.params.releaseDateTime = new DateTime("2011-05-15T14:12:00+10:00")
        controller.params.embargoPeriod = 3
        addOneSurgeryToParams()

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertNotNull(release.embargoDate)

        // Embargo date == 2011-08-15"
        Calendar expectedCal = Calendar.getInstance()
        expectedCal.setTime(dateFormat.parse("2011-08-15"))

        Calendar releaseCal = Calendar.getInstance()
        releaseCal.setTime(release.embargoDate)

        assertTrue("year", expectedCal.get(Calendar.YEAR) == releaseCal.get(Calendar.YEAR))
        assertTrue("day", expectedCal.get(Calendar.DAY_OF_YEAR) == releaseCal.get(Calendar.DAY_OF_YEAR))
    }

    void testUpdateNoEmbargo() {
        // Create the release first...
        Animal animal = new Animal(species:species,
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        addOneSurgeryToParams()

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertNull(release.embargoDate)

        // ... now do an update.
        controller.params.id = release.id
        controller.params.embargoPeriod = null

        controller.update()

        release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)

        // Embargo date must be null.
        assertNull(release.embargoDate)
    }

    void testUpdate3MonthsEmbargo() {
        // Create the release first...
        Animal animal = new Animal(species:species,
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        addOneSurgeryToParams()

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertNull(release.embargoDate)

        // ... now do an update.
        controller.params.id = release.id
        controller.params.embargoPeriod = 3

        controller.update()

        release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertNotNull(release.embargoDate)

        // Embargo date == 2011-08-15"
        Calendar expectedCal = Calendar.getInstance()
        expectedCal.setTime(dateFormat.parse("2011-08-15"))

        Calendar releaseCal = Calendar.getInstance()
        releaseCal.setTime(release.embargoDate)

        assertTrue("year", expectedCal.get(Calendar.YEAR) == releaseCal.get(Calendar.YEAR))
        assertTrue("day", expectedCal.get(Calendar.DAY_OF_YEAR) == releaseCal.get(Calendar.DAY_OF_YEAR))
    }

    void testSave3MonthUpdateNo() {
        Animal animal = new Animal(species:species,
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        // controller.params.releaseDateTime = new DateTime("2011-05-15T14:12:00+10:00")
        controller.params.embargoPeriod = 3
        addOneSurgeryToParams()

        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertNotNull(release.embargoDate)

        // Embargo date == 2011-08-15"
        Calendar expectedCal = Calendar.getInstance()
        expectedCal.setTime(dateFormat.parse("2011-08-15"))

        Calendar releaseCal = Calendar.getInstance()
        releaseCal.setTime(release.embargoDate)

        assertTrue("year", expectedCal.get(Calendar.YEAR) == releaseCal.get(Calendar.YEAR))
        assertTrue("day", expectedCal.get(Calendar.DAY_OF_YEAR) == releaseCal.get(Calendar.DAY_OF_YEAR))

        // Now set embargo period back to null and update (embargo date should be left unchanged).
        controller.params.embargoPeriod = null
        controller.params.id = release.id

        controller.update()

        release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertNotNull(release.embargoDate)
    }

    void testCreate() {
        assertEquals(2, candidateEntitiesService.projects().size())

        def model = controller.create()

        assertEquals(2, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(project1))
        assertTrue(model.candidateProjects.contains(project2))

        assertEquals("6 months", model.embargoPeriods[6])
        assertEquals("12 months", model.embargoPeriods[12])
    }

    void testSaveWithError() {
        controller.params.animal = [id:null]
        controller.params.speciesId = null

        def model = controller.save()

        assertEquals(2, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(project1))
        assertTrue(model.candidateProjects.contains(project2))

        assertEquals("6 months", model.embargoPeriods[6])
        assertEquals("12 months", model.embargoPeriods[12])
    }
}
