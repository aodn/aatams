package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.report.*
import au.org.emii.aatams.test.AbstractFiltersUnitTestCase
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

import grails.test.*

import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

import org.apache.shiro.SecurityUtils

class EmbargoFiltersTests extends AbstractFiltersUnitTestCase
{
    def embargoService
    def permissionUtilsService

    Project project1
    Project project2

    AnimalController animalController
    AnimalMeasurementController animalMeasurementController
    AnimalReleaseController releaseController
    DetectionController detectionController
    DetectionSurgeryController detectionSurgeryController
    SensorController sensorController
    SurgeryController surgeryController
    TagController tagController

    def releaseList

    Animal animalNonEmbargoed
    Animal animalEmbargoedReadableProject
    Animal animalEmbargoedNonReadableProject
    Animal animalPastEmbargoed

    AnimalMeasurement animalMeasurementNonEmbargoed
    AnimalMeasurement animalMeasurementEmbargoedReadableProject
    AnimalMeasurement animalMeasurementEmbargoedNonReadableProject
    AnimalMeasurement animalMeasurementPastEmbargoed

    AnimalRelease releaseNonEmbargoed
    AnimalRelease releaseEmbargoedReadableProject
    AnimalRelease releaseEmbargoedNonReadableProject
    AnimalRelease releasePastEmbargoed

    DetectionSurgery detectionSurgeryNonEmbargoed
    DetectionSurgery detectionSurgeryEmbargoedReadableProject
    DetectionSurgery detectionSurgeryEmbargoedNonReadableProject
    DetectionSurgery detectionSurgeryPastEmbargoed

    Tag tagNonEmbargoed
    Tag tagEmbargoedReadableProject
    Tag tagEmbargoedNonReadableProject
    Tag tagPastEmbargoed

    Sensor sensorNonEmbargoed
    Sensor sensorEmbargoedReadableProject
    Sensor sensorEmbargoedNonReadableProject
    Sensor sensorPastEmbargoed
	Sensor sensorPingerNonEmbargoed
	Sensor sensorPingerEmbargoedReadableProject
	Sensor sensorPingerEmbargoedNonReadableProject
	Sensor sensorPingerPastEmbargoed

    Surgery surgeryNonEmbargoed
    Surgery surgeryEmbargoedReadableProject
    Surgery surgeryEmbargoedNonReadableProject
    Surgery surgeryPastEmbargoed

    ValidDetection detectionNonEmbargoed
    ValidDetection detectionEmbargoedReadableProject
    ValidDetection detectionEmbargoedNonReadableProject
    ValidDetection detectionPastEmbargoed

	def queryService = queryService
	def reportInfoService

    Person user

