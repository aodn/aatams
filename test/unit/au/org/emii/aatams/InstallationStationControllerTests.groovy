package au.org.emii.aatams

import grails.test.*

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
        mockDomain(InstallationStation)
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
}
