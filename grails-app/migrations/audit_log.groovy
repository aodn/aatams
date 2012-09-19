databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1344832698398-1") {
		createTable(tableName: "audit_log") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_pkey")
			}

			column(name: "actor", type: "VARCHAR(255)")

			column(name: "class_name", type: "VARCHAR(255)")

			column(name: "date_created", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "event_name", type: "VARCHAR(255)")

			column(name: "last_updated", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "new_value", type: "VARCHAR(255)")

			column(name: "old_value", type: "VARCHAR(255)")

			column(name: "persisted_object_id", type: "VARCHAR(255)")

			column(name: "persisted_object_version", type: "int8")

			column(name: "property_name", type: "VARCHAR(255)")

			column(name: "uri", type: "VARCHAR(255)")
		}
	}
}
