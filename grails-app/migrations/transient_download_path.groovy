databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1348644752975-13") {
		dropColumn(columnName: "path", tableName: "receiver_download_file")
	}
}
