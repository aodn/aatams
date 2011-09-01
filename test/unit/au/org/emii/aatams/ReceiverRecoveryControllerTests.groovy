package au.org.emii.aatams

import grails.test.*

class ReceiverRecoveryControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
        
        // See http://jira.grails.org/browse/GRAILS-5926
        controller.metaClass.message = { Map map -> return "error message" }
        
        mockLogging(CandidateEntitiesService)
        def candidateEntitiesService = new CandidateEntitiesService()
        
        def projectList = 
        [
            new Project(id:1, name:'Project 1'),
            new Project(id:2, name:'Project 2'),
            new Project(id:3, name:'Project 3'),
        ]
//        mockDomain(Project, projectList)
//        projectList.each { it.save() }

        candidateEntitiesService.metaClass.readableProjects =
        {
            return [projectList[0], projectList[1]] // Only return the first two projects.
        }
        
        controller.candidateEntitiesService = candidateEntitiesService
        
        // Create the required entities.
        def installation1 = new Installation(id:1, project:projectList[0])
        def installation2 = new Installation(id:2, project:projectList[1])
        def installation3 = new Installation(id:3, project:projectList[2])
        
        def station1 = new InstallationStation(id:1, installation:installation1)
        def station2 = new InstallationStation(id:2, installation:installation2)
        def station3 = new InstallationStation(id:3, installation:installation3)
        
        def deployment1NoRecovery = new ReceiverDeployment(id:1, station:station1)
        def deployment2 = new ReceiverDeployment(id:2, station:station2)
        def deployment3 = new ReceiverDeployment(id:3, station:station3)
        def deploymentList = [deployment1NoRecovery, deployment2, deployment3]
        
        mockDomain(ReceiverDeployment, deploymentList)
        
        def recovery2 = new ReceiverRecovery(id:2, deployment:deployment2)
        deployment2.recovery = recovery2

        def recovery3 = new ReceiverRecovery(id:3, deployment:deployment3)
        deployment3.recovery = recovery3
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testMemberProjects()
    {
        def model = controller.list()
        
        assertEquals(2, model.readableProjects.size())
        assertEquals(3, model.receiverDeploymentInstanceList.size())
        assertEquals(3, model.receiverDeploymentInstanceTotal)
    }
    
    void testFilterNone() 
    {
        def model = controller.filter()
        
        assertEquals(2, model.readableProjects.size())
        assertEquals(3, model.receiverDeploymentInstanceList.size())
        assertEquals(3, model.receiverDeploymentInstanceTotal)
        assertNull(model.selectedProjectId)
        assertNull(model.unrecoveredOnly)
    }
    
    void testFilterByProject()
    {
        def projectId = 1
        controller.params.filter = 
            [project:[id:projectId],
             unrecoveredOnly:false]
             
        def model = controller.filter()
        
        assertEquals(1, model.receiverDeploymentInstanceList.size())
        assertEquals(1, model.receiverDeploymentInstanceTotal)
        assertEquals(projectId, model.selectedProjectId)
        assertFalse(model.unrecoveredOnly)
    }
    
    void testFilterByRecovered()
    {
        // Get all...
        controller.params.filter = 
            [project:null,
             unrecoveredOnly:false]
             
        def model = controller.filter()
        
        assertEquals(3, model.receiverDeploymentInstanceList.size())
        assertFalse(model.unrecoveredOnly)

        // Get unrecovered only
        controller.params.filter = 
            [project:null,
             unrecoveredOnly:true]
             
        model = controller.filter()
        
        assertEquals(1, model.receiverDeploymentInstanceList.size())
        assertEquals(1, model.receiverDeploymentInstanceTotal)
        assertTrue(model.unrecoveredOnly)
    }

    void testFilterByProjectAndRecovered()
    {
        // Get project 2, including recovered.
        def projectId = 2
        controller.params.filter = 
            [project:[id:projectId],
            unrecoveredOnly:false]
            
        def model = controller.filter()
        
        assertEquals(1, model.receiverDeploymentInstanceList.size())
        assertEquals(1, model.receiverDeploymentInstanceTotal)
        assertEquals(projectId, model.selectedProjectId)
        assertFalse(model.unrecoveredOnly)
        
        // Get project 1, unrecovered (should be no result).
        controller.params.filter = 
            [project:[id:projectId],
            unrecoveredOnly:true]
        
        model = controller.filter()

        println model.receiverDeploymentInstanceList
        assertTrue(model.receiverDeploymentInstanceList.isEmpty())
        assertEquals(0, model.receiverDeploymentInstanceTotal)
        assertEquals(projectId, model.selectedProjectId)
        assertTrue(model.unrecoveredOnly)
    }
}
