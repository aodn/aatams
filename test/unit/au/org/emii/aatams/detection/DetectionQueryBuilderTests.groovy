package au.org.emii.aatams.detection

import org.joda.time.DateTime

import grails.test.*

class DetectionQueryBuilderTests extends GrailsUnitTestCase {

    def query
    DateTime startTime = new DateTime("2010-01-01T12:34:56+11:00")
    DateTime endTime = new DateTime("2010-01-01T17:00:01+11:00")

    void setUp() {
        super.setUp()

        query = new DetectionQueryBuilder()
    }

    void testGetSelectColumns() {
        assertEquals(
            [
                "valid_detection.id as valid_detection_id",
                "to_char((timestamp::timestamp with time zone) at time zone '00:00', 'YYYY-MM-DD HH24:MI:SS') as formatted_timestamp",
                "valid_detection.transmitter_id as valid_detection_transmitter_id",
                "installation_station.name as installation_station_name",
                "st_y(installation_station.location) as latitude",
                "st_x(installation_station.location) as longitude",
                "sec_user.name as uploader",
                "organisation.name as organisation_name",
                "animal_release.project_id as release_project_id",
                "*"
            ],
            query.selectColumns
        )

    }

    void testGetSelect() {
        assertEquals("SELECT ${query.selectColumns.join(', ')}", query.select)
    }

    void testGetFrom() {
        assertEquals('FROM detection_ids', query.from)
    }

    void testGetOrderBy() {
        assertEquals('ORDER BY timestamp, receiver_name, valid_detection.transmitter_id', query.orderBy)
    }

    void testGetTxrJoins() {
        assertEquals(
            [
                "left join sensor on valid_detection.transmitter_id = sensor.transmitter_id",
                "left join device tag on sensor.tag_id = tag.id",
                "left join surgery on tag.id = surgery.tag_id",
                "left join animal_release on surgery.release_id = animal_release.id",
                "left join animal on animal_release.animal_id = animal.id",
                "left join species on animal.species_id = species.id"
            ],
            query.getTxrJoins())
    }

    void testGetRxrJoins() {
        assertEquals(
            [
                "join receiver_deployment on receiver_deployment_id = receiver_deployment.id",
                "join installation_station on receiver_deployment.station_id = installation_station.id",
                "join installation on installation_station.installation_id = installation.id",
                "join project on installation.project_id = project.id"
            ],
            query.getRxrJoins()
        )
    }

    void testGetJoins() {
        query.metaClass.getValidDetectionJoin = { 'vd1' }
        query.metaClass.getTxrJoins = { [ 'txr1', 'txr2' ] }
        query.metaClass.getRxrJoins = { [ 'rxr1', 'rxr2' ] }
        query.metaClass.getOrgAndUserJoins = { [ 'org1' ] }

        assertEquals(
            [ 'vd1', 'rxr1', 'rxr2', 'txr1', 'txr2', 'org1' ],
            query.getJoins()
        )
    }

