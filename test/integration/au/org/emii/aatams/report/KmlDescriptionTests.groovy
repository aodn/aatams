package au.org.emii.aatams.report

import org.joda.time.DateTime;

import grails.test.*
import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*

class KmlDescriptionTests extends GroovyPagesTestCase  {

    def slurper = new XmlSlurper()
    
    String projectName = "Whale Sharks"
    String installationName = "Ningaloo Array"
    String stationName = "Ningaloo SW1"
    
    protected void setUp()  {
        super.setUp()
    }

    protected void tearDown()  {
        super.tearDown()
    }

    void testNoData()  {
        def div = executeTemplate([:])
        
        def allNodes = div.depthFirst().collect{ it }
        assertEquals(4, allNodes.grep { it.name() == "tr"}.size())
        assertEquals(8, allNodes.grep { it.name() == "td"}.size())
    }
    
    void testHeaderData() {
        InstallationStation stationInstance = setupStation()
        stationInstance.metaClass.getDetectionCount = { -> 0 }
        
        def div = executeTemplate([installationStationInstance:stationInstance])
        
        def allNodes = div.depthFirst().collect{ it }
        def vals = allNodes.grep { it.name() == "td"}
        assertEquals("Project", vals[0].text())
        assertEquals(projectName, vals[1].text())
        assertEquals("Installation", vals[2].text())
        assertEquals(installationName, vals[3].text())
        assertEquals("Active", vals[4].text())
        assertEquals("false", vals[5].text())
        assertEquals("Detection Count", vals[6].text())
        assertEquals("0", vals[7].text())
    }

    private InstallationStation setupStation()  {
        Project project = new Project(name: projectName)
        Installation installation = new Installation(name: installationName, project: project)
        InstallationStation stationInstance = new InstallationStation(name: stationName, installation: installation)
        return stationInstance
    }
    
    private def executeTemplate(model)  {
        def kmlDescTemplate = new File("grails-app/views/report/_kmlDescriptionTemplate.gsp")
        assertNotNull(kmlDescTemplate)

        def html = applyTemplate(kmlDescTemplate.text, model)
        
        def div = slurper.parseText(html)
        return div
    }
}
