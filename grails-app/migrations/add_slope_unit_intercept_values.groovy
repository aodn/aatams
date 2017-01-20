databaseChangeLog = {

    changeSet(author: 'ankit', id: '1675295815000-08') {
        grailsChange {
            change {
                sql.execute("update aatams.transmitter_type set is_slope_needed = 'true' where transmitter_type_name = 'PRESSURE'")
                sql.execute("update aatams.transmitter_type set is_intercept_needed = 'true' where transmitter_type_name = 'PRESSURE'")
                sql.execute("update aatams.transmitter_type set is_unit_needed = 'true' where transmitter_type_name = 'PRESSURE'")


                sql.execute("update aatams.transmitter_type set is_slope_needed = 'true' where transmitter_type_name = 'TEMPERATURE'")
                sql.execute("update aatams.transmitter_type set is_intercept_needed = 'true' where transmitter_type_name = 'TEMPERATURE'")
                sql.execute("update aatams.transmitter_type set is_unit_needed = 'true' where transmitter_type_name = 'TEMPERATURE'")


                sql.execute("update aatams.transmitter_type set is_slope_needed = 'true' where transmitter_type_name = 'ACCELEROMETER'")
                sql.execute("update aatams.transmitter_type set is_intercept_needed = 'true' where transmitter_type_name = 'ACCELEROMETER'")
                sql.execute("update aatams.transmitter_type set is_unit_needed = 'true' where transmitter_type_name = 'ACCELEROMETER'")
            }
        }
    }
}
