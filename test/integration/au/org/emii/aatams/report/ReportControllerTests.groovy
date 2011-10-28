package au.org.emii.aatams.report

import grails.test.*

import au.org.emii.aatams.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils
 
import org.codehaus.groovy.grails.plugins.jasper.*
import org.apache.commons.lang.StringUtils

import javax.servlet.ServletContext

/**
 * Report formats are CSV, so that we can easily compare controller output to
 * an expected CSV output. (as opposed to PDF).
 */
class ReportControllerTests extends ControllerUnitTestCase 
{
    def reportInfoService
    def reportQueryExecutorService
    
    def jasperService
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(ReportController, true)
        
        /**
         * Setup security manager.
         */
        def subject = [ getPrincipal: { "jkburges" },
                        isAuthenticated: { true },
                        isPermitted: { true }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
        
        controller.metaClass.servletContext = 
            [ getRealPath: {System.getProperty("user.dir") + "/web-app" + it }] as ServletContext

        controller.params.pdf = "PDF"
        controller.params._format = "CSV"
		controller.params._type = "report"
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
                    ["organisation.name":null, 
                     organisation:[name:null]]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverNoFilter")
    }
    
    void testExecuteReceiverFilterByOrg() 
    {
        controller.params._name = "receiver"
        controller.params._file = "receiverList"
        controller.params.filter = 
                    [organisation:[name:"IMOS"]]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverFilterByOrg")
    }

    void testExecuteInstallationStationNoFilter() 
    {
        controller.params._name = "installationStation"
        controller.params._file = "installationStationList"
        controller.params.filter = 
                    [installation:[project:[name:null]]] 
                 
        controller.execute()
        
        checkResponse("testExecuteInstallationStationNoFilter")
    }
    
    void testExecuteInstallationStationByProject() 
    {
        controller.params._name = "installationStation"
        controller.params._file = "installationStationList"
        controller.params.filter = 
                    [installation:[project:[name:"Seal Count"]]] 
                 
        controller.execute()
        
        checkResponse("testExecuteInstallationStationByProject")
    }

    void testExecuteReceiverDeploymentNoFilter() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [station:[installation:[project:[name:null], name:null]]]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentNoFilter")
    }
    
    void testExecuteReceiverDeploymentByProject() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [station:[installation:[project:[name:"Seal Count"], name:null]]]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentByProject")
    }
    
    void testExecuteReceiverDeploymentByInstallation() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [station:[installation:[project:[name:null], name:"Ningaloo Array"]]]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentByInstallation")
    }

    void testExecuteReceiverDeploymentByProjectAndInstallation() 
    {
        controller.params._name = "receiverDeployment"
        controller.params._file = "receiverDeploymentList"
        controller.params.filter = 
            [station:[installation:[project:[name:"Seal Count"], name:"Heron Island"]]]
                 
        controller.execute()
        
        checkResponse("testExecuteReceiverDeploymentByProjectAndInstallation")
    }
    
    void testExecuteAnimalReleaseSummary()
    {
        controller.params._name = "animalReleaseSummary"
        controller.params._file = "animalReleaseSummary"
        controller.params.filter = [:]
                 
        controller.execute()
        
        checkResponse("testExecuteAnimalReleaseSummary")
    }
    
    void testExecuteTag()
    {
        controller.params._name = "tag"
        controller.params._file = "tagExtract"
        controller.params.filter = [:]
		controller.params._type = "extract"
		
        controller.execute()
        
        checkResponse("testExecuteTag")
    }
	
	void testExecuteDetection()
	{
		controller.params._name = "detection"
		controller.params._file = "detectionExtract"
		controller.params.filter = [:]
		controller.params._type = "extract"
		
		controller.execute()
		
		checkResponse("testExecuteDetection")
	}

//	void testExecuteDetectionWithNonMatchingFilter()
//	{
//		controller.params._name = "detection"
//		controller.params._file = "detectionExtract"
//
//		try
//		{
//			controller.params.filter = [receiverDeployment:[station:[name:"no match"]]]
//			controller.execute()
//			
//			controller.params.filter = [receiverDeployment:[station:[name:"Bondi SW2"]]]
//			controller.execute()
//		}
//		catch (Exception e)
//		{
//			fail()
//		}
//	}

    private void checkResponse(def expectedFileName)
    {
        // Compare the response content with expected.
        String expectedFilePath = \
            System.getProperty("user.dir") + \
            "/test/integration/au/org/emii/aatams/report/resources/" + \
            expectedFileName + ".expected.csv"
            
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
            else
            {
                retString += it + '\n'
            }
            
            index++
        }
        
        return retString 
    }
}
