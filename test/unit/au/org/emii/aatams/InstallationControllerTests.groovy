package au.org.emii.aatams

import grails.test.*

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
        mockDomain(Installation)
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
}
