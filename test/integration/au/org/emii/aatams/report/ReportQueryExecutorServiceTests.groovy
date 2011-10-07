package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

import org.joda.time.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

class ReportQueryExecutorServiceTests extends GrailsUnitTestCase
{
    def embargoService
    def permissionUtilsService
    def reportQueryExecutorService
    
    Installation installation1
    Installation installation2
    
    protected void setUp() 
    {
        super.setUp()
     
        embargoService = new EmbargoService()
        mockLogging(PermissionUtilsService, true)
        permissionUtilsService = new PermissionUtilsService()
        embargoService.permissionUtilsService = permissionUtilsService
        
        reportQueryExecutorService = new ReportQueryExecutorService()
        reportQueryExecutorService.embargoService = embargoService
        reportQueryExecutorService.permissionUtilsService = permissionUtilsService
        
        // Create some projects and installations.
        Project project1 = Project.buildLazy(name: "project 1")
        Project project2 = Project.buildLazy(name: "project 2")
        Project project3 = Project.buildLazy(name: "project 3")
        def projectList = [project1, project2, project3]
        projectList.each{ it.save()}
        
        InstallationConfiguration config = InstallationConfiguration.buildLazy(type:"CURTAIN").save()
        
        installation1 = Installation.buildLazy(name: "installation 1",
                                                        project: project1,
                                                        configuration:config)
        installation2 = Installation.buildLazy(name: "installation 2",
                                                        project: project2,
                                                        configuration:config)
        Installation installation3 = Installation.buildLazy(name: "installation 3",
                                                        project: project3,
                                                        configuration:config)
        def installationList = [installation1, installation2, installation3]
        installationList.each { it.save() }
        
        Person user = Person.build(username: 'user', 
                                   name: "A User", 
                                   organisation:Organisation.build(name:"Some Org"),
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
        ProjectRoleType piRoleType = ProjectRoleType.buildLazy(displayName: ProjectRoleType.PRINCIPAL_INVESTIGATOR)
       
        ProjectRole project1Pi = ProjectRole.buildLazy(project:project1, person:user, roleType:piRoleType, access:ProjectAccess.READ_ONLY)
        ProjectRole project2Pi = ProjectRole.buildLazy(project:project2, person:user, roleType:piRoleType, access:ProjectAccess.READ_WRITE)
        def projectRoleList = [project1Pi, project2Pi]
        
        projectRoleList.each { user.addToProjectRoles(it) }
        user.save()
        
        println ("Roles: " + ProjectRole.list())
    }

    protected void tearDown() 
    {
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
}
