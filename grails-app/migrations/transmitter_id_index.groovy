databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1326865329563-1") {
		modifyDataType(columnName: "initialisationdatetime_timestamp", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "receiver_deployment")
	}

	changeSet(author: "jburgess (generated)", id: "1326865329563-2") {
		dropNotNullConstraint(columnDataType: "VARCHAR(255)", columnName: "transmitter_id", tableName: "sensor")
	}

	changeSet(author: "jburgess (generated)", id: "1326865329563-3") {
		createIndex(indexName: "transmitterid_index", tableName: "raw_detection", unique: "false") {
			column(name: "transmitter_id")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1326865329563-4") {
		createIndex(indexName: "receivername_index", tableName: "raw_detection", unique: "false") {
			column(name: "receiver_name")
		}
	}
}
