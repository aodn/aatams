databaseChangeLog = 
{
    // Create materialized view for detection view.
	changeSet(author: "jburgess", id: "1360028054000-2")
    {
        sql('''SELECT create_matview('detection_extract_view_mv', 'detection_extract_view');''');

        // Add indexes.
        ['project', 'installation', 'station', 'transmitter_id', 'spcode', 'timestamp'].each
        {
            columnName ->
                
                createIndex(indexName: "detection_extract_mv_${columnName}_index", tableName: "detection_extract_view_mv", unique: "false")
                {
                    column(name: columnName)
                }
        }
    }
    
    // Trim (this avoids us having to do it every time we run a query).
	changeSet(author: "jburgess", id: "1360028054000-3")
    {
        ['project': 'name', 'installation': 'name', 'installation_station': 'name'].each
        {
            tableName, columnName ->

            sql("UPDATE ${tableName} SET ${columnName} = trim(${columnName}) WHERE ${columnName} IS NOT null")
        }
    }

	changeSet(author: "jburgess", id: "1360028054000-4")
    {
		createTable(tableName: "statistics")
        {
			column(name: "id", type: "int8")
            {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "statistics_pkey")
			}

			column(name: "version", type: "int8")
            {
				constraints(nullable: "false")
			}

			column(name: "key", type: "VARCHAR(255)")
            {
				constraints(nullable: "false")
			}
            1
			column(name: "value", type: "int8")
            {
				constraints(nullable: "false")
			}
		}

		addUniqueConstraint(columnNames: "key", constraintName: "statistics_key_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "statistics")
	
        sql("INSERT INTO statistics (id, version, key, value) VALUES (1, 0, 'numValidDetections', (SELECT COUNT(*) FROM detection_extract_view_mv))")
	}
}
