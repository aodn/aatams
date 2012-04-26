package au.org.emii.aatams

import org.joda.time.DateTime;

import grails.test.*

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import com.vividsolutions.jts.geom.*

class ReceiverRecoveryControllerTests extends AbstractControllerUnitTestCase 
{
    def candidateEntitiesService

    def project1
    def project2
	
	def receiver

	DeviceStatus recovered
	
    protected void setUp() 
    {
        super.setUp()
        
        mockDomain(Person)

        // See http://jira.grails.org/browse/GRAILS-5926
        controller.metaClass.message = { Map map -> return "error message" }
        
        mockConfig("grails.gorm.default.list.max = 10")
        controller.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
        
        mockLogging(CandidateEntitiesService)
        def candidateEntitiesService = new CandidateEntitiesService()
        
        project1 = new Project(id:1, name:'Project 1')
        project2 = new Project(id:2, name:'Project 2')
        def projectList = 
        [
            project1, 
            project2, 
            new Project(id:3, name:'Project 3'),
        ]

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

        mockDomain(ReceiverRecovery)
		
		DeviceStatus deployed = new DeviceStatus(status: 'DEPLOYED')
		recovered = new DeviceStatus(status: 'RECOVERED')
		def statusList = [deployed, recovered]
		mockDomain(DeviceStatus, statusList)
		statusList.each { it.save() }
		
		ReceiverDeviceModel model = new ReceiverDeviceModel(modelName: "VR2W")
		receiver = new Receiver(model: model, serialNumber: "1234")
		mockDomain(Receiver, [receiver])
		receiver.save()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testList()
    {
        def model = controller.list()
        
        assertEquals(2, model.readableProjects.size())
        assertTrue(model.readableProjects.contains(project1))
        assertTrue(model.readableProjects.contains(project2))
        assertEquals(3, model.entityList.size())
        assertEquals(3, model.total)
    }
	
	void testSave()
	{
		def deployment = createDeployment(createStation(), new GeometryFactory().createPoint(new Coordinate(34f, 34f)))
		deployment.mooringType = new MooringType()
		deployment.save(failOnError:true)
		
		DateTime initDate = new DateTime("2012-01-01T12:34:56")
		controller.params.deployment = [initialisationDateTime: initDate]
		controller.params.deploymentId = deployment.id
		controller.params.recoverer = new ProjectRole()
		controller.params.recoveryDateTime = new DateTime()
		controller.params.location = new GeometryFactory().createPoint(new Coordinate(34f, 34f))
		controller.params.status = new DeviceStatus()
		
		controller.save()
		
		assertEquals("show", controller.redirectArgs.action)
		assertEquals(initDate, deployment.initialisationDateTime)
		def recovery = ReceiverRecovery.get(controller.redirectArgs.id)
		assertNotNull(deployment)
	}
    
	void testCreateUseDeploymentsLocation()
	{
		Point deploymentLocation = new GeometryFactory().createPoint(new Coordinate(34f, 34f))
		deploymentLocation.setSRID(4326)

		InstallationStation station = createStation()
		
		ReceiverDeployment deployment = createDeployment(station, deploymentLocation)
		
		def model = controller.create()
		
		assertRecoveryDefaults(model, deployment, deploymentLocation)
	}

	void testCreateUseStationsLocation()
	{
		InstallationStation station = createStation()
		ReceiverDeployment deployment = createDeployment(station, null)
		
		def model = controller.create()
		
		assertRecoveryDefaults(model, deployment, station.location)
	}
	
	void testUpdate()
	{
		def deployment = createDeployment(createStation(), new GeometryFactory().createPoint(new Coordinate(34f, 34f)))
		deployment.mooringType = new MooringType()
		deployment.save(failOnError:true)

		def recovery = new ReceiverRecovery(
			deployment: deployment, 
			recoverer: new ProjectRole(), 
			location: new GeometryFactory().createPoint(new Coordinate(34f, 34f)), 
			status: recovered)
		recovery.save(failOnError:true)
		
		DateTime initDate = new DateTime("2012-01-01T12:34:56")
		controller.params.deployment = [initialisationDateTime: initDate]
		controller.params.id = recovery.id
		
		controller.update()
		
		assertEquals(initDate, deployment.initialisationDateTime)
	}
	
	private InstallationStation createStation() {
		InstallationStation station = new InstallationStation(location:new GeometryFactory().createPoint(new Coordinate(12f, 34f)))
		station.location.setSRID(4326)
		mockDomain(InstallationStation, [station])
		station.save()
		return station
	}

	private ReceiverDeployment createDeployment(InstallationStation station, location) 
	{
		ReceiverDeployment deployment = new ReceiverDeployment(location:location, station:station, receiver:receiver)
		mockDomain(ReceiverDeployment, [deployment])
		deployment.save()
		deployment.metaClass.toString = { "test deployment"}

		controller.params.deploymentId = deployment.id
		
		return deployment
	}

	private void assertRecoveryDefaults(model, ReceiverDeployment deployment, Point deploymentLocation) 
	{
		assertNotNull(model.receiverRecoveryInstance)
		assertEquals(deployment, model.receiverRecoveryInstance.deployment)
		assertEquals(deploymentLocation, model.receiverRecoveryInstance.location)
	}
}
