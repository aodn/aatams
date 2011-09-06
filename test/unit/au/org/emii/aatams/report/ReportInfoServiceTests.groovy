package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

class ReportInfoServiceTests extends GrailsUnitTestCase 
{
    def reportInfoService
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(ReportInfoService, true)
        reportInfoService = new ReportInfoService()
        
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
        
        mockDomain(Organisation)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testGetReportInfo() 
    {
        def reportInfos = reportInfoService.getReportInfo()
        
        assertEquals(2, reportInfos.size())

        ReportInfo receiverReportInfo = reportInfos.get(Receiver.class)
        
        assertNotNull(receiverReportInfo)
        assertEquals("Receivers", receiverReportInfo.getDisplayName())
        assertEquals("receiverList", receiverReportInfo.getJrxmlFilename())
    }

    
    void testFilterParamsToReportFormat()
    {
        def filter = [user:"Jon Burgess", 
                      "organisation.name":"CSIRO", 
                      organisation:[name:"CSIRO"]]
    
        def result = reportInfoService.filterParamsToReportFormat(filter)
        assertEquals([user:"Jon Burgess", organisation:"CSIRO"], result)

        filter = [user:"Jon Burgess", 
                  "organisation":null, 
                  organisation:[name:null]]
    
        result = reportInfoService.filterParamsToReportFormat(filter)
        assertEquals([user:"Jon Burgess"], result)
        
        filter = [user:"Jon Burgess", 
                  "organisation":"", 
                  organisation:[name:""]]
    
        result = reportInfoService.filterParamsToReportFormat(filter)
        assertEquals([user:"Jon Burgess"], result)
    }
}
