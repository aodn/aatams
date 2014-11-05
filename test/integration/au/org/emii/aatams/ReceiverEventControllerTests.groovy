package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

import org.joda.time.*

class ReceiverEventControllerTests extends AbstractControllerUnitTestCase
{
    protected void setUp()
    {
        buildEventsForProject(projectName: 'Shark', count: 3)
        buildEventsForProject(projectName: 'Tuna', count: 2)

        super.setUp()

        controller.params.format = "CSV"
    }

    void buildEventsForProject(params) {
        def project = Project.buildLazy(name: params.projectName)
        def installation = Installation.buildLazy(project: project)
        def station = InstallationStation.buildLazy(installation: installation, name: "${params.projectName} station")
        def receiverModel = ReceiverDeviceModel.buildLazy(modelName: 'VR2W')
        def receiver = Receiver.buildLazy(model: receiverModel, serialNumber: '101338')
        def deployment = ReceiverDeployment.buildLazy(station: station, receiver: receiver)

        def timestamp = new DateTime('2012-02-03T04:05:00')

        params.count.times {
            ValidReceiverEvent.buildLazy(timestamp: timestamp.plusSeconds(it).toDate(),
                                         receiverName: receiver.name,
                                         receiverDeployment: deployment,
                                         description: 'desc',
                                         data: '123',
                                         units: 'm')
        }
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
