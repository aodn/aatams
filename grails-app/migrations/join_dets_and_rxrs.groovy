databaseChangeLog = {
    changeSet(author: "jburgess", id: "1430268900000-01") {

        createTable(tableName: "detection") {
            column(name: "id", type: "int8") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "detection_pkey")
            }

            // The following three columns form the "real" primary key of the detection table.
            column(name: "timestamp", type: "TIMESTAMP WITH TIME ZONE") {
                constraints(nullable: "false")
            }
            column(name: "receiver_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
            column(name: "transmitter_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            // Possibly also forms part of the primary key.
            column(name: "receiver_download_id", type: "int8") {
                constraints(nullable: "false")
            }

            column(name: "location", type: "geometry")
            column(name: "sensor_unit", type: "VARCHAR(255)")
            column(name: "sensor_value", type: "FLOAT4(8,8)")
            column(name: "station_name", type: "VARCHAR(255)")
            column(name: "transmitter_name", type: "VARCHAR(255)")
            column(name: "transmitter_serial_number", type: "VARCHAR(255)")

            // Values for the columns here on can be determined dynamically, but we keep them for now for comparison.
            // invalid properties
            column(name: "reason", type: "VARCHAR(255)")

            // valid properties
            column(name: "receiver_deployment_id", type: "int8")
        }

        // move data
        grailsChange {
            change {
                sql.execute('''
                    insert into detection (id, timestamp, receiver_name, transmitter_id, receiver_download_id, location, sensor_unit, sensor_value, station_name, transmitter_name, transmitter_serial_number, receiver_deployment_id)
                      select nextval('hibernate_sequence'), timestamp, receiver_name, transmitter_id, receiver_download_id, location, sensor_unit, sensor_value, station_name, transmitter_name, transmitter_serial_number, receiver_deployment_id from valid_detection
                ''')

                sql.execute('''
                    insert into detection (id, timestamp, receiver_name, transmitter_id, receiver_download_id, location, sensor_unit, sensor_value, station_name, transmitter_name, transmitter_serial_number, reason)
                      select nextval('hibernate_sequence'), timestamp, receiver_name, transmitter_id, receiver_download_id, location, sensor_unit, sensor_value, station_name, transmitter_name, transmitter_serial_number, reason from invalid_detection
                ''')
            }
        }

        [ 'timestamp', 'receiver_name', 'transmitter_id', 'reason', 'receiver_download_id' ].each { columnName ->
            createIndex(indexName: "detection_${columnName}_index", tableName: 'detection', unique: 'false') {
                column(name: columnName)
            }
        }

        addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "detection", constraintName: "receiver_download_id_detection_fk", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencesUniqueColumn: "false")
    }
}
