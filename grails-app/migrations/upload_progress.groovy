databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1340245869457-1") {
		addColumn(tableName: "receiver_download_file") {
			column(name: "percent_complete", type: "int4")
		}
	}
}
