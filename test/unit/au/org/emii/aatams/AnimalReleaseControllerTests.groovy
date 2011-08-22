package au.org.emii.aatams

import grails.test.*

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import org.joda.time.*
import org.joda.time.format.DateTimeFormat

import java.text.DateFormat
import java.text.SimpleDateFormat

class AnimalReleaseControllerTests extends ControllerUnitTestCase 
{
    WKTReader reader = new WKTReader()
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")

    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(AnimalReleaseController)
        
        // See http://jira.grails.org/browse/GRAILS-5926
        controller.metaClass.message = { Map map -> return "error message" }
        
        mockDomain(AnimalRelease)
        
        mockLogging(AnimalFactoryService)
        def animalFactoryService = new AnimalFactoryService()
        controller.animalFactoryService = animalFactoryService
        
        mockLogging(TagFactoryService)
        def tagFactoryService = new TagFactoryService()
        controller.tagFactoryService = tagFactoryService
        
        DeviceStatus deployedStatus = new DeviceStatus(status:"DEPLOYED")
        mockDomain(DeviceStatus, [deployedStatus])
        deployedStatus.save()
        
        TransmitterType pinger = new TransmitterType(transmitterTypeName:'PINGER')
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
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testAddSurgeryNoExistingRelease()
    {
        Project project = new Project()
        mockDomain(Project, [project])
        project.save()
        
        controller.params.projectId = project.id
        
        def model = controller.addSurgery()
        assertNull(model.animalReleaseInstance.id)
        assertEquals(project, model.animalReleaseInstance.project)
    }

    void testAddSurgeryExistingRelease()
    {
        AnimalRelease release = new AnimalRelease()
        mockDomain(AnimalRelease, [release])
        release.save()
        
        controller.params.id = release.id
        def model = controller.addSurgery()
        
        assertEquals(release.id, model.animalReleaseInstance.id)
    }
    
    void testSaveNoAnimalOrSpecies()
    {
        def model = controller.save()
        assertNull(model.animalReleaseInstance.animal)
    }
    
    void testSaveWithAnimal()
    {
        Animal animal = new Animal(species:new Species(),
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        
        // animal.id
        controller.params.animal = [id:animal.id]
        
        def model = controller.save()
        
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)
    }
    
