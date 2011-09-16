package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

class ReportQueryExecutorServiceTests extends GrailsUnitTestCase
{
    def embargoService
    def permissionUtilsService
    def reportQueryExecutorService
    
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
        Project project1 = Project.build(name: "project 1")
        Project project2 = Project.build(name: "project 2")
        def projectList = [project1, project2]
        projectList.each{ it.save()}
        
        InstallationConfiguration config = InstallationConfiguration.buildLazy(type:"CURTAIN").save()
        
        Installation installation1 = Installation.build(name: "installation 1",
                                                        project: project1,
                                                        configuration:config)
        Installation installation2 = Installation.build(name: "installation 2",
                                                        project: project2,
                                                        configuration:config)
        def installationList = [installation1, installation2]
        installationList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testExecuteQueryNullFilter()
    {
        def results = reportQueryExecutorService.executeQuery(Project, null)
        assertEquals(5, results.size())
    }
    
    void testExecuteQueryFilterByProjectName()
    {
        def results = reportQueryExecutorService.executeQuery(Project, ["name":"project 1"])
        
        assertEquals(1, results.size())
        assertEquals("project 1", results[0].name)
    }
    
    /**
     * An indirect filter parameter is one that refers to an associated entity
     * property, e.g. the associated project's name.
     */
    void testExecuteQueryIndirectFilter()
    {
        def filterParams = [project:[name: "project 1"]]
        def results = 
            reportQueryExecutorService.executeQuery(Installation, filterParams)
            
        assertEquals(1, results.size())
        assertEquals("installation 1", results[0].name)
    }
}
