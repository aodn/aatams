package au.org.emii.aatams

import au.org.emii.aatams.detection.*
import au.org.emii.aatams.report.*
import au.org.emii.aatams.report.filter.ReportFilterFactoryService;
import au.org.emii.aatams.test.AbstractFiltersUnitTestCase

import grails.test.*

import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

import org.apache.shiro.SecurityUtils

class EmbargoFiltersTests extends AbstractFiltersUnitTestCase 
{
    def embargoService
    def permissionUtilsService
    
    Project project1
    Project project2
    
    AnimalReleaseController releaseController
    DetectionController detectionController
    SensorController sensorController
    TagController tagController

    def releaseList
    
    AnimalRelease releaseNonEmbargoed
    AnimalRelease releaseEmbargoedReadableProject
    AnimalRelease releaseEmbargoedNonReadableProject
    AnimalRelease releasePastEmbargoed
    
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

    ValidDetection detectionNonEmbargoed
    ValidDetection detectionEmbargoedReadableProject
    ValidDetection detectionEmbargoedNonReadableProject
    ValidDetection detectionPastEmbargoed
    
	def reportFilterFactoryService
	def reportInfoService
	
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
        
        Person user = new Person(username: 'jbloggs')
        
        // Need this for "findByUsername()" etc.
        mockDomain(Person, [user])
        user.save()
        
