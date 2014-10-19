databaseChangeLog = {

    changeSet(author: "jburgess", id: "14123169288000-01") {

        //
        // Find duplicate detections and move them to the invalid detection table.
        //
        [
            // Give a decent amount of temp table space.
            '''SET temp_buffers = 3500''',

            // Find duplicates (write IDs to temp table).
            '''CREATE TEMP TABLE dup_det_ids AS
                 SELECT id
                 FROM (
                   SELECT id,
                   row_number() over (partition BY timestamp, transmitter_id, receiver_name ORDER BY id) AS rnum
                   FROM valid_detection
                 ) t
               WHERE t.rnum > 1''',

            // Copy dups to invalid.
            '''INSERT INTO invalid_detection (id, version, location, message, reason, receiver_download_id, receiver_name, sensor_unit,
                           sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number)
                 SELECT nextval('hibernate_sequence'), version, location, 'Invalid detection: duplicate', 'DUPLICATE', receiver_download_id, receiver_name, sensor_unit,
                        sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number
                 FROM valid_detection
                 WHERE id in (select id from dup_det_ids)''',

            // Delete from valid.
            '''DELETE FROM valid_detection WHERE id in (select id from dup_det_ids)'''
        ].each {

            query ->

            grailsChange {
                change {
                    sql.execute(query)
                }
            }
        }

        // Update statistics.
        grailsChange {
            change {
                sql.execute('''
                    UPDATE statistics SET value = (
                        SELECT COUNT(*) FROM valid_detection
                    ) WHERE key = 'numValidDetections';'''
                )
            }

        }

        createIndex(indexName: "pagination_index", tableName: "valid_detection", unique: "true") {
            column(name: "timestamp")
            column(name: "receiver_name")
            column(name: "transmitter_id")
        }
    }

    changeSet(author: "jburgess", id: "14123169288000-02") {
        createIndex(indexName: "installation_station_name_index", tableName: "installation_station") {
            column(name: "name")
        }
    }

    changeSet(author: "jburgess", id: "14123169288000-03") {
        createIndex(indexName: "receiver_deployment_receiver_id_index", tableName: "receiver_deployment") {
            column(name: "receiver_id")
        }

        createIndex(indexName: "device_model_id_index", tableName: "device") {
            column(name: "model_id")
        }

        createIndex(indexName: "receiver_download_file_request_user_id_index", tableName: "receiver_download_file") {
            column(name: "requesting_user_id")
        }

        createIndex(indexName: "device_organisation_id_index", tableName: "device") {
            column(name: "organisation_id")
        }
    }

    changeSet(author: "jburgess", id: "14123169288000-04") {
        dropColumn(columnName: "embargo_date", tableName: "receiver_deployment")
        dropColumn(columnName: "embargo_date", tableName: "species")
    }

    // Index some columns used for joins.
    changeSet(author: "jburgess", id: "14123169288000-05") {
        createIndex(indexName: "sensor_tag_id_index", tableName: "sensor") {
            column(name: "tag_id")
        }

        createIndex(indexName: "surgery_release_id_index", tableName: "surgery") {
            column(name: "release_id")
        }

        createIndex(indexName: "animal_release_animal_id_index", tableName: "animal_release") {
            column(name: "animal_id")
        }

        createIndex(indexName: "animal_species_id_index", tableName: "animal") {
            column(name: "species_id")
        }
    }
}
