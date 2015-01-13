package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.report.*
import au.org.emii.aatams.test.AbstractFiltersUnitTestCase
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

import org.apache.shiro.SecurityUtils

class VisibilityControlFiltersTests extends AbstractFiltersUnitTestCase
{
    def visibilityControlService
    def permissionUtilsService

    Project projectWithMembership
    Project projectNoMembership
    Project protectedProjectWithMembership
    Project protectedProjectNoMembership

    AnimalController animalController
    AnimalMeasurementController animalMeasurementController
    AnimalReleaseController releaseController
    DetectionController detectionController
    SensorController sensorController
    SurgeryController surgeryController
    TagController tagController

    def releaseList

    Animal animalNonEmbargoed
    Animal animalEmbargoedReadableProject
    Animal animalEmbargoedNonReadableProject
    Animal animalPastEmbargoed
    Animal animalProtectedReadableProject
    Animal animalProtectedNonReadableProject

    AnimalMeasurement animalMeasurementNonEmbargoed
    AnimalMeasurement animalMeasurementEmbargoedReadableProject
    AnimalMeasurement animalMeasurementEmbargoedNonReadableProject
    AnimalMeasurement animalMeasurementPastEmbargoed
    AnimalMeasurement animalMeasurementProtectedReadableProject
    AnimalMeasurement animalMeasurementProtectedNonReadableProject

    AnimalRelease releaseNonEmbargoed
    AnimalRelease releaseEmbargoedReadableProject
    AnimalRelease releaseEmbargoedNonReadableProject
    AnimalRelease releasePastEmbargoed
    AnimalRelease releaseProtectedReadableProject
    AnimalRelease releaseProtectedNonReadableProject

    Tag tagNonEmbargoed
    Tag tagEmbargoedReadableProject
    Tag tagEmbargoedNonReadableProject
    Tag tagPastEmbargoed
    Tag tagProtectedReadableProject
    Tag tagProtectedNonReadableProject

    Sensor sensorNonEmbargoed
    Sensor sensorEmbargoedReadableProject
    Sensor sensorEmbargoedNonReadableProject
    Sensor sensorPastEmbargoed
    Sensor sensorProtectedReadableProject
    Sensor sensorProtectedNonReadableProject
    Sensor sensorPingerNonEmbargoed
    Sensor sensorPingerEmbargoedReadableProject
    Sensor sensorPingerEmbargoedNonReadableProject
    Sensor sensorPingerPastEmbargoed
    Sensor sensorPingerProtectedReadableProject
    Sensor sensorPingerProtectedNonReadableProject

    Surgery surgeryNonEmbargoed
    Surgery surgeryEmbargoedReadableProject
    Surgery surgeryEmbargoedNonReadableProject
    Surgery surgeryPastEmbargoed
    Surgery surgeryProtectedReadableProject
    Surgery surgeryProtectedNonReadableProject

    ValidDetection detectionNonEmbargoed
    ValidDetection detectionEmbargoedReadableProject
    ValidDetection detectionEmbargoedNonReadableProject
    ValidDetection detectionPastEmbargoed
    ValidDetection detectionProtectedReadableProject
    ValidDetection detectionProtectedNonReadableProject

    def queryService
    def reportInfoService

    Person user

