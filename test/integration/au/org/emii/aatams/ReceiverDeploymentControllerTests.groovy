package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class ReceiverDeploymentControllerTests extends AbstractControllerUnitTestCase
{
	void testExecuteReceiverDeploymentNoFilter()
	{
		assertExport([:], "testExecuteReceiverDeploymentNoFilter")
	}
	
	void testExecuteReceiverDeploymentByProject()
	{
		assertExport([station:[installation:[project:[eq:["name", "Seal Count"]]]]], "testExecuteReceiverDeploymentByProject")
	}
	
	void testExecuteReceiverDeploymentByInstallation()
	{
		assertExport([station:[installation:[eq:["name", "Ningaloo Array"]]]], "testExecuteReceiverDeploymentByInstallation")
	}

	void testExecuteReceiverDeploymentByProjectAndInstallation()
	{
		assertExport([station:[installation:[project:[eq:["name", "Seal Count"]], eq:["name", "Heron Island Curtain"]]]], "testExecuteReceiverDeploymentByProjectAndInstallation")
	}
}
