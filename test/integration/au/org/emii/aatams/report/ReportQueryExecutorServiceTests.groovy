package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*
import au.org.emii.aatams.report.filter.*
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase;

import org.joda.time.*

class ReportQueryExecutorServiceTests extends AbstractGrailsUnitTestCase
{
    def embargoService
    def permissionUtilsService
	def reportFilterFactoryService
	def reportQueryExecutorService
    def searchableService
    
    Installation installation1
    Installation installation2
    Installation installation3
    
    ProjectRole project1Pi
    ProjectRole project2Pi
    
	Project project1
	
    protected void setUp() 
    {
        super.setUp()
        
        // Hibernate is getting its knickers in a knot for some reason in this test, so turn off mirroring:
        // org.hibernate.HibernateException: Found two representations of same collection: shiro.SecUser.permissions
        searchableService.stopMirroring()
     
        // Create some projects and installations.
        Person requester = Person.findByUsername("jkburges")
        project1 = new Project(name: "project 1", description: "desc", status:EntityStatus.ACTIVE, requestingUser:requester)
        Project project2 = new Project(name: "project 2", description: "desc", status:EntityStatus.ACTIVE, requestingUser:requester)
        Project project3 = new Project(name: "project 3", description: "desc", status:EntityStatus.ACTIVE, requestingUser:requester)
        def projectList = [project1, project2, project3]
        projectList.each{ it.save()}
        
        InstallationConfiguration config = InstallationConfiguration.findByType("CURTAIN")
        
        installation1 = new Installation(name: "installation 1",
                                         project: project1,
                                         configuration:config)
        installation2 = new Installation(name: "installation 2",
                                         project: project2,
                                         configuration:config)
        installation3 = new Installation(name: "installation 3",
                                         project: project3,
                                         configuration:config)
        def installationList = [installation1, installation2, installation3]
        installationList.each { it.save() }
        
        // Add roles on project 1 and 2, but not 3
        ProjectRoleType piRoleType = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
       
        project1Pi = new ProjectRole(project:project1, person:user, roleType:piRoleType, access:ProjectAccess.READ_ONLY)
        project2Pi = new ProjectRole(project:project2, person:user, roleType:piRoleType, access:ProjectAccess.READ_WRITE)
        def projectRoleList = [project1Pi, project2Pi]
        
        projectRoleList.each { user.addToProjectRoles(it) }
        user.save()
    }

	def customUser = null
	
	protected Person getUser()
	{
		if (!customUser)
		{
			def imos = Organisation.findByName('IMOS')
			
			customUser = new Person(username: 'user',
			passwordHash: 'password',
			name: "A User",
			emailAddress:"auser@auser.com",
			phoneNumber:"1234",
			status:EntityStatus.ACTIVE,
			organisation:imos,
			defaultTimeZone:DateTimeZone.forID("Australia/Hobart"))
		}
		
		return customUser
	}
	
	protected boolean isPermitted(String permission)
	{
		if (permission == "project:" + project1.id + ":read")
		{
		    return true
		}
		
		return false
	}
	
    protected void tearDown() 
    {
        searchableService.startMirroring()
        super.tearDown()
    }

    void testExecuteQueryNullFilter()
    {
		assertReportFilter(6, Project)
    }
    
    void testExecuteQueryFilterByProjectName()
    {
		def results = assertReportFilter(1, Project, [name:"project 1"])
        assertEquals("project 1", results[0].name)
    }

    void testExecuteQueryInstallationNullFilter()
    {
		assertReportFilter(6, Installation)
    }
   
    void testExecuteQueryIndirectFilter()
    {
		def results = assertReportFilter(1, Installation, [project:[name: "project 1"]])
        assertEquals("installation 1", results[0].name)
    }

    void testExecuteQueryFilterByMemberProjects()
    {
		def results = assertReportFilter(2, Installation, [project:[name: ReportInfoService.MEMBER_PROJECTS]])
		assertTrue(results.contains(installation1))
        assertTrue(results.contains(installation2))
    }
	
	void testDetectionNoFilter()
	{
		assertDetectionsMatchingFilter(22, [:])
	}
	
	void testDetectionFilterByProject()
	{
		assertDetectionsMatchingFilter(19, [receiverDeployment:[station:[installation:[project:[name:'Seal Count']]]]])
		assertDetectionsMatchingFilter(3, [receiverDeployment:[station:[installation:[project:[name:'Tuna']]]]])
	}
	