        // Check permissions are behaving correctly.
        assertTrue(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project1.id)))
        assertFalse(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project2.id)))

        mockConfig("grails.gorm.default.list.max = 10")

		mockLogging(ReportFilterFactoryService)
		reportFilterFactoryService = new ReportFilterFactoryService()
		mockLogging(ReportInfoService)
		reportInfoService = new ReportInfoService()
		
        mockController(AnimalReleaseController)
        mockLogging(AnimalReleaseController)
        releaseController = new AnimalReleaseController()
        releaseController.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
		releaseController.reportInfoService = reportInfoService
		releaseController.reportFilterFactoryService = reportFilterFactoryService
        
        mockController(DetectionController)
        mockLogging(DetectionController)
        detectionController = new DetectionController()
        detectionController.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
        
        mockController(SensorController)
        mockLogging(SensorController)
        sensorController = new SensorController()
        sensorController.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
		sensorController.reportInfoService = reportInfoService
		sensorController.reportFilterFactoryService = reportFilterFactoryService

        mockController(TagController)
        mockLogging(TagController)
        tagController = new TagController()
        tagController.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
		tagController.reportInfoService = reportInfoService
		tagController.reportFilterFactoryService = reportFilterFactoryService

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

        releaseNonEmbargoed = new AnimalRelease(project:project1)
        releaseEmbargoedReadableProject = new AnimalRelease(project:project1, embargoDate:nextYear())
        releaseEmbargoedNonReadableProject = new AnimalRelease(project:project2, embargoDate:nextYear())
        releasePastEmbargoed = new AnimalRelease(project:project2, embargoDate:lastYear())

        Surgery surgeryNonEmbargoed = new Surgery(tag:tagNonEmbargoed, release:releaseNonEmbargoed)
        Surgery surgeryEmbargoedReadableProject = new Surgery(tag:tagEmbargoedReadableProject, release:releaseEmbargoedReadableProject)
        Surgery surgeryEmbargoedNonReadableProject = new Surgery(tag:tagEmbargoedNonReadableProject, release:releaseEmbargoedNonReadableProject)
        Surgery surgeryPastEmbargoed = new Surgery(tag:tagPastEmbargoed, release:releasePastEmbargoed)

		ReceiverDownloadFile receiverDownload = new ReceiverDownloadFile(requestingUser:user)
		mockDomain(ReceiverDownloadFile, [receiverDownload])
		receiverDownload.save()
		
        detectionNonEmbargoed = new ValidDetection(receiverDownload:receiverDownload)
        detectionEmbargoedReadableProject = new ValidDetection(receiverDownload:receiverDownload)
        detectionEmbargoedNonReadableProject = new ValidDetection(receiverDownload:receiverDownload)
        detectionPastEmbargoed = new ValidDetection(receiverDownload:receiverDownload)

        DetectionSurgery detectionSurgeryNonEmbargoed = new DetectionSurgery(surgery:surgeryNonEmbargoed, detection:detectionNonEmbargoed, sensor:sensorPingerNonEmbargoed)
        DetectionSurgery detectionSurgeryEmbargoedReadableProject = new DetectionSurgery(surgery:surgeryEmbargoedReadableProject, detection:detectionEmbargoedReadableProject, sensor:sensorPingerEmbargoedReadableProject)
        DetectionSurgery detectionSurgeryEmbargoedNonReadableProject = new DetectionSurgery(surgery:surgeryEmbargoedNonReadableProject, detection:detectionEmbargoedNonReadableProject, sensor:sensorPingerEmbargoedNonReadableProject)
        DetectionSurgery detectionSurgeryPastEmbargoed = new DetectionSurgery(surgery:surgeryPastEmbargoed, detection:detectionPastEmbargoed, sensor:sensorPingerPastEmbargoed)

        
        def tagList =     [tagNonEmbargoed,     tagEmbargoedReadableProject,     tagEmbargoedNonReadableProject,     tagPastEmbargoed]
        def sensorList =  [sensorNonEmbargoed,  sensorEmbargoedReadableProject,  sensorEmbargoedNonReadableProject,  sensorPastEmbargoed]
        sensorList +=  [sensorPingerNonEmbargoed,  sensorPingerEmbargoedReadableProject,  sensorPingerEmbargoedNonReadableProject,  sensorPingerPastEmbargoed]
        releaseList =     [releaseNonEmbargoed, releaseEmbargoedReadableProject, releaseEmbargoedNonReadableProject, releasePastEmbargoed]
        def surgeryList = [surgeryNonEmbargoed, surgeryEmbargoedReadableProject, surgeryEmbargoedNonReadableProject, surgeryPastEmbargoed]
        def detectionList =
                          [detectionNonEmbargoed, detectionEmbargoedReadableProject, detectionEmbargoedNonReadableProject, detectionPastEmbargoed]
        def detectionSurgeryList =
                          [detectionSurgeryNonEmbargoed, detectionSurgeryEmbargoedReadableProject, detectionSurgeryEmbargoedNonReadableProject, detectionSurgeryPastEmbargoed]
        
        mockDomain(Tag, tagList)
        mockDomain(Sensor, sensorList)
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
        
        releaseEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSensors(sensorEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSensors(sensorPingerEmbargoedReadableProject)
        sensorEmbargoedReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedReadableProject)
        detectionEmbargoedReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedReadableProject)
        
        releaseEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSensors(sensorEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSensors(sensorPingerEmbargoedNonReadableProject)
        sensorEmbargoedNonReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedNonReadableProject)
        detectionEmbargoedNonReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedNonReadableProject)
        
        releasePastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSensors(sensorPastEmbargoed)
        tagPastEmbargoed.addToSensors(sensorPingerPastEmbargoed)
        sensorPastEmbargoed.addToDetectionSurgeries(detectionSurgeryPastEmbargoed)
        detectionPastEmbargoed.addToDetectionSurgeries(detectionSurgeryPastEmbargoed)
        
        detectionNonEmbargoed.metaClass.getProject = { project1 }
        detectionEmbargoedReadableProject.metaClass.getProject = { project1 }
        detectionEmbargoedNonReadableProject.metaClass.getProject = { project2 }
        detectionPastEmbargoed.metaClass.getProject = { project2 }
        
        tagList.each { it.save() }
        sensorList.each { it.save() }
        releaseList.each { it.save() }
        surgeryList.each { it.save() }
        detectionList.each { it.save() }
        detectionSurgeryList.each { it.save() }
    }        

    protected void tearDown() 
    {
        super.tearDown()
    }
   
	protected def getPrincipal()
	{
		return 'jbloggs'
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

    void testAnimalReleaseList() 
    {
        checkList(releaseController, "animalRelease")
    }

    void testAnimalReleaseNotList() 
    {
        checkEmbargoed(releaseController, releaseNonEmbargoed, false, 'animalRelease')
        checkEmbargoed(releaseController, releaseEmbargoedReadableProject, false, 'animalRelease')
        checkEmbargoed(releaseController, releaseEmbargoedNonReadableProject, true, 'animalRelease')
        checkEmbargoed(releaseController, releasePastEmbargoed, false, 'animalRelease')
    }

    void testTagList()
    {
		tagController.metaClass.insertNoSensorRestriction = {}
        checkList(tagController, "tag")
    }
    
    void testTagNotList()
    {
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
        checkEmbargoed(sensorController, sensorNonEmbargoed, false, 'sensor')
        checkEmbargoed(sensorController, sensorEmbargoedReadableProject, false, 'sensor')
        checkEmbargoed(sensorController, sensorEmbargoedNonReadableProject, true, 'sensor')
        checkEmbargoed(sensorController, sensorPastEmbargoed, false, 'sensor')

		checkEmbargoed(sensorController, sensorPingerNonEmbargoed, false, 'sensor')
		checkEmbargoed(sensorController, sensorPingerEmbargoedReadableProject, false, 'sensor')
		checkEmbargoed(sensorController, sensorPingerEmbargoedNonReadableProject, true, 'sensor')
		checkEmbargoed(sensorController, sensorPingerPastEmbargoed, false, 'sensor')
    }
    
    void testDetectionNotList()
    {
        checkDetection(detectionNonEmbargoed, false)
        checkDetection(detectionEmbargoedReadableProject, false)
        checkDetection(detectionEmbargoedNonReadableProject, true)
        checkDetection(detectionPastEmbargoed, false)
    }
    
    private void checkDetection(def detection, boolean isEmbargoed)
    {
        assert(detection)
        
        detectionController.params.id = detection.id
        def model = detectionController.show()
        assertNotNull(model)
        assertEquals(1, model.size())
        
        FilterConfig filter = getFilter("detectionNotList")
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
		
        if (isEmbargoed)
        {
            // ... but not the associated detectionSurgeries (which links detection back to tag/release)
            assertTrue(model.detectionInstance.detectionSurgeries.isEmpty())
        }
        else
        {
            assertEquals(1, model.detectionInstance.detectionSurgeries.size())
        }
    }
    
    private void checkList(def controller, def entityName)
    {
		controller.params._name = "entityName"
		
		int expectedNum = (entityName == 'sensor') ? 8 : 4
        def model = controller.list()
		
println ("model: " + model)
		
        assertEquals(expectedNum, model.entityList.size())
        assertEquals(expectedNum, model.total)

        // Embargoed releases should not appear at all after filter.
        FilterConfig filter = getFilter(entityName + 'List')
        assertNotNull(filter)
        
        filter.after(model)
        assertNotNull(model)
        
		int expectedNumAfterEmbargo = (entityName == 'sensor') ? 6 : 3
        assertEquals(expectedNumAfterEmbargo, model.entityList.size())
        assertEquals(expectedNum, model.total)
    }
    
    private void checkEmbargoed(def controller, def entity, boolean isEmbargoed, String entityName)
    {
        controller.params.id = entity.id
        assert(controller.params)
        
        def model = controller.show()
        assertNotNull(model)
        assertEquals(1, model.size())
        
        FilterConfig filter = getFilter(entityName + "NotList")
        assertNotNull(filter)
        
        Map redirectParams = [:] 
        filter.metaClass.redirect = 
        { Map m -> redirectParams.putAll m }

        boolean result = filter.after(model)
        
        if (isEmbargoed)
        {
            // redirect auth/unauthorized
            assertEquals("auth", redirectParams.controller)
            assertEquals("unauthorized", redirectParams.action)
        }
        else
        {
            assertNull(result)
        }
    }
}
