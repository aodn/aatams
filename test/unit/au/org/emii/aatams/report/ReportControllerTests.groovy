package au.org.emii.aatams.report

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.test.AbstractControllerUnitTestCase

import org.codehaus.groovy.grails.plugins.jasper.*

class ReportControllerTests extends AbstractControllerUnitTestCase
{
    def visibilityControlService
    def jasperService
    def permissionUtilsService
    def reportInfoService
    def queryService

    Person user

    protected void setUp()
    {
        super.setUp()

        mockLogging(VisibilityControlService)
        visibilityControlService = new VisibilityControlService()
        visibilityControlService.permissionUtilsService = permissionUtilsService

        mockLogging(JasperService)
        jasperService = new JasperService()

        mockLogging(PermissionUtilsService)
        permissionUtilsService = new PermissionUtilsService()

        mockLogging(QueryService, true)
        queryService = new QueryService()
        queryService.visibilityControlService = visibilityControlService

        mockLogging(ReportInfoService)
        reportInfoService = new ReportInfoService()
        reportInfoService.permissionUtilsService = permissionUtilsService

        controller.jasperService = jasperService
        controller.permissionUtilsService = permissionUtilsService
        controller.queryService = queryService
        controller.reportInfoService = reportInfoService

        mockDomain(Installation)
        mockDomain(Organisation)
        mockDomain(Project)
        mockDomain(Receiver)
        mockDomain(ReceiverEvent)
        mockDomain(ValidDetection)

        user = new Person(username: 'user')
        hasRole = true

        // Need this for "findByUsername()" etc.
        mockDomain(Person, [user])
        user.save()
    }

    protected def getPrincipal()
    {
        return user?.id
    }

    protected boolean isPermitted(permission)
    {
        if (permission == "project:" + projectWithMembership.id + ":read")
        {
            return true
        }

        return false
    }

    void testNoResults()
    {
        controller.params._name = "receiver"
        controller.params._file = "receiverList"
        controller.params._format = "PDF"
        controller.params._type = "report"

        controller.execute()

        assertEquals("create", redirectArgs['action'])
        assertEquals("receiver", redirectArgs.params.name)
//        assertEquals(["PDF"], redirectArgs.params.formats)
        assertEquals("No matching records.", controller.flash.message)
    }
}
