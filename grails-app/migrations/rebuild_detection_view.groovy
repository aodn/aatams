databaseChangeLog = {
    changeSet(author: 'dnahodil', id: '1445913902000-00', runOnChange: true, failOnError: true) {
        dropView(viewName: 'valid_detection')
        dropView(viewName: 'invalid_detection')
        dropView(viewName: 'detection_view')
	dropView(viewName: 'surgery_with_end_date')        

	createView(
            '''
            SELECT
                surgery.*,
                CASE
                    WHEN
                        lead(timestamp_timestamp) OVER (partition BY tag_id order by timestamp_timestamp) IS NOT null
                    THEN
                        lead(timestamp_timestamp) OVER (partition BY tag_id order by timestamp_timestamp)
                    ELSE
                        'infinity'::timestamptz
                    END
                AS end_timestamp_timestamp
            FROM surgery
            ''',
            viewName: 'surgery_with_end_date'
        )

        createView('''
            SELECT
                detection.id AS detection_id,
                detection."timestamp",
                detection.receiver_name,
                detection.transmitter_id AS transmitter_id,
                detection.transmitter_name,
                detection.transmitter_serial_number,
                detection.sensor_value,
                detection.sensor_unit,
                detection.station_name,
                detection.latitude,
                detection.longitude,
                detection.receiver_download_id,
                sec_user.name AS uploader,
                rxr_organisation.name AS organisation_name,
                deployment_and_recovery.receiver_deployment_id,
                rxr_project.name AS rxr_project_name,
                installation.name AS installation_name,
                station.id AS station_id,
                station.name AS station_station_name,
                st_y(station.location) AS station_latitude,
                st_x(station.location) AS station_longitude,
                species.spcode,
                species.scientific_name,
                species.common_name,
		species.name AS species_name,
                invalid_reason(detection.*, receiver.*, deployment_and_recovery.*) AS invalid_reason,
                sensor.transmitter_id AS sensor_id,
                tag.project_id AS tag_project_id,
                surgery.id AS surgery_id,
                release.id AS release_id,
                release.project_id AS release_project_id,
                release.embargo_date

            FROM detection
                JOIN receiver_download_file ON detection.receiver_download_id = receiver_download_file.id
                JOIN sec_user ON receiver_download_file.requesting_user_id = sec_user.id

                LEFT JOIN receiver ON detection.receiver_name::text = receiver.receiver_name
                LEFT JOIN organisation rxr_organisation ON receiver.organisation_id = rxr_organisation.id
                LEFT JOIN deployment_and_recovery deployment_and_recovery
                  ON receiver.id = deployment_and_recovery.receiver_id
                  AND detection."timestamp" >= deployment_and_recovery.initialisationdatetime_timestamp
                  AND detection."timestamp" <= deployment_and_recovery.recoverydatetime_timestamp
                LEFT JOIN receiver_deployment ON deployment_and_recovery.receiver_deployment_id = receiver_deployment.id
                LEFT JOIN installation_station station ON receiver_deployment.station_id = station.id
                LEFT JOIN installation ON station.installation_id = installation.id
                LEFT JOIN project rxr_project ON installation.project_id = rxr_project.id
                LEFT JOIN sensor ON detection.transmitter_id = sensor.transmitter_id
                LEFT JOIN device tag ON sensor.tag_id = tag.id
                LEFT JOIN surgery_with_end_date surgery
                    ON tag.id = surgery.tag_id
                    AND detection."timestamp" >= surgery.timestamp_timestamp
                    AND detection."timestamp" < surgery.end_timestamp_timestamp
                LEFT JOIN animal_release release ON surgery.release_id = release.id
                LEFT JOIN animal ON release.animal_id = animal.id
                LEFT JOIN species ON animal.species_id = species.id
            ''',
            viewName: 'detection_view'
        )

        createView(
            'SELECT * FROM detection_view WHERE invalid_reason IS NULL',
            viewName: 'valid_detection'
        )

        createView(
            'SELECT * FROM detection_view WHERE invalid_reason IS NOT NULL',
            viewName: 'invalid_detection'
        )
    }
}
