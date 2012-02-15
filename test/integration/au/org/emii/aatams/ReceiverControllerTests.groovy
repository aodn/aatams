package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class ReceiverControllerTests extends AbstractControllerUnitTestCase 
{
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSaveWithWithspaceAroundSerialNumber() 
	{
		controller.params.serialNumber = " 234 "
		controller.params.organisation = Organisation.findByName("IMOS")
		controller.params.model = ReceiverDeviceModel.findByModelName("VR2W")
		controller.save()
		
		def receiver = Receiver.get(controller.redirectArgs.id)
		assertEquals("234", receiver.serialNumber)
		
		receiver.delete(failOnError:true)
    }
}
