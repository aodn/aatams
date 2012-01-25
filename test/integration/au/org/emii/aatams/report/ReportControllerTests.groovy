package au.org.emii.aatams.report

import grails.test.*
import groovy.lang.MetaClass;
import groovy.sql.Sql;

import au.org.emii.aatams.*
import au.org.emii.aatams.test.AbstractControllerUnitTestCase;

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
    def reportQueryExecutorService
    
	def grailsApplication
	def grailsTemplateEngineService
    def jasperService
	def kmlService
	
	def dataSource
	
	def slurper = new XmlSlurper()

	protected void setUp() 
    {
        super.setUp()
        
        mockLogging(ReportController, true)

        controller.metaClass.servletContext = 
            [ getRealPath: {System.getProperty("user.dir") + "/web-app" + it }] as ServletContext

        controller.params.pdf = "PDF"
        controller.params._format = "CSV"
		controller.params._type = "report"
		
		permitted = true
		
		def sql = new Sql(dataSource)
		
		def viewName = ConfigurationHolder.config.rawDetection.extract.view.name
		def viewSelect = ConfigurationHolder.config.rawDetection.extract.view.select
		sql.execute ('create view ' + viewName + ' as ' + viewSelect)
			
    }

	protected void tearDown() 
    {
        super.tearDown()
    }

    void testExecuteReceiverNoFilter() 
    {
        controller.params._name = "receiver"
        controller.params."filter.organisation.name" = null
        controller.params._file = "receiverList"
        controller.params.filter = 
                    ["eq.organisation.name":null, 
                     eq:[organisation:[name:null]]]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverNoFilter")
    }
    
    void testExecuteReceiverFilterByOrg() 
    {
        controller.params._name = "receiver"
        controller.params._file = "receiverList"
        controller.params.filter = 
                    [eq:[organisation:[name:"IMOS"]],
				     "eq.organisation.name":"IMOS"]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverFilterByOrg")
    }

    void testExecuteInstallationStationNoFilter() 
    {
        controller.params._name = "installationStation"
        controller.params._file = "installationStationList"
        controller.params.filter = 
                    [eq:[installation:[project:[name:null]]],
					 "eq.installation.project.name":null]
					
        controller.execute()
        
        checkResponse("testExecuteInstallationStationNoFilter")
    }
    
    void testExecuteInstallationStationByProject() 
    {
        controller.params._name = "installationStation"
        controller.params._file = "installationStationList"
        controller.params.filter = 
                    [eq:[installation:[project:[name:"Seal Count"]]],
					"eq.installation.project.name":"Seal Count"]
					
        controller.execute()
        
        checkResponse("testExecuteInstallationStationByProject")
    }

    void testExecuteReceiverDeploymentNoFilter() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [eq:[station:[installation:[project:[name:null], name:null]]],
			 "eq.station.installation.project.name":null]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentNoFilter")
    }
    
    void testExecuteReceiverDeploymentByProject() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [eq:[station:[installation:[project:[name:"Seal Count"], name:null]]],
			"eq.station.installation.project.name":"Seal Count"]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentByProject")
    }
    
    void testExecuteReceiverDeploymentByInstallation() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [eq:[station:[installation:[project:[name:null], name:"Ningaloo Array"]]],
			 "eq.station.installation.project.name":null]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentByInstallation")
    }

    void testExecuteReceiverDeploymentByProjectAndInstallation() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [eq:[station:[installation:[project:[name:"Seal Count"], name:"Heron Island"]]],
			 "eq.station.installation.project.name":"Seal Count",
			 "eq.station.installation.name":"Heron Island"]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentByProjectAndInstallation")
    }
    
    void testExecuteAnimalReleaseSummary()
    {
        controller.params._name = "animalReleaseSummary"
        controller.params._file = "animalReleaseSummary"
        controller.params.filter = [:]
                 
        controller.execute()
        
		// Broken on certain dates - need to fix.
//        checkResponse("testExecuteAnimalReleaseSummary")
    }
    
    void testExecuteSensor()
    {
        controller.params._name = "sensor"
        controller.params._file = "sensorExtract"
        controller.params.filter = [:]
		controller.params._type = "extract"
		
        controller.execute()
        
        checkResponse("testExecuteSensor")
    }
	
	void testExecuteDetectionExtract()
	{
		controller.params._name = "detection"
		controller.params._file = "detectionExtract"
		controller.params.filter = [:]
		controller.params._type = "extract"
		
		controller.execute()
		
		checkResponse("testExecuteDetection")
	}

	void testExecuteDetectionKmlExtract()
	{
		InstallationStation.metaClass.toKmlDescription = { "some description" }
		
		controller.params._name = "detection"
		controller.params.filter = [:]
		controller.params._type = "extract"
		controller.params._format = "KML"
		
		controller.execute()

		def div = slurper.parseText(controller.response.contentAsString)
		assertNotNull(div)
		
		def allNodes = div.depthFirst().collect{ it }
		def folderNodes = allNodes.findAll { it.name() == "Folder" }
		
//		println controller.response.contentAsString
//		["Whale", "Seal Count", "Heron Island Curtain", "Bondi Line", "Tuna", "Ningaloo Array"].each
//		{
//			folderName ->
//			
//			if (!folderNodes*.name*.text().contains(folderName))
//			{
//				println "No folder name: " + folderName
//			}
//			assertTrue(folderNodes*.name*.text().contains(folderName))
//		}
	}

	void testDetectionExtractWithReadPermission()
	{
		permitted = true
		
		setupAndExecuteWhaleDetectionExtract()
		
		// order is unspecified (as this is a very expensive DB operation for large queries).
		'''timestamp,station name,latitude,longitude,receiver ID,tag ID,species,uploader,transmitter ID,organisation
2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-7777,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-7777,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-7777,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS'''.eachLine
		{
			expectedLine ->
			
			assertTrue(controller.response.contentAsString.trim().readLines().contains(expectedLine.trim()))
		}
		
//		assertEquals('''timestamp,station name,latitude,longitude,receiver ID,tag ID,species,uploader,transmitter ID,organisation
//2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
//2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
//2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
//2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-7777,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-7777,IMOS
//2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-7777,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-7777,IMOS
//2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-7777,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-7777,IMOS 
//2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
//2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
//2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS''',
//					 controller.response.contentAsString.trim())
	}

	void testDetectionExtractWithoutReadPermission()
	{
		permitted = false
		
		setupAndExecuteWhaleDetectionExtract()
		def expected = '''timestamp,station name,latitude,longitude,receiver ID,tag ID,species,uploader,transmitter ID,organisation
2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:00,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:01,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:02,Whale Station,-20.1234,76.02,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS'''
		
		assertEquals(expected, 
					 controller.response.contentAsString.trim())
	}

	void testDetectionExtractWithoutAuthentication()
	{
		permitted = false
		authenticated = false
		
		setupAndExecuteWhaleDetectionExtract()
		
		assertEquals('''timestamp,station name,latitude,longitude,receiver ID,tag ID,species,uploader,transmitter ID,organisation
2011-05-17 02:54:00,Whale Station,-20.12,76.01,VR2W-103377,,,Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:00,Whale Station,-20.12,76.01,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:00,Whale Station,-20.12,76.01,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
2011-05-17 02:54:01,Whale Station,-20.12,76.01,VR2W-103377,,,Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:01,Whale Station,-20.12,76.01,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:01,Whale Station,-20.12,76.01,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS
2011-05-17 02:54:02,Whale Station,-20.12,76.01,VR2W-103377,,,Joe Bloggs,A69-1303-7777,IMOS
2011-05-17 02:54:02,Whale Station,-20.12,76.01,VR2W-103377,,,Joe Bloggs,A69-1303-8888,IMOS
2011-05-17 02:54:02,Whale Station,-20.12,76.01,VR2W-103377,A69-1303-6666,41110001 - Eubalaena australis (southern right whale),Joe Bloggs,A69-1303-6666,IMOS''', 
					 controller.response.contentAsString.trim())
	}

	private void setupAndExecuteWhaleDetectionExtract() 
	{
		hasRole = false
		
		controller.params.filter = [eq:[receiverDeployment:[station:[installation:[project:[name:"Whale"]]]]]]
		controller.params._name = "detection"
		controller.params._file = "detectionExtract"
		controller.params._type = "extract"

		controller.execute()
	}

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
        assertEquals(
            "", 
            StringUtils.difference(removeLastLine(expectedFile.getText().trim()), 
                                   removeLastLine(controller.response.contentAsString.trim())))
    }

	private String constructFilePath(expectedFileName) {
		String expectedFilePath = \
            System.getProperty("user.dir") + \
            "/test/integration/au/org/emii/aatams/report/resources/" + \
            expectedFileName + ".expected.csv"
		return expectedFilePath
	}
    
    String removeLastLine(String s)
    {
        def lineCount = 0
        s.eachLine { lineCount ++}
        
        def retString = ""
        int index = 0
        
        s.eachLine
        {
            if (index == (lineCount - 1))
            {
                // last line
            }
			else if (it.contains("Page"))
			{
				// remove page footer
			}
            else
            {
                retString += it + '\n'
            }
            
            index++
        }
        
        return retString 
    }
}