    protected void setUp()
    {
        super.setUp()

        mockLogging(VisibilityControlService, true)
        visibilityControlService = new VisibilityControlService()

        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()

        filters.visibilityControlService = visibilityControlService
        visibilityControlService.permissionUtilsService = permissionUtilsService

        projectWithMembership = new Project(name: "project 1 (member of)")
        projectNoMembership = new Project(name: "project 2 (not member of)")
        protectedProjectWithMembership = new Project(name: "project 3 (member of, protected)", isProtected: true)
        protectedProjectNoMembership = new Project(name: "project 4 (not member, protected)", isProtected: true)
        def projectList = [projectWithMembership, projectNoMembership, protectedProjectWithMembership, protectedProjectNoMembership]
        mockDomain(Project, projectList)
        projectList.each{ it.save() }

        user = new Person(username: 'jbloggs')

        // Need this for "findByUsername()" etc.
        mockDomain(Person, [user])
        user.save()

        // Check permissions are behaving correctly.
        assertTrue(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(projectWithMembership.id)))
        assertFalse(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(projectNoMembership.id)))
        assertTrue(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(protectedProjectWithMembership.id)))
        assertFalse(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(protectedProjectNoMembership.id)))

        mockConfig('''grails.gorm.default.list.max = 100
                      filter.count.max = 10000''')

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

        // Set up some data.
        CodeMap codeMap = new CodeMap(codeMap:"A69-1303")
        tagNonEmbargoed = new Tag(project:projectWithMembership, codeMap:codeMap)
        tagEmbargoedReadableProject = new Tag(project:projectWithMembership, codeMap:codeMap)
        tagEmbargoedNonReadableProject = new Tag(project:projectNoMembership, codeMap:codeMap)
        tagPastEmbargoed = new Tag(project:projectNoMembership, codeMap:codeMap)
        tagProtectedReadableProject = new Tag(project:protectedProjectWithMembership, codeMap:codeMap)
        tagProtectedNonReadableProject = new Tag(project:protectedProjectNoMembership, codeMap:codeMap)

        sensorNonEmbargoed = new Sensor(tag:tagNonEmbargoed, pingCode:1111)
        sensorEmbargoedReadableProject = new Sensor(tag:tagEmbargoedReadableProject, pingCode:2222)
        sensorEmbargoedNonReadableProject = new Sensor(tag:tagEmbargoedNonReadableProject, pingCode:3333)
        sensorPastEmbargoed = new Sensor(tag:tagPastEmbargoed, pingCode:4444)
        sensorProtectedReadableProject = new Sensor(tag:tagProtectedReadableProject, pingCode:9999)
        sensorProtectedNonReadableProject = new Sensor(tag:tagProtectedNonReadableProject, pingCode:10000)

        sensorPingerNonEmbargoed = new Sensor(tag:tagNonEmbargoed, pingCode:5555)
        sensorPingerEmbargoedReadableProject = new Sensor(tag:tagEmbargoedReadableProject, pingCode:6666)
        sensorPingerEmbargoedNonReadableProject = new Sensor(tag:tagEmbargoedNonReadableProject, pingCode:7777)
        sensorPingerPastEmbargoed = new Sensor(tag:tagPastEmbargoed, pingCode:8888)
        sensorPingerProtectedReadableProject = new Sensor(tag:tagProtectedReadableProject, pingCode:10001)
        sensorPingerProtectedNonReadableProject = new Sensor(tag:tagProtectedNonReadableProject, pingCode:10002)

        animalNonEmbargoed = new Animal()
        animalEmbargoedReadableProject = new Animal()
        animalEmbargoedNonReadableProject = new Animal()
        animalPastEmbargoed = new Animal()
        animalProtectedReadableProject = new Animal()
        animalProtectedNonReadableProject = new Animal()

        animalMeasurementNonEmbargoed = new AnimalMeasurement()
        animalMeasurementEmbargoedReadableProject = new AnimalMeasurement()
        animalMeasurementEmbargoedNonReadableProject = new AnimalMeasurement()
        animalMeasurementPastEmbargoed = new AnimalMeasurement()
        animalMeasurementProtectedReadableProject = new AnimalMeasurement()
        animalMeasurementProtectedNonReadableProject = new AnimalMeasurement()

        releaseNonEmbargoed = new AnimalRelease(project:projectWithMembership)
        releaseEmbargoedReadableProject = new AnimalRelease(project:projectWithMembership, embargoDate:nextYear())
        releaseEmbargoedNonReadableProject = new AnimalRelease(project:projectNoMembership, embargoDate:nextYear())
        releasePastEmbargoed = new AnimalRelease(project:projectNoMembership, embargoDate:lastYear())
        releaseProtectedReadableProject = new AnimalRelease(project:protectedProjectWithMembership, embargoDate:nextYear())
        releaseProtectedNonReadableProject = new AnimalRelease(project:protectedProjectNoMembership, embargoDate:nextYear())

        surgeryNonEmbargoed = new Surgery(tag:tagNonEmbargoed, release:releaseNonEmbargoed)
        surgeryEmbargoedReadableProject = new Surgery(tag:tagEmbargoedReadableProject, release:releaseEmbargoedReadableProject)
        surgeryEmbargoedNonReadableProject = new Surgery(tag:tagEmbargoedNonReadableProject, release:releaseEmbargoedNonReadableProject)
        surgeryPastEmbargoed = new Surgery(tag:tagPastEmbargoed, release:releasePastEmbargoed)
        surgeryProtectedReadableProject = new Surgery(tag:tagProtectedReadableProject, release:releaseProtectedReadableProject)
        surgeryProtectedNonReadableProject = new Surgery(tag:tagProtectedNonReadableProject, release:releaseProtectedNonReadableProject)

        ReceiverDownloadFile receiverDownload = new ReceiverDownloadFile(requestingUser:user)
        mockDomain(ReceiverDownloadFile, [receiverDownload])
        receiverDownload.save()

        detectionNonEmbargoed = new ValidDetection(receiverDownload:receiverDownload)
        detectionEmbargoedReadableProject = new ValidDetection(receiverDownload:receiverDownload)
        detectionEmbargoedNonReadableProject = new ValidDetection(receiverDownload:receiverDownload)
        detectionPastEmbargoed = new ValidDetection(receiverDownload:receiverDownload)
        detectionProtectedReadableProject = new ValidDetection(receiverDownload:receiverDownload)
        detectionProtectedNonReadableProject = new ValidDetection(receiverDownload:receiverDownload)

        def animalList = [animalNonEmbargoed, animalEmbargoedReadableProject, animalEmbargoedNonReadableProject, animalPastEmbargoed, animalProtectedReadableProject, animalProtectedNonReadableProject]
        def animalMeasurementList = [animalMeasurementNonEmbargoed, animalMeasurementEmbargoedReadableProject, animalMeasurementEmbargoedNonReadableProject, animalMeasurementPastEmbargoed, animalMeasurementProtectedReadableProject, animalMeasurementProtectedNonReadableProject]
        def tagList = [tagNonEmbargoed, tagEmbargoedReadableProject, tagEmbargoedNonReadableProject, tagPastEmbargoed, tagProtectedReadableProject, tagProtectedNonReadableProject]
        def sensorList = [sensorNonEmbargoed, sensorEmbargoedReadableProject, sensorEmbargoedNonReadableProject, sensorPastEmbargoed, sensorProtectedReadableProject, sensorProtectedNonReadableProject,
                          sensorPingerNonEmbargoed, sensorPingerEmbargoedReadableProject, sensorPingerEmbargoedNonReadableProject, sensorPingerPastEmbargoed, sensorPingerProtectedReadableProject, sensorPingerProtectedNonReadableProject]
        releaseList = [releaseNonEmbargoed, releaseEmbargoedReadableProject, releaseEmbargoedNonReadableProject, releasePastEmbargoed, releaseProtectedReadableProject, releaseProtectedNonReadableProject]
        def surgeryList = [surgeryNonEmbargoed, surgeryEmbargoedReadableProject, surgeryEmbargoedNonReadableProject, surgeryPastEmbargoed, surgeryProtectedReadableProject, surgeryProtectedNonReadableProject]
        def detectionList = [detectionNonEmbargoed, detectionEmbargoedReadableProject, detectionEmbargoedNonReadableProject, detectionPastEmbargoed, detectionProtectedReadableProject, detectionProtectedNonReadableProject]
        detectionList.each {
            it.receiverDeployment = new ReceiverDeployment(location: new GeometryFactory().createPoint(new Coordinate(145f, -42f)))
        }

        mockDomain(Tag, tagList)
        mockDomain(Sensor, sensorList)
        mockDomain(Animal, animalList)
        mockDomain(AnimalMeasurement, animalMeasurementList)
        mockDomain(AnimalRelease, releaseList)
        mockDomain(Surgery, surgeryList)
        mockDomain(ValidDetection, detectionList)

        releaseNonEmbargoed.addToSurgeries(surgeryNonEmbargoed)
        tagNonEmbargoed.addToSurgeries(surgeryNonEmbargoed)
        tagNonEmbargoed.addToSensors(sensorNonEmbargoed)
        tagNonEmbargoed.addToSensors(sensorPingerNonEmbargoed)
        animalNonEmbargoed.addToReleases(releaseNonEmbargoed)
        animalMeasurementNonEmbargoed.release = releaseNonEmbargoed

        releaseEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSensors(sensorEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSensors(sensorPingerEmbargoedReadableProject)
        animalEmbargoedReadableProject.addToReleases(releaseEmbargoedReadableProject)
        animalMeasurementEmbargoedReadableProject.release = releaseEmbargoedReadableProject

        releaseEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSensors(sensorEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSensors(sensorPingerEmbargoedNonReadableProject)
        animalEmbargoedNonReadableProject.addToReleases(releaseEmbargoedNonReadableProject)
        animalMeasurementEmbargoedNonReadableProject.release = releaseEmbargoedNonReadableProject

        releasePastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSensors(sensorPastEmbargoed)
        tagPastEmbargoed.addToSensors(sensorPingerPastEmbargoed)
        animalPastEmbargoed.addToReleases(releasePastEmbargoed)
        animalMeasurementPastEmbargoed.release = releasePastEmbargoed

        releaseProtectedReadableProject.addToSurgeries(surgeryProtectedReadableProject)
        tagProtectedReadableProject.addToSurgeries(surgeryProtectedReadableProject)
        tagProtectedReadableProject.addToSensors(sensorProtectedReadableProject)
        tagProtectedReadableProject.addToSensors(sensorPingerProtectedReadableProject)
        animalProtectedReadableProject.addToReleases(releaseProtectedReadableProject)
        animalMeasurementProtectedReadableProject.release = releaseProtectedReadableProject

        releaseProtectedNonReadableProject.addToSurgeries(surgeryProtectedNonReadableProject)
        tagProtectedNonReadableProject.addToSurgeries(surgeryProtectedNonReadableProject)
        tagProtectedNonReadableProject.addToSensors(sensorProtectedNonReadableProject)
        tagProtectedNonReadableProject.addToSensors(sensorPingerProtectedNonReadableProject)
        animalProtectedNonReadableProject.addToReleases(releaseProtectedNonReadableProject)
        animalMeasurementProtectedNonReadableProject.release = releaseProtectedNonReadableProject

        detectionNonEmbargoed.metaClass.getProject = { projectWithMembership }
        detectionEmbargoedReadableProject.metaClass.getProject = { projectWithMembership }
        detectionEmbargoedNonReadableProject.metaClass.getProject = { projectNoMembership }
        detectionPastEmbargoed.metaClass.getProject = { projectNoMembership }
        detectionProtectedReadableProject.metaClass.getProject = { protectedProjectWithMembership }
        detectionProtectedNonReadableProject.metaClass.getProject = { protectedProjectNoMembership }

        detectionNonEmbargoed.metaClass.getSurgeries = { [surgeryNonEmbargoed] }
        detectionEmbargoedReadableProject.metaClass.getSurgeries = { [surgeryEmbargoedReadableProject] }
        detectionEmbargoedNonReadableProject.metaClass.getSurgeries = { [surgeryEmbargoedNonReadableProject] }
        detectionPastEmbargoed.metaClass.getSurgeries = { [surgeryPastEmbargoed] }

        animalList.each { it.save() }
        animalMeasurementList.each { it.save() }
        tagList.each { it.save() }
        sensorList.each { it.save() }
        releaseList.each { it.save() }
        surgeryList.each { it.save() }
        detectionList.each { it.save() }

        ReceiverDownloadFile.metaClass.getPath = { "/some/path" }
    }

    private void setupControllers() {

        [AnimalController, AnimalMeasurementController, AnimalReleaseController, DetectionController, SensorController, SurgeryController, TagController
        ].each {
            clazz ->

                mockController(clazz)
                mockLogging(clazz)
        }

        animalController = new AnimalController()
        animalMeasurementController = new AnimalMeasurementController()
        releaseController = new AnimalReleaseController()
        detectionController = new DetectionController()
        sensorController = new SensorController()
        surgeryController = new SurgeryController()
        tagController = new TagController()

        [animalController, animalMeasurementController, releaseController, detectionController, sensorController, surgeryController, tagController].each {
            controller ->

                controller.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
                controller.reportInfoService = reportInfoService
                controller.queryService = queryService
                controller.queryService.visibilityControlService = new VisibilityControlService()
                controller.queryService.visibilityControlService.permissionUtilsService = permissionUtilsService
        }
    }

    protected void tearDown()
    {
        super.tearDown()

        VisibilityControlFilters.metaClass = null
    }

    protected def getPrincipal()
    {
        return user.id
    }

    protected boolean isPermitted(String permString)
    {
        def memberProjects = [projectWithMembership, protectedProjectWithMembership]
        def permissions = memberProjects.collect{ "project:${it.id}:read" }*.toString()

        return permissions.contains(permString)
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
        checkEmbargoed(animalController, animalProtectedReadableProject, false, 'animal')
        checkEmbargoed(animalController, animalProtectedNonReadableProject, true, 'animal')
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
        checkEmbargoed(animalMeasurementController, animalMeasurementProtectedReadableProject, false, 'animalMeasurement')
        checkEmbargoed(animalMeasurementController, animalMeasurementProtectedNonReadableProject, true, 'animalMeasurement')
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
        checkEmbargoed(releaseController, releaseProtectedReadableProject, false, 'animalRelease')
        checkEmbargoed(releaseController, releaseProtectedNonReadableProject, true, 'animalRelease')
    }

    void testTagNotList()
    {
        controllerName = "tag"
        actionName = "show"

        checkEmbargoed(tagController, tagNonEmbargoed, false, 'tag')
        checkEmbargoed(tagController, tagEmbargoedReadableProject, false, 'tag')
        checkEmbargoed(tagController, tagEmbargoedNonReadableProject, true, 'tag')
        checkEmbargoed(tagController, tagPastEmbargoed, false, 'tag')
        checkEmbargoed(tagController, tagProtectedReadableProject, false, 'tag')
        checkEmbargoed(tagController, tagProtectedNonReadableProject, true, 'tag')
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
        checkEmbargoed(sensorController, sensorProtectedReadableProject, false, 'sensor')
        checkEmbargoed(sensorController, sensorProtectedNonReadableProject, true, 'sensor')

        checkEmbargoed(sensorController, sensorPingerNonEmbargoed, false, 'sensor')
        checkEmbargoed(sensorController, sensorPingerEmbargoedReadableProject, false, 'sensor')
        checkEmbargoed(sensorController, sensorPingerEmbargoedNonReadableProject, true, 'sensor')
        checkEmbargoed(sensorController, sensorPingerPastEmbargoed, false, 'sensor')
        checkEmbargoed(sensorController, sensorPingerProtectedReadableProject, false, 'sensor')
        checkEmbargoed(sensorController, sensorPingerProtectedNonReadableProject, true, 'sensor')
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

        def filtered = model.entityList

        assertNotNull(filtered.find{ it.isDuplicate(detectionNonEmbargoed) })
        assertNotNull(filtered.find{ it.isDuplicate(detectionEmbargoedReadableProject) })
        assertNotNull(filtered.find{ it.isDuplicate(detectionEmbargoedNonReadableProject) })
        assertNotNull(filtered.find{ it.isDuplicate(detectionPastEmbargoed) })
        assertNotNull(filtered.find{ it.isDuplicate(detectionProtectedReadableProject) })
        assertNotNull(filtered.find{ it.isDuplicate(detectionProtectedNonReadableProject) })
    }

    void testDetectionNotList()
    {
        controllerName = "detection"
        actionName = "show"

        checkDetection(detectionNonEmbargoed, false)
        checkDetection(detectionEmbargoedReadableProject, false)
        checkDetection(detectionEmbargoedNonReadableProject, true)
        checkDetection(detectionPastEmbargoed, false)
        checkDetectionProtected(detectionProtectedReadableProject, true)
        checkDetectionProtected(detectionProtectedNonReadableProject, false)
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

    void testRedirectToLoginWhenEmbargoedAndNotAuthenticated()
    {
        controllerName = "sensor"
        actionName = "show"
        authenticated = false

        checkEmbargoed(sensorController, sensorEmbargoedNonReadableProject, true, 'sensor', "login", "/sensor/show/" + sensorEmbargoedNonReadableProject.id)
    }

    void testRedirectToLoginWhenProtectedAndNotAuthenticated()
    {
        controllerName = "sensor"
        actionName = "show"
        authenticated = false

        checkEmbargoed(sensorController, sensorProtectedNonReadableProject, true, 'sensor', "login", "/sensor/show/" + sensorProtectedNonReadableProject.id)
    }

    void testRedirectToUnauthorizedWhenEmbargoedAndAuthenticated()
    {
        controllerName = "sensor"
        actionName = "show"
        authenticated = true

        checkEmbargoed(sensorController, sensorEmbargoedNonReadableProject, true, 'sensor', "unauthorized", null)
    }

    void testRedirectToUnauthorizedWhenProtectedAndAuthenticated()
    {
        controllerName = "sensor"
        actionName = "show"
        authenticated = true

        checkEmbargoed(sensorController, sensorProtectedNonReadableProject, true, 'sensor', "unauthorized", null)
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
        assertNotNull(model.detectionInstance)
        assertNotNull(model.detectionInstance.receiverDownload)
        assertNotNull(model.detectionInstance.receiverDownload.requestingUser)
        assertEquals('jbloggs', model.detectionInstance.receiverDownload.requestingUser.username)
        assertEquals(detection.timestamp, model.detectionInstance.timestamp)

        if (isEmbargoed) {
            // ... but not the associated detectionSurgeries (which links detection back to tag/release)
            assertTrue(model.detectionInstance.surgeries.isEmpty())
        }
        else {
            assertEquals(1, model.detectionInstance.surgeries.size())
        }
    }

    private void checkDetectionProtected(detection, shouldBeVisible) {

        assertNotNull(detection)

        detectionController.params.id = detection.id
        def model = detectionController.show()
        assertNotNull(model)
        assertEquals(1, model.size())

        FilterConfig filter = getFilter("genericNotList")
        assertNotNull(filter)
        filter.after(model)

        if (shouldBeVisible) {
            assertNotNull(model.detectionInstance)
        }
        else {
            assertNull(model.detectionInstance)
        }
    }

    private void checkList(controller, entityName)
    {
        controller.params._name = "entityName"

        int expectedNum = 4
        int expectedTotal = 6
        int expectedNumAfterEmbargo = 4
        int expectedTotalAfterEmbargo = 6

        if (entityName == 'sensor') {
            // Twice as many sensor objects as others
            expectedNum *= 2
            expectedTotal *= 2
            expectedNumAfterEmbargo *= 2
            expectedTotalAfterEmbargo *= 2
        }

        def model = controller.list()

        assertEquals(expectedNum, model.entityList.size())
        assertEquals(expectedTotal, model.total)

        // Embargoed releases should not appear at all after filter.
        FilterConfig filter = getFilter('genericList')
        assertNotNull(filter)

        filter.after(model)
        assertNotNull(model)

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

        VisibilityControlFilters.metaClass.getTargetUri =
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
