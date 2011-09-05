package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

class ReportQueryExecutorServiceTests extends GrailsUnitTestCase 
{
    def reportQueryExecutorService
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(ReportQueryExecutorService, true)
        reportQueryExecutorService = new ReportQueryExecutorService()
        
        // Create a couple of projects and installations.
        Project project1 = new Project(name: "project 1")
        Project project2 = new Project(name: "project 2")
        def projectList = [project1, project2]
        mockDomain(Project, projectList)
        projectList.each{ it.save()}
        
        Installation installation1 = new Installation(name: "installation 1")
        Installation installation2 = new Installation(name: "installation 2")
        def installationList = [installation1, installation2]
        mockDomain(Installation, installationList)
        installationList.each { it.save() }
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
}
