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
}
