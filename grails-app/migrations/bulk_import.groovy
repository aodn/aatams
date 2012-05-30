databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1338352662081-1") {
		createTable(tableName: "bulk_import") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "bulk_import_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "import_finish_date", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "import_start_date", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "organisation_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1338352662081-2") {
		createTable(tableName: "bulk_import_record") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "bulk_import_record_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "bulk_import_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "dst_class", type: "VARCHAR(255)")

			column(name: "dst_pk", type: "int8")

			column(name: "src_modified_date", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "src_pk", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "src_table", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1338352662081-4") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "bulk_import", baseTableSchemaName: "public", constraintName: "fkf7eb277299b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1338352662081-5") {
		addForeignKeyConstraint(baseColumnNames: "bulk_import_id", baseTableName: "bulk_import_record", baseTableSchemaName: "public", constraintName: "fk53875dbec6323f02", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "bulk_import", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}
}