	void testDetectionFilterByProjectAndEmptyDetectionSurgeries()
	{
		assertDetectionsMatchingFilter(19, [receiverDeployment:[station:[installation:[project:[name:'Seal Count']]]], detectionSurgeries:['surgery.release.animal.species.spcode':'']])
		assertDetectionsMatchingFilter(3, [receiverDeployment:[station:[installation:[project:[name:'Tuna']]]], detectionSurgeries:['surgery.release.animal.species.spcode':'']])
	}
	
	void testDetectionFilterByInstallation()
	{
		assertDetectionsMatchingFilter(3, [receiverDeployment:[station:[installation:[name:"Ningaloo Array"]]]])
		assertDetectionsMatchingFilter(16, [receiverDeployment:[station:[installation:[name:"Bondi Line"]]]])
	}

	void testDetectionFilterByStation()
	{
		assertDetectionsMatchingFilter(3, [receiverDeployment:[station:[name:"Bondi SW2"]]])
		assertDetectionsMatchingFilter(13, [receiverDeployment:[station:[name:"Bondi SW1"]]])
		assertDetectionsMatchingFilter(0, [receiverDeployment:[station:[name:"Bondi SW1", installation:[name:"Ningaloo Array"]]]])
	}

	void testDetectionFilterByTagCodeName()
	{
		assertDetectionsMatchingFilter(10, [detectionSurgeries:[sensor:[transmitterId:"A69-1303-62339"]]])
		assertDetectionsMatchingFilter(6, [detectionSurgeries:[sensor:[transmitterId:"A69-1303-46601"]]])
	}
	
	void testDetectionFilterBySpeciesCommonName()
	{
		assertDetectionsMatchingFilter(10, [detectionSurgeries:[surgery:[release:[animal:[species:[commonName:"White Shark"]]]]]])
		assertDetectionsMatchingFilter(9, [detectionSurgeries:[surgery:[release:[animal:[species:[commonName:"Southern Bluefin Tuna"]]]]]])
		assertDetectionsMatchingFilter(0, [detectionSurgeries:[surgery:[release:[animal:[species:[commonName:"Blue-eye Trevalla"]]]]]])
	}
	
	void testDetectionFilterBySpeciesScientificName()
	{
		assertDetectionsMatchingFilter(10, [detectionSurgeries:[surgery:[release:[animal:[species:[scientificName:"Carcharodon carcharias"]]]]]])
		assertDetectionsMatchingFilter(9, [detectionSurgeries:[surgery:[release:[animal:[species:[scientificName:"Thunnus maccoyii"]]]]]])
		assertDetectionsMatchingFilter(0, [detectionSurgeries:[surgery:[release:[animal:[species:[scientificName:"Hyperoglyphe antarctica"]]]]]])
	}
	
