package au.org.emii.aatams

import grails.test.*
import grails.converters.JSON

class InstallationControllerTests extends ControllerUnitTestCase 
{
    def candidateEntitiesService
    
    def project1
    def project2
    
    protected void setUp() 
    {
        super.setUp()
        
        project1 = new Project()
        project2 = new Project()
        
        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.metaClass.projects =
        {
            return [project1, project2]
        }
        
        controller.candidateEntitiesService = candidateEntitiesService
		
		Installation bondi = new Installation(name:'Bondi')
		Installation ningaloo = new Installation(name:'Ningaloo')
		
		def installationList = [bondi, ningaloo]
        mockDomain(Installation, installationList)
		installationList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testCreate() 
    {
        def model = controller.create()
        
        assertNotNull(model.installationInstance)
        assertEquals(2, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(project1))
        assertTrue(model.candidateProjects.contains(project2))
    }

    void testSaveError() 
    {
        def model = controller.save()
        
        assertNotNull(model.installationInstance)
        assertEquals(2, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(project1))
        assertTrue(model.candidateProjects.contains(project2))
    }
	
	void testLookupByName()
	{
		assertLookupWithTerm(0, 'x')
		assertLookupWithTerm(1, 'B')
		assertLookupWithTerm(1, 'b')
		assertLookupWithTerm(2, 'I')
		assertLookupWithTerm(2, 'i')
	}
	
	private assertLookupWithTerm(expectedNumResults, term) 
	{
		controller.params.term = term
		controller.lookupByName()

		def jsonResponse = JSON.parse(controller.response.contentAsString)
		println(jsonResponse)
		
		assertEquals(expectedNumResults, jsonResponse.size())
		
		// Need to reset the response so that this method can be called multiple times within a single test case.
		// Also requires workaround to avoid exception, see: http://jira.grails.org/browse/GRAILS-6483
		mockResponse?.committed = false // Current workaround
		reset()
	}
}
