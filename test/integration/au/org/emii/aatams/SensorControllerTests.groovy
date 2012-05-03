package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class SensorControllerTests extends AbstractControllerUnitTestCase
{
	protected void setUp()
	{
		super.setUp()

		controller.params.format = "CSV"
	}
	
	void testExecuteSensorPermitted()
	{
		permitted = true
		assertExport([:], "testExecuteSensorPermitted")
	}

	void testExecuteSensorNotPermitted()
	{
		permitted = false
		assertExport([:], "testExecuteSensorNotPermitted")
	}
}
