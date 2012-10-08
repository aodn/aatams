databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1348546601623-7") {
		dropNotNullConstraint(columnDataType: "TIMESTAMP WITH TIME ZONE", columnName: "src_modified_date", tableName: "bulk_import_record")
	}
}
