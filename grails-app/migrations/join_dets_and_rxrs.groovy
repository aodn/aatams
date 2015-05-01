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

    changeSet(author: "jburgess", id: "1430268900000-02", runOnChange: true) {

        createView(
            '''select device.id, model_name || '-' || serial_number as receiver_name
               from device
               join device_model on device.model_id = device_model.id
               where device.class = 'au.org.emii.aatams.Receiver' ''',
            viewName: 'receiver'
        )

        createView(
            '''select receiver_id, receiver_deployment.id as receiver_deployment_id,
                 receiver_recovery.id as receiver_recovery_id,
                 initialisationdatetime_timestamp, recoverydatetime_timestamp
               from receiver_recovery
               join receiver_deployment on receiver_recovery.deployment_id = receiver_deployment.id''',
            viewName: 'deployment_and_recovery'
        )

        createProcedure(
            '''CREATE FUNCTION invalid_reason(
                 detection detection,
                   receiver receiver,
                   deployment_and_recovery deployment_and_recovery)
               RETURNS text AS $$
                 BEGIN
                   IF receiver.id IS NULL THEN
                     RETURN 'UNKNOWN_RECEIVER';
                   END IF;

                   IF deployment_and_recovery.receiver_deployment_id IS NULL THEN
                     RETURN 'NO_DEPLOYMENT_OR_RECOVERY_AT_DATE_TIME';
                   END IF;

                   RETURN NULL;
                 END;
               $$ LANGUAGE plpgsql;'''
        )

        createView(
            '''select detection.id, timestamp, detection.receiver_name, transmitter_id,
                 initialisationdatetime_timestamp, recoverydatetime_timestamp, receiver.id as receiver_id,
                 deployment_and_recovery.receiver_deployment_id, deployment_and_recovery.receiver_recovery_id,
                 invalid_reason(detection, receiver, deployment_and_recovery) as derived_reason, reason as set_reason,
                 location, receiver_download_id, sensor_unit, sensor_value, station_name,
                 transmitter_name, transmitter_serial_number

               from detection
               left join receiver on detection.receiver_name = receiver.receiver_name
               left join deployment_and_recovery
                 on receiver.id = deployment_and_recovery.receiver_id
                 and detection.timestamp >= initialisationdatetime_timestamp
                 and detection.timestamp <= recoverydatetime_timestamp;''',
            viewName: 'detection_with_reason'
        )

        createView(
            '''select detection_with_reason.id, 0 as version, location, receiver_deployment_id,
                 receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name,
                 timestamp, transmitter_id, transmitter_name, transmitter_serial_number, false as provisional
               from detection_with_reason
               where derived_reason is NULL''',
            viewName: 'valid_detection_view'
        )

        createView(
            '''select detection_with_reason.id, 0 as version, location, 'message' as message, derived_reason as reason,
                 receiver_download_id, receiver_name, sensor_unit, sensor_value, station_name,
                 timestamp, transmitter_id, transmitter_name, transmitter_serial_number
               from detection_with_reason
               where derived_reason is not NULL''',
            viewName: 'invalid_detection_view'
        )
    }
}
