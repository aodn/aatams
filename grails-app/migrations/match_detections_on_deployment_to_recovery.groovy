databaseChangeLog = {

    changeSet(author: "ja", id: "1458164418000-01") {

        grailsChange {
            change {
                sql.execute("""
                  -- match detections based on deployment to recovery time instead of initialisation to recovery time
                  -- in-place update of view code, keeping field names and types the same
                  CREATE OR REPLACE VIEW deployment_and_recovery AS
                   SELECT receiver_deployment.receiver_id,
                      receiver_deployment.id AS receiver_deployment_id,
                      receiver_recovery.id AS receiver_recovery_id,
                      receiver_deployment.deploymentdatetime_timestamp AS initialisationdatetime_timestamp,
                      receiver_recovery.recoverydatetime_timestamp
                     FROM receiver_recovery
                       JOIN receiver_deployment ON receiver_recovery.deployment_id = receiver_deployment.id;

                  -- rename the field names
                  -- will also update the internal join fields of the detection_count_per_station and detection_view
                  ALTER TABLE deployment_and_recovery
                    RENAME COLUMN initialisationdatetime_timestamp
                    TO deploymentdatetime_timestamp;
                  """)
            }
        }
    }
}
