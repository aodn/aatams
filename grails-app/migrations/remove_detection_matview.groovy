databaseChangeLog = {
    changeSet(author: "jburgess", id: "1420772532000-01") {

        grailsChange {
            change {
                sql.execute("select drop_matview('detection_count_per_station_mv')")
            }
        }

        dropView(viewName: 'detection_count_per_station')

        dropView(viewName: 'public_detection_view')

        grailsChange {
            change {
                sql.execute("select drop_matview('detection_extract_view_mv')")
            }
        }

        dropView(viewName: 'detection_extract_view')

        createView(
            '''SELECT detection_view."timestamp",
            detection_view.formatted_timestamp,
            detection_view.station,
            detection_view.station_id,
            detection_view.location,
            detection_view.latitude,
            detection_view.longitude,
            detection_view.receiver_name,
            detection_view.sensor_id,
            detection_view.species_name,
            detection_view.uploader,
            detection_view.transmitter_id,
            detection_view.organisation,
            detection_view.project,
            detection_view.installation,
            detection_view.spcode,
            detection_view.animal_release_id,
            detection_view.embargo_date,
            detection_view.project_id,
            detection_view.detection_id,
            scramblepoint(detection_view.location) AS public_location,
                CASE
                    WHEN isreleaseembargoed(detection_view.animal_release_id) THEN 'embargoed'::text
                    ELSE detection_view.species_name
                END AS public_species_name,
                CASE
                    WHEN isreleaseembargoed(detection_view.animal_release_id) THEN 'embargoed'::character varying
                    ELSE detection_view.spcode
                END AS public_spcode
            FROM detection_view;''',
            viewName: 'public_detection_view'
        )

        // TODO: this view can most likely be optimised - it currently takes a long time (hours) to run.
        createView(
            '''SELECT public_detection_view.station,
            public_detection_view.installation,
            public_detection_view.project,
            public_detection_view.public_location,
            st_x(public_detection_view.public_location) AS public_lon,
            st_y(public_detection_view.public_location) AS public_lat,
            'http://localhost:8080/aatams/installationStation/show/'::text || public_detection_view.station_id AS installation_station_url,
            'http://localhost:8080/aatams/detection/list?filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in='::text || public_detection_view.station::text AS detection_download_url,
            count(*) AS detection_count,
            log(GREATEST(count(*), 1::bigint)::double precision) / log((( SELECT max(t.detection_count) AS max
                   FROM ( SELECT public_detection_view_1.station,
                            count(public_detection_view_1.station) AS detection_count
                           FROM public_detection_view public_detection_view_1
                          GROUP BY public_detection_view_1.station) t))::double precision) * 10::double precision AS relative_detection_count,
            public_detection_view.station_id
           FROM public_detection_view
          GROUP BY public_detection_view.station, public_detection_view.installation, public_detection_view.project, public_detection_view.public_location, public_detection_view.station_id
UNION ALL
         SELECT installation_station.name AS station,
            installation.name AS installation,
            project.name AS project,
            scramblepoint(installation_station.location) AS public_location,
            st_x(scramblepoint(installation_station.location)) AS public_lon,
            st_y(scramblepoint(installation_station.location)) AS public_lat,
            'http://localhost:8080/aatams/installationStation/show/'::text || installation_station.id AS installation_station_url,
            ''::text AS detection_download_url,
            0 AS detection_count,
            0 AS relative_detection_count,
            installation_station.id AS station_id
           FROM installation_station
      LEFT JOIN installation ON installation_station.installation_id = installation.id
   LEFT JOIN project ON installation.project_id = project.id
   LEFT JOIN public_detection_view ON installation_station.id = public_detection_view.station_id
  WHERE public_detection_view.station_id IS NULL;''',
            viewName: 'detection_count_per_station'
        )

        grailsChange {
            change {
                sql.execute("select create_matview('detection_count_per_station_mv', 'detection_count_per_station')")
            }
        }
    }
}
