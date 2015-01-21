package au.org.emii.aatams.detection

import au.org.emii.aatams.*
import au.org.emii.aatams.test.*

import groovy.sql.*

abstract class AbstractJdbcTemplateVueDetectionFileProcessorServiceIntegrationTests extends AbstractGrailsUnitTestCase
{
    def dataSource
    def grailsApplication
    def jdbcTemplateVueDetectionFileProcessorService
    def sql

    protected void setUp()
    {
        super.setUp()

        sql = Sql.newInstance(dataSource)
        createMatviews()
    }

    protected void tearDown()
    {
        Statistics.findByKey('numValidDetections')?.delete()
        dropMatviews()

        super.tearDown()
    }

    private def dropMatviews()
    {
        def viewName = grailsApplication.config.rawDetection.extract.view.name
        def viewSelect = grailsApplication.config.rawDetection.extract.view.select
        sql.execute('DROP VIEW IF EXISTS ' + viewName)
        sql.execute('DROP TABLE IF EXISTS matviews;')
        sql.execute('DROP TABLE IF EXISTS detection_extract_view_mv;')

    }

    private def createMatviews()
    {
        // TODO: this should really be handled by the migration scripts, i.e. running
        // the migrations as part of grails test-app.  However, only later version
        // of the plugin support this, grails 1.3.7 doesn't seem to be compatible with
        // later versions.
        //
        // Also, some work needs to be done to reconcile reference data and bootstrap initialised data
        // (it should probably all be done in migrations specific to test env).
        //

        def viewName = grailsApplication.config.rawDetection.extract.view.name
        def viewSelect = grailsApplication.config.rawDetection.extract.view.select
        sql.execute ('DROP VIEW IF EXISTS ' + viewName)
        sql.execute ('create or replace view ' + viewName + ' as ' + viewSelect)

        // create matview table
        sql.execute("DROP TABLE IF EXISTS matviews;")
        sql.execute("CREATE TABLE matviews (mv_name NAME, v_name NAME, last_refresh TIMESTAMP WITH TIME ZONE);")

        // create create_matview function
        sql.execute('''
                        CREATE OR REPLACE FUNCTION create_matview(NAME, NAME)
                        RETURNS VOID
                        SECURITY DEFINER
                        LANGUAGE plpgsql AS
                        $$
                        DECLARE
                            matview ALIAS FOR $1;
                            view_name ALIAS FOR $2;
                            entry matviews%ROWTYPE;
                        BEGIN
                            SELECT * INTO entry FROM matviews WHERE mv_name = matview;

                            IF FOUND THEN
                                RAISE EXCEPTION 'Materialized view % already exists.', matview;
                            END IF;

                            EXECUTE 'REVOKE ALL ON ' || view_name || ' FROM PUBLIC';

                            EXECUTE 'GRANT SELECT ON ' || view_name || ' TO PUBLIC';

                            EXECUTE 'CREATE TABLE ' || matview || ' AS SELECT * FROM ' || view_name;

                            EXECUTE 'REVOKE ALL ON ' || matview || ' FROM PUBLIC';

                            EXECUTE 'GRANT SELECT ON ' || matview || ' TO PUBLIC';

                            INSERT INTO matviews (mv_name, v_name, last_refresh)
                              VALUES (matview, view_name, CURRENT_TIMESTAMP);

                            RETURN;
                        END;
                        $$''')

        sql.execute('DROP TABLE IF EXISTS detection_extract_view_mv')
        sql.execute("select create_matview('detection_extract_view_mv', 'detection_extract_view');")

        def statistics = Statistics.findByKey('numValidDetections')?: new Statistics(key: 'numValidDetections', value: sql.firstRow('select count(*) from valid_detection;').count)
        statistics.save(failOnError: true, flush: true)
    }
}
