package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder

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
        InstallationStation.metaClass.toKmlDescription = { "some description" }
        controller.params.format = "KML"

        assertExport([:], "testExecuteStationKmlExtract")
    }
}
