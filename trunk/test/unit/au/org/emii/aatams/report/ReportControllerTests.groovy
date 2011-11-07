package au.org.emii.aatams.report

import grails.test.*

import au.org.emii.aatams.*
import au.org.emii.aatams.report.filter.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import org.codehaus.groovy.grails.plugins.jasper.*

class ReportControllerTests extends ControllerUnitTestCase 
{
    def embargoService
    def jasperService
    def permissionUtilsService
    def reportFilterFactoryService
    def reportInfoService
    def reportQueryExecutorService

    protected void setUp() 
    {
        super.setUp()

        mockLogging(EmbargoService)
        embargoService = new EmbargoService()
        embargoService.permissionUtilsService = permissionUtilsService
        
        mockLogging(JasperService)
        jasperService = new JasperService()
        
        mockLogging(PermissionUtilsService)
        permissionUtilsService = new PermissionUtilsService()
        
		mockLogging(ReportFilterFactoryService, true)
		reportFilterFactoryService = new ReportFilterFactoryService()
		
        mockLogging(ReportInfoService)
        reportInfoService = new ReportInfoService()
        
        mockLogging(ReportQueryExecutorService)
        reportQueryExecutorService = new ReportQueryExecutorService()
        reportQueryExecutorService.embargoService = embargoService
        reportQueryExecutorService.permissionUtilsService = permissionUtilsService
        
        controller.jasperService = jasperService
        controller.permissionUtilsService = permissionUtilsService
		controller.reportFilterFactoryService = reportFilterFactoryService
        controller.reportInfoService = reportInfoService
        controller.reportQueryExecutorService = reportQueryExecutorService
        
        mockDomain(Receiver)

        Person user = new Person(username: 'user')
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
        
        // Need this for "findByUsername()" etc.
        mockDomain(Person, [user])
        user.save()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testNoResults()
    {
        controller.params._name = "receiver"
        controller.params._file = "receiverList"
        controller.params.filter = [:]
        controller.params._format = "PDF"
		controller.params._type = "report"

        def model = controller.execute()
        
        assertEquals("create", redirectArgs['action'])
        assertEquals("receiver", redirectArgs.params.name)
        assertEquals(["PDF"], redirectArgs.params.formats)
        assertEquals("No matching records.", controller.flash.message)
        
    }
}
