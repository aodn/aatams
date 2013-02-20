databaseChangeLog = {
	changeSet(author: "jburgess", id: "1361335767000-1") {

        createIndex(indexName: 'valid_detection_receiver_download_id_index', tableName: 'valid_detection') {
            column(name: 'receiver_download_id')
        }

        createIndex(indexName: 'invalid_detection_receiver_download_id_index', tableName: 'invalid_detection') {
            column(name: 'receiver_download_id')
        }

        createIndex(indexName: 'invalid_detection_reason_index', tableName: 'invalid_detection') {
            column(name: 'reason')
        }
    }
}
                