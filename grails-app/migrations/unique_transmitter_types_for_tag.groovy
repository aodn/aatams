databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1339995065328-4") {
		addUniqueConstraint(columnNames: "tag_id, transmitter_type_id", constraintName: "sensor_tag_id_transmitter_type_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sensor")
	}
}
