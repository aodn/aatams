package au.org.emii.aatams.detection

import org.joda.time.DateTime

import grails.test.*

class FilteredDetectionQueryBuilderTests extends GrailsUnitTestCase {

    def query
    DateTime startTime = new DateTime("2010-01-01T12:34:56+11:00")
    DateTime endTime = new DateTime("2010-01-01T17:00:01+11:00")

    void setUp() {
        super.setUp()

        query = new FilteredDetectionQueryBuilder()
    }

    void testGetSelect() {
        assertEquals('SELECT valid_detection.id as vd_id', query.select)
    }

    void testGetFrom() {
        assertEquals('FROM valid_detection', query.from)
    }

    void testGetOrderBy() {
        assertEquals('ORDER BY timestamp, receiver_name, valid_detection.transmitter_id', query.orderBy)
    }

    void testGetTxrJoinsWithoutTxrFilter() {
        query.metaClass.isFilteredByTxr = { return false }

        assertEquals([], query.getTxrJoins())
    }

    void testGetTxrJoinsWithTxrFilter() {
        query.metaClass.isFilteredByTxr = { return true }

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

    void testGetRxrJoinsWithoutRxrFilter() {
        query.metaClass.isFilteredByRxr = { return false }

        assertEquals([], query.getRxrJoins())
    }

    void testGetRxrJoinsWithRxrFilter() {
        query.metaClass.isFilteredByRxr = { return true }

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
        query.metaClass.getTxrJoins = { [ 'txr1', 'txr2' ] }
        query.metaClass.getRxrJoins = { [ 'rxr1', 'rxr2' ] }

        assertEquals(
            [ 'rxr1', 'rxr2', 'txr1', 'txr2' ],
            query.getJoins()
        )
    }

    void testGetLimitWithFilter() {
        query.metaClass.isFiltered = { return true }
        assertEquals('', query.getLimit([ limit: 123 ]))
    }

    void testGetLimitNoFilter() {
        query.metaClass.isFiltered = { return false }
        assertEquals('LIMIT 123', query.getLimit([ limit: 123 ]))
    }

    void testToSql() {
        assertEquals(
            [
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
