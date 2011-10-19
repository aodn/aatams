databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1318897991574-1") {
		addColumn(tableName: "receiver_recovery") {
			column(name: "deployment_id", type: "int8", defaultValueNumeric:0) {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318897991574-2") {
		dropForeignKeyConstraint(baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb157c50c982")
	}

	changeSet(author: "jburgess (generated)", id: "1318897991574-3") {
		dropForeignKeyConstraint(baseTableName: "receiver_deployment_raw_detection", baseTableSchemaName: "public", constraintName: "fk9ea19f84e9e13deb")
	}

	changeSet(author: "jburgess (generated)", id: "1318897991574-4") {
		dropForeignKeyConstraint(baseTableName: "receiver_deployment_raw_detection", baseTableSchemaName: "public", constraintName: "fk9ea19f847197496f")
	}

	changeSet(author: "jburgess (generated)", id: "1318897991574-5") {
		addUniqueConstraint(columnNames: "deployment_id", constraintName: "receiver_recovery_deployment_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "receiver_recovery")
	}

	changeSet(author: "jburgess (generated)", id: "1318897991574-8") {
		addForeignKeyConstraint(baseColumnNames: "deployment_id", baseTableName: "receiver_recovery", baseTableSchemaName: "public", constraintName: "fk82de83e579a96182", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318897991574-9") {
		dropColumn(columnName: "recovery_id", tableName: "receiver_deployment")
	}

	changeSet(author: "jburgess (generated)", id: "1318897991574-10") {
		dropTable(tableName: "receiver_deployment_raw_detection")
	}
}
