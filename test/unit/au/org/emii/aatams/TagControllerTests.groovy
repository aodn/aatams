package au.org.emii.aatams

import grails.test.*

class TagControllerTests extends ControllerUnitTestCase 
{
    protected void setUp() 
    {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testLookupNonDeployedByCodeName() 
    {
        DeviceStatus newStatus = new DeviceStatus(status:'NEW')
        DeviceStatus deployedStatus = new DeviceStatus(status:'DEPLOYED')
        DeviceStatus recoveredStatus = new DeviceStatus(status:'RECOVERED')
        def statusList = [newStatus, deployedStatus, recoveredStatus]
        mockDomain(DeviceStatus, statusList)
        statusList.each { it.save() }
        
        Tag newTag = new Tag(codeName:'A69-1303-1111', status:newStatus)
        Tag deployedTag = new Tag(codeName:'A69-1303-2222', status:deployedStatus)
        Tag recoveredTag = new Tag(codeName:'A69-1303-3333', status:recoveredStatus)
        def tagList = [newTag, deployedTag, recoveredTag]
        mockDomain(Tag, tagList)
        tagList.each { it.save() }
        
        controller.params.term = 'A69-1303'
        def tagAsJson = controller.lookupNonDeployedByCodeName()
        
        println tagAsJson
       // assertEquals(2, model.)
    }
}
