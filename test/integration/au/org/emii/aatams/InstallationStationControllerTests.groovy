package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class InstallationStationControllerTests extends AbstractControllerUnitTestCase 
{
	void testExecuteInstallationStationNoFilter()
	{
		assertExport([:], "testExecuteInstallationStationNoFilter")
	}
	
	void testExecuteInstallationStationByProject()
	{
		assertExport([installation: [project: [eq: ["name", "Seal Count"]]]], "testExecuteInstallationStationByProject")
	}
}
