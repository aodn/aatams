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
        assertEquals(
            [des.getSelect(), des.getWhereClause(), des.getOrderByClause(), des.getLimitClause()]
                .grep { it }
                .join(newline),
            detectionExtractService.constructQuery()
        )
    }

    void testGetSelectNoConstraint() {
        assertTrue(des.getSelect().startsWith("SELECT ${des.selectColumns} FROM valid_detection"))
    }

    void testGetSelectCountOnlyFalse() {
        assertTrue(des.getSelect(countOnly: false).startsWith("SELECT ${des.selectColumns} FROM valid_detection"))
    }

    void testGetSelectCountOnlyTrue() {
        assertTrue(des.getSelect(countOnly: true).startsWith("SELECT ${des.selectCountColumns} FROM valid_detection"))
    }


    void testGetWhereClauseOneProject() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales'] ]
            ]
        ]

        assertEquals("WHERE project.name IN ('Whales')", des.getWhereClause(filter: filter))
    }

    void testGetWhereClauseOneProjectBlank() {
        def filter = [
            'in': [
                [ field: 'project.name', values: [' '] ]
            ]
        ]

        assertEquals("", des.getWhereClause(filter: filter))
    }

    void testGetWhereClauseTwoProjects() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales', 'Sharks'] ]
            ]
        ]

        assertEquals("WHERE project.name IN ('Whales', 'Sharks')", des.getWhereClause(filter: filter))
    }

    void testGetWhereClauseTwoProjectsOneInstallation() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales', 'Sharks'] ],
                [ field: 'installation.name', values: ['Bondi'] ]
            ]
        ]

        assertEquals(
            "WHERE project.name IN ('Whales', 'Sharks') AND installation.name IN ('Bondi')",
            des.getWhereClause(filter: filter))
    }

    void testGetWhereClauseTimestampRange() {
        def filter = [
            'between': [
                [ field: 'timestamp', start: startTime, end: endTime ]
            ]
        ]

        assertEquals(
            "WHERE timestamp BETWEEN '2010-01-01T12:34:56.000+11:00' AND '2010-01-01T17:00:01.000+11:00'",
            des.getWhereClause(filter: filter))
    }

    void testGetWhereClauseTimestampRangeAndOneProject() {
        def filter = [
            'between': [
                [ field: 'timestamp', start: startTime, end: endTime ]
            ],
            'in': [
                [ field: 'project.name', values: ['Whales'] ]
            ]
        ]

        assertEquals(
            "WHERE project.name IN ('Whales') AND " +
                "timestamp BETWEEN '2010-01-01T12:34:56.000+11:00' AND '2010-01-01T17:00:01.000+11:00'",
            des.getWhereClause(filter: filter))
    }

    void testGetWhereClausePageIndex() {
        PageIndex index = new PageIndex(
            timestamp: new DateTime("2010-01-01T12:34:56+11:00"),
            receiverName: 'VR2W-1234',
            transmitterId: 'A69-1303-5678'
        )

        assertEquals(
            "WHERE (timestamp, receiver_name, valid_detection.transmitter_id) > " +
              "('2010-01-01T12:34:56.000+11:00', 'VR2W-1234', 'A69-1303-5678')",
            des.getWhereClause(pageIndex: index)
        )
    }

    void testGetWhereClauseOneProjectAndPageIndex() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales'] ]
            ]
        ]

        assertEquals("WHERE project.name IN ('Whales')", des.getWhereClause(filter: filter))
        PageIndex index = new PageIndex(
            timestamp: new DateTime("2010-01-01T12:34:56+11:00"),
            receiverName: 'VR2W-1234',
            transmitterId: 'A69-1303-5678'
        )

        assertEquals(
            "WHERE (timestamp, receiver_name, valid_detection.transmitter_id) > " +
              "('2010-01-01T12:34:56.000+11:00', 'VR2W-1234', 'A69-1303-5678') " +
              "AND project.name IN ('Whales')",
            des.getWhereClause(filter: filter, pageIndex: index)
        )
    }

    // test order by count only
    void testGetOrderByNoConstraint() {
        assertEquals('ORDER BY timestamp, receiver_name, valid_detection.transmitter_id', des.getOrderByClause())
    }

    void testGetOrderByCountOnlyFalse() {
        assertEquals(
            'ORDER BY timestamp, receiver_name, valid_detection.transmitter_id',
            des.getOrderByClause(countOnly: false)
        )
    }

    void testGetOrderByCountOnlyTrue() {
        assertEquals(
            '',
            des.getOrderByClause(countOnly: true)
        )
    }

    void testGetLimitNoConstraint() {
        assertEquals('', des.getLimitClause())
    }

    void testGetLimitConstraintHasLimit() {
        assertEquals('LIMIT 20', des.getLimitClause(limit: 20))
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
}