    void testGetWhereOneProject() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales'] ]
            ]
        ]

        assertTrue(query.isFilteredByRxr(filter: filter))
        assertFalse(query.isFilteredByTxr(filter: filter))
        assertEquals(["project.name IN ('Whales')"], query.getWhere(filter: filter))
    }

    void testGetWhereTwoProjects() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales', 'Sharks'] ]
            ]
        ]

        assertTrue(query.isFilteredByRxr(filter: filter))
        assertFalse(query.isFilteredByTxr(filter: filter))
        assertEquals(["project.name IN ('Whales', 'Sharks')"], query.getWhere(filter: filter))
    }

    void testGetWhereTwoProjectsOneInstallation() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales', 'Sharks'] ],
                [ field: 'installation.name', values: ['Bondi'] ]
            ]
        ]

        assertTrue(query.isFilteredByRxr(filter: filter))
        assertFalse(query.isFilteredByTxr(filter: filter))
        assertEquals(
            [
                "project.name IN ('Whales', 'Sharks')",
                "installation.name IN ('Bondi')"
            ], query.getWhere(filter: filter))
    }

    void testGetWhereOneInstallation() {
        def filter = [
            'in': [
                [ field: 'installation.name', values: ['Bondi'] ]
            ]
        ]

        assertTrue(query.isFilteredByRxr(filter: filter))
        assertFalse(query.isFilteredByTxr(filter: filter))
        assertEquals(
            [
                "installation.name IN ('Bondi')"
            ], query.getWhere(filter: filter))
    }

    void testGetWhereOneStation() {
        def filter = [
            'in': [
                [ field: 'installation_station.name', values: ['BL 2'] ]
            ]
        ]

        assertTrue(query.isFilteredByRxr(filter: filter))
        assertFalse(query.isFilteredByTxr(filter: filter))
        assertEquals(
            [
                "installation_station.name IN ('BL 2')"
            ], query.getWhere(filter: filter))
    }

    void testGetWhereOneSpecies() {
        def filter = [
            'in': [
                [ field: 'spcode', values: ['1111'] ]
            ]
        ]

        assertFalse(query.isFilteredByRxr(filter: filter))
        assertTrue(query.isFilteredByTxr(filter: filter))
        assertEquals(
            [
                "spcode IN ('1111')"
            ], query.getWhere(filter: filter))
    }

    void testGetWhereTwoSpecies() {
        def filter = [
            'in': [
                [ field: 'spcode', values: ['1111', '2222'] ]
            ]
        ]

        assertFalse(query.isFilteredByRxr(filter: filter))
        assertTrue(query.isFilteredByTxr(filter: filter))
        assertEquals(
            [
                "spcode IN ('1111', '2222')"
            ], query.getWhere(filter: filter))
    }

   void testGetWhereTimestampRange() {
        def filter = [
            'between': [
                [ field: 'timestamp', start: startTime, end: endTime ]
            ]
        ]

        assertFalse(query.isFilteredByRxr(filter: filter))
        assertFalse(query.isFilteredByTxr(filter: filter))
        assertEquals(
            [
                "timestamp BETWEEN '2010-01-01T12:34:56.000+11:00' AND '2010-01-01T17:00:01.000+11:00'"
            ], query.getWhere(filter: filter))
    }

    void testGetWhereTimestampRangeAndOneProject() {
        def filter = [
            'between': [
                [ field: 'timestamp', start: startTime, end: endTime ]
            ],
            'in': [
                [ field: 'project.name', values: ['Whales'] ]
            ]
        ]

        assertTrue(query.isFilteredByRxr(filter: filter))
        assertFalse(query.isFilteredByTxr(filter: filter))
        assertEquals(
            [
                "project.name IN ('Whales')",
                "timestamp BETWEEN '2010-01-01T12:34:56.000+11:00' AND '2010-01-01T17:00:01.000+11:00'"
            ], query.getWhere(filter: filter))
    }

    void testGetWherePageIndex() {
        PageIndex index = new PageIndex(
            timestamp: new DateTime("2010-01-01T12:34:56+11:00"),
            receiverName: 'VR2W-1234',
            transmitterId: 'A69-1303-5678'
        )

        assertEquals(
            [
                "(timestamp, receiver_name, valid_detection.transmitter_id) > " +
                  "('2010-01-01T12:34:56.000+11:00', 'VR2W-1234', 'A69-1303-5678')"
            ],
            query.getWhere(pageIndex: index)
        )
    }

    void testGetWhereOneProjectAndPageIndex() {
        def filter = [
            'in': [
                [ field: 'project.name', values: ['Whales'] ]
            ]
        ]

        PageIndex index = new PageIndex(
            timestamp: new DateTime("2010-01-01T12:34:56+11:00"),
            receiverName: 'VR2W-1234',
            transmitterId: 'A69-1303-5678'
        )

        assertEquals(
            [
                "(timestamp, receiver_name, valid_detection.transmitter_id) > " +
                  "('2010-01-01T12:34:56.000+11:00', 'VR2W-1234', 'A69-1303-5678')",
                "project.name IN ('Whales')"
            ],
            query.getWhere(filter: filter, pageIndex: index)
        )
    }

    void testGetLimitWithFilter() {
        query.metaClass.isFiltered = { return true }
        assertEquals('LIMIT 123', query.getLimit([ limit: 123 ]))
    }

    void testGetLimitNoFilter() {
        query.metaClass.isFiltered = { return false }
        assertEquals('LIMIT 123', query.getLimit([ limit: 123 ]))
    }

    void testGetWith() {
        assertEquals(
            [
                "WITH filtered_detections as (${query.getFilteredDetectionsCet()})",
                "detection_ids as (select vd_id from filtered_detections ${query.getLimit()})"
            ].join(",${System.getProperty('line.separator')}"),
            query.getWith()
        )
    }

    void testToSql() {
        assertEquals(
            [
                query.getWith(),
                query.select,
                query.from,
                query.getJoins().join('\n'),
                query.getWhereClause(),
                query.orderBy,
                query.getLimit()
            ].join('\n'),
            query.toSql()
        )
    }
}