    void testSaveWithSpeciesNoSex()
    {
        Species species = new Species()
        mockDomain(Species, [species])
        species.save()
        
        mockDomain(Animal)
        mockDomain(AnimalRelease)
        mockDomain(Sex)
        
        // speciesId
        controller.params.speciesId = species.id
        
        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)
        
        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertEquals(species, release.animal.species)
        assertNull(release.animal.sex)
    }
    
    void testSaveWithSpeciesAndSex()
    {
        Species species = new Species()
        mockDomain(Species, [species])
        species.save()
        
        Sex sex = new Sex(sex:'MALE')
        mockDomain(Sex, [sex])
        sex.save()
        
        mockDomain(Animal)
        mockDomain(AnimalRelease)
        
        // speciesId
        controller.params.speciesId = species.id
        controller.params.sex = [id:sex.id]
        
        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertEquals(species, release.animal.species)
        assertEquals(sex, release.animal.sex)
    }
    
    void testSaveWithOneSurgery()
    {
        Animal animal = new Animal(species:new Species())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        Project project = new Project()
        mockDomain(Project, [project])
        project.save()
        controller.params.project = project     // Overrides setup()
        
        mockDomain(AnimalRelease)

        Tag tag = new Tag(codeName:"A69-1303-11111",
                          status:new DeviceStatus(status:'NEW'))
        mockDomain(Tag, [tag])
        tag.save()
        
        // Surgery 0.
        mockDomain(Surgery)
        mockForConstraintsTests(Surgery)

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
            tagCodeName: tag.codeName,
            tagSerialNumber: "12345",
            tagModelId: 1]
        
        controller.params.surgery = ['0':surgery0]
        
        controller.params.species = animal.species
        
        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertEquals(1, release.surgeries.size())
        
        // tag should now be deployed
        release.surgeries.each(
        {
            assertEquals(tag.codeName, it.tag.codeName)
            assertEquals(new DeviceStatus(status:'DEPLOYED').status, it.tag.status.status)
        })
    }
    
    void testSaveWithMultipleSurgeries()
    {
        Animal animal = new Animal(species:new Species())
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
        
        def numSurgeries = 3
        numSurgeries.times(
        {
            Tag tag = new Tag(codeName:"A69-1303-" + it,
                              status:new DeviceStatus(status:'NEW'))
            tags.add(tag)              
            
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
                tagCodeName: tag.codeName,
                tagSerialNumber: "12345",
                tagModelId: 1]
            
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
        assertEquals(numSurgeries, release.surgeries.size())
        
        // tag should now be deployed
        release.surgeries.each(
        {
            assertEquals(new DeviceStatus(status:'DEPLOYED').status, it.tag.status.status)
        })
    }
    
//    void testSaveWithOneMeasurement()
//    void testSaveWithMultipleMeasurements()
//    void testSaveWithSurgeriesAndMeasurements()

    void testSaveOneSurgeryNewTag()
    {
        Animal animal = new Animal(species:new Species())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]

        Project project = new Project()
        mockDomain(Project, [project])
        project.save()
        controller.params.project = project     // Overrides setup()
        
        mockDomain(AnimalRelease)

        // New tag - need to give DeviceModel
        DeviceModel deviceModel = new DeviceModel()
        mockDomain(DeviceModel, [deviceModel])
        deviceModel.save()
        mockDomain(Tag)
        def codeName = "A69-1303-12345"
        def serialNum = "12345"
        
        
        // Surgery 0.
        mockDomain(Surgery)
        mockForConstraintsTests(Surgery)

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
            tagCodeName: codeName,
            tagSerialNumber: serialNum,
            tagModelId: deviceModel.id]
        
        controller.params.surgery = ['0':surgery0]
        
        controller.params.species = animal.species
        
        def model = controller.save()
        
        assertEquals("show", controller.redirectArgs.action)
        assertNotNull(controller.redirectArgs.id)

        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertEquals(1, release.surgeries.size())
        
        // tag should be created
        release.surgeries.each(
        {
            Tag tag = it.tag
            assertNotNull(tag)
            assertEquals(codeName, tag.codeName)
            assertEquals(new DeviceStatus(status:'DEPLOYED').status, tag.status.status)
        })
    }


    void testSaveNoProject()
    {
        Species species = new Species()
        mockDomain(Species, [species])
        species.save()
        
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
    void testSaveStatusCurrentPreviousFinished()
    {
        Species species = new Species(name:"flathead")
        mockDomain(Species, [species])
        species.save()
        
        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        controller.params.species = animal.species

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
    
    void testSaveNoEmbargo()
    {
        Animal animal = new Animal(species:new Species(),
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        
        def model = controller.save()
        assertEquals("show", controller.redirectArgs.action)
        
        AnimalRelease release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        
        // Embargo date must be null.
        assertNull(release.embargoDate)
    }
    
    void testSave3MonthsEmbargo()
    {
        Animal animal = new Animal(species:new Species(),
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        
        // controller.params.releaseDateTime = new DateTime("2011-05-15T14:12:00+10:00")
        controller.params.embargoPeriod = 3
        
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

    void testUpdateNoEmbargo()
    {
        // Create the release first...
        Animal animal = new Animal(species:new Species(),
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        
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
    
    void testUpdate3MonthsEmbargo()
    {
        // Create the release first...
        Animal animal = new Animal(species:new Species(),
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        
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
    
    void testSave3MonthUpdateNo()
    {
        Animal animal = new Animal(species:new Species(),
                                   sex:new Sex())
        mockDomain(Animal, [animal])
        animal.save()
        controller.params.animal = [id:animal.id]
        
        // controller.params.releaseDateTime = new DateTime("2011-05-15T14:12:00+10:00")
        controller.params.embargoPeriod = 3
        
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
        
        // Now set embargo period back to null and update.
        controller.params.embargoPeriod = null
        controller.params.id = release.id
        
        controller.update()
        
        release = AnimalRelease.get(controller.redirectArgs.id)
        assertNotNull(release)
        assertNull(release.embargoDate)
    }
}
