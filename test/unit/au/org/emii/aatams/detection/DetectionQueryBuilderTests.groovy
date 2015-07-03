package au.org.emii.aatams.detection

import org.joda.time.DateTime;

import au.org.emii.aatams.PermissionUtilsService;
import au.org.emii.aatams.test.AbstractGrailsUnitTestCase

import grails.test.*

import org.jooq.conf.ParamType

class DetectionQueryBuilderTests extends GrailsUnitTestCase {
    void testConstructQueryNoFilterParams() {
        assertQueryFromFilterEquals('', [:])
    }

    void testConstructQueryOneProject() {
        assertQueryFromFilterEquals(
            '''where "rxr_project_name" in ('Whales')''',
            [filter: [receiverDeployment:[station:[installation:[project:[in: ["name", "Whales | "]]]]]]]
        )
    }

    void testConstructQueryTwoProjects() {
        assertQueryFromFilterEquals(
            '''where "rxr_project_name" in ('Whales', 'Sharks')''',
            [filter: [receiverDeployment:[station:[installation:[project:[in:["name", "Whales | Sharks | "]]]]]]]
        )
    }

    void testConstructQueryOneProjectOneInstallation() {
        assertQueryFromFilterEquals(
            '''where ("rxr_project_name" in ('Whales') and "installation_name" in ('Bondi'))''',
            [filter: [receiverDeployment:[station:[installation:[project:[in:["name", "Whales | "]], in:["name", "Bondi"]]]]]]
        )
    }

    void testConstructQueryTwoProjectsOneInstallation() {
        assertQueryFromFilterEquals(
            '''where ("rxr_project_name" in ('Whales', 'Sharks') and "installation_name" in ('Bondi'))''',
            [filter: [receiverDeployment:[station:[installation:[project:[in:["name", "Whales | Sharks | "]], in:["name", "Bondi"]]]]]]
        )
    }

    void testConstructQueryOneInstallation() {
        assertQueryFromFilterEquals(
            '''where "installation_name" in ('Bondi')''',
            [filter: [receiverDeployment:[station:[installation:[in:["name", "Bondi | "]]]]]]
        )
    }

    void testConstructQueryOneStation() {
        assertQueryFromFilterEquals(
            '''where "station_station_name" in ('CTBAR East')''',
            [filter: [receiverDeployment:[station:[in:["name", "CTBAR East | "]]]]]
        )
    }

    void testConstructQueryOneTagID() {
        assertQueryFromFilterEquals(
            '''where "transmitter_id" in ('A69-1303-12345')''',
            [filter: [in:["transmitterId", "A69-1303-12345 | "]]]
        )
    }

    void testConstructQueryTimestampRange() {
        DateTime startTime = new DateTime("2010-01-01T12:34:56")
        DateTime endTime = new DateTime("2010-01-01T17:00:01")

        assertQueryFromFilterEquals(
            """where "timestamp" between timestamp '2010-01-01 12:34:56.0' and timestamp '2010-01-01 17:00:01.0'""",
            [filter: [between: ["0": "timestamp", "1": startTime.toDate(), "2": endTime.toDate()]]]
        )
    }

    void testConstructQueryTimestampRangeOneProject() {
        DateTime startTime = new DateTime("2010-01-01T12:34:56")
        DateTime endTime = new DateTime("2010-01-01T17:00:01")

        assertQueryFromFilterEquals(
            """where ("rxr_project_name" in ('Whales') and "timestamp" between timestamp '2010-01-01 12:34:56.0' and timestamp '2010-01-01 17:00:01.0')""",
            [filter: [between: ["0": "timestamp", "1": startTime.toDate(), "2": endTime.toDate()],
                      receiverDeployment:[station:[installation:[project:[in:["name", "Whales | "]]]]]]]
        )
    }

    void testConstructQueryOffsetLimit() {
        def limit = 1
        def offset = 2

        assertQueryFromFilterEquals(
            "limit ${limit} offset ${offset}",
            [ limit: limit, offset: offset ]
        )
    }

    void testConstructCountQuery() {
        assertEquals(
            "select count(*) from ${new DetectionQueryBuilder().getViewName()}".trim(),
            new DetectionQueryBuilder().constructCountQuery([:]).getSQL(ParamType.INLINED).trim()
        )
    }

    void testGetViewNameNoFilter() {
        assertEquals("valid_detection", new DetectionQueryBuilder().getViewName([:]))
    }

    void testGetViewNameOneProjectFilter() {
        assertEquals(
            "valid_detection",
            new DetectionQueryBuilder().getViewName([filter: [receiverDeployment:[station:[installation:[project:[in: ["name", "Whales | "]]]]]]])
        )
    }

    void testGetViewNameOneSpeciesFilter() {
        assertEquals(
            "valid_detection",
            new DetectionQueryBuilder().getViewName([filter: [surgeries:[release:[animal:[species:[in:["spcode", "12345 | "]]]]]]])
        )
    }

    void testGetViewNameEmptySpeciesFilter() {
        assertEquals(
            "valid_detection",
            new DetectionQueryBuilder().getViewName([filter: [surgeries:[release:[animal:[species:[in:["spcode", ""]]]]]]])
        )
    }

    void testGetViewNameOneSpeciesOneProjectFilter() {
        assertEquals(
            "valid_detection",
            new DetectionQueryBuilder().getViewName(
                [filter: [receiverDeployment:[station:[installation:[project:[in: ["name", "Whales | "]]]]],
                          surgeries:[release:[animal:[species:[in:["spcode", "12345 | "]]]]]]]
            )
        )
    }

    def assertQueryFromFilterEquals(expectedSql, filter) {
        assertEquals(
            "select * from ${new DetectionQueryBuilder().getViewName()} ${expectedSql}".trim(),
            new DetectionQueryBuilder().constructQuery(filter).getSQL(ParamType.INLINED).trim()
        )
    }
}
