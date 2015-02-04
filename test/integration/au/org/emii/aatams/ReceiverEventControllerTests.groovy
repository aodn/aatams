package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import au.org.emii.aatams.test.TestUtils

import grails.test.*

class ReceiverEventControllerTests extends AbstractControllerUnitTestCase
{
    def dataSource

    protected void setUp()
    {
        super.setUp()

        TestUtils.createReceiverEventView(dataSource)

        controller.params.format = "CSV"
    }

    void testExecuteReceiverEventNoFilter()
    {
        assertExport([:], "testExecuteReceiverEventNoFilter")
    }

    void testExecuteReceiverEventByProject()
    {
        assertExport([receiverDeployment: [station: [installation: [project: [in: ["name", "Tuna"]]]]]], "testExecuteReceiverEventByProject")
    }
}
