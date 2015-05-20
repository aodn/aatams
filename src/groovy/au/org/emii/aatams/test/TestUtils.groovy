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

    enum VisibilityLevel {
        VISIBLE,
        VISIBLE_BUT_SANITISED,
        NOT_VISIBLE
    }
}
