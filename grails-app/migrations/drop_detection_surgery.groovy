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
}
