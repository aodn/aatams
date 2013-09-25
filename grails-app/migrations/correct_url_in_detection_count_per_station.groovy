databaseChangeLog = {

    changeSet(author: "jburgess", id: "1380080080000-1") {
        dropView(viewName: 'detection_count_per_station')
    }

    changeSet(author: "jburgess", id: "1380080080000-2") {
        grailsChange {
            change {
                sql.execute('''
CREATE OR REPLACE VIEW detection_count_per_station AS
         SELECT public_detection_view.station, public_detection_view.installation, public_detection_view.project, public_detection_view.public_location, st_x(public_detection_view.public_location) AS public_lon, st_y(public_detection_view.public_location) AS public_lat, ''' + "'" + application.config.grails.serverURL + '''/installationStation/show/'::text || public_detection_view.station_id AS installation_station_url, ''' + "'" + application.config.grails.serverURL + '''/detection/list?filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in='::text || public_detection_view.station::text AS detection_download_url, count(*) AS detection_count, log(GREATEST(count(*), 1::bigint)::double precision) / log((( SELECT max(t.detection_count) AS max
                   FROM ( SELECT public_detection_view.station, count(public_detection_view.station) AS detection_count
                           FROM public_detection_view
                          GROUP BY public_detection_view.station) t))::double precision) * 10::double precision AS relative_detection_count, public_detection_view.station_id
           FROM public_detection_view
          GROUP BY public_detection_view.station, public_detection_view.installation, public_detection_view.project, public_detection_view.public_location, public_detection_view.station_id
UNION ALL
         SELECT installation_station.name AS station, installation.name AS installation, project.name AS project, scramblepoint(installation_station.location) AS public_location, st_x(scramblepoint(installation_station.location)) AS public_lon, st_y(scramblepoint(installation_station.location)) AS public_lat, ''' + "'" + application.config.grails.serverURL + '''/installationStation/show/'::text || installation_station.id AS installation_station_url, '' AS detection_download_url, 0 AS detection_count, 0 AS relative_detection_count, installation_station.id AS station_id
           FROM installation_station
      LEFT JOIN installation ON installation_station.installation_id = installation.id
   LEFT JOIN project ON installation.project_id = project.id
   LEFT JOIN public_detection_view ON installation_station.id = public_detection_view.station_id
  WHERE public_detection_view.station_id IS NULL;
''')
            }
        }
    }
}
