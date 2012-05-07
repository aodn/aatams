package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class ReceiverEventControllerTests extends AbstractControllerUnitTestCase 
{
	protected void setUp()
	{
		super.setUp()

		controller.params.format = "CSV"
	}
	
	void testExecuteReceiverEventNoFilter()
	{
		assertExport([:], "testExecuteReceiverEventNoFilter")
	}
	
	void testExecuteReceiverEventByProject()
	{
		assertExport([receiverDeployment: [station: [installation: [project: [eq: ["name", "Tuna"]]]]]], "testExecuteReceiverEventByProject")
	}
}
