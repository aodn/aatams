databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1329372641779-1") {
		dropNotNullConstraint(columnDataType: "int8", columnName: "status_id", tableName: "device")
	}

	// This probably belongs in a previous migration script...
	changeSet(author: "jburgess (generated)", id: "1329372641779-2") {
		addNotNullConstraint(columnDataType: "geometry", columnName: "location", tableName: "installation_station")
	}
/**
	changeSet(author: "jburgess (generated)", id: "1329372641779-3") {
		modifyDataType(columnName: "initialisationdatetime_timestamp", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "receiver_deployment")
	}
*/
	// ... as does this one.
	changeSet(author: "jburgess (generated)", id: "1329372641779-4") {
		addNotNullConstraint(columnDataType: "geometry", columnName: "location", tableName: "receiver_recovery")
	}

	// ... and this one :-)
	changeSet(author: "jburgess (generated)", id: "1329372641779-5") {
		dropColumn(columnName: "sensors_idx", tableName: "sensor")
	}
/**
	changeSet(author: "jburgess (generated)", id: "1329372641779-6") {
		dropView(schemaName: "public", viewName: "detection_extract_view")
	}
*/
}
