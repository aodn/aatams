package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.ValidDetection
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import org.joda.time.*

class ReportInfoServiceTests extends AbstractGrailsUnitTestCase 
{
    def permissionUtilsService
    def reportInfoService
	Person user
	Project project1
	
	Person authUser
	
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(ReportInfoService, true)
        reportInfoService = new ReportInfoService()
        
        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()
        reportInfoService.permissionUtilsService = permissionUtilsService
        
        // Create a couple of projects and installations.
        project1 = new Project(name: "project 1")
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

        user = new Person(username: 'user')
		hasRole = true
		authUser = user
		
        // Need this for "findByUsername()" etc.
        mockDomain(Person, [user])
        user.save()
		
		mockDomain(CaabSpecies)
		mockDomain(InstallationStation)
		mockDomain(ReceiverEvent)
		mockDomain(Tag)
		
		ValidDetection detFirst = new ValidDetection(timestamp:new DateTime("2011-03-01T12:34:56").toDate())
		ValidDetection detLast = new ValidDetection(timestamp:new DateTime("2012-03-01T12:34:56").toDate())
		def detectionList = [detFirst, detLast]
		mockDomain(ValidDetection, detectionList)
		detectionList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

	protected def getPrincipal()
	{
		return authUser?.username
	}
	
	protected boolean isPermitted(permission)
	{
		if (permission == "project:" + project1.id + ":read")
		{
			return true
		}
		
		return false
	}
	
    void testGetReportInfoReceiver() 
    {
        def reportInfos = reportInfoService.getReportInfo()
        
        assertEquals(9, reportInfos.size())

        ReportInfo receiverReportInfo = reportInfos.get(Receiver.class)
        
        assertNotNull(receiverReportInfo)
        assertEquals("Receivers", receiverReportInfo.getDisplayName())
        assertEquals("receiverList", receiverReportInfo.getJrxmlFilename()["report"])
        assertEquals("receiverExtract", receiverReportInfo.getJrxmlFilename()["extract"])
    }
    
    void testGetReportInfoInstallationStation()
    {
        ReportInfo stationReportInfo = reportInfoService.getReportInfo("installationStation")
        assertNotNull(stationReportInfo)
        assertEquals("Installation Stations", stationReportInfo.displayName)

        def filterParams = stationReportInfo.filterParams
        assertNotNull(filterParams)
        assertEquals("project", filterParams[0].label)
        assertEquals("installation.project.name", filterParams[0].propertyName)
        assertEquals(ReportInfoService.MEMBER_PROJECTS, filterParams[0].range[0])
        assertTrue(filterParams[0].range.contains("project 1"))
        assertTrue(filterParams[0].range.contains("project 2"))
        
    }
    
    void testGetReportInfoInstallationStationLoggedIn()
    {
        ReportInfo stationReportInfo = reportInfoService.getReportInfo("installationStation")
        def filterParams = stationReportInfo.filterParams
        assertNotNull(filterParams)
        assertEquals(ReportInfoService.MEMBER_PROJECTS, filterParams[0].range[0])
    }
     
    void testGetReportInfoInstallationStationNotLoggedIn()
    {
		authUser = null
		authenticated = false
		
        ReportInfo stationReportInfo = reportInfoService.getReportInfo("installationStation")
        def filterParams = stationReportInfo.filterParams
        assertNotNull(filterParams)
        assertFalse(filterParams[0].range.contains(ReportInfoService.MEMBER_PROJECTS))
    }
    
    void testGetReportInfoDetection() 
    {
		
        def reportInfos = reportInfoService.getReportInfo()
        
        assertEquals(9, reportInfos.size())

        ReportInfo detectionReportInfo = reportInfos.get(ValidDetection.class)
        
        assertNotNull(detectionReportInfo)
        assertEquals("Detections", detectionReportInfo.getDisplayName())
        assertEquals("detectionExtract", detectionReportInfo.getJrxmlFilename()["extract"])
		
		def filterParams = detectionReportInfo.filterParams
		assertNotNull(filterParams)
		
		// Project
		assertEquals("project", filterParams[0].label)
		assertTrue(filterParams[0] instanceof AjaxMultiSelectReportParameter)
		assertEquals("receiverDeployment.station.installation.project.name", filterParams[0].propertyName)
		assertEquals('/project/lookupByName', filterParams[0].lookupPath)
		assertEquals("/report/filter/ajaxMultiSelectTemplate", filterParams[0].template)
		
   		assertEquals("installation", filterParams[1].label)
		assertEquals("receiverDeployment.station.installation.name", filterParams[1].propertyName)

   		assertEquals("station", filterParams[2].label)
		assertEquals("receiverDeployment.station.name", filterParams[2].propertyName)

   		assertEquals("timestamp", filterParams[5].label)
		assertEquals("timestamp", filterParams[5].propertyName)
		assertTrue(filterParams[5] instanceof DateRangeReportParameter)
		assertEquals(new DateTime("2011-03-01T12:34:56").toDate(), filterParams[5].minRange)
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
