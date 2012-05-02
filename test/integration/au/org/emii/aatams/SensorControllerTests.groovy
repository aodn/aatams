package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class SensorControllerTests extends AbstractControllerUnitTestCase
{
	protected void setUp()
	{
		super.setUp()

		permitted = true
		controller.params.format = "CSV"
	}
	
	void testExecuteSensor()
	{
		assertExport([:], "testExecuteSensor")
	}
}
