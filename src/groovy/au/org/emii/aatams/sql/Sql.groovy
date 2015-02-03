package au.org.emii.aatams.sql

import groovy.transform.InheritConstructors

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@InheritConstructors
class Sql extends groovy.sql.Sql {
    Integer fetchSize
    Boolean autoCommit

    @Override
    protected Connection createConnection() throws SQLException {
        Connection connection = super.createConnection();
        if (autoCommit != null) {
            connection.setAutoCommit(autoCommit)
        }
        return connection
    }

    @Override
    protected void configure(Statement statement) {
        if (fetchSize != null) {
            statement.fetchSize = fetchSize
        }
    }

}
