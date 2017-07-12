package au.org.emii.aatams

import grails.test.*
import org.joda.time.DateTime

class AnimalReleaseServiceTests extends GrailsUnitTestCase {
    def releaseService
    def params
    def animalReleaseInstance

    def animalFactoryService
    def tagFactoryService

    def codeMap
    def species

    protected void setUp() {
        super.setUp()

        mockLogging(AnimalFactoryService, true)
        animalFactoryService = new AnimalFactoryService()

        mockLogging(TagFactoryService, true)
        tagFactoryService = new TagFactoryService()

        mockLogging(AnimalReleaseService, true)
        releaseService = new AnimalReleaseService()
        releaseService.animalFactoryService = animalFactoryService
        releaseService.tagFactoryService = tagFactoryService
        releaseService.metaClass.runAsync = { Closure c -> }

        codeMap = new CodeMap(codeMap:"A69-1303")
        mockDomain(CodeMap, [codeMap])
        codeMap.save()

        mockDomain(ValidCodeMapTransmitterType)

        params = setupParams()

        animalReleaseInstance = new AnimalRelease()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSaveWithEmptyParams() {
        params = [:]
        assertExceptionWithGlobalError("Parameters cannot be empty.", null)
    }

    void testSaveWithValidParamsExistingAnimalAndTag() {
        assertNoExceptionAfterSave()
    }

    void testSaveWithNoSpeciesOrAnimal() {
        params.animal = null
        params.species = null

        assertExceptionWithGlobalError("Species or existing animal must be specified.", "animalRelease.noSpeciesOrAnimal")
        assertNull(animalReleaseInstance.animal)
    }

    void testSaveWithNoCaptureLocality() {
        params.captureLocality = null
        assertExceptionWithFieldError("captureLocality", "nullable")
    }

    void testSaveWithNoCaptureLocation() {
        params.captureLocation = null
        assertNoExceptionAfterSave()
    }

    void testSaveWithNoCaptureDateTime() {
        params.captureDateTime = null
        assertExceptionWithFieldError("captureDateTime", "nullable")
    }

    void testSaveWithNoCaptureMethod() {
        params.captureMethod = null
        assertExceptionWithFieldError("captureMethod", "nullable")
    }

    void testSaveWithNoSurgery() {
        params.surgery = [:]
        assertExceptionWithGlobalError("Animal release must have at least one tagging.", "animalRelease.noTaggings")
    }

    void testSaveWithOneSurgeryExistingTag() {
        saveMultipleSurgeries(1)
    }

    void testDeleteTagStatusRevertsToNew() {
        saveMultipleSurgeries(1)
        assertEquals(DeviceStatus.DEPLOYED, Tag.get(1).status)
        assertEquals(1, AnimalRelease.count())

        releaseService.delete(animalReleaseInstance)
        assertEquals(DeviceStatus.NEW, Tag.get(1).status)
        assertEquals(0, AnimalRelease.count())
    }

    void testSaveWithTwoSurgeriesExistingTags() {
        saveMultipleSurgeries(2)
    }

    void testSaveWithOneSurgeryNewTag() {
        saveMultipleSurgeriesWithNewTags(1)
    }

    void testSaveWithTwoSurgeriesNewTags() {
        saveMultipleSurgeriesWithNewTags(2)
    }

    void testSaveWithTwoSurgeriesOneExistingTagOneNewTag() {
        assertEquals(2, Tag.count())

        Tag tag = Tag.get(1)
        assertNotNull(tag)
        assertEquals(DeviceStatus.NEW, tag.status)

        params.surgery = [:]

        def surgeryExisting = [
                    timestamp_day: 1,
                    timestamp_month: 6,
                    timestamp_year: 2009,
                    timestamp_hour: 12,
                    timestamp_minute: 34,
                    timestamp_zone: 45,
                    type: [id:SurgeryType.get(1).id],
                    treatmentType : [id:SurgeryTreatmentType.get(1).id],
                    comments: "",
                    tag:[codeMap:tag.codeMap, pingCode:tag.pinger.pingCode, serialNumber: tag.serialNumber, model:[id: tag.model.id]]]

        params.surgery += ['0':surgeryExisting]

        def codeName = "A69-1303-3333"
        def serialNum = "3333"

        def surgeryNew = [
                    timestamp_day: 1,
                    timestamp_month: 6,
                    timestamp_year: 2009,
                    timestamp_hour: 12,
                    timestamp_minute: 34,
                    timestamp_zone: 45,
                    type: [id:1],
                    treatmentType : [id:1],
                    comments: "",
                    tag:[codeMap:codeMap, pingCode:"3333", serialNumber: serialNum, model:new TagDeviceModel()]]

        params.surgery += ['1':surgeryNew]

        assertNoExceptionAfterSave()
        def surgeries = Surgery.findAllByRelease(animalReleaseInstance)
        assertEquals(2, surgeries.size())
        assertNotNull(tag)

        surgeries = Surgery.findAllByTag(tag)
        assertTrue(surgeries*.tag.id.contains(tag.id))
        assertEquals(1, surgeries.size())
        assertEquals(DeviceStatus.DEPLOYED, tag.status)
        assertEquals(animalReleaseInstance.project.id, tag.project.id)
        assertEquals(3, Tag.count())
    }

    void testSavePreviousReleaseStatusFinished() {
        Animal animal = Animal.get(1)
        assertNotNull(animal)

        AnimalRelease prevRelease = new AnimalRelease(status:AnimalReleaseStatus.CURRENT)
        animal.addToReleases(prevRelease)
        animal.save()

        assertEquals(AnimalReleaseStatus.CURRENT, prevRelease.status)
        assertNoExceptionAfterSave()
        assertEquals(AnimalReleaseStatus.FINISHED, prevRelease.status)
    }

    void testSaveWithNoMeasurement() {
        params.measurement = null
        assertNoExceptionAfterSave()
    }

    void testSaveWithOneMeasurement() {
        saveMultipleMeasurements(1)
    }

    void testSaveWithTwoMeasurements() {
        saveMultipleMeasurements(2)
    }

    void testSaveWithNoReleaseLocality() {
        params.releaseLocality = null
        assertExceptionWithFieldError("releaseLocality", "nullable")
    }

    void testSaveWithNoReleaseLocation() {
        params.releaseLocation = null
        assertNoExceptionAfterSave()
    }

    void testSaveWithNoReleaseDateTime() {
        params.releaseDateTime = null
        assertExceptionWithFieldError("releaseDateTime", "nullable")
    }

    void testSaveWithAnimal() {
        params.animal = [id:1]
        assertNoExceptionAfterSave()
    }

    void testSaveWithSpeciesNoSex() {
        params.animal = null
        params.speciesId = CaabSpecies.findByName(species.name).id
        params.sex = null
        assertNoExceptionAfterSave()

        assertNull(animalReleaseInstance.animal.sex)
        assertEquals(CaabSpecies.findByName(species.name), animalReleaseInstance.animal.species)
    }

    void testSaveWithSpeciesAndSex() {
        params.animal = null
        params.speciesId = CaabSpecies.findByName(species.name).id
        params.sex = [id:Sex.findBySex('MALE').id]

        assertNoExceptionAfterSave()

        assertNotNull(animalReleaseInstance.animal.sex)
        assertEquals('MALE', animalReleaseInstance.animal.sex.sex)
    }

    void testSaveNoEmbargo() {
        assertNoExceptionAfterSave()
        assertNull(animalReleaseInstance.embargoDate)
    }

    void testSave3MonthsEmbargo() {
        //new DateTime("2011-03-03T12:12:12")
        params.embargoPeriod = 3

        assertNoExceptionAfterSave()
        assertNotNull(animalReleaseInstance.embargoDate)

        // Embargo date == 2011-08-15"
        DateTime expectedEmbargoDateTime = animalReleaseInstance.releaseDateTime.plusMonths(3)
        Calendar expectedCal = Calendar.getInstance()
        expectedCal.setTime(expectedEmbargoDateTime.toDate())

        Calendar embargoCal = Calendar.getInstance()
        embargoCal.setTime(animalReleaseInstance.embargoDate)

        assertTrue("year", expectedCal.get(Calendar.YEAR) == embargoCal.get(Calendar.YEAR))
        assertTrue("day", expectedCal.get(Calendar.DAY_OF_YEAR) == embargoCal.get(Calendar.DAY_OF_YEAR))
    }

    private void assertNoExceptionAfterSave() {
        try {
            releaseService.save(animalReleaseInstance, params)

            assertNotNull(animalReleaseInstance)
            assertFalse(animalReleaseInstance.hasErrors())

            assertNotNull(animalReleaseInstance.animal)
            assertNotNull(animalReleaseInstance.animal.species)

            assertNotNull(AnimalRelease.get(animalReleaseInstance.id))

            def surgeries = Surgery.findAllByRelease(animalReleaseInstance)
            assertFalse(surgeries.isEmpty())

            surgeries.each {
                assertFalse(it.hasErrors())
            }

            def animalId = animalReleaseInstance.animal.id
            assertFalse(Animal.get(animalId).releases.isEmpty())
        }
        catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage())
        }
    }

    private void assertExceptionWithGlobalError(msg, errorCode) {
        assertException(msg, errorCode)

        if (errorCode) {
            assertEquals(errorCode, animalReleaseInstance.errors.getGlobalError().getCode())
        }
    }

    private void assertExceptionWithFieldError(field, errorCode) {
        assertException(null, errorCode)

        if (errorCode) {
            assertEquals(errorCode, animalReleaseInstance.errors.getFieldError(field).getCode())
        }
    }

    private assertException(msg, errorCode) {
        try {
            releaseService.save(animalReleaseInstance, params)
            fail("Save passed when expected to fail.")
        }
        catch (IllegalArgumentException e) {
            if (msg) {
                assertEquals(msg, e.getMessage())
            }

            if (errorCode) {
                assertTrue(animalReleaseInstance.hasErrors())
            }
        }
    }

    private Map setupParams() {
        mockDomain(AnimalRelease)
        mockDomain(Surgery)

        species = new CaabSpecies(name:'White Shark')
        mockDomain(CaabSpecies, [species])
        mockDomain(Species, [species]) // not done automatically by above

        Sex male = new Sex(sex:'MALE')
        mockDomain(Sex, [male])
        male.save()


        Project project = new Project()
        mockDomain(Project, [project])
        project.save()

        Animal animal = new Animal(species:species)
        mockDomain(Animal, [animal])
        animal.save()

        CaptureMethod captureMethod = new CaptureMethod()
        mockDomain(CaptureMethod, [captureMethod])
        captureMethod.save()

        SurgeryType surgeryType = new SurgeryType()
        mockDomain(SurgeryType, [surgeryType])
        surgeryType.save()

        SurgeryTreatmentType surgeryTreatmentType = new SurgeryTreatmentType()
        mockDomain(SurgeryTreatmentType, [surgeryTreatmentType])
        surgeryTreatmentType.save()

        TagDeviceModel tagModel = new TagDeviceModel()
        mockDomain(TagDeviceModel, [tagModel])
        tagModel.save()

        TransmitterType pinger = new TransmitterType(transmitterTypeName:"PINGER")
        mockDomain(TransmitterType, [pinger])
        pinger.save()

        Tag tag1 = new Tag(codeMap:codeMap, serialNumber:"12345", model:tagModel, status:DeviceStatus.NEW)
        Tag tag2 = new Tag(codeMap:codeMap, serialNumber:"22222", model:tagModel, status:DeviceStatus.NEW)
        def tagList = [tag1, tag2]
        mockDomain(Tag, tagList)

        Sensor sensor1 = new Sensor(pingCode:12345, transmitterType:pinger, tag:tag1)
        Sensor sensor2 = new Sensor(pingCode:22222, transmitterType:pinger, tag:tag2)
        def sensorList = [sensor1, sensor2]
        mockDomain(Sensor, sensorList)

        tag1.addToSensors(sensor1)
        tag2.addToSensors(sensor2)

        tagList.each { it.save() }

        def surgeryParams = [timestamp:new DateTime(),
                             type:surgeryType,
                             treatmentType:surgeryTreatmentType,
                             tag:[serialNumber: tag1.serialNumber]]

        Map params =
            [project:project,
               animal:[id:animal.id],
              captureLocality:"Neptune Islands",
             captureDateTime:new DateTime("2011-03-03T12:12:12"),
             captureMethod:captureMethod,
             releaseLocality:"Neptune Islands",
             releaseDateTime:new DateTime("2011-03-03T12:12:12"),
             surgery:['0':surgeryParams]]

        return params
    }

    private saveMultipleSurgeries(int numSurgeries) {
        params.surgery = [:]

        numSurgeries.times {
            Tag tag = Tag.get(it + 1)
            assertNotNull(tag)
            assertEquals(DeviceStatus.NEW, tag.status)

            def surgery = [
                        timestamp_day: 1,
                        timestamp_month: 6,
                        timestamp_year: 2009,
                        timestamp_hour: 12,
                        timestamp_minute: 34,
                        timestamp_zone: 45,
                        type: [id:SurgeryType.get(1).id],
                        treatmentType : [id:SurgeryTreatmentType.get(1).id],
                        comments: "",
                        tag:[codeMap: tag.codeMap, pingCode: tag.pinger.pingCode, serialNumber: tag.serialNumber, model:[id: tag.model.id]]]

            params.surgery += [(String.valueOf(it)):surgery]
        }

        assertNoExceptionAfterSave()

        def surgeries = Surgery.findAllByRelease(animalReleaseInstance)
        assertEquals(numSurgeries, surgeries.size())

        numSurgeries.times {
            Tag tag = Tag.get(it + 1)
            assertNotNull(tag)

            surgeries = Surgery.findAllByTag(tag)
            assertTrue(surgeries*.tag.id.contains(tag.id))
            assertEquals(1, surgeries.size())
            assertEquals(DeviceStatus.DEPLOYED, tag.status)
            assertEquals(animalReleaseInstance.project.id, tag.project.id)
        }
    }

    private saveMultipleMeasurements(int numMeasurements) {
        params.measurement = [:]

        AnimalMeasurementType measurementType = new AnimalMeasurementType()
        mockDomain(AnimalMeasurementType, [measurementType])
        measurementType.save()

        MeasurementUnit measurementUnit = new MeasurementUnit()
        mockDomain(MeasurementUnit, [measurementUnit])
        measurementUnit.save()

        numMeasurements.times {
            def measurement =
            [
                type:measurementType,
                value:12.3f,
                unit:measurementUnit,
                estimate:true,
                comments:"some comment"
            ]

            params.measurement += [(String.valueOf(it)):measurement]
        }

        assertNoExceptionAfterSave()
        assertEquals(numMeasurements, animalReleaseInstance.measurements.size())
    }

    private saveMultipleSurgeriesWithNewTags(int numNewTags) {
        assertEquals(2, Tag.count())

        params.surgery = [:]

        numNewTags.times {
            def serialNum = "3333" + it

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
                        tag:[codeMap:new CodeMap(), pingCode:it, serialNumber: serialNum, model:new TagDeviceModel()]]

            params.surgery += [(String.valueOf(it)):surgery]
        }

        assertNoExceptionAfterSave()
        def surgeries = Surgery.findAllByRelease(animalReleaseInstance)
        assertEquals(numNewTags, surgeries.size())
        assertEquals(2 + numNewTags, Tag.count())
    }
}
