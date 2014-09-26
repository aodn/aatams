package au.org.emii.aatams.detection

import org.joda.time.DateTime;

import au.org.emii.aatams.PermissionUtilsService;
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import grails.test.*

class DetectionExtractServiceTests extends AbstractGrailsUnitTestCase
{
    def detectionExtractService
    def permissionUtilsService

    protected void setUp()
    {
        super.setUp()

        mockLogging(DetectionExtractService, true)
        detectionExtractService = new DetectionExtractService()

        mockLogging(PermissionUtilsService, true)
        detectionExtractService.permissionUtilsService = new PermissionUtilsService()
    }

    protected void tearDown()
    {
        super.tearDown()
    }

    def getExpectedViewName()
    {
        return "detection_extract_view"
    }

    void testConstructQueryNoFilterParams()
    {
        println "query: " + detectionExtractService.constructQuery([:], 10000, 0)
        assertEquals("${DetectionExtractService.SELECT} limit 10000 offset 0", detectionExtractService.constructQuery([:], 10000, 0))
    }

    void testConstructQueryOneProject()
    {
        assertEquals("${DetectionExtractService.SELECT} where project.name in ('Whales') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [receiverDeployment:[station:[installation:[project:[in: ["name", "Whales, "]]]]]]], 10000, 0))
    }

    void testConstructQueryTwoProjects()
    {
        assertEquals("${DetectionExtractService.SELECT} where project.name in ('Whales', 'Sharks') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [receiverDeployment:[station:[installation:[project:[in:["name", "Whales, Sharks, "]]]]]]], 10000, 0))
    }

    void testConstructQueryOneProjectOneInstallation()
    {
        assertEquals("${DetectionExtractService.SELECT} where project.name in ('Whales') and installation.name in ('Bondi') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [receiverDeployment:[station:[installation:[project:[in:["name", "Whales, "]], in:["name", "Bondi"]]]]]], 10000, 0))
    }

    void testConstructQueryTwoProjectsOneInstallation()
    {
        assertEquals("${DetectionExtractService.SELECT} where project.name in ('Whales', 'Sharks') and installation.name in ('Bondi') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [receiverDeployment:[station:[installation:[project:[in:["name", "Whales, Sharks, "]], in:["name", "Bondi"]]]]]], 10000, 0))
    }

    void testConstructQueryOneInstallation()
    {
        assertEquals("${DetectionExtractService.SELECT} where installation.name in ('Bondi') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [receiverDeployment:[station:[installation:[in:["name", "Bondi, "]]]]]], 10000, 0))
    }

    void testConstructQueryOneStation()
    {
        assertEquals("${DetectionExtractService.SELECT} where installation_station.name in ('CTBAR East') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [receiverDeployment:[station:[in:["name", "CTBAR East, "]]]]], 10000, 0))
    }

    void testConstructQueryOneTagID()
    {
        assertEquals("${DetectionExtractService.SELECT} where valid_detection.transmitter_id in ('A69-1303-12345') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [in:["transmitterId", "A69-1303-12345, "]]], 10000, 0))
    }

    void testConstructQueryOneSpecies()
    {
        assertEquals("${DetectionExtractService.SELECT} where spcode in ('12345') limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [surgeries:[release:[animal:[species:[in:["spcode", "12345, "]]]]]]], 10000, 0))
    }

    void testConstructQueryTimestampRange()
    {
        DateTime startTime = new DateTime("2010-01-01T12:34:56")
        DateTime endTime = new DateTime("2010-01-01T17:00:01")

        assertEquals("${DetectionExtractService.SELECT} where timestamp between '2010-01-01 12:34:56.0' and '2010-01-01 17:00:01.0' limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [between: ["0": "timestamp", "1": startTime.toDate(), "2": endTime.toDate()]]], 10000, 0))
    }

    void testConstructQueryTimestampRangeOneProject()
    {
        DateTime startTime = new DateTime("2010-01-01T12:34:56")
        DateTime endTime = new DateTime("2010-01-01T17:00:01")

        assertEquals("${DetectionExtractService.SELECT} where project.name in ('Whales') and timestamp between '2010-01-01 12:34:56.0' and '2010-01-01 17:00:01.0' limit 10000 offset 0",
                     detectionExtractService.constructQuery([filter: [between: ["0": "timestamp", "1": startTime.toDate(), "2": endTime.toDate()],
                                                              receiverDeployment:[station:[installation:[project:[in:["name", "Whales, "]]]]]]], 10000, 0))
    }
}
