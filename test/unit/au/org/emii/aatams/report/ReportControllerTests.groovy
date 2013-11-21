package au.org.emii.aatams.report

import grails.test.*

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.report.filter.*
import au.org.emii.aatams.test.AbstractControllerUnitTestCase

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

import net.sourceforge.cobertura.coveragedata.ProjectData;

import org.codehaus.groovy.grails.plugins.jasper.*

class ReportControllerTests extends AbstractControllerUnitTestCase
{
    def embargoService
    def jasperService
	def permissionUtilsService
    def reportInfoService
	def queryService

	Person user

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

		mockLogging(QueryService, true)
		queryService = new QueryService()
		queryService.embargoService = embargoService

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

    protected void tearDown()
    {
        super.tearDown()
    }

	protected def getPrincipal()
	{
		return user?.id
	}

	protected boolean isPermitted(permission)
	{
		if (permission == "project:" + project1.id + ":read")
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

        def model = controller.execute()

        assertEquals("create", redirectArgs['action'])
        assertEquals("receiver", redirectArgs.params.name)
//        assertEquals(["PDF"], redirectArgs.params.formats)
        assertEquals("No matching records.", controller.flash.message)
    }
}
