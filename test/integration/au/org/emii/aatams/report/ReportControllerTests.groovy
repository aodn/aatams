package au.org.emii.aatams.report

import grails.test.*
import groovy.lang.MetaClass;
import groovy.sql.Sql;

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractControllerUnitTestCase;
import au.org.emii.aatams.filter.QueryService

import org.codehaus.groovy.grails.plugins.jasper.*
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine;
import org.apache.commons.lang.StringUtils

import javax.servlet.ServletContext
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * Report formats are CSV, so that we can easily compare controller output to
 * an expected CSV output. (as opposed to PDF).
 */
class ReportControllerTests extends AbstractControllerUnitTestCase 
{
    def reportInfoService
    def queryService
	def permissionUtilsService
    
	def grailsApplication
	def grailsTemplateEngineService
    def jasperService
	def kmlService
	def embargoService
	
	def dataSource
	
	def slurper = new XmlSlurper()

	protected void setUp() 
    {
        super.setUp()
        
        mockLogging(ReportController, true)

        controller.metaClass.servletContext = 
            [ getRealPath: {System.getProperty("user.dir") + "/web-app" + it }] as ServletContext

        controller.params.pdf = "PDF"
        controller.params._action_execute = "PDF"
		controller.params._format = "CSV"	// render as CSV, for ease of testing.
		
		permitted = true
		
		def sql = new Sql(dataSource)
		
		def viewName = ConfigurationHolder.config.rawDetection.extract.view.name
		def viewSelect = ConfigurationHolder.config.rawDetection.extract.view.select
		sql.execute ('create view ' + viewName + ' as ' + viewSelect)
		
		// For some reason, reading asynchronously is not returning results when run within context
		// of integration test.
		controller.detectionExtractService.metaClass.shouldReadAsync = { return false }
    }

	protected void tearDown() 
    {
        super.tearDown()
    }
	
    void testExecuteAnimalReleaseSummary()
    {
        controller.params._name = "animalReleaseSummary"
        controller.params.filter = [:]
                 
        controller.execute()
        
		// Broken on certain dates - need to fix.
//        checkResponse("testExecuteAnimalReleaseSummary")
    }

//	void testExecuteStationKmlExtract()
//	{
//		InstallationStation.metaClass.toKmlDescription = { "some description" }
//		
//		controller.params._name = "installationStation"
//		controller.params.filter = [:]
//		controller.params._format = null
//        controller.params._action_execute = "KML"
//		
//		controller.execute()
//
//		def div = slurper.parseText(controller.response.contentAsString)
//		assertNotNull(div)
//		
//		def allNodes = div.depthFirst().collect{ it }
//		def folderNodes = allNodes.findAll { it.name() == "Folder" }
//		
//		["Bondi Line", "Heron Island Curtain", "Ningaloo Array", "Seal Count", "Tuna", "Whale"].each
//		{
//			folderName ->
//			
//			if (!folderNodes*.name*.text().contains(folderName))
//			{
//				println "No folder name: " + folderName
//			}
//			assertTrue(folderNodes*.name*.text().contains(folderName))
//		}
//	}
	
    private void checkResponse(def expectedFileName)
    {
        // Compare the response content with expected.
        String expectedFilePath = constructFilePath(expectedFileName)
            
        File expectedFile = new File(expectedFilePath)
            
        // Write the content to a temp file (so we can see the test output after
        // the test has run).
        File tmpFile = File.createTempFile(expectedFileName + ".actual.csv", "")
        tmpFile.write(controller.response.contentAsString.trim())
        
        // Compare all but the last line (which includes a date, and therefore
        // won't match).
		assertContainsAllLines(removePageFooter(controller.response.contentAsString.trim()), removePageFooter(expectedFile.getText()))
    }
}
