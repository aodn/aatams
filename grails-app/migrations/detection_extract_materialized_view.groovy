databaseChangeLog = 
{
    // Trim (this avoids us having to do it every time we run a query).
	changeSet(author: "jburgess", id: "1360028054000-1")
    {
        ['project': 'name', 'installation': 'name', 'installation_station': 'name'].each
        {
            tableName, columnName ->

            sql("UPDATE ${tableName} SET ${columnName} = trim(${columnName}) WHERE ${columnName} IS NOT null")
        }
    }

	changeSet(author: "jburgess", id: "1360028054000-2") {

        sql('''SELECT create_matview('detection_extract_view_mv', 'detection_extract_view');''')
        
        // Add indexes.
        ['project', 'installation', 'station', 'transmitter_id', 'spcode', 'timestamp', 'provisional'].each
        {
            columnName ->

                createIndex(indexName: "detection_extract_view_mv_${columnName}_index", tableName: "detection_extract_view_mv", unique: "false")
                {
                    column(name: columnName)
                }
        }
    }
    
	changeSet(author: "jburgess", id: "1360028054000-3")
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

			column(name: "value", type: "int8")
            {
				constraints(nullable: "false")
			}
		}

		addUniqueConstraint(columnNames: "key", constraintName: "statistics_key_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "statistics")
	
        sql("INSERT INTO statistics (id, version, key, value) VALUES (1, 0, 'numValidDetections', (SELECT COUNT(*) FROM detection_extract_view_mv))")
	}

	changeSet(author: "jburgess", id: "1360028054000-4")
	{
		grailsChange
		{
			change
			{
				sql.execute("DROP VIEW IF EXISTS detection_count_per_station")
				sql.execute('''create or replace view detection_count_per_station as \

							select
								station,
								installation,
								project,
								public_location,
								st_x(public_location) as public_lon,
								st_y(public_location) as public_lat,
								''' + "'" + application.config.grails.serverURL + '''/installationStation/show/' || station_id as installation_station_url,
								''' + "'" + application.config.grails.serverURL + '''/detection/list?filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in=' || station as detection_download_url,
								count(*) as detection_count,
								log(greatest(count(*), 1)) / log((select max(detection_count) from
																 (
																	 select station, count(station) as detection_count
																	 from public_detection_view
																	 group by station
																 ) t)) * 10 as relative_detection_count,
                                station_id
							from public_detection_view
							group by station, installation, project, public_location, station_id
														
							union all
							select
								installation_station.name as station,
								installation.name as installation,
								project.name as project,
								scramblepoint(installation_station.location),
								st_x(scramblepoint(installation_station.location)) as public_lon,
								st_y(scramblepoint(installation_station.location)) as public_lat,
								''' + "'" + application.config.grails.serverURL + '''/installationStation/show/' || installation_station.id as installation_station_url,
								'' as detection_download_url,
								0 as detection_count,
								0 as releative_detection_count,
                                installation_station.id as station_id
							from installation_station
							left join installation on installation_station.installation_id = installation.id
							left join project on installation.project_id = project.id
							left join public_detection_view on installation_station.id = public_detection_view.station_id
							where public_detection_view.station_id is null;''')
			}
		}

        sql("SELECT drop_matview('detection_count_per_station_mv');")
        sql("SELECT create_matview('detection_count_per_station_mv', 'detection_count_per_station');")
	}
}
