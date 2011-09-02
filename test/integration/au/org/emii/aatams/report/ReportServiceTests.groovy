package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

class ReportServiceTests extends GroovyTestCase
{
    def reportService
    
    protected void setUp() 
    {
        super.setUp()
        
//        mockLogging(ReportService, true)
        reportService = new ReportService()
        
        // Create a couple of projects and installations.
        Project project1 = new Project(name: "project 1")
        Project project2 = new Project(name: "project 2")
        def projectList = [project1, project2]
//        mockDomain(Project, projectList)
        projectList.each{ it.save()}
        
        Installation installation1 = new Installation(name: "installation 1")
        Installation installation2 = new Installation(name: "installation 2")
        def installationList = [installation1, installation2]
//        mockDomain(Installation, installationList)
        installationList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testExecuteQueryNullFilter()
    {
        def results = reportService.executeQuery(Project, null)
        assertEquals(2, results.size())
    }
    
    void testExecuteQueryFilterByProjectName()
    {
        def results = reportService.executeQuery(Project, ["name":"project 1"])
        
        assertEquals(1, results.size())
        assertEquals("project 1", results[0].name)
    }
}
