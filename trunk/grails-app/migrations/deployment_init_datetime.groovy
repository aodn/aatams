databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1323911939878-1") {
		addColumn(tableName: "receiver_deployment") {
			column(name: "initialisationdatetime_timestamp", type: "TIMESTAMP WITH TIME ZONE")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-2") {
		addColumn(tableName: "receiver_deployment") {
			column(name: "initialisationdatetime_zone", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-3") {
		addColumn(tableName: "receiver_event") {
			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-4") {
		addColumn(tableName: "receiver_event") {
			column(name: "message", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-5") {
		addColumn(tableName: "receiver_event") {
			column(name: "reason", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-6") {
		addColumn(tableName: "receiver_event") {
			column(name: "receiver_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-7") {
		dropNotNullConstraint(columnDataType: "int8", columnName: "receiver_deployment_id", tableName: "receiver_event")
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-8") {
		createIndex(indexName: "event_timestamp_index", tableName: "receiver_event", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-9") {
		dropUniqueConstraint(constraintName: "device_ping_code_key", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1323911939878-10") {
		dropColumn(columnName: "detection_surgeries_idx", tableName: "detection_surgery")
	}
}
