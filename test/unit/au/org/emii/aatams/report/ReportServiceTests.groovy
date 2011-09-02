package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

class ReportServiceTests extends GrailsUnitTestCase 
{
    def reportService
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(ReportService, true)
        reportService = new ReportService()
        
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

    void testGetReportInfo() 
    {
        def reportInfos = reportService.getReportInfo()
        
        assertEquals(1, reportInfos.size())

        ReportInfo receiverReportInfo = reportInfos.get(Receiver.class)
        
        assertNotNull(receiverReportInfo)
        assertEquals("Receivers", receiverReportInfo.getDisplayName())
        assertEquals("receiverList", receiverReportInfo.getJrxmlFilename())
        
        def projectFilterParam = receiverReportInfo.getFilterParams()[0]
        assertEquals("project", projectFilterParam.domainName)
        assertEquals("name", projectFilterParam.propertyName)
        assertEquals("project 1", projectFilterParam.range[0])
        assertEquals("project 2", projectFilterParam.range[1])
        
        def installationFilterParam = receiverReportInfo.getFilterParams()[1]
        assertEquals("installation", installationFilterParam.domainName)
        assertEquals("name", installationFilterParam.propertyName)
        assertEquals("installation 1", installationFilterParam.range[0])
        assertEquals("installation 2", installationFilterParam.range[1])
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
