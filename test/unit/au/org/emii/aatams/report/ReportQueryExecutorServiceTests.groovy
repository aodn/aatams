package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class ReportQueryExecutorServiceTests extends GrailsUnitTestCase 
{
    def permissionUtilsService
    def reportQueryExecutorService
    
    Project project1
    Project project2

    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()
        
        mockLogging(ReportQueryExecutorService, true)
        reportQueryExecutorService = new ReportQueryExecutorService()
        
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
        
        // Tags which have an embargo date in the future and belong to non-readable project
        // should be filtered.
        Tag tagNonEmbargoed = new Tag(project:project1, codeName:"A69-1303-1111", codeMap:"A69-1303", pingCode:1111)
        AnimalRelease releaseNonEmbargoed = new AnimalRelease()
        Surgery surgeryNonEmbargoed = new Surgery(tag:tagNonEmbargoed, release:releaseNonEmbargoed)
        
        Tag tagEmbargoedReadableProject = new Tag(project:project1, codeName:"A69-1303-2222", codeMap:"A69-1303", pingCode:2222)
        AnimalRelease releaseEmbargoedReadableProject = new AnimalRelease()
        Surgery surgeryEmbargoedReadableProject = new Surgery(tag:tagEmbargoedReadableProject, release:releaseEmbargoedReadableProject)
        
        Tag tagEmbargoedNonReadableProject = new Tag(project:project2, codeName:"A69-1303-3333", codeMap:"A69-1303", pingCode:3333)
        AnimalRelease releaseEmbargoedNonReadableProject = new AnimalRelease()
        Surgery surgeryEmbargoedNonReadableProject = new Surgery(tag:tagEmbargoedNonReadableProject, release:releaseEmbargoedNonReadableProject)
        
        def tagList = [tagNonEmbargoed, tagEmbargoedReadableProject, tagEmbargoedNonReadableProject]
        def releaseList = [releaseEmbargoedNonReadableProject, releaseEmbargoedReadableProject, releaseNonEmbargoed]
        def surgeryList = [surgeryNonEmbargoed, surgeryEmbargoedReadableProject, surgeryEmbargoedNonReadableProject]
        
        mockDomain(Tag, tagList)
        mockDomain(AnimalRelease, releaseList)
        mockDomain(Surgery, surgeryList)
        
        releaseNonEmbargoed.addToSurgeries(surgeryNonEmbargoed)
        tagNonEmbargoed.addToSurgeries(surgeryNonEmbargoed)
        releaseEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        tagEmbargoedReadableProject.addToSurgeries(surgeryEmbargoedReadableProject)
        releaseEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        tagEmbargoedNonReadableProject.addToSurgeries(surgeryEmbargoedNonReadableProject)
        
        tagList.each { it.save() }
        releaseList.each { it.save() }
        surgeryList.each { it.save() }
        
        def tags = reportQueryExecutorService.executeQuery(Tag.class, [:])
        println(tags)
        assertEquals(2, tags.size())
        assertTrue(tags.contains(tagNonEmbargoed))
        assertTrue(tags.contains(tagEmbargoedReadableProject))
    }
    
    void testDetectionEmbargoFiltering()
    {
        
    }
    
}
