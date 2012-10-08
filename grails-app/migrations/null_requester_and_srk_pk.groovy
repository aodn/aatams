databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1349063356399-11") {
		dropNotNullConstraint(columnDataType: "int8", columnName: "requesting_user_id", tableName: "receiver_download_file")
	}

	changeSet(author: "jburgess (generated)", id: "1349063356399-12") {
		dropNotNullConstraint(columnDataType: "int8", columnName: "src_pk", tableName: "bulk_import_record")
	}
}
