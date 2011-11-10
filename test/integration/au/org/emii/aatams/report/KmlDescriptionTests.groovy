package au.org.emii.aatams.report

import org.joda.time.DateTime;

import grails.test.*
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*

class KmlDescriptionTests extends GroovyPagesTestCase 
{

	def slurper = new XmlSlurper()
	
	String projectName = "Whale Sharks"
	String installationName = "Ningaloo Array"
	String stationName = "Ningaloo SW1"
	
    protected void setUp() 
	{
        super.setUp()
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testNoData() 
	{
		def div = executeTemplate([:])
		
		def allNodes = div.depthFirst().collect{ it }
		assertEquals(3, allNodes.grep { it.name() == "tr"}.size())
		assertEquals(6, allNodes.grep { it.name() == "td"}.size())
    }
	
	void testHeaderData()
	{
		InstallationStation stationInstance = setupStation()
		
		def div = executeTemplate([installationStationInstance:stationInstance])
		
		def allNodes = div.depthFirst().collect{ it }
		def vals = allNodes.grep { it.name() == "td"}
		assertEquals("Project", vals[0].text())
		assertEquals(projectName, vals[1].text())
		assertEquals("Installation", vals[2].text())
		assertEquals(installationName, vals[3].text())
		assertEquals("Active", vals[4].text())
		assertEquals("false", vals[5].text())
	}

	void testDetectionCounts()
	{
		InstallationStation stationInstance = InstallationStation.findByName('Bondi SW1')
		assertNotNull(stationInstance)
		
		def div = executeTemplate([installationStationInstance:stationInstance])

		def allNodes = div.depthFirst().collect{ it }
		def vals = allNodes.grep { it.name() == "td"}

		assertEquals("37010003 - Carcharodon carcharias (White Shark)", vals[6].text())
		assertEquals("10", vals[7].text())

		assertEquals("37441004 - Thunnus maccoyii (Southern Bluefin Tuna)", vals[8].text())
		assertEquals("3", vals[9].text())

		assertEquals("A69-1303-62339", vals[10].text())
		assertEquals("10", vals[11].text())

		assertEquals("A69-1303-46601", vals[12].text())
		assertEquals("3", vals[13].text())
	}

	private InstallationStation setupStation() 
	{
		Project project = new Project(name: projectName)
		Installation installation = new Installation(name: installationName, project: project)
		InstallationStation stationInstance = new InstallationStation(name: stationName, installation: installation)
		return stationInstance
	}
	
	private def executeTemplate(model) 
	{
		def kmlDescTemplate = new File("grails-app/views/report/_kmlDescriptionTemplate.gsp")
		assertNotNull(kmlDescTemplate)

		def html = applyTemplate(kmlDescTemplate.text, model)
		println html
		
		def div = slurper.parseText(html)
		return div
	}
}
