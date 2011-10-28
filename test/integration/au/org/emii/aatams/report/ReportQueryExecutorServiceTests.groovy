package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*

import org.joda.time.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class ReportQueryExecutorServiceTests extends GrailsUnitTestCase
{
    def embargoService
    def permissionUtilsService
    def reportQueryExecutorService
    def searchableService
    
    Installation installation1
    Installation installation2
    Installation installation3
    
    Person user
    
    ProjectRole project1Pi
    ProjectRole project2Pi
    
    protected void setUp() 
    {
        super.setUp()
        
        // Hibernate is getting its knickers in a knot for some reason in this test, so turn off mirroring:
        // org.hibernate.HibernateException: Found two representations of same collection: shiro.SecUser.permissions
        searchableService.stopMirroring()
     
        // Create some projects and installations.
        Person requester = Person.findByUsername("jkburges")
        Project project1 = new Project(name: "project 1", description: "desc", status:EntityStatus.ACTIVE, requestingUser:requester)
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
        
        def imos = Organisation.findByName('IMOS')
        user = new Person(username: 'user', 
                                   passwordHash: 'password',
                                   name: "A User",
                                   emailAddress:"auser@auser.com",
                                   phoneNumber:"1234",
                                   status:EntityStatus.ACTIVE,
                                   organisation:imos,
                                   defaultTimeZone:DateTimeZone.forID("Australia/Hobart"))
        def subject = [ getPrincipal: { user.username },
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
        
        // Add roles on project 1 and 2, but not 3
        ProjectRoleType piRoleType = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
       
        project1Pi = new ProjectRole(project:project1, person:user, roleType:piRoleType, access:ProjectAccess.READ_ONLY)
        project2Pi = new ProjectRole(project:project2, person:user, roleType:piRoleType, access:ProjectAccess.READ_WRITE)
        def projectRoleList = [project1Pi, project2Pi]
        
        projectRoleList.each { user.addToProjectRoles(it) }
        user.save()
    }

    protected void tearDown() 
    {
        searchableService.startMirroring()
        super.tearDown()
    }

    void testExecuteQueryNullFilter()
    {
        def results = reportQueryExecutorService.executeQuery(Project, null)
        assertEquals(6, results.size())
    }
    
    void testExecuteQueryFilterByProjectName()
    {
        def results = reportQueryExecutorService.executeQuery(Project, ["name":"project 1"])
        
        assertEquals(1, results.size())
        assertEquals("project 1", results[0].name)
    }
    
    void testExecuteQueryInstallationNullFilter()
    {
        def results = reportQueryExecutorService.executeQuery(Installation, null)
        assertEquals(6, results.size())
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

    void testExecuteQueryFilterByMemberProjects()
    {
        def filterParams = [project:[name: ReportInfoService.MEMBER_PROJECTS]]
        def results = 
            reportQueryExecutorService.executeQuery(Installation, filterParams)
            
        assertEquals(2, results.size())
        assertTrue(results.contains(installation1))
        assertTrue(results.contains(installation2))
    }
	
	void testDetectionNoFilter()
	{
		assertDetectionsMatchingFilter(16, [:])
	}
	
	void testDetectionFilterByProject()
	{
		assertDetectionsMatchingFilter(13, [receiverDeployment:[station:[installation:[project:[name:'Seal Count']]]]])
		assertDetectionsMatchingFilter(3, [receiverDeployment:[station:[installation:[project:[name:'Tuna']]]]])
	}
	void testDetectionFilterByInstallation()
	{
		assertDetectionsMatchingFilter(3, [receiverDeployment:[station:[installation:[name:"Ningaloo Array"]]]])
		assertDetectionsMatchingFilter(13, [receiverDeployment:[station:[installation:[name:"Bondi Line"]]]])
	}

	void testDetectionFilterByStation()
	{
		assertDetectionsMatchingFilter(3, [receiverDeployment:[station:[name:"Bondi SW2"]]])
		assertDetectionsMatchingFilter(10, [receiverDeployment:[station:[name:"Bondi SW1"]]])
		assertDetectionsMatchingFilter(0, [receiverDeployment:[station:[name:"Bondi SW1", installation:[name:"Ningaloo Array"]]]])
	}

	void testDetectionFilterByTagCodeName()
	{
		assertDetectionsMatchingFilter(10, [detectionSurgeries:[tag:[codeName:"A69-1303-62339"]]])
		assertDetectionsMatchingFilter(3, [detectionSurgeries:[tag:[codeName:"A69-1303-46601"]]])
	}
	
	private assertDetectionsMatchingFilter(int expectedNumDetections, filterParams) 
	{
		assertEquals(expectedNumDetections, reportQueryExecutorService.executeQuery(ValidDetection.class, filterParams).size())
	}
}
