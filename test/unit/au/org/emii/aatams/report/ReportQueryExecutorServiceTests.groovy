package au.org.emii.aatams.report

import grails.test.*

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*
import au.org.emii.aatams.report.filter.*
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import org.apache.shiro.SecurityUtils

class ReportQueryExecutorServiceTests extends AbstractGrailsUnitTestCase 
{
    def embargoService
    def permissionUtilsService
    def reportQueryExecutorService
    
    Project project1
    Project project2

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

	Person jkburges
	
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(EmbargoService, true)
        embargoService = new EmbargoService()
        
        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()
        embargoService.permissionUtilsService = permissionUtilsService
        
        mockLogging(ReportQueryExecutorService, true)
        reportQueryExecutorService = new ReportQueryExecutorService()
        reportQueryExecutorService.embargoService = embargoService
        
        // Create a couple of projects and installations.
        project1 = new Project(name: "project 1")
        project2 = new Project(name: "project 2")
        def projectList = [project1, project2]
        mockDomain(Project, projectList)
        projectList.each{ it.save()}
        
        Installation installation1 = new Installation(name: "installation 1")
        Installation installation2 = new Installation(name: "installation 2")
        def installationList = [installation1, installation2]
        mockDomain(Installation, installationList)
        installationList.each { it.save() }
        
        jkburges = new Person(username: 'jkburges')
        
        // Need this for "findByUsername()" etc.
        mockDomain(Person, [jkburges])
        jkburges.save()
        
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
		return jkburges.username
	}    
	
	protected boolean isPermitted(permission)
	{
		if (permission == "project:" + project1.id + ":read")
		{
			return true
		}
		
		return false
	}

    void testTagEmbargoFiltering()
    {
        // Check permissions are behaving correctly.
        assertTrue(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project1.id)))
        assertFalse(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project2.id)))
        
		ReportFilter filter = new ReportFilter(Tag)
        def tags = reportQueryExecutorService.executeQuery(filter)
        assertEquals(3, tags.size())
        assertTrue(tags.contains(tagNonEmbargoed))
        assertTrue(tags.contains(tagEmbargoedReadableProject))
        assertFalse(tags.contains(tagEmbargoedNonReadableProject))
        assertTrue(tags.contains(tagPastEmbargoed))
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
}
