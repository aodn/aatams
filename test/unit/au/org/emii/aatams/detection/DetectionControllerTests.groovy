package au.org.emii.aatams.detection

import au.org.emii.aatams.*

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
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testCreate() 
    {
        controller.create()
        
        assertEquals("receiverDownloadFile", controller.redirectArgs.controller)
        assertEquals("createDetections", controller.redirectArgs.action)
    }
}
