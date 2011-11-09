package au.org.emii.aatams.report

import grails.test.*

import au.org.emii.aatams.*
import au.org.emii.aatams.report.filter.*
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

import net.sourceforge.cobertura.coveragedata.ProjectData;

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
	
	void testInstallationStationsAsKmlNoResults()
	{
		generateStationsKml([])
		
		assertEquals("extract", redirectArgs['action'])
		assertEquals("installationStation", redirectArgs.params.name)
		assertEquals(["KML"], redirectArgs.params.formats)
		assertEquals("No matching records.", controller.flash.message)
	}

	void testInstallationStationsAsKmlOneResult()
	{
		Project project = new Project(name:"Whale Sharks")
		Installation installation = new Installation(name:"Ningaloo", project:project)
		
		InstallationStation station = 
			new InstallationStation(name: "Ningaloo SW1", 
									installation:installation,
									location:new GeometryFactory().createPoint(new Coordinate(34.0123f, -42.3456f)))
		generateStationsKml([station])

		def kml = new XmlSlurper().parseText(controller.response.contentAsString)
		def document = kml.Document[0]
		assertNotNull(document)
		assertEquals("Installation Stations", document.name[0].text())
		
		assertEquals(1, document.Placemark.size())
		def placemark = document.Placemark[0]
		
		assertEquals("Ningaloo SW1", placemark.name[0].text())
		assertEquals("1", placemark.open[0].text())
		
		assertEquals(1, placemark.Point.size())
		def point = placemark.Point[0]
		assertEquals("34.012298583984375,-42.34560012817383", point.coordinates[0].text())
		
		assertEquals(1, placemark.ExtendedData.size())
		def extendedData = placemark.ExtendedData[0]
		
		def projectData = extendedData.Data[0]
		assertEquals("Project", projectData.@name.text())
		assertEquals("Whale Sharks", projectData.value[0].text())
		
		def installationData = extendedData.Data[1]
		assertEquals("Installation", installationData.@name.text())
		assertEquals("Ningaloo", installationData.value[0].text())
		
//		- receiver(s) deployed
		def activeData = extendedData.Data[2]
		assertEquals("Active", activeData.@name.text())
		assertEquals("false", activeData.value[0].text())
	}
	
	private void generateStationsKml(stationList) 
	{
		mockDomain(InstallationStation, stationList)
		stationList.each { it.save() }
		
		reportQueryExecutorService.metaClass.executeQuery =
		{
			return stationList
		}

		controller.params._name = "installationStation"
		controller.params.filter = [:]
		controller.params._format = "KML"
		controller.params._type = "extract"

		def model = controller.execute()
	}
}
