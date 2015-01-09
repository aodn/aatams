package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import au.org.emii.aatams.test.TestUtils

import grails.test.*

class InstallationStationControllerTests extends AbstractControllerUnitTestCase
{
    def dataSource
    def slurper = new XmlSlurper()

    void testExecuteInstallationStationNoFilter()
    {
        assertExport([:], "testExecuteInstallationStationNoFilter")
    }

    void testExecuteInstallationStationByProject()
    {
        assertExport([installation: [project: [eq: ["name", "Seal Count"]]]], "testExecuteInstallationStationByProject")
    }

    // TODO: move this to ExportServiceTests
    // Need to refactor the KML stuff out of ExportService and in to KmlService.
    void testExecuteStationKmlExtract()
    {
        TestUtils.createDetectionViews(dataSource)

        InstallationStation.metaClass.toKmlDescription = { "some description" }
        controller.params.format = "KML"

        assertExport([:], "testExecuteStationKmlExtract")
    }
}
