package au.org.emii.aatams.detection

import org.joda.time.DateTime;

import au.org.emii.aatams.PermissionUtilsService;
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import grails.test.*

class DetectionExtractServiceTests extends AbstractGrailsUnitTestCase {
    def detectionExtractService
    def des

    def permissionUtilsService
    def newline = System.getProperty("line.separator")

    DateTime startTime = new DateTime("2010-01-01T12:34:56+11:00")
    DateTime endTime = new DateTime("2010-01-01T17:00:01+11:00")

    protected void setUp() {
        super.setUp()

        mockLogging(DetectionExtractService, true)
        detectionExtractService = new DetectionExtractService()

        mockLogging(PermissionUtilsService, true)
        detectionExtractService.permissionUtilsService = new PermissionUtilsService()

        des = detectionExtractService   // shorthand alias.
    }

    void testConstructQuery() {
        des.metaClass.getQueryBuilder = {
            def queryBuilder = new DetectionQueryBuilder()
            queryBuilder.metaClass.toSql = { 'some sql' }

            return queryBuilder
        }

        assertEquals('some sql', des.constructQuery())
    }

    void testRequestParamsToFilter() {

        // (K, V) is (input request parameter map, expected filter map)
        def requestParamsToExpectedFilter = [

            [:]:
            null,

            [ filter: [receiverDeployment: [station: [installation: [project: [in: ["name", "Whales, "]]]]]]]:
            [ 'in': [ [ field: 'project.name', values: ['Whales']]]],

            [ filter: [receiverDeployment: [station: [installation: [project: [in: ["name", "Whales, Sharks,  "]]]]]]]:
            [ 'in': [ [ field: 'project.name', values: ['Whales', 'Sharks']]]],

            [ filter: [receiverDeployment: [station: [installation: [project: [in: ["name", "Whales, "]], in:["name", "Bondi"]]]]]]:
            [ 'in': [
                [ field: 'project.name', values: ['Whales']],
                [ field: 'installation.name', values: ['Bondi']]
            ]],

            [ filter: [receiverDeployment: [station: [installation: [project: [in: ["name", "Whales, Sharks, "]], in:["name", "Bondi"]]]]]]:
            [ 'in': [
                [ field: 'project.name', values: ['Whales', 'Sharks']],
                [ field: 'installation.name', values: ['Bondi']]
            ]],

            [ filter: [receiverDeployment:[station:[installation:[in:["name", "Bondi, "]]]]]]:
            [ 'in': [ [ field: 'installation.name', values: ['Bondi']]]],

            [ filter: [receiverDeployment:[station:[in:["name", "CTBAR East, "]]]]]:
            [ 'in': [ [ field: 'installation_station.name', values: ['CTBAR East']]]],

            [ filter: [in:["transmitterId", "A69-1303-12345, "]]]:
            [ 'in': [ [ field: 'valid_detection.transmitter_id', values: ['A69-1303-12345']]]],

            [ filter: [surgeries:[release:[animal:[species:[in:["spcode", "12345, "]]]]]]]:
            [ 'in': [ [ field: 'spcode', values: ['12345']]]],

            [ filter: [between: ["0": "timestamp", "1": startTime.toDate(), "2": endTime.toDate()]]]:
            [ 'between': [ [ field: 'timestamp', start: startTime, end: endTime ]]],

            [
                filter: [
                    between: ["0": "timestamp", "1": startTime.toDate(), "2": endTime.toDate()],
                    receiverDeployment: [station: [installation: [project: [in: ["name", "Whales, "]]]]]
                ],
            ]:
            [
                'in': [
                    [ field: 'project.name', values: ['Whales'] ]
                ],
                'between': [ [ field: 'timestamp', start: startTime, end: endTime ]]
            ]
        ]

        requestParamsToExpectedFilter.each {
            requestParams, filter ->

            assertEquals(filter, des.requestParamsToFilter(requestParams))
        }
    }

    void testRequestParamsToQueryBuilderParams() {
        def requestParams = [ max: 123 ]

        assertEquals(123, des.requestParamsToQueryBuilderParams(requestParams).limit)
    }
}
