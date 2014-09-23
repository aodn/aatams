databaseChangeLog = {

    changeSet(author: "jburgess", id: "1411349648000-01") {
        createIndex(indexName: "transmitter_id_index", tableName: "sensor", unique: "false") {
            column(name: "transmitter_id")
        }
    }

    changeSet(author: "jburgess", id: "1411349648000-02") {
        createIndex(indexName: "tag_id_index", tableName: "surgery", unique: "false") {
            column(name: "tag_id")
        }
    }

    changeSet(author: "jburgess", id: "1411349648000-03") {

        //
        // Delete the things that depend (directly or indirectly) on 'detectionSurgery'...
        //
        grailsChange {
            change {
                // The previous migrations don't properly set up mat view - they
                // create the tables themselves but they are not properly registered.
                // Hence, this change is a bit more complicated than it should be to
                // support both prod and running from scratch.
                ['detection_count_per_station_mv', 'detection_extract_view_mv'].each {
                    matview_name ->

                    if (sql.firstRow("select * from matviews where mv_name = ${matview_name};")) {
                        sql.execute("select drop_matview(${matview_name});")
                    }

                    sql.execute("drop table if exists " + matview_name)
                }
            }
        }

        dropView(viewName: "detection_count_per_station")
        dropView(viewName: "public_detection_view")
        dropView(viewName: "detections_by_project")
        dropView(viewName: "detections_by_species")
        dropView(viewName: "detection_extract_view")


        // This is the thing that we're actually getting rid of here...
        dropTable(tableName: "detection_surgery")

        //
        // Now recreate everything without a dependency on 'detectionSurgery'.
        //
        createView('''select timestamp, to_char((timestamp::timestamp with time zone) at time zone '00:00', 'YYYY-MM-DD HH24:MI:SS') as formatted_timestamp,
            installation_station.name as station,
            installation_station.id as station_id,
            installation_station.location as location,
            st_y(installation_station.location) as latitude, st_x(installation_station.location) as longitude,
            (device_model.model_name || '-' || device.serial_number) as receiver_name,
            COALESCE(sensor.transmitter_id, '') as sensor_id,
            COALESCE((species.spcode || ' - ' || species.scientific_name || ' (' || species.common_name || ')'), '') as species_name,

            sec_user.name as uploader,
            valid_detection.transmitter_id as "transmitter_id",
            organisation.name as organisation,
            project.name as project,
            installation.name as installation,
            COALESCE(species.spcode, '') as spcode,
            animal_release.id as animal_release_id,
            animal_release.embargo_date as embargo_date,
            project.id as project_id,
            valid_detection.id as detection_id,

            animal_release.project_id as release_project_id,
            valid_detection.sensor_value, valid_detection.sensor_unit,
            valid_detection.provisional

            from valid_detection

            left join receiver_deployment on receiver_deployment_id = receiver_deployment.id
            left join installation_station on receiver_deployment.station_id = installation_station.id
            left join installation on installation_station.installation_id = installation.id
            left join project on installation.project_id = project.id
            left join device on receiver_deployment.receiver_id = device.id
            left join device_model on device.model_id = device_model.id
            left join receiver_download_file on receiver_download_id = receiver_download_file.id
            left join sec_user on receiver_download_file.requesting_user_id = sec_user.id
            left join organisation on device.organisation_id = organisation.id

            left join sensor on valid_detection.transmitter_id = sensor.transmitter_id
            left join device tag on sensor.tag_id = tag.id

            left join surgery on tag.id = surgery.tag_id
            left join animal_release on surgery.release_id = animal_release.id

            left join animal on animal_release.animal_id = animal.id
            left join species on animal.species_id = species.id''', viewName: "detection_extract_view")

        createView('''SELECT detection_extract_view."timestamp", detection_extract_view.formatted_timestamp, detection_extract_view.station, detection_extract_view.station_id, detection_extract_view.location, detection_extract_view.latitude, detection_extract_view.longitude, detection_extract_view.receiver_name, detection_extract_view.sensor_id, detection_extract_view.species_name, detection_extract_view.uploader, detection_extract_view.transmitter_id, detection_extract_view.organisation, detection_extract_view.project, detection_extract_view.installation, detection_extract_view.spcode, detection_extract_view.animal_release_id, detection_extract_view.embargo_date, detection_extract_view.project_id, detection_extract_view.detection_id, scramblepoint(detection_extract_view.location) AS public_location, CASE WHEN isreleaseembargoed(detection_extract_view.animal_release_id) THEN 'embargoed'::text ELSE detection_extract_view.species_name END AS public_species_name, CASE WHEN isreleaseembargoed(detection_extract_view.animal_release_id) THEN 'embargoed'::character varying ELSE detection_extract_view.spcode END AS public_spcode FROM detection_extract_view;''', viewName: "public_detection_view")

        createView("SELECT public_detection_view.station, public_detection_view.installation, public_detection_view.project, public_detection_view.public_location, st_x(public_detection_view.public_location) AS public_lon, st_y(public_detection_view.public_location) AS public_lat, ('http://localhost:8080/aatams/installationStation/show/'::text || public_detection_view.station_id) AS installation_station_url, ('http://localhost:8080/aatams/detection/list?filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in='::text || (public_detection_view.station)::text) AS detection_download_url, count(*) AS detection_count, ((log((GREATEST(count(*), (1)::bigint))::double precision) / log(((SELECT max(t.detection_count) AS max FROM (SELECT public_detection_view.station, count(public_detection_view.station) AS detection_count FROM public_detection_view GROUP BY public_detection_view.station) t))::double precision)) * (10)::double precision) AS relative_detection_count, public_detection_view.station_id FROM public_detection_view GROUP BY public_detection_view.station, public_detection_view.installation, public_detection_view.project, public_detection_view.public_location, public_detection_view.station_id UNION ALL SELECT installation_station.name AS station, installation.name AS installation, project.name AS project, scramblepoint(installation_station.location) AS public_location, st_x(scramblepoint(installation_station.location)) AS public_lon, st_y(scramblepoint(installation_station.location)) AS public_lat, ('http://localhost:8080/aatams/installationStation/show/'::text || installation_station.id) AS installation_station_url, '' AS detection_download_url, 0 AS detection_count, 0 AS relative_detection_count, installation_station.id AS station_id FROM (((installation_station LEFT JOIN installation ON ((installation_station.installation_id = installation.id))) LEFT JOIN project ON ((installation.project_id = project.id))) LEFT JOIN public_detection_view ON ((installation_station.id = public_detection_view.station_id))) WHERE (public_detection_view.station_id IS NULL);", viewName: "detection_count_per_station")

        // create mat views
        grailsChange {
            change {
                sql.execute("select create_matview('detection_extract_view_mv', 'detection_extract_view');")
                sql.execute("select create_matview('detection_count_per_station_mv', 'detection_count_per_station');")
            }
        }

        [ 'installation', 'project', 'provisional', 'spcode', 'station', 'timestamp', 'transmitter_id' ].each {
            columnName ->

            createIndex(indexName: "detection_extract_view_mv_${columnName}_index",
                        tableName: "detection_extract_view_mv",
                        unique: "false") {
                column(name: columnName)
            }
        }
    }
}
