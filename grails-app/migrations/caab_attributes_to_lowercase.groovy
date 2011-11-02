databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1320212508393-1") {
		createIndex(indexName: "spcode_index", tableName: "species", unique: "false") {
			column(name: "spcode")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1320212508393-2") {
		dropUniqueConstraint(constraintName: "species_spcode_key", tableName: "species")
	}
}
