package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class ReportQueryExecutorServiceTests extends GrailsUnitTestCase 
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
    
    ValidDetection detectionNonEmbargoed
    ValidDetection detectionEmbargoedReadableProject
    ValidDetection detectionEmbargoedNonReadableProject
    ValidDetection detectionPastEmbargoed

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
        
        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )
                        
        Person jkburges = new Person(username: 'jkburges')
        def subject = [ getPrincipal: { jkburges.username },
                        isAuthenticated: { true },
                        hasRole: { true },
                        isPermitted:
                        {
                            if (it == "project:" + project1.id + ":read")
                            {
                                return true
                            }
                            
                            return false
                        }
                        
                        
                      ] as Subject

        SecurityUtils.metaClass.static.getSubject = { subject }
        
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
        tagNonEmbargoed = new Tag(project:project1, codeName:"A69-1303-1111", codeMap:"A69-1303", pingCode:1111)
        tagEmbargoedReadableProject = new Tag(project:project1, codeName:"A69-1303-2222", codeMap:"A69-1303", pingCode:2222)
        tagEmbargoedNonReadableProject = new Tag(project:project2, codeName:"A69-1303-3333", codeMap:"A69-1303", pingCode:3333)
        tagPastEmbargoed = new Tag(project:project2, codeName:"A69-1303-4444", codeMap:"A69-1303", pingCode:4444)

        sensorNonEmbargoed = new Sensor(project:project1, tag:tagNonEmbargoed, codeName:"sensor-1111", codeMap:"sensor", pingCode:1111)
        sensorEmbargoedReadableProject = new Sensor(project:project1, tag:tagEmbargoedReadableProject, codeName:"sensor-2222", codeMap:"sensor", pingCode:2222)
        sensorEmbargoedNonReadableProject = new Sensor(project:project2, tag:tagEmbargoedNonReadableProject, codeName:"sensor-3333", codeMap:"sensor", pingCode:3333)
        sensorPastEmbargoed = new Sensor(project:project2, tag:tagPastEmbargoed, codeName:"sensor-4444", codeMap:"sensor", pingCode:4444)

        releaseNonEmbargoed = new AnimalRelease(project:project1)
        releaseEmbargoedReadableProject = new AnimalRelease(project:project1, embargoDate:nextYear())
        releaseEmbargoedNonReadableProject = new AnimalRelease(project:project2, embargoDate:nextYear())
        releasePastEmbargoed = new AnimalRelease(project:project2, embargoDate:lastYear())

        Surgery surgeryNonEmbargoed = new Surgery(tag:tagNonEmbargoed, release:releaseNonEmbargoed)
        Surgery surgeryEmbargoedReadableProject = new Surgery(tag:tagEmbargoedReadableProject, release:releaseEmbargoedReadableProject)
        Surgery surgeryEmbargoedNonReadableProject = new Surgery(tag:tagEmbargoedNonReadableProject, release:releaseEmbargoedNonReadableProject)
        Surgery surgeryPastEmbargoed = new Surgery(tag:tagPastEmbargoed, release:releasePastEmbargoed)

        detectionNonEmbargoed = new ValidDetection()
        detectionEmbargoedReadableProject = new ValidDetection()
        detectionEmbargoedNonReadableProject = new ValidDetection()
        detectionPastEmbargoed = new ValidDetection()

        DetectionSurgery detectionSurgeryNonEmbargoed = new DetectionSurgery(surgery:surgeryNonEmbargoed, detection:detectionNonEmbargoed, tag:tagNonEmbargoed)
        DetectionSurgery detectionSurgeryEmbargoedReadableProject = new DetectionSurgery(surgery:surgeryEmbargoedReadableProject, detection:detectionEmbargoedReadableProject, tag:tagEmbargoedReadableProject)
        DetectionSurgery detectionSurgeryEmbargoedNonReadableProject = new DetectionSurgery(surgery:surgeryEmbargoedNonReadableProject, detection:detectionEmbargoedNonReadableProject, tag:tagEmbargoedNonReadableProject)
        DetectionSurgery detectionSurgeryPastEmbargoed = new DetectionSurgery(surgery:surgeryPastEmbargoed, detection:detectionPastEmbargoed, tag:tagPastEmbargoed)

        
        def tagList =     [tagNonEmbargoed,     tagEmbargoedReadableProject,     tagEmbargoedNonReadableProject,     tagPastEmbargoed]
        def sensorList =  [sensorNonEmbargoed,  sensorEmbargoedReadableProject,  sensorEmbargoedNonReadableProject,  sensorPastEmbargoed]
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
        tagNonEmbargoed.addToDetectionSurgeries(detectionSurgeryNonEmbargoed)
        detectionNonEmbargoed.addToDetectionSurgeries(detectionSurgeryNonEmbargoed)
        
        releaseEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSensors(sensorEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedReadableProject)
        detectionEmbargoedReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedReadableProject)
        
        releaseEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSensors(sensorEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedNonReadableProject)
        detectionEmbargoedNonReadableProject.addToDetectionSurgeries(detectionSurgeryEmbargoedNonReadableProject)
        
        releasePastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSurgeries(surgeryPastEmbargoed)
        tagPastEmbargoed.addToSensors(sensorPastEmbargoed)
        tagPastEmbargoed.addToDetectionSurgeries(detectionSurgeryPastEmbargoed)
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
    
    void testNullFilter()
    {
        def results = reportQueryExecutorService.executeQuery(Project, null)
        assertEquals(2, results.size())
    }
    
    void testMapFilterParam()
    {
        def filterParams = [project:[name:"Seal Count"]]
        assertTrue(reportQueryExecutorService.isMap(filterParams.project))
        
        filterParams = [name:"Seal Count"]
        assertFalse(reportQueryExecutorService.isMap(filterParams.name))

        filterParams = [name:""]
        assertFalse(reportQueryExecutorService.isMap(filterParams.name))

        filterParams = [name:null]
        assertFalse(reportQueryExecutorService.isMap(filterParams.name))
    }

    void testLeafFilterParam()
    {
        def filterParams = [project:[name:"Seal Count"]]
        assertFalse(reportQueryExecutorService.isLeaf("project", filterParams.project))
        
        filterParams = [name:"Seal Count"]
        assertTrue(reportQueryExecutorService.isLeaf("name", filterParams.name))

        filterParams = [name:""]
        assertFalse(reportQueryExecutorService.isLeaf("name", filterParams.name))

        filterParams = [name:null]
        assertFalse(reportQueryExecutorService.isLeaf("name", filterParams.name))

        filterParams = ["project.name":[project:"sdfg"]]
        filterParams.each
        {
            key, value ->
    
            assertFalse(reportQueryExecutorService.isLeaf(key, value))
        }
    }
    
    void testTagEmbargoFiltering()
    {
        // Check permissions are behaving correctly.
        assertTrue(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project1.id)))
        assertFalse(SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectReadPermission(project2.id)))
        
        def tags = reportQueryExecutorService.executeQuery(Tag.class, [:])
        assertEquals(3, tags.size())
        assertTrue(tags.contains(tagNonEmbargoed))
        assertTrue(tags.contains(tagEmbargoedReadableProject))
        assertFalse(tags.contains(tagEmbargoedNonReadableProject))
        assertTrue(tags.contains(tagPastEmbargoed))
    }
    
    void testDetectionEmbargoFiltering()
    {
        
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
