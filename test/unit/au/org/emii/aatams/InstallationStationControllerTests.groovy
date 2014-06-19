package au.org.emii.aatams

import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.io.WKTReader

import grails.test.*
import grails.converters.JSON

class InstallationStationControllerTests extends ControllerUnitTestCase 
{
    def candidateEntitiesService
    
    def installation1
    def installation2
    
    protected void setUp() 
    {
        super.setUp()
        
        
        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.metaClass.installations =
        {
            return [installation1, installation2]
        }
        
        controller.candidateEntitiesService = candidateEntitiesService
        WKTReader reader = new WKTReader()
        InstallationStation bondi = new InstallationStation(name:'Bondi', location:(Point)reader.read("POINT(30.1234 30.1234)"))
        InstallationStation ningaloo = new InstallationStation(name:'Ningaloo', location:(Point)reader.read("POINT(30.1234 30.1234)"))
        
        def installationStationList = [bondi, ningaloo]
        mockDomain(InstallationStation, installationStationList)
        installationStationList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testCreate() 
    {
        def model = controller.create()
        
        assertNotNull(model.installationStationInstance)
        assertEquals(2, model.candidateInstallations.size())
        assertTrue(model.candidateInstallations.contains(installation1))
        assertTrue(model.candidateInstallations.contains(installation2))
    }

    void testSaveError() 
    {
        def model = controller.save()
        
        assertNotNull(model.installationStationInstance)
        assertEquals(2, model.candidateInstallations.size())
        assertTrue(model.candidateInstallations.contains(installation1))
        assertTrue(model.candidateInstallations.contains(installation2))
    }

    void testLookupByName()
    {
//        assertLookupWithTerm(0, 'x')
//        assertLookupWithTerm(1, 'B')
//        assertLookupWithTerm(1, 'b')
//        assertLookupWithTerm(2, 'I')
//        assertLookupWithTerm(2, 'i')
    }
    
    private void assertLookupWithTerm(expectedNumResults, term) 
    {
        controller.params.term = term
        controller.lookupByName()

        def jsonResponse = JSON.parse(controller.response.contentAsString)
        assertEquals(expectedNumResults, jsonResponse.size())
        
        // Need to reset the response so that this method can be called multiple times within a single test case.
        // Also requires workaround to avoid exception, see: http://jira.grails.org/browse/GRAILS-6483
        mockResponse?.committed = false // Current workaround
        reset()
    }
}
