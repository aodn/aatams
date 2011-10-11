package au.org.emii.aatams.report

import grails.test.*
import au.org.emii.aatams.*

class ReportInfoServiceTests extends GroovyTestCase
{
    def reportInfoService
    
    Project project1
    Project project2
    Installation installation1
    Installation installation2
    
    protected void setUp() 
    {
        super.setUp()

        reportInfoService = new ReportInfoService()
        
        // Create a couple of projects and installations.
        project1 = new Project(name: "project 1")
        project2 = new Project(name: "project 2")
        def projectList = [project1, project2]
        projectList.each{ it.save()}
        
        installation1 = new Installation(name: "installation 1")
        installation2 = new Installation(name: "installation 2")
        def installationList = [installation1, installation2]
        installationList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }
    
    void testSomething()
    {
        
    }
    
}
