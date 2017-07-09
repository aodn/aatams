package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerIntegrationTestCase

import grails.test.*

class ReceiverEventControllerIntegrationTests extends AbstractControllerIntegrationTestCase {
    def dataSource

    protected void setUp() {
        super.setUp()

        controller.params.format = "CSV"
    }

    void testExecuteReceiverEventNoFilter() {
        assertExport([:], "testExecuteReceiverEventNoFilter")
    }

    void testExecuteReceiverEventByProject() {
        assertExport([receiverDeployment: [station: [installation: [project: [in: ["name", "Tuna"]]]]]], "testExecuteReceiverEventByProject")
    }
}
