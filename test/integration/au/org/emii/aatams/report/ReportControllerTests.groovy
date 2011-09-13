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
        def subject = [ getPrincipal: { "iamauser" },
                        isAuthenticated: { true }
                      ] as Subject

        ThreadContext.put( ThreadContext.SECURITY_MANAGER_KEY, 
                            [ getSubject: { subject } ] as SecurityManager )

        SecurityUtils.metaClass.static.getSubject = { subject }
        
        controller.metaClass.servletContext = 
            [ getRealPath: {System.getProperty("user.dir") + "/web-app" + it }] as ServletContext
        
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testExecuteReceiverNoFilter() 
    {
        controller.params.pdf = "PDF"
        controller.params._format = "CSV"
        controller.params._name = "receiver"
        controller.params."filter.organisation.name" = null
        controller.params._file = "receiverList"
        controller.params.filter = 
                    ["organisation.name":null, 
                     organisation:[name:null]]
                 
        controller.execute()
        
        // Compare the response content with expected.
        String expectedFileName = \
            System.getProperty("user.dir") + \
            "/test/integration/au/org/emii/aatams/report/resources/" + \
            "testExecuteReceiverNoFilter.expected.csv"
        File expectedFile = new File(expectedFileName)
            
        // Write the content to a temp file (so we can see the test output after
        // the test has run).
        File tmpFile = File.createTempFile("testExecuteReceiverNoFilter.actual.csv", "")
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
