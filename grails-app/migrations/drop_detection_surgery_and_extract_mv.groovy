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
        grailsChange {
            change {
                sql.execute("select drop_matview('detection_count_per_station_mv');")
                sql.execute("select drop_matview('detection_extract_view_mv');")
            }
        }

        dropView(viewName: "detection_count_per_station")

        // These views were originally there in order to server layers in the portal, but they're
        // not being used anymore.
        dropView(viewName: "public_detection_view")
        dropView(viewName: "detections_by_project")
        dropView(viewName: "detections_by_species")
        dropView(viewName: "detection_extract_view")

        dropTable(tableName: "detection_surgery")
        dropColumn(columnName: "provisional", tableName: "valid_detection")

        createView('''select installation_station.id as station_id, installation_station.name as station_name, count(valid_detection.id) as detection_count

from installation_station

join receiver_deployment on installation_station.id = receiver_deployment.station_id
left join valid_detection on receiver_deployment.id = valid_detection.receiver_deployment_id

group by installation_station.id''', viewName: "detection_count_per_station")

        grailsChange {
            change {
                sql.execute("select create_matview('detection_count_per_station_mv', 'detection_count_per_station');")
            }
        }
    }
}
