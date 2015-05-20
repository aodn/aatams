package au.org.emii.aatams

import grails.test.*
import groovy.sql.Sql

class InstallationStationTests extends GroovyTestCase {

    def dataSource

    // Tests a null station that doesn't exist in view
    void testStationJustAddedNotInMaterializedView() {
        // A station that doesn't exist in the materialized view
        // should return 0
        def installationStation = new InstallationStation()
        assertEquals(0, installationStation.getDetectionCount())
    }

    // Tests a station with detections
    void testStationHasDetections() {
        def sql = new Sql(dataSource)
        // Inject a new station to the view and query its detection count
        sql.execute ('''INSERT INTO detection_count_per_station_mv (station_id, detection_count) VALUES (0, 100);''')
        def installationStation = new InstallationStation()
        installationStation.id = 0
        assertEquals(100, installationStation.getDetectionCount())
    }
}