    protected void setUp()
    {
        super.setUp()

        mockLogging(EmbargoService, true)
        embargoService = new EmbargoService()

        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()

        filters.embargoService = embargoService
        embargoService.permissionUtilsService = permissionUtilsService

        project1 = new Project(name: "project 1")
        project2 = new Project(name: "project 2")
        def projectList = [project1, project2]
        mockDomain(Project, projectList)
        projectList.each{ it.save()}

        user = new Person(username: 'jbloggs')

        // Need this for "findByUsername()" etc.
        mockDomain(Person, [user])
        user.save()

        // Check permissions are behaving correctly.
        assertTrue(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project1.id)))
        assertFalse(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project2.id)))

        mockConfig("grails.gorm.default.list.max = 10")

		mockLogging(QueryService)
		queryService = new QueryService()
		mockLogging(ReportInfoService)
		reportInfoService = new ReportInfoService()

        setupControllers()

        mockLogging(Tag)
        mockLogging(Sensor)
        mockLogging(AnimalRelease)
        mockLogging(Surgery)
        mockLogging(ValidDetection)
        mockLogging(DetectionSurgery)

        // Set up some data.
		CodeMap codeMap = new CodeMap(codeMap:"A69-1303")
        tagNonEmbargoed = new Tag(project:project1, codeMap:codeMap)
        tagEmbargoedReadableProject = new Tag(project:project1, codeMap:codeMap)
        tagEmbargoedNonReadableProject = new Tag(project:project2, codeMap:codeMap)
        tagPastEmbargoed = new Tag(project:project2, codeMap:codeMap)

        sensorNonEmbargoed = new Sensor(tag:tagNonEmbargoed, pingCode:1111)
        sensorEmbargoedReadableProject = new Sensor(tag:tagEmbargoedReadableProject, pingCode:2222)
        sensorEmbargoedNonReadableProject = new Sensor(tag:tagEmbargoedNonReadableProject, pingCode:3333)
        sensorPastEmbargoed = new Sensor(tag:tagPastEmbargoed, pingCode:4444)

        sensorPingerNonEmbargoed = new Sensor(tag:tagNonEmbargoed, pingCode:5555)
        sensorPingerEmbargoedReadableProject = new Sensor(tag:tagEmbargoedReadableProject, pingCode:6666)
        sensorPingerEmbargoedNonReadableProject = new Sensor(tag:tagEmbargoedNonReadableProject, pingCode:7777)
        sensorPingerPastEmbargoed = new Sensor(tag:tagPastEmbargoed, pingCode:8888)

        animalNonEmbargoed = new Animal()
        animalEmbargoedReadableProject = new Animal()
        animalEmbargoedNonReadableProject = new Animal()
        animalPastEmbargoed = new Animal()

        animalMeasurementNonEmbargoed = new AnimalMeasurement()
        animalMeasurementEmbargoedReadableProject = new AnimalMeasurement()
        animalMeasurementEmbargoedNonReadableProject = new AnimalMeasurement()
        animalMeasurementPastEmbargoed = new AnimalMeasurement()

        releaseNonEmbargoed = new AnimalRelease(project:project1)
        releaseEmbargoedReadableProject = new AnimalRelease(project:project1, embargoDate:nextYear())
        releaseEmbargoedNonReadableProject = new AnimalRelease(project:project2, embargoDate:nextYear())
        releasePastEmbargoed = new AnimalRelease(project:project2, embargoDate:lastYear())

        surgeryNonEmbargoed = new Surgery(tag:tagNonEmbargoed, release:releaseNonEmbargoed)
        surgeryEmbargoedReadableProject = new Surgery(tag:tagEmbargoedReadableProject, release:releaseEmbargoedReadableProject)
        surgeryEmbargoedNonReadableProject = new Surgery(tag:tagEmbargoedNonReadableProject, release:releaseEmbargoedNonReadableProject)
        surgeryPastEmbargoed = new Surgery(tag:tagPastEmbargoed, release:releasePastEmbargoed)

		ReceiverDownloadFile receiverDownload = new ReceiverDownloadFile(requestingUser:user)
		mockDomain(ReceiverDownloadFile, [receiverDownload])
		receiverDownload.save()

        detectionNonEmbargoed = new ValidDetection(receiverDownload:receiverDownload)
        detectionEmbargoedReadableProject = new ValidDetection(receiverDownload:receiverDownload)

        detectionEmbargoedNonReadableProject = new ValidDetection(receiverDownload:receiverDownload)
        detectionPastEmbargoed = new ValidDetection(receiverDownload:receiverDownload)

        detectionSurgeryNonEmbargoed = new DetectionSurgery(surgery:surgeryNonEmbargoed, detection:detectionNonEmbargoed, sensor:sensorPingerNonEmbargoed)
        detectionSurgeryEmbargoedReadableProject = new DetectionSurgery(surgery:surgeryEmbargoedReadableProject, detection:detectionEmbargoedReadableProject, sensor:sensorPingerEmbargoedReadableProject)
        detectionSurgeryEmbargoedNonReadableProject = new DetectionSurgery(surgery:surgeryEmbargoedNonReadableProject, detection:detectionEmbargoedNonReadableProject, sensor:sensorPingerEmbargoedNonReadableProject)
        detectionSurgeryPastEmbargoed = new DetectionSurgery(surgery:surgeryPastEmbargoed, detection:detectionPastEmbargoed, sensor:sensorPingerPastEmbargoed)

        def animalList = [animalNonEmbargoed,  animalEmbargoedReadableProject,  animalEmbargoedNonReadableProject, animalPastEmbargoed]
        def animalMeasurementList = [animalMeasurementNonEmbargoed,  animalMeasurementEmbargoedReadableProject,  animalMeasurementEmbargoedNonReadableProject, animalMeasurementPastEmbargoed]
        def tagList =     [tagNonEmbargoed,     tagEmbargoedReadableProject,     tagEmbargoedNonReadableProject,     tagPastEmbargoed]
        def sensorList =  [sensorNonEmbargoed,  sensorEmbargoedReadableProject,  sensorEmbargoedNonReadableProject,  sensorPastEmbargoed]
        sensorList +=  [sensorPingerNonEmbargoed,  sensorPingerEmbargoedReadableProject,  sensorPingerEmbargoedNonReadableProject,  sensorPingerPastEmbargoed]
        releaseList =     [releaseNonEmbargoed, releaseEmbargoedReadableProject, releaseEmbargoedNonReadableProject, releasePastEmbargoed]
        def surgeryList = [surgeryNonEmbargoed, surgeryEmbargoedReadableProject, surgeryEmbargoedNonReadableProject, surgeryPastEmbargoed]
        def detectionList =
                          [detectionNonEmbargoed, detectionEmbargoedReadableProject, detectionEmbargoedNonReadableProject, detectionPastEmbargoed]
		detectionList.each {
			it.receiverDeployment = new ReceiverDeployment(location: new GeometryFactory().createPoint(new Coordinate(145f, -42f)))
		}
        def detectionSurgeryList =
                          [detectionSurgeryNonEmbargoed, detectionSurgeryEmbargoedReadableProject, detectionSurgeryEmbargoedNonReadableProject, detectionSurgeryPastEmbargoed]

        mockDomain(Tag, tagList)
        mockDomain(Sensor, sensorList)
        mockDomain(Animal, animalList)
        mockDomain(AnimalMeasurement, animalMeasurementList)
        mockDomain(AnimalRelease, releaseList)
        mockDomain(Surgery, surgeryList)
        mockDomain(ValidDetection, detectionList)
        mockDomain(DetectionSurgery, detectionSurgeryList)

        releaseNonEmbargoed.addToSurgeries(surgeryNonEmbargoed)
        tagNonEmbargoed.addToSurgeries(surgeryNonEmbargoed)
        tagNonEmbargoed.addToSensors(sensorNonEmbargoed)
        tagNonEmbargoed.addToSensors(sensorPingerNonEmbargoed)
        sensorNonEmbargoed.addToDetectionSurgeries(detectionSurgeryNonEmbargoed)
        detectionNonEmbargoed.addToDetectionSurgeries(detectionSurgeryNonEmbargoed)
        animalNonEmbargoed.addToReleases(releaseNonEmbargoed)
        animalMeasurementNonEmbargoed.release = releaseNonEmbargoed

        releaseEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSensors(sensorEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSensors(sensorPingerEmbargoedReadableProject)
        sensorEmbargoedReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedReadableProject)
        detectionEmbargoedReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedReadableProject)
        animalEmbargoedReadableProject.addToReleases(releaseEmbargoedReadableProject)
        animalMeasurementEmbargoedReadableProject.release = releaseEmbargoedReadableProject

        releaseEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSensors(sensorEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSensors(sensorPingerEmbargoedNonReadableProject)
        sensorEmbargoedNonReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedNonReadableProject)
        detectionEmbargoedNonReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedNonReadableProject)
        animalEmbargoedNonReadableProject.addToReleases(releaseEmbargoedNonReadableProject)
        animalMeasurementEmbargoedNonReadableProject.release = releaseEmbargoedNonReadableProject

        releasePastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSensors(sensorPastEmbargoed)
        tagPastEmbargoed.addToSensors(sensorPingerPastEmbargoed)
        sensorPastEmbargoed.addToDetectionSurgeries(detectionSurgeryPastEmbargoed)
        detectionPastEmbargoed.addToDetectionSurgeries(detectionSurgeryPastEmbargoed)
        animalPastEmbargoed.addToReleases(releasePastEmbargoed)
        animalMeasurementPastEmbargoed.release = releasePastEmbargoed

        detectionNonEmbargoed.metaClass.getProject = { project1 }
        detectionEmbargoedReadableProject.metaClass.getProject = { project1 }
        detectionEmbargoedNonReadableProject.metaClass.getProject = { project2 }
        detectionPastEmbargoed.metaClass.getProject = { project2 }

        animalList.each {  it.save() }
        animalMeasurementList.each {  it.save() }
        tagList.each { it.save() }
        sensorList.each { it.save() }
        releaseList.each { it.save() }
        surgeryList.each { it.save() }
        detectionList.each { it.save() }
        detectionSurgeryList.each { it.save() }

		ReceiverDownloadFile.metaClass.getPath = { "/some/path" }
    }

    private void setupControllers() {

        [AnimalController, AnimalMeasurementController, AnimalReleaseController, DetectionController, DetectionSurgeryController, SensorController, SurgeryController, TagController
        ].each {
            clazz ->

                mockController(clazz)
                mockLogging(clazz)
        }

        animalController = new AnimalController()
        animalMeasurementController = new AnimalMeasurementController()
        releaseController = new AnimalReleaseController()
        detectionController = new DetectionController()
        detectionSurgeryController = new DetectionSurgeryController()
        sensorController = new SensorController()
        surgeryController = new SurgeryController()
        tagController = new TagController()

        [animalController, animalMeasurementController, releaseController, detectionController, detectionSurgeryController, sensorController, surgeryController, tagController].each {
            controller ->

                controller.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
                controller.reportInfoService = reportInfoService
                controller.queryService = queryService
                controller.queryService.embargoService = new EmbargoService()
                controller.queryService.embargoService.permissionUtilsService = permissionUtilsService
        }
    }

    protected void tearDown()
    {
        super.tearDown()
    }

	protected def getPrincipal()
	{
		return user.id
	}

	protected boolean isPermitted(String permString)
	{
		if (permString == "project:" + project1.id + ":read")
		{
			return true
		}

		return false
	}

    private Date now()
    {
        return new Date()
    }

    private Date nextYear()
    {
        Calendar cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, 1)
        return cal.getTime()
    }

    private Date lastYear()
    {
        Calendar cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        return cal.getTime()
    }

    void testAnimalList()
    {
        checkList(animalController, "animal")
    }

    void testAnimalNotList()
    {
		controllerName = "animal"
		actionName = "show"

        checkEmbargoed(animalController, animalNonEmbargoed, false, 'animal')
        checkEmbargoed(animalController, animalEmbargoedReadableProject, false, 'animal')
        checkEmbargoed(animalController, animalEmbargoedNonReadableProject, true, 'animal')
        checkEmbargoed(animalController, animalPastEmbargoed, false, 'animal')
    }

    void testAnimalMeasurementList()
    {
        checkList(animalMeasurementController, "animalMeasurement")
    }

    void testAnimalMeasurementNotList()
    {
		controllerName = "animalMeasurement"
		actionName = "show"

        checkEmbargoed(animalMeasurementController, animalMeasurementNonEmbargoed, false, 'animalMeasurement')
        checkEmbargoed(animalMeasurementController, animalMeasurementEmbargoedReadableProject, false, 'animalMeasurement')
        checkEmbargoed(animalMeasurementController, animalMeasurementEmbargoedNonReadableProject, true, 'animalMeasurement')
        checkEmbargoed(animalMeasurementController, animalMeasurementPastEmbargoed, false, 'animalMeasurement')
    }

    void testAnimalReleaseList()
    {
        checkList(releaseController, "animalRelease")
    }

    void testAnimalReleaseNotList()
    {
		controllerName = "animalRelease"
		actionName = "show"

        checkEmbargoed(releaseController, releaseNonEmbargoed, false, 'animalRelease')
        checkEmbargoed(releaseController, releaseEmbargoedReadableProject, false, 'animalRelease')
        checkEmbargoed(releaseController, releaseEmbargoedNonReadableProject, true, 'animalRelease')
        checkEmbargoed(releaseController, releasePastEmbargoed, false, 'animalRelease')
    }

    void testTagNotList()
    {
		controllerName = "tag"
		actionName = "show"

        checkEmbargoed(tagController, tagNonEmbargoed, false, 'tag')
        checkEmbargoed(tagController, tagEmbargoedReadableProject, false, 'tag')
        checkEmbargoed(tagController, tagEmbargoedNonReadableProject, true, 'tag')
        checkEmbargoed(tagController, tagPastEmbargoed, false, 'tag')
    }

    void testSensorList()
    {
        checkList(sensorController, "sensor")
    }

    void testSensorNotList()
    {
		controllerName = "sensor"
		actionName = "show"

        checkEmbargoed(sensorController, sensorNonEmbargoed, false, 'sensor')
        checkEmbargoed(sensorController, sensorEmbargoedReadableProject, false, 'sensor')
        checkEmbargoed(sensorController, sensorEmbargoedNonReadableProject, true, 'sensor')
        checkEmbargoed(sensorController, sensorPastEmbargoed, false, 'sensor')

		checkEmbargoed(sensorController, sensorPingerNonEmbargoed, false, 'sensor')
		checkEmbargoed(sensorController, sensorPingerEmbargoedReadableProject, false, 'sensor')
		checkEmbargoed(sensorController, sensorPingerEmbargoedNonReadableProject, true, 'sensor')
		checkEmbargoed(sensorController, sensorPingerPastEmbargoed, false, 'sensor')
    }

	void testDetectionList()
	{
		controllerName = "detection"

        def model = [entityList: ValidDetection.list(), total: ValidDetection.count()]
		assertNotNull(model)

		FilterConfig filter = getFilter("genericList")
		assertNotNull(filter)

		filter.after(model)
		assertNotNull(model)

		assertTrue(model.entityList.containsAll(detectionNonEmbargoed, detectionPastEmbargoed, detectionEmbargoedReadableProject))
		assertFalse(model.entityList.contains(detectionEmbargoedNonReadableProject))
	}

    void testDetectionNotList()
    {
		controllerName = "detection"
		actionName = "show"

        checkDetection(detectionNonEmbargoed, false)
        checkDetection(detectionEmbargoedReadableProject, false)
        checkDetection(detectionEmbargoedNonReadableProject, true)
        checkDetection(detectionPastEmbargoed, false)
    }

    void testSurgeryList() {
        checkList(surgeryController, "surgery")
    }

    void testSurgeryNotList() {
		controllerName = "surgery"
		actionName = "show"

        checkEmbargoed(surgeryController, surgeryNonEmbargoed, false, 'surgery')
        checkEmbargoed(surgeryController, surgeryEmbargoedReadableProject, false, 'surgery')
        checkEmbargoed(surgeryController, surgeryEmbargoedNonReadableProject, true, 'surgery')
        checkEmbargoed(surgeryController, surgeryPastEmbargoed, false, 'surgery')
    }

    void testDetectionDetectionSurgeryList() {
        checkList(detectionSurgeryController, "detectionSurgery")
    }

    void testDetectionDetectionSurgeryNotList() {
		controllerName = "detectionSurgery"
		actionName = "show"

        checkEmbargoed(detectionSurgeryController, detectionSurgeryNonEmbargoed, false, 'detectionSurgery')
        checkEmbargoed(detectionSurgeryController, detectionSurgeryEmbargoedReadableProject, false, 'detectionSurgery')
        checkEmbargoed(detectionSurgeryController, detectionSurgeryEmbargoedNonReadableProject, true, 'detectionSurgery')
        checkEmbargoed(detectionSurgeryController, detectionSurgeryPastEmbargoed, false, 'detectionSurgery')
    }

	void testRedirectToLoginWhenNotAuthenticated()
	{
		controllerName = "sensor"
		actionName = "show"
		authenticated = false
		checkEmbargoed(sensorController, sensorEmbargoedNonReadableProject, true, 'sensor', "login", "/sensor/show/" + sensorEmbargoedNonReadableProject.id)
	}

	void testRedirectToUnauthorizedWhenAuthenticated()
	{
		controllerName = "sensor"
		actionName = "show"
		authenticated = true
		checkEmbargoed(sensorController, sensorEmbargoedNonReadableProject, true, 'sensor', "unauthorized", null)
	}

    private void checkDetection(def detection, boolean isEmbargoed)
    {
        assert(detection)

        detectionController.params.id = detection.id
        def model = detectionController.show()
        assertNotNull(model)
        assertEquals(1, model.size())

        FilterConfig filter = getFilter("genericNotList")
        assertNotNull(filter)

        // All entities should be there...
        filter.after(model)
        assertNotNull(model)
        assertEquals(1, model.size())

        if (isEmbargoed)
        {
			assertNull(model.detectionInstance)
        }
        else
        {
			assertNotNull(model.detectionInstance)
        }
    }

    private void checkList(def controller, def entityName)
    {
		controller.params._name = "entityName"

		int expectedNum = (entityName == 'sensor') ? 6 : 3
		int expectedTotal = (entityName == 'sensor') ? 8 : 4
        def model = controller.list()

        assertEquals(expectedNum, model.entityList.size())
        assertEquals(expectedTotal, model.total)

        // Embargoed releases should not appear at all after filter.
        FilterConfig filter = getFilter('genericList')
        assertNotNull(filter)

        filter.after(model)
        assertNotNull(model)

		int expectedNumAfterEmbargo = (entityName == 'sensor') ? 6 : 3
		int expectedTotalAfterEmbargo = (entityName == 'sensor') ? 8 : 4
        assertEquals(expectedNumAfterEmbargo, model.entityList.size())
        assertEquals(expectedTotalAfterEmbargo, model.total)
    }

    private void checkEmbargoed(def controller, def entity, boolean isEmbargoed, String entityName)
    {
		checkEmbargoed(controller, entity, isEmbargoed, entityName, "unauthorized", null)
    }

    private void checkEmbargoed(def controller, def entity, boolean isEmbargoed, String entityName, expectedRedirectAction, expectedTargetUri)
    {
        controller.params.id = entity.id
        assert(controller.params)

        def model = controller.show()
        assertNotNull(model)
        assertEquals(1, model.size())

        FilterConfig filter = getFilter("genericNotList")
        assertNotNull(filter)

		EmbargoFilters.metaClass.getTargetUri =
		{
			params ->

			return expectedTargetUri
		}

        boolean result = filter.after(model)

        if (isEmbargoed)
        {
            // redirect auth/unauthorized
            assertEquals("auth", redirectArgs.controller)
            assertEquals(expectedRedirectAction, redirectArgs.action)
			if (expectedTargetUri)
			{
				assertEquals(expectedTargetUri, redirectArgs.params.targetUri)
			}
        }
        else
        {
            assertNull(result)
        }
    }
}
