databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1348627365857-1") {
		addColumn(tableName: "receiver_valid_detection") {
			column(name: "receiver_detections_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-2") {
		modifyDataType(columnName: "date_created", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "audit_log")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-3") {
		modifyDataType(columnName: "last_updated", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "audit_log")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-4") {
		modifyDataType(columnName: "import_finish_date", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "bulk_import")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-5") {
		modifyDataType(columnName: "import_start_date", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "bulk_import")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-6") {
		modifyDataType(columnName: "src_modified_date", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "bulk_import_record")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-7") {
		addNotNullConstraint(columnDataType: "VARCHAR(255)", columnName: "message", tableName: "invalid_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-8") {
		modifyDataType(columnName: "timestamp", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "invalid_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-9") {
		modifyDataType(columnName: "initialisationdatetime_timestamp", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "receiver_deployment")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-10") {
		modifyDataType(columnName: "timestamp", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "valid_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-11") {
		addUniqueConstraint(columnNames: "model_id, serial_number", constraintName: "device_model_id_serial_number_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-12") {
		dropUniqueConstraint(constraintName: "device_serial_number_key", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-13") {
		addForeignKeyConstraint(baseColumnNames: "receiver_detections_id", baseTableName: "receiver_valid_detection", baseTableSchemaName: "public", constraintName: "fka1600bb292eec5e4", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-14") {
		addForeignKeyConstraint(baseColumnNames: "valid_detection_id", baseTableName: "receiver_valid_detection", baseTableSchemaName: "public", constraintName: "fka1600bb2428744c3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "valid_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-15") {
		dropColumn(columnName: "receiver_valid_detections_id", tableName: "receiver_valid_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-16") {
		dropColumn(columnName: "surgeries_idx", tableName: "surgery")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-17") {
		dropView(schemaName: "public", viewName: "detection_count_per_station")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-18") {
		dropView(schemaName: "public", viewName: "detection_extract_view")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-19") {
		dropView(schemaName: "public", viewName: "public_detection_view")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-20") {
		dropTable(tableName: "detection_count_per_station_mv")
	}

	changeSet(author: "jburgess (generated)", id: "1348627365857-21") {
		dropTable(tableName: "matviews")
	}
}
