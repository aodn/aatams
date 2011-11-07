databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1318985564205-1") {
		createTable(tableName: "request") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "request_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "organisation_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "requester_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318985564205-2") {
		dropForeignKeyConstraint(baseTableName: "organisation", baseTableSchemaName: "public", constraintName: "fk3a5300daa7db8c31")
	}

	changeSet(author: "jburgess (generated)", id: "1318985564205-3") {
		addUniqueConstraint(columnNames: "organisation_id", constraintName: "request_organisation_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "request")
	}

	changeSet(author: "jburgess (generated)", id: "1318985564205-4") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "request", baseTableSchemaName: "public", constraintName: "fk414ef28f99b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318985564205-5") {
		addForeignKeyConstraint(baseColumnNames: "requester_id", baseTableName: "request", baseTableSchemaName: "public", constraintName: "fk414ef28f4ad0c2c", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318985564205-6") {
		dropColumn(columnName: "requesting_user_id", tableName: "organisation")
	}
}
