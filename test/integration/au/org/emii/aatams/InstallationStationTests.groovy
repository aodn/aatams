package au.org.emii.aatams

import grails.test.*
import groovy.sql.Sql

class InstallationStationTests extends GroovyTestCase {

    def dataSource

    protected void setUp() {
        super.setUp()

        def sql = new Sql(dataSource)
        // TODO: Ugly but no other way!
        // Mock the detection_count_per_station_mv table
        sql.execute ("""CREATE TABLE detection_count_per_station_mv
(
  station character varying(255),
  installation character varying(255),
  project character varying(255),
  public_location geometry,
  public_lon double precision,
  public_lat double precision,
  installation_station_url text,
  detection_download_url text,
  detection_count bigint,
  relative_detection_count double precision,
  station_id bigint
)
WITH (
  OIDS=FALSE
);""")
    }

    protected void tearDown() {
        def sql = new Sql(dataSource)
        // Delete the materialized view we've created
        sql.execute ('''DROP TABLE detection_count_per_station_mv;''')

        super.tearDown()
    }

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
