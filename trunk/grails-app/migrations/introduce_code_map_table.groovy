databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1320284911545-1") {
		createTable(tableName: "code_map") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "code_map_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "code_map", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1320284911545-2") {
		addColumn(tableName: "device") {
			column(name: "code_map_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1320284911545-3") {
		addUniqueConstraint(columnNames: "code_map", constraintName: "code_map_code_map_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "code_map")
	}

	changeSet(author: "jburgess (generated)", id: "1320284911545-4") {
		addForeignKeyConstraint(baseColumnNames: "code_map_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e56472540a6", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "code_map", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1320284911545-5") {
		dropColumn(columnName: "code_map", tableName: "device")
	}
	
	changeSet(author: "jburgess", id: "1320284911545-6")
	{
		int id = 1
		
		["A69", "A180"].each
		{
			freq ->
			
			["1303", "1601", "9001", "9003", "1206", "1105", "9002", "9004"].each
			{
				codeSpace ->
				
				insert(tableName: "code_map")
				{
					column(name:"id", valueNumeric:id)
					column(name:"version", valueNumeric:0)
					column(name:"code_map", value:freq + "-" + codeSpace)
				}
				
				id++
			}
		}
	}
	
	changeSet(author: "jburgess", id: "1320284911545-7")
	{
		update(tableName: "device")
		{
			column(name:"code_map_id", valueNumeric:7)
			where "id = 153"
		}
		update(tableName: "device")
		{
			column(name:"code_name", value:"A69-9002-8605")
			column(name:"code_map_id", valueNumeric:7)
			where "id = 199"
		}
		update(tableName: "device")
		{
			column(name:"code_name", value:"A69-1303-49134")
			column(name:"code_map_id", valueNumeric:1)
			where "id = 228"
		}
	}
}
