package au.org.emii.aatams.test

import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * Utility methods common to all tests.
 *
 * @author jburgess
 */
class TestUtils
{
    static setupMessage(controller)
    {
        controller.metaClass.message = { LinkedHashMap args -> return "${args.code}" }
    }

    static void createDetectionViews(dataSource) {
        def sql = new Sql(dataSource)

        [ 'detection_view', 'detection_by_species_view' ].each {
            viewName ->

            sql.execute(String.valueOf("create or replace view ${viewName} as ${ConfigurationHolder.config.detection.extract[viewName].definition}"))
        }
    }

    static void createReceiverEventView(dataSource) {
        def sql = new Sql(dataSource)

        sql.execute(String.valueOf("create or replace view receiver_event_view as ${ConfigurationHolder.config.receiver_event.extract.receiver_event_view.definition}"))
    }

    enum VisibilityLevel {
        VISIBLE,
        VISIBLE_BUT_SANITISED,
        NOT_VISIBLE
    }
}
