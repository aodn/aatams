package au.org.emii.aatams

import grails.test.*
import grails.converters.JSON

class TagControllerTests extends ControllerUnitTestCase 
{
    DeviceStatus newStatus
    DeviceStatus deployedStatus
    DeviceStatus recoveredStatus
    
    def candidateEntitiesService
    
    def project1
    def project2
    
    protected void setUp() 
    {
        super.setUp()

        newStatus = new DeviceStatus(status:'NEW')
        deployedStatus = new DeviceStatus(status:'DEPLOYED')
        recoveredStatus = new DeviceStatus(status:'RECOVERED')
        def statusList = [newStatus, deployedStatus, recoveredStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }
        
        project1 = new Project()
        project2 = new Project()
        
        candidateEntitiesService = new CandidateEntitiesService()
        candidateEntitiesService.metaClass.projects =
        {
            return [project1, project2]
        }
        
        controller.candidateEntitiesService = candidateEntitiesService
        mockDomain(Tag)
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testLookupNonDeployedBySerialNumber() 
    {
        Tag newTag = new Tag(codeName:'A69-1303-1111', serialNumber:'1111-A', status:newStatus)
        Tag deployedTag = new Tag(codeName:'A69-1303-2222', serialNumber:'2222', status:deployedStatus)
        Tag recoveredTag = new Tag(codeName:'A69-1303-3333', serialNumber:'1111-B', status:recoveredStatus)
        def tagList = [newTag, deployedTag, recoveredTag]
        mockDomain(Tag, tagList)
        tagList.each { it.save() }
        
        controller.params.term = '1111'
        controller.lookupNonDeployedBySerialNumber()
        
        def controllerResponse = controller.response.contentAsString
        def jsonResult = JSON.parse(controllerResponse)
        
        assertEquals(2, jsonResult.size())
        
        assertEquals('A69-1303-1111', jsonResult[0].codeName)
        assertEquals(newStatus.status, jsonResult[0].status.status)
        assertEquals('A69-1303-3333', jsonResult[1].codeName)
        assertEquals(recoveredStatus.status, jsonResult[1].status.status)
    }
    
    void testNoSensorsInList()
    {
        Tag tag1 = new Tag(codeName:"1111", status:newStatus)
        Tag tag2 = new Tag(codeName:"2222", status:newStatus)
        def tagList = [tag1, tag2]

        Sensor sensor1 = new Sensor(tag:tag1)
        Sensor sensor2 = new Sensor(tag:tag1)
        def sensorList = [sensor1, sensor2]
        mockDomain(Tag, tagList + sensorList)
        
        tagList.each { it.save() }
        sensorList.each { it.save() }
        
        tag1.addToSensors(sensor1)
        tag1.addToSensors(sensor2)
        
        def model = controller.list()
        assertEquals(2, model.tagInstanceList.size())
        assertEquals(2, model.tagInstanceTotal)
        assertTrue(model.tagInstanceList.contains(tag1))
        assertTrue(model.tagInstanceList.contains(tag2))
    }
    
    void testCreate() 
    {
        def model = controller.create()
        
        assertNotNull(model.tagInstance)
        assertEquals(2, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(project1))
        assertTrue(model.candidateProjects.contains(project2))
    }

    void testSaveError() 
    {
        TransmitterType pinger = new TransmitterType(transmitterTypeName:"PINGER")
        mockDomain(TransmitterType, [pinger])
        pinger.save()
        
        def model = controller.save()
        
        assertNotNull(model.tagInstance)
        assertEquals(2, model.candidateProjects.size())
        assertTrue(model.candidateProjects.contains(project1))
        assertTrue(model.candidateProjects.contains(project2))
    }
}
