databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1348560203431-11") {
		dropUniqueConstraint(constraintName: "device_serial_number_key", tableName: "device")
	}
	
	changeSet(author: "jburgess (generated)", id: "1348627365857-11") {
		addUniqueConstraint(columnNames: "model_id, serial_number", constraintName: "device_model_id_serial_number_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device")
	}
}
