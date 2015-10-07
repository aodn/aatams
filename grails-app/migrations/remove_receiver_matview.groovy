
databaseChangeLog = {

    changeSet(author: 'jfca', id: 'ja-01234') {

        dropView(viewName: 'valid_detection')

        dropView(viewName: 'invalid_detection')

        dropView(viewName: 'detection_count_per_station')

        dropView(viewName: 'detection_view')

        sql("drop function invalid_reason(detection detection, receiver receiver, deployment_and_recovery deployment_and_recovery)")

        sql("drop materialized view receiver")

        sql("drop trigger device_trigger on device")

        sql("drop trigger device_model_trigger on device_model")

        sql("drop function aatams.refresh_receiver_mv()")

        createView(
            '''
              SELECT 
                device.id, 
                (((device_model.model_name)::text || '-'::text) || (device.serial_number)::text) AS receiver_name, 
                device.organisation_id 
              FROM (device JOIN device_model ON ((device.model_id = device_model.id))) 
              WHERE ((device.class)::text = 'au.org.emii.aatams.Receiver'::text)
            ''',
            viewName: "receiver"
        )

        createProcedure(
            '''CREATE FUNCTION invalid_reason(
                 detection detection,
                 receiver receiver,
                 deployment_and_recovery deployment_and_recovery)
               RETURNS text AS $$
                 BEGIN
                   IF detection.duplicate THEN
                     RETURN 'DUPLICATE';
                   ELSIF receiver.id IS NULL THEN
                     RETURN 'UNKNOWN_RECEIVER';
                   ELSIF deployment_and_recovery.receiver_deployment_id IS NULL THEN
                     RETURN 'NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME';
                   END IF;

                   RETURN NULL;
                 END;
               $$ LANGUAGE plpgsql;'''
        )

        createView(
            '''
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
                rxr_organisation.name as organisation_name,

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
                invalid_reason(detection.*, receiver.*, deployment_and_recovery.*) AS invalid_reason,

                sensor.transmitter_id as sensor_id,
                tag.project_id as tag_project_id,
                surgery.id AS surgery_id,
                release.id AS release_id,
                release.project_id AS release_project_id,
                embargo_date


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
                LEFT JOIN surgery ON tag.id = surgery.tag_id
                LEFT JOIN animal_release release ON surgery.release_id = release.id
                LEFT JOIN animal ON release.animal_id = animal.id
                LEFT JOIN species ON animal.species_id = species.id
             ''',
             viewName: 'detection_view'
        )

        createView(
            '''SELECT * FROM detection_view WHERE invalid_reason IS NULL''',
                   viewName: 'valid_detection'
        )

        createView(
            '''SELECT * FROM detection_view WHERE invalid_reason IS NOT NULL''',
                   viewName: 'invalid_detection'
        )

        createView('''
            SELECT
              station_station_name AS station,
              installation_name AS installation,
              rxr_project_name AS project,
              aatams.scramblepoint(st_makepoint(station_longitude, station_latitude)) AS public_location,
              st_x(aatams.scramblepoint(st_makepoint(station_longitude, station_latitude))) AS public_lon,
              st_y(aatams.scramblepoint(st_makepoint(station_longitude, station_latitude))) AS public_lat,
              ('http://localhost:8080/aatams/installationStation/show/'::text || station_id) AS installation_station_url,
              ('http://localhost:8080/aatams/detection/lISt?filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in='::text || (station_station_name)::text) AS detection_download_url,
              count(*) AS detection_count,
              ((log((GREATEST(count(*), (1)::bigint))::double precISion) / log(((SELECT max(t.detection_count) AS max FROM (
                SELECT
                  station_station_name,
                  count(station_station_name) AS detection_count
                FROM aatams.detection_view GROUP BY station_station_name
                ) t))::double precision)) * (10)::double precision) AS relative_detection_count,
              station_id

            FROM aatams.detection_view
            WHERE station_id IS not null

            GROUP BY
              station_station_name,
              installation_name,
              rxr_project_name,
              station_longitude,
              station_latitude,
              station_id

            UNION ALL
            SELECT
              installation_station.name AS station,
              installation.name AS installation,
              project.name AS project,
              aatams.scramblepoint(installation_station.location) AS public_location,
              st_x(aatams.scramblepoint(installation_station.location)) AS public_lon,
              st_y(aatams.scramblepoint(installation_station.location)) AS public_lat,
              ('http://localhost:8080/aatams/installationStation/show/'::text || installation_station.id) AS installation_station_url,
              '' AS detection_download_url,
              0 AS detection_count,
              0 AS relative_detection_count,
              installation_station.id AS station_id
            FROM (((aatams.installation_station LEFT JOIN aatams.installation ON ((installation_station.installation_id = installation.id)))
              LEFT JOIN aatams.project ON ((installation.project_id = project.id)))
              LEFT JOIN aatams.detection_view ON ((installation_station.id = detection_view.station_id)))
            WHERE (detection_view.station_id IS NULL);
                        ''',
                viewName: 'detection_count_per_station'
        )
    }
}
