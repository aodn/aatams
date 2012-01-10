package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractGrailsUnitTestCase
import grails.test.*

class CandidateEntitiesServiceTests extends AbstractGrailsUnitTestCase 
{
    def candidateEntitiesService
    def permService
    def person
    
    Receiver newReceiver
    Receiver deployedReceiver
    Receiver recoveredReceiver
    Receiver csiroReceiver
    
    Project notPermittedProject
    Project permittedProject

    Installation notPermittedInstallation1
    Installation notPermittedInstallation2
    Installation permittedInstallation1
    Installation permittedInstallation2
	
	InstallationStation stationAAA
	InstallationStation stationBBB
	InstallationStation stationCCC
	InstallationStation stationDDD
	
    protected void setUp()
    {
        super.setUp()
        
        mockLogging(PermissionUtilsService)
        permService = new PermissionUtilsService()
        
        mockLogging(CandidateEntitiesService)
        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.permissionUtilsService = permService

        // User belongs to IMOS, but not to CSIRO.
        def imos = new Organisation(name:"IMSO")
        def csiro = new Organisation(name:"CSIRO")
        def orgList = [imos, csiro]
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }
        
        person = new Person(username:"person",
                            organisation:imos)
                               
        mockDomain(Person, [person])
        person.save()
        
        notPermittedProject = new Project(name:"not permitted", status:EntityStatus.ACTIVE)
        permittedProject = new Project(name:"permitted", status:EntityStatus.ACTIVE)
        def projectList = [notPermittedProject, permittedProject]
        mockDomain(Project, projectList)
        
        notPermittedInstallation1 = new Installation(name: "not permitted 1", project:notPermittedProject)
        notPermittedInstallation2 = new Installation(name: "not permitted 2", project:notPermittedProject)
        
        permittedInstallation1 = new Installation(name: "permitted 1", project:permittedProject)
        permittedInstallation2 = new Installation(name: "permitted 2", project:permittedProject)
        
        def installationList = [notPermittedInstallation1, notPermittedInstallation2, permittedInstallation1, permittedInstallation2]
        mockDomain(Installation, installationList)
        installationList.each { it.save() }
        
        notPermittedProject.addToInstallations(notPermittedInstallation1)
        notPermittedProject.addToInstallations(notPermittedInstallation2)
        permittedProject.addToInstallations(permittedInstallation1)
        permittedProject.addToInstallations(permittedInstallation2)
        
        projectList.each { it.save() }
        
        DeviceStatus newStatus = new DeviceStatus(status:"NEW")
        DeviceStatus deployedStatus = new DeviceStatus(status:"DEPLOYED")
        DeviceStatus recoveredStatus = new DeviceStatus(status:"RECOVERED")
        
        def statusList = [newStatus, deployedStatus, recoveredStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }
        
        newReceiver = new Receiver(serialNumber:"111", status:newStatus, organisation:imos)
        deployedReceiver = new Receiver(serialNumber:"222", status:deployedStatus, organisation:imos)
        recoveredReceiver = new Receiver(serialNumber:"333", status:recoveredStatus, organisation:imos)
        csiroReceiver = new Receiver(serialNumber:"444", status:recoveredStatus, organisation:csiro)

        def receiverList = [recoveredReceiver, newReceiver, deployedReceiver]
        mockDomain(Receiver, receiverList)
        receiverList.each 
        {
            imos.addToReceivers(it)
            it.save() 
        }
        
        imos.save()
		
		stationAAA = new InstallationStation(name:'AAA', installation:permittedInstallation1)
		stationBBB = new InstallationStation(name:'BBB', installation:permittedInstallation1)
		stationCCC = new InstallationStation(name:'CCC', installation:permittedInstallation1)
		stationDDD = new InstallationStation(name:'DDD', installation:permittedInstallation1)
		// save out of order
		def stationList = [stationBBB, stationCCC, stationAAA, stationDDD]
		mockDomain(InstallationStation, stationList)
		stationList.each { it.save() }
    }

	protected def getPrincipal()
	{
		return person.username
	}
	
	protected boolean isPermitted(String permission)
	{
        if (permission == "project:" + permittedProject.id + ":write")
        {
            return true
        }
        return false
	}

	protected void tearDown() 
    {
        super.tearDown()
    }

    void testReceivers() 
    {
        def receivers = candidateEntitiesService.receivers()
        
        assertEquals(2, receivers.size())
        
        assertEquals(newReceiver.serialNumber, receivers[0].serialNumber)
        assertEquals(recoveredReceiver.serialNumber, receivers[1].serialNumber)
        assertFalse(receivers.contains(deployedReceiver))
        assertFalse(receivers.contains(csiroReceiver))
    }
    
    void testInstallations()
    {
        def installations = candidateEntitiesService.installations()
        assertEquals(2, installations.size())
        assertTrue(installations.contains(permittedInstallation1))
        assertTrue(installations.contains(permittedInstallation2))
    }

    void testProjects()
    {
        assertEquals(2, Project.list().size())
        assertEquals(2, Project.findAllByStatus(EntityStatus.ACTIVE).size())
        
        def projects = candidateEntitiesService.projects()
        assertEquals(1, projects.size())
        assertTrue(projects.contains(permittedProject ))
    }
	
	void testStations()
	{
		[stationAAA, stationBBB, stationCCC].each 
		{
			it.metaClass.isActive = { false }
		}
		stationDDD.metaClass.isActive = { true }
		
		assertEquals(stationAAA.name, candidateEntitiesService.stations()[0].name, )
		assertEquals(stationBBB.name, candidateEntitiesService.stations()[1].name, )
		assertEquals(stationCCC.name, candidateEntitiesService.stations()[2].name, )
		
		assertFalse(candidateEntitiesService.stations()*.name.contains(stationDDD.name))
	}
}
