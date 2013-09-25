databaseChangeLog = {

    changeSet(author: "jburgess", id: "1380074275000-01") {
        createIndex(indexName: "valid_detection_receiver_deployment_index", tableName: "valid_detection", unique: "false") {
            column(name: "receiver_deployment_id")
        }
    }

    changeSet(author: "jburgess", id: "1380074275000-02") {
        createIndex(indexName: "receiver_deployment_installation_station_id_index", tableName: "receiver_deployment", unique: "false") {
            column(name: "station_id")
        }
    }

    changeSet(author: "jburgess", id: "1380074275000-03") {
        createIndex(indexName: "installation_station_installation_id_index", tableName: "installation_station", unique: "false") {
            column(name: "installation_id")
        }
    }

    changeSet(author: "jburgess", id: "1380074275000-04") {
        createIndex(indexName: "installation_project_id_index", tableName: "installation", unique: "false") {
            column(name: "project_id")
        }
    }
}
