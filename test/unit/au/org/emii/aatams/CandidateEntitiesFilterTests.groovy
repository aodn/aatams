package au.org.emii.aatams

import grails.test.*
import au.org.emii.aatams.detection.*
import au.org.emii.aatams.filter.QueryService
import au.org.emii.aatams.report.ReportInfoService
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

class CandidateEntitiesFilterTests extends AbstractGrailsUnitTestCase 
{
    PermissionUtilsService permService
    Project activeProj
    Project pendingProj
    Project nonWriteProj
    Installation activeInstallation
    InstallationStation activeStation
    Person person
    Receiver imosReceiver
    Receiver csiroReceiver
    ReceiverDeployment activeDeployment
    
    AnimalReleaseController animalReleaseController
    DetectionController detectionController
    InstallationController installationController
    InstallationStationController installationStationController
    ReceiverDeploymentController receiverDeploymentController
    ReceiverRecoveryController receiverRecoveryController
    TagController tagController
    
    protected void setUp() 
    {
        super.setUp()
        
        mockLogging(PermissionUtilsService)
        permService = new PermissionUtilsService()
        
        def newStatus = new DeviceStatus(status:"NEW")
        def deployedStatus = new DeviceStatus(status:"DEPLOYED")
        def recoveredStatus = new DeviceStatus(status:"RECOVERED")
        
        def statusList = [newStatus, deployedStatus, recoveredStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }
        
        // User belongs to IMOS, but not to CSIRO.
        def imos = new Organisation(name:"IMSO")
        def csiro = new Organisation(name:"CSIRO")
        def orgList = [imos, csiro]
        mockDomain(Organisation, orgList)
        orgList.each { it.save() }
        
        // Active projects that user belongs to.
        activeProj = 
            new Project(name:"Active Project",
                        status:EntityStatus.ACTIVE)
        pendingProj =
            new Project(name:"Pending Project",
                        status:EntityStatus.PENDING)

        // User can't write to this project.
        nonWriteProj = 
            new Project(name:"Non-member project",
                        status:EntityStatus.ACTIVE)

        def projList = [activeProj, pendingProj, nonWriteProj]
        mockDomain(Project, projList)
        projList.each { it.save() }
        
        person = new Person(username:"person",
                                   organisation:imos)
                               
        mockDomain(Person, [person])
        person.save()
        
        ProjectRoleType roleType = new ProjectRoleType(displayName:"some role")
        ProjectRoleType piRoleType = new ProjectRoleType(displayName:ProjectRoleType.PRINCIPAL_INVESTIGATOR)
        def roleTypeList = [piRoleType, roleType]
        mockDomain(ProjectRoleType, roleTypeList)
        
        ProjectRole activeProjRole = 
            new ProjectRole(project:activeProj,
                            person:person,
                            roleType:roleType,
                            access:ProjectAccess.READ_WRITE)
        ProjectRole pendingProjRole = 
            new ProjectRole(project:pendingProj,
                            person:person,
                            roleType:roleType,
                            access:ProjectAccess.READ_WRITE)
        ProjectRole nonWriteProjRole = 
            new ProjectRole(project:nonWriteProj,
                            person:person,
                            roleType:roleType,
                            access:ProjectAccess.READ_ONLY)
        def roles = [activeProjRole, pendingProjRole, nonWriteProjRole]
        mockDomain(ProjectRole, roles)
        roles.each { permService.setPermissions(it) }
        
        mockDomain(AnimalRelease)
        
        activeInstallation = 
            new Installation(project:activeProj,
                             name:"installationActive")
        Installation pendingInstallation = 
            new Installation(project:pendingProj,
                             name:"installationPending")
        Installation nonWriteInstallation = 
            new Installation(project:nonWriteProj,
                             name:"installationNonWrite")
                         
        def installList = [activeInstallation, pendingInstallation, nonWriteInstallation]
        mockDomain(Installation, installList)
        Installation.metaClass.static.createCriteria = {[list: {Closure cls -> [activeInstallation]}]}
        installList.each { it.save() }

        activeStation =
            new InstallationStation(installation:activeInstallation)
        def pendingStation = 
            new InstallationStation(installation:pendingInstallation)
        def nonWriteStation =
            new InstallationStation(installation:nonWriteInstallation)
        def stationList = [activeStation, pendingStation, nonWriteInstallation] 
        mockDomain(InstallationStation, stationList)
        stationList.each { it.save() }
        
        imosReceiver = new Receiver(organisation:imos)
        csiroReceiver = new Receiver(organisation:csiro)
        def receiverList = [imosReceiver, csiroReceiver]
        mockDomain(Receiver, receiverList)
        receiverList.each { it.save() }
        
        imos.addToReceivers(imosReceiver)
        imos.save()
        
        csiro.addToReceivers(csiroReceiver)
        csiro.save()
        
        activeDeployment =
            new ReceiverDeployment(station:activeStation,
                                   receiver:imosReceiver)
        def pendingDeployment =
            new ReceiverDeployment(station:pendingStation,
                                   receiver:imosReceiver)
        def nonWriteDeployment =
            new ReceiverDeployment(station:nonWriteStation,
                                   receiver:imosReceiver)
        def deploymentList = [activeDeployment, pendingDeployment, nonWriteDeployment]
        mockDomain(ReceiverDeployment, deploymentList)
        deploymentList.each { it.save() }
        
        mockDomain(Surgery)
        mockDomain(Tag)

        CandidateEntitiesService candEntitiesService = new CandidateEntitiesService()
        candEntitiesService.permissionUtilsService = permService
        
        mockController(AnimalReleaseController)
        mockLogging(AnimalReleaseController, true)
        animalReleaseController = new AnimalReleaseController()
        animalReleaseController.metaClass.message = { Map map -> return "error message" }
        animalReleaseController.candidateEntitiesService = candEntitiesService

        mockController(DetectionController)
        mockLogging(DetectionController, true)
        detectionController = new DetectionController()
        detectionController.metaClass.message = { Map map -> return "error message" }
        detectionController.candidateEntitiesService = candEntitiesService
        
        mockController(InstallationController)
        mockLogging(InstallationController, true)
        installationController = new InstallationController()
        installationController.metaClass.message = { Map map -> return "error message" }
        installationController.candidateEntitiesService = candEntitiesService

        mockController(InstallationStationController)
        mockLogging(InstallationStationController, true)
        installationStationController = new InstallationStationController()
        installationStationController.metaClass.message = { Map map -> return "error message" }
        installationStationController.candidateEntitiesService = candEntitiesService

        mockController(ReceiverDeploymentController)
        mockLogging(ReceiverDeploymentController, true)
        receiverDeploymentController = new ReceiverDeploymentController()
        receiverDeploymentController.metaClass.message = { Map map -> return "error message" }
        receiverDeploymentController.candidateEntitiesService = candEntitiesService

        mockController(ReceiverRecoveryController)
        mockLogging(ReceiverRecoveryController, true)
        mockLogging(ReportInfoService, true)
        receiverRecoveryController = new ReceiverRecoveryController()
        receiverRecoveryController.metaClass.message = { Map map -> return "error message" }
        receiverRecoveryController.candidateEntitiesService = candEntitiesService
        receiverRecoveryController.queryService = new QueryService()
        receiverRecoveryController.queryService.embargoService = new EmbargoService()
        receiverRecoveryController.reportInfoService = new ReportInfoService()
        mockConfig("grails.gorm.default.list.max = 10")
        receiverRecoveryController.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
        

        mockController(TagController)
        mockLogging(TagController, true)
        tagController = new TagController()
        tagController.metaClass.message = { Map map -> return "error message" }
        tagController.candidateEntitiesService = candEntitiesService
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    protected def getPrincipal()
    {
        return person.id
    }
    
    protected boolean isPermitted(String permString)
    {
        if (permString == "project:" + activeProj.id + ":write")
        {
            return true
        }
        else if (permString == "project:" + pendingProj.id + ":write")
        {
            return true
        }
        else if (permString == "project:" + activeProj.id + ":read")
        {
            return true
        }
        else if (permString == "project:" + pendingProj.id + ":read")
        {
            return true
        }
        else if (permString == "project:" + nonWriteProj.id + ":read")
        {
            return true
        }
        
        return false
    }
    
    void testAnimalReleaseCreate()
    {
        def model = animalReleaseController.create()
        
        assertNotNull(model.candidateProjects)
        assertEquals(1, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(activeProj))
        
        // embargoPeriods
        assertNotNull(model.embargoPeriods)
        assertEquals(3, model.embargoPeriods.size())
        model.embargoPeriods.each
        {
            if (it.key == 6) assertEquals("6 months", it.value)
            if (it.key == 12) assertEquals("12 months", it.value)
            if (it.key == 36) assertEquals("3 years", it.value)
        }
    }
    
    void testAnimalReleaseEdit()
    {
        AnimalRelease release = new AnimalRelease()
        mockDomain(AnimalRelease, [release])
        release.save()
        assertNotNull(release.id)
        
        animalReleaseController.params.id = release.id
        def model = animalReleaseController.edit()
        
        assertNotNull(model.candidateProjects)
        assertEquals(1, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(activeProj))

        // embargoPeriods
        assertNotNull(model.embargoPeriods)
        assertEquals(3, model.embargoPeriods.size())
        model.embargoPeriods.each
        {
            if (it.key == 6) assertEquals("6 months", it.value)
            if (it.key == 12) assertEquals("12 months", it.value)
            if (it.key == 36) assertEquals("3 years", it.value)
        }
    }
    
    void testDetectionEdit()
    {
        ValidDetection detection = new ValidDetection()
        mockDomain(ValidDetection, [detection])
        detection.save()
        
        // candidateDeployments
        detectionController.params.id = detection.id
        def model = detectionController.edit()
        
        assertNotNull(model.candidateDeployments)
        assertEquals(1, model.candidateDeployments.size())
        assertTrue(model.candidateDeployments.contains(activeDeployment))
    }
    
    void testInstallationCreate()
    {
        // candidateProjects
        def model = installationController.create()
        
        assertNotNull(model.candidateProjects)
        assertEquals(1, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(activeProj))
    }
    
    void testInstallationEdit()
    {
        def installation = new Installation()
        mockDomain(Installation, [installation])
        installation.save()
        
        // candidateProjects
        installationController.params.id = installation.id
        def model = installationController.edit()
        
        assertNotNull(model.candidateProjects)
        assertEquals(1, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(activeProj))
    }
    
    void testInstallationStationCreate()
    {
        // candidateInstallations
        def model = installationStationController.create()
        
        assertNotNull(model.candidateInstallations)
        assertEquals(1, model.candidateInstallations.size())
        assertTrue(model.candidateInstallations.contains(activeInstallation))
    }
    
    void testInstallationStationEdit()
    {
        InstallationStation station = new InstallationStation()
        mockDomain(InstallationStation, [station])
        station.save()
        
        installationStationController.params.id = station.id
        def model = installationStationController.edit()
        
        assertNotNull(model.candidateInstallations)
        assertEquals(1, model.candidateInstallations.size())
        assertTrue(model.candidateInstallations.contains(activeInstallation))
    }
    
    void testReceiverDeploymentCreate()
    {
        def model = receiverDeploymentController.create()

        // candidateStations
        assertNotNull(model.candidateStations)
        assertEquals(1, model.candidateStations.size())
        assertTrue(model.candidateStations.contains(activeStation))

        // candidateReceivers (as SysAdmin)
        assertNotNull(model.candidateReceivers)
        assertEquals(2, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(imosReceiver))
        assertTrue(model.candidateReceivers.contains(csiroReceiver))
        
        // candidateReceivers (as non SysAdmin)
        setNonAdminUser()
        model = receiverDeploymentController.create()

        assertNotNull(model.candidateReceivers)
        assertEquals(1, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(imosReceiver))
    }
    
    void testReceiverDeploymentEdit()
    {
        def deployment = new ReceiverDeployment()
        mockDomain(ReceiverDeployment, [deployment])
        deployment.save()
        
        receiverDeploymentController.params.id = deployment.id
        def model = receiverDeploymentController.edit()

        // candidateStations
        assertNotNull(model.candidateStations)
        assertEquals(1, model.candidateStations.size())
        assertTrue(model.candidateStations.contains(activeStation))

        // candidateReceivers (as SysAdmin)
        assertNotNull(model.candidateReceivers)
        assertEquals(2, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(imosReceiver))
        assertTrue(model.candidateReceivers.contains(csiroReceiver))
        
        // candidateReceivers (as non SysAdmin)
        setNonAdminUser()
        model = receiverDeploymentController.edit()
        assertNotNull(model.candidateReceivers)
        assertEquals(1, model.candidateReceivers.size())
        assertTrue(model.candidateReceivers.contains(imosReceiver))
    }
    
    void testReceiverRecoveryList()
    {
        def model = receiverRecoveryController.list()
        
        assertNotNull(model.readableProjects)
        assertEquals(2, model.readableProjects.size())
        assertTrue(model.readableProjects.contains(activeProj))
        assertTrue(model.readableProjects.contains(nonWriteProj))
    }
    
    void testTagCreate()
    {
        DeviceStatus status = new DeviceStatus(status:"NEW")
        mockDomain(DeviceStatus, [status])
        status.save()
        
        // candidateProjects
        def model = tagController.create()
        
        assertNotNull(model.candidateProjects)
        assertEquals(1, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(activeProj))
    }

    void testTagEdit()
    {
        def tag = new Tag()
        mockDomain(Tag, [tag])
        tag.save()
        
        // candidateProjects
        tagController.params.id = tag.id
        def model = tagController.edit()
        
        assertNotNull(model.candidateProjects)
        assertEquals(1, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(activeProj))
    }
    
    private void setNonAdminUser()
    {
        hasRole = false
    }
}
