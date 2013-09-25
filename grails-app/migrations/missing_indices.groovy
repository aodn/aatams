databaseChangeLog = {

    changeSet(author: "jburgess", id: "1380071456000-01") {
		createIndex(indexName: "device_status_index", tableName: "device", unique: "false") {
			column(name: "status_id")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-02") {
		createIndex(indexName: "invalid_detection_receiver_download_id_index", tableName: "invalid_detection", unique: "false") {
			column(name: "receiver_download_id")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-03") {
		createIndex(indexName: "sensor_fk_index", tableName: "detection_surgery", unique: "false") {
			column(name: "sensor_id")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-04") {
		createIndex(indexName: "status_index", tableName: "device", unique: "false") {
			column(name: "status_id")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-05") {
		createIndex(indexName: "surgery_fk_index", tableName: "detection_surgery", unique: "false") {
			column(name: "surgery_id")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-06") {
		createIndex(indexName: "valid_detection_fk_index", tableName: "detection_surgery", unique: "false") {
			column(name: "detection_id")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-07") {
		createIndex(indexName: "valid_detection_receiver_download_id_index", tableName: "valid_detection", unique: "false") {
			column(name: "receiver_download_id")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-08") {
		createIndex(indexName: "common_name_index", tableName: "species", unique: "false") {
			column(name: "common_name")
		}
	}

    changeSet(author: "jburgess", id: "1380071456000-09") {
		createIndex(indexName: "scientific_name_index", tableName: "species", unique: "false") {
			column(name: "scientific_name")
		}
	}
}
