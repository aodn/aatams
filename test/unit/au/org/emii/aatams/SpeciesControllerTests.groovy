package au.org.emii.aatams

import grails.test.*
import grails.converters.JSON

class SpeciesControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(SpeciesService)
        def speciesService = new SpeciesService()
        controller.speciesService = speciesService
        
        CaabSpecies whiteShark = new CaabSpecies(scientificName:"Carcharodon carcharias", commonName:"White Shark", spcode:"37010003", name: "37010003 - Carcharodon carcharias (White Shark)")
        CaabSpecies blueFinTuna = new CaabSpecies(scientificName:"Thunnus maccoyii", commonName:"Southern Bluefin Tuna", spcode:"37441004")
        CaabSpecies blueEyeTrevalla = new CaabSpecies(scientificName:"Hyperoglyphe antarctica", commonName:"Blue-eye Trevalla", spcode:"37445001")
        def speciesList = [whiteShark, blueFinTuna, blueEyeTrevalla]
        mockDomain(CaabSpecies, speciesList)
        speciesList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }
    
    void testNoDuplicatesInLookupByName()
    {
        // Bug #1729.
        assertLookupByNameWithTerm(1, "White Shark")
    }

    void testLookupByNameAndReturnSpcode()
    {
        assertLookupByNameAndReturnSpcodeWithTerm(0, 'X')
        assertLookupByNameAndReturnSpcodeWithTerm(3, '0')
        assertLookupByNameAndReturnSpcodeWithTerm(2, '374')
        assertLookupByNameAndReturnSpcodeWithTerm(3, '37')
        assertLookupByNameAndReturnSpcodeWithTerm(1, '370')
        assertLookupByNameAndReturnSpcodeWithTerm(2, '44')
    }
    
    private void assertLookupByNameWithTerm(expectedNumResults, term) 
    {
        controller.params.term = term
        controller.lookupByName()
        
        checkJsonResponse(expectedNumResults)
    }

    private void assertLookupByNameAndReturnSpcodeWithTerm(expectedNumResults, term)
    {
        controller.params.term = term
        controller.lookupByNameAndReturnSpcode()
        
        checkJsonResponse(expectedNumResults)
    }
    
    private void checkJsonResponse(expectedNumResults) 
    {
        def jsonResponse = JSON.parse(controller.response.contentAsString)
        assertEquals(expectedNumResults, jsonResponse.size())

        // Need to reset the response so that this method can be called multiple times within a single test case.
        // Also requires workaround to avoid exception, see: http://jira.grails.org/browse/GRAILS-6483
        mockResponse?.committed = false // Current workaround
        reset()
    }
}