	void testDetectionFilterBySpeciesCaabCode()
	{
		assertDetectionsMatchingFilter(10, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37010003"]]]]]])
		assertDetectionsMatchingFilter(9, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37441004"]]]]]])
		assertDetectionsMatchingFilter(0, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37445001"]]]]]])
	}
	
	void testDetectionFilterByTimestamp()
	{
		assertDetectionsMatchingFilter(5, [timestamp:new DateTime("2011-05-17T12:54:00").toDate()])
		assertDetectionsMatchingFilter(1, [timestamp:new DateTime("2011-05-17T12:54:03").toDate()])
	}
	
	void testDetectionFilterEmpty()
	{
		ReportFilter filter = new ReportFilter(ValidDetection)
		assertDetectionsMatchingFilter(22, filter)	
	}
	
	void testDetectionFilterByEqualsSpeciesCommonName()
	{
		assertDetectionsMatchingEqualsFilter(10, [detectionSurgeries:[surgery:[release:[animal:[species:[commonName:"White Shark"]]]]]])
		assertDetectionsMatchingEqualsFilter(9, [detectionSurgeries:[surgery:[release:[animal:[species:[commonName:"Southern Bluefin Tuna"]]]]]])
		assertDetectionsMatchingEqualsFilter(0, [detectionSurgeries:[surgery:[release:[animal:[species:[commonName:"Blue-eye Trevalla"]]]]]])
	}
	
	void testDetectionFilterByEqualsSpeciesScientificName()
	{
		assertDetectionsMatchingEqualsFilter(10, [detectionSurgeries:[surgery:[release:[animal:[species:[scientificName:"Carcharodon carcharias"]]]]]])
		assertDetectionsMatchingEqualsFilter(9, [detectionSurgeries:[surgery:[release:[animal:[species:[scientificName:"Thunnus maccoyii"]]]]]])
		assertDetectionsMatchingEqualsFilter(0, [detectionSurgeries:[surgery:[release:[animal:[species:[scientificName:"Hyperoglyphe antarctica"]]]]]])
	}
	
	void testDetectionFilterByEqualsSpeciesCaabCode()
	{
		assertDetectionsMatchingEqualsFilter(10, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37010003"]]]]]])
		assertDetectionsMatchingEqualsFilter(9, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37441004"]]]]]])
		assertDetectionsMatchingEqualsFilter(0, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37445001"]]]]]])
	}
	
	private void assertDetectionsMatchingEqualsFilter(expectedCount, filterParams)
	{
		ReportFilter filter = new ReportFilter(ValidDetection)
		filter.addCriterion(new EqualsReportFilterCriterion(filterParams))
		assertDetectionsMatchingFilter(expectedCount, filter)
	}
	
	void testDetectionFilterByInSpeciesCommonName()
	{
		assertDetectionsMatchingInFilter(19, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37010003, 37441004"]]]]]])
		assertDetectionsMatchingInFilter(9, [detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37445001, 37441004"]]]]]])
	}

	private void assertDetectionsMatchingInFilter(expectedCount, filterParams)
	{
		ReportFilter filter = new ReportFilter(ValidDetection)
		filter.addCriterion(new InReportFilterCriterion(filterParams))
		assertDetectionsMatchingFilter(expectedCount, filter)
	}
	
	void testDetectionFilterByBetweenTimestamp()
	{
		assertDetectionsMatchingBetweenFilter(15, [timestamp: [new DateTime("2011-05-17T12:54:00").toDate(), new DateTime("2011-05-17T12:54:02").toDate()]])
		assertDetectionsMatchingBetweenFilter(2, [timestamp: [new DateTime("2011-05-17T12:54:03").toDate(), new DateTime("2011-05-17T12:54:04").toDate()]])
	}
	
	private void assertDetectionsMatchingBetweenFilter(expectedCount, filterParams)
	{
		ReportFilter filter = new ReportFilter(ValidDetection)
		filter.addCriterion(new BetweenReportFilterCriterion(filterParams))
		assertDetectionsMatchingFilter(expectedCount, filter)
	}
	
	void testDetectionFilterByEqualsSpeciesCaabCodeAndBetweenTimestamp()
	{
		ReportFilter filter = new ReportFilter(ValidDetection)
		filter.addCriterion(new EqualsReportFilterCriterion([detectionSurgeries:[surgery:[release:[animal:[species:[spcode:"37010003"]]]]]]))
		filter.addCriterion(new BetweenReportFilterCriterion([timestamp: [new DateTime("2011-05-17T12:54:00").toDate(), new DateTime("2011-05-17T12:54:02").toDate()]]))
		assertDetectionsMatchingFilter(3, filter)
	}
	
	private void assertDetectionsMatchingFilter(int expectedNumDetections, Map filterParams) 
	{
		assertReportFilter(expectedNumDetections, ValidDetection, filterParams)
	}

	private void assertDetectionsMatchingFilter(int expectedNumDetections, ReportFilter filterParams) 
	{
		assertEquals(expectedNumDetections, reportQueryExecutorService.executeQuery(filterParams).size())
	}
	
	private List assertReportFilter(expectedResultCount, clazz, params) 
	{
		ReportFilter filter = reportFilterFactoryService.newFilter(clazz, [eq:params])
//		filter.addCriterion(new EqualsReportFilterCriterion(params))
		def results = reportQueryExecutorService.executeQuery(filter)
		assertEquals(expectedResultCount, results.size())
		return results
	}
    
	private List assertReportFilter(expectedResultCount, clazz) 
	{
		ReportFilter filter = new ReportFilter(clazz)
		def results = reportQueryExecutorService.executeQuery(filter)
		assertEquals(expectedResultCount, results.size())
		return results
	}
}
