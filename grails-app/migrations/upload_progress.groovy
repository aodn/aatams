databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1341814816791-1") {
		createTable(tableName: "receiver_download_file_progress") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "receiver_download_file_progress_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "percent_complete", type: "int4")

			column(name: "receiver_download_file_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1341814816791-3") {
		addUniqueConstraint(columnNames: "receiver_download_file_id", constraintName: "receiver_download_file_progress_receiver_download_file_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "receiver_download_file_progress")
	}

	changeSet(author: "jburgess (generated)", id: "1341814816791-4") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_file_id", baseTableName: "receiver_download_file_progress", baseTableSchemaName: "public", constraintName: "fk5875ae6950da4c63", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}
}
