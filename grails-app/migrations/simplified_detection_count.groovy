databaseChangeLog = {
    changeSet(author: 'jburgess', id: '1444179014-00') {
        grailsChange {
            change {
                sql.execute("select drop_matview('detection_count_per_station_mv')")
            }
        }

        dropView(viewName: 'detection_count_per_station')

        createView(
            '''
SELECT count(*) AS detection_count, station_id
FROM (
  SELECT detection.id, station.id as station_id, aatams.invalid_reason(detection.*, receiver.*, deployment_and_recovery.*) AS invalid_reason
FROM detection
    JOIN aatams.receiver_download_file ON detection.receiver_download_id = receiver_download_file.id
     JOIN aatams.receiver ON detection.receiver_name::text = receiver.receiver_name
     JOIN aatams.deployment_and_recovery deployment_and_recovery ON receiver.id = deployment_and_recovery.receiver_id AND detection."timestamp" >= deployment_and_recovery.initialisationdatetime_timestamp AND detection."timestamp" <= deployment_and_recovery.recoverydatetime_timestamp
     JOIN aatams.receiver_deployment ON deployment_and_recovery.receiver_deployment_id = receiver_deployment.id
     JOIN aatams.installation_station station ON receiver_deployment.station_id = station.id
) AS dets
WHERE invalid_reason IS null
GROUP BY station_id
''',
            viewName: 'detection_count_per_station'
        )

        grailsChange {
            change {
                sql.execute("select create_matview('detection_count_per_station_mv', 'detection_count_per_station')")
            }
        }
    }
}
