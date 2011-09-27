package au.org.emii.aatams

import grails.test.*

class DetectionControllerTests extends ControllerUnitTestCase 
{
    def candidateEntitiesService
    
    def deployment1
    def deployment2
    
    protected void setUp() 
    {
        super.setUp()
        
        mockDomain(Person)
        deployment1 = new ReceiverDeployment()
        deployment2 = new ReceiverDeployment()
        
        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.metaClass.deployments =
        {
            return [deployment1, deployment2]
        }
        
        controller.candidateEntitiesService = candidateEntitiesService
        mockDomain(Detection)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testSaveError() 
    {
        def model = controller.save()
        
        assertNotNull(model.detectionInstance)
        assertEquals(2, model.candidateDeployments.size())
        assertTrue(model.candidateDeployments.contains(deployment1))
        assertTrue(model.candidateDeployments.contains(deployment2))
    }

    void testCreate() 
    {
        controller.create()
        
        assertEquals("receiverDownloadFile", controller.redirectArgs.controller)
        assertEquals("create", controller.redirectArgs.action)
    }
}
