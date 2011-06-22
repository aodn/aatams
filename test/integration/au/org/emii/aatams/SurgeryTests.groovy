package au.org.emii.aatams

import grails.test.*

class SurgeryTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

//    void testTagDeployedOnSurgery() 
//    {
//        Tag tag = Tag.findByCodeName("A69-1303-11111")
//        assert(tag)
//       
//        DeviceStatus newStatus = DeviceStatus.findByStatus('NEW')
//        DeviceStatus deployedStatus = DeviceStatus.findByStatus('DEPLOYED')
//        
//        assert(tag.status == newStatus)
//        
//        def surgeryController = new SurgeryController()
//        surgeryController.request.contentType = "text/json"
//        surgeryController.request.content = '{"id":1,"class":"Book","title":"The Stand"}'.getBytes()
// 
//        surgeryController.save()
//        
//        assert(tag.status == deployedStatus)
//    }
}
