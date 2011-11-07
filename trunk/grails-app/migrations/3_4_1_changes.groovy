databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1319673217092-1") {
		dropNotNullConstraint(columnDataType: "bytea", columnName: "capture_location", tableName: "animal_release")
	}

	changeSet(author: "jburgess (generated)", id: "1319673217092-2") {
		dropNotNullConstraint(columnDataType: "bytea", columnName: "release_location", tableName: "animal_release")
	}
}
