package au.org.emii.aatams

import au.org.emii.aatams.report.ReportInfoService
import au.org.emii.aatams.report.filter.ReportFilterFactoryService
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
    
	def reportFilterFactoryService
	def reportInfoService

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
		
		mockLogging(ReportFilterFactoryService)
		reportFilterFactoryService = new ReportFilterFactoryService()
		mockLogging(ReportInfoService)
		reportInfoService = new ReportInfoService()
		controller.reportInfoService = reportInfoService
		controller.reportFilterFactoryService = reportFilterFactoryService

        controller.candidateEntitiesService = candidateEntitiesService
        mockDomain(Tag)
        
        mockConfig("grails.gorm.default.list.max = 10")
        controller.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}

		CodeMap codeMap = new CodeMap(codeMap:"A69-1303")
		mockDomain(CodeMap, [codeMap])
		codeMap.save()

		Tag newTag = new Tag(codeMap:codeMap, codeName:'A69-1303-1111', serialNumber:'1111-A', status:newStatus)
        Tag deployedTag = new Tag(codeMap:codeMap, codeName:'A69-1303-2222', serialNumber:'2222', status:deployedStatus)
        Tag recoveredTag = new Tag(codeMap:codeMap, codeName:'A69-1303-3333', serialNumber:'1111-B', status:recoveredStatus)
        def tagList = [newTag, deployedTag, recoveredTag]
        mockDomain(Tag, tagList)
        tagList.each { it.save() }
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testLookupNonDeployedBySerialNumber() 
    {
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
        Tag tag1 = new Tag(codeMap:"A69-9002", pingCode: 1111, codeName:"A69-9002-1111", status:newStatus)
        Tag tag2 = new Tag(codeMap:"A69-9002", pingCode: 2222, codeName:"A69-9002-2222", status:newStatus)
        def tagList = [tag1, tag2]
		mockDomain(Tag, tagList)
		tagList.each { it.save() }

        Sensor sensor1 = new Sensor(codeName: "sensor1", tag:tag1)
        Sensor sensor2 = new Sensor(codeName: "sensor2", tag:tag1)
        def sensorList = [sensor1, sensor2]
		mockDomain(Sensor, sensorList)
        sensorList.each { it.save() }
        
        tag1.addToSensors(sensor1)
        tag1.addToSensors(sensor2)
        
        def model = controller.list()
		
		println(model.entityList)
		
        assertEquals(2, model.entityList.size())
        assertEquals(2, model.total)
        assertTrue(model.entityList.contains(tag1))
        assertTrue(model.entityList.contains(tag2))
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
	
	void testLookupByCodeName()
	{
//		Tag newTag = new Tag(codeName:'A69-1303-1111', serialNumber:'1111-A', status:newStatus)
//        Tag deployedTag = new Tag(codeName:'A69-1303-2222', serialNumber:'2222', status:deployedStatus)
//        Tag recoveredTag = new Tag(codeName:'A69-1303-3333', serialNumber:'1111-B', status:recoveredStatus)
		
		assertLookupWithTerm(0, 'X')
		assertLookupWithTerm(1, '11')
		assertLookupWithTerm(1, 'A69-1303-1')
		assertLookupWithTerm(3, 'A69-')
		assertLookupWithTerm(3, '1303')
	}
	
	private void assertLookupWithTerm(expectedNumResults, term) 
	{
		controller.params.term = term
		controller.lookupByCodeName()

		def jsonResponse = JSON.parse(controller.response.contentAsString)
		assertEquals(expectedNumResults, jsonResponse.size())
		
		// Need to reset the response so that this method can be called multiple times within a single test case.
		// Also requires workaround to avoid exception, see: http://jira.grails.org/browse/GRAILS-6483
		mockResponse?.committed = false // Current workaround
		reset()
	}
}
