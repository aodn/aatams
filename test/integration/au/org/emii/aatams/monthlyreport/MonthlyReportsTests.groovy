package au.org.emii.aatams.monthlyreport

import groovy.sql.Sql
import java.sql.SQLException

class MonthlyReportsTests extends GroovyTestCase {
    def dataSource

    // A failing test will cause subsequent tests to also fail with 'aborted transaction' if transactional.
    def transactional = false

    void testQueries() {
        def failureMessages = []

        new File('test/integration/au/org/emii/aatams/monthlyreport').eachFileMatch(~/.*.sql/) { sqlFile ->

            try {
                new Sql(dataSource).execute(sqlFile.text)
            }
            catch (SQLException e) {
                failureMessages << "${sqlFile.path} failed: ${e.message}"
            }
        }

        if (!failureMessages.isEmpty()) {
            fail(failureMessages.join(System.getProperty('line.separator')))
        }
    }
}
