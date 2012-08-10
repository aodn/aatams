databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1344579824226-1") {
		createTable(tableName: "audit_log") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "action", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "clazz", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "entity_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "person_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1344579824226-3") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "audit_log", baseTableSchemaName: "public", constraintName: "fkb8308e0e985cdb3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}
}
