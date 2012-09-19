databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1347934661760-1") {
		createTable(tableName: "invalid_detection") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "invalid_detection_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "geometry")

			column(name: "message", type: "VARCHAR(255)")

			column(name: "reason", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "receiver_download_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "sensor_unit", type: "VARCHAR(255)")

			column(name: "sensor_value", type: "FLOAT4(8,8)")

			column(name: "station_name", type: "VARCHAR(255)")

			column(name: "timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_id", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_name", type: "VARCHAR(255)")

			column(name: "transmitter_serial_number", type: "VARCHAR(255)")
		}
	}

//	changeSet(author: "jburgess (generated)", id: "1347934661760-2") {
//		createTable(tableName: "receiver_invalid_detection") {
//			column(name: "receiver_invalid_detections_id", type: "int8")
//
//			column(name: "invalid_detection_id", type: "int8")
//		}
//	}
//
	changeSet(author: "jburgess (generated)", id: "1347934661760-3") {
		createTable(tableName: "receiver_valid_detection") {
			column(name: "receiver_valid_detections_id", type: "int8")

			column(name: "valid_detection_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-4") {
		createTable(tableName: "valid_detection") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "valid_detection_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "geometry")

			column(name: "receiver_deployment_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_download_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "sensor_unit", type: "VARCHAR(255)")

			column(name: "sensor_value", type: "FLOAT4(8,8)")

			column(name: "station_name", type: "VARCHAR(255)")

			column(name: "timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_id", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_name", type: "VARCHAR(255)")

			column(name: "transmitter_serial_number", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-8") {
		dropForeignKeyConstraint(baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3bea110b34")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-9") {
		dropForeignKeyConstraint(baseTableName: "raw_detection", baseTableSchemaName: "public", constraintName: "fk8f206b2eb30f8f32")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-10") {
		dropForeignKeyConstraint(baseTableName: "raw_detection", baseTableSchemaName: "public", constraintName: "fk8f206b2e7eb9cbee")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-11") {
		dropForeignKeyConstraint(baseTableName: "receiver_raw_detection", baseTableSchemaName: "public", constraintName: "fkc88698dee9e13deb")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-12") {
		dropForeignKeyConstraint(baseTableName: "receiver_raw_detection", baseTableSchemaName: "public", constraintName: "fkc88698de92eec5e4")
	}

	// Migrate existing detections...
	changeSet(author: "jburgess (generated)", id: "1347934661760-12-1") {
		grailsChange
		{
			change
			{
				sql.eachRow('select * from raw_detection')
				{
					row ->
					
					if (row.class == 'au.org.emii.aatams.detection.ValidDetection')
					{
						String insertStatement = '''insert into valid_detection (id, version, location, receiver_deployment_id, receiver_download_id, receiver_name, sensor_unit, ''' +
							'''sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'''
						sql.executeInsert(insertStatement, [row.id, row.version, row.location, row.receiver_deployment_id, row.receiver_download_id, row.receiver_name, row.sensor_unit,
							row.sensor_value, row.station_name, row.timestamp, row.transmitter_id, row.transmitter_name, row.transmitter_serial_number])
					}
					else if (row.class == 'au.org.emii.aatams.detection.InvalidDetection')
					{
						String insertStatement = '''insert into invalid_detection (id, version, location, message, reason, receiver_download_id, receiver_name, sensor_unit, ''' +
							'''sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'''
						sql.executeInsert(insertStatement, [row.id, row.version, row.location, row.message, row.reason, row.receiver_download_id, row.receiver_name, row.sensor_unit, 
							row.sensor_value, row.station_name, row.timestamp, row.transmitter_id, row.transmitter_name, row.transmitter_serial_number])
					}
					else
					{
						assert(false): "Unknown detection class."
					}
				}
			}
		}
	}
	
	// Migrate existing receiver <-> detection join table.
//	TODO 1347934661760-12-2
	
	// Drop views (which depend on raw_detection, about to be dropped).
	changeSet(author: "jburgess", id: "1347934661760-12-3")
	{
		grailsChange
		{
			change
			{
				sql.execute ('drop view detection_count_per_station')
				sql.execute ('drop view public_detection_view')
				def viewName = application.config.rawDetection.extract.view.name
				sql.execute ('drop view ' + viewName)
			}
		}
	}

	
	
	changeSet(author: "jburgess (generated)", id: "1347934661760-13") {
		createIndex(indexName: "invalid_receivername_index", tableName: "invalid_detection", unique: "false") {
			column(name: "receiver_name")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-14") {
		createIndex(indexName: "invalid_timestamp_index", tableName: "invalid_detection", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-15") {
		createIndex(indexName: "invalid_transmitterid_index", tableName: "invalid_detection", unique: "false") {
			column(name: "transmitter_id")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-16") {
		createIndex(indexName: "valid_receivername_index", tableName: "valid_detection", unique: "false") {
			column(name: "receiver_name")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-17") {
		createIndex(indexName: "valid_timestamp_index", tableName: "valid_detection", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-18") {
		createIndex(indexName: "valid_transmitterid_index", tableName: "valid_detection", unique: "false") {
			column(name: "transmitter_id")
		}
	}

	// See #1988 - remove invalid detection surgeries.
	changeSet(author: "jburgess (generated)", id: "1347934661760-19-0") 
	{
		grailsChange
		{
			change
			{
				sql.execute('delete from detection_surgery using invalid_detection where detection_surgery.detection_id = invalid_detection.id')
			}
		}
	}
	
	changeSet(author: "jburgess (generated)", id: "1347934661760-19") {
		addForeignKeyConstraint(baseColumnNames: "detection_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3b55219160", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "valid_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-20") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "invalid_detection", baseTableSchemaName: "public", constraintName: "fk2b27ee3d7eb9cbee", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

//	changeSet(author: "jburgess (generated)", id: "1347934661760-21") {
//		addForeignKeyConstraint(baseColumnNames: "invalid_detection_id", baseTableName: "receiver_invalid_detection", baseTableSchemaName: "public", constraintName: "fkc55fc3ed112bca8d", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "invalid_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
//	}
//
//	changeSet(author: "jburgess (generated)", id: "1347934661760-22") {
//		addForeignKeyConstraint(baseColumnNames: "receiver_invalid_detections_id", baseTableName: "receiver_invalid_detection", baseTableSchemaName: "public", constraintName: "fkc55fc3ed35159d9c", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
//	}
//
//	changeSet(author: "jburgess (generated)", id: "1347934661760-23") {
//		addForeignKeyConstraint(baseColumnNames: "receiver_valid_detections_id", baseTableName: "receiver_valid_detection", baseTableSchemaName: "public", constraintName: "fka1600bb24727ae1", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
//	}
//
//	changeSet(author: "jburgess (generated)", id: "1347934661760-24") {
//		addForeignKeyConstraint(baseColumnNames: "valid_detection_id", baseTableName: "receiver_valid_detection", baseTableSchemaName: "public", constraintName: "fka1600bb2428744c3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "valid_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
//	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-25") {
		addForeignKeyConstraint(baseColumnNames: "receiver_deployment_id", baseTableName: "valid_detection", baseTableSchemaName: "public", constraintName: "fk28ce8a02b30f8f32", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-26") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "valid_detection", baseTableSchemaName: "public", constraintName: "fk28ce8a027eb9cbee", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-27") {
		dropIndex(indexName: "receivername_index", tableName: "raw_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-28") {
		dropIndex(indexName: "timestamp_index", tableName: "raw_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-29") {
		dropIndex(indexName: "transmitterid_index", tableName: "raw_detection")
	}

//	changeSet(author: "jburgess (generated)", id: "1347934661760-30") {
//		dropColumn(columnName: "surgeries_idx", tableName: "surgery")
//	}

//	changeSet(author: "jburgess (generated)", id: "1347934661760-31") {
//		dropView(schemaName: "public", viewName: "detection_count_per_station")
//	}
//
//	changeSet(author: "jburgess (generated)", id: "1347934661760-32") {
//		dropView(schemaName: "public", viewName: "detection_extract_view")
//	}
//
//	changeSet(author: "jburgess (generated)", id: "1347934661760-33") {
//		dropView(schemaName: "public", viewName: "public_detection_view")
//	}
//
//	changeSet(author: "jburgess (generated)", id: "1347934661760-34") {
//		dropTable(tableName: "detection_count_per_station_mv")
//	}
//
//	changeSet(author: "jburgess (generated)", id: "1347934661760-35") {
//		dropTable(tableName: "matviews")
//	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-36") {
		dropTable(tableName: "raw_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1347934661760-37") {
		dropTable(tableName: "receiver_raw_detection")
	}
	
	
	// Update views "detection_extract_view"...
	changeSet(author: "jburgess", id: "1347934661760-37-1")
	{
		grailsChange
		{
			change
			{
				def viewName = application.config.rawDetection.extract.view.name
				def viewSelect = application.config.rawDetection.extract.view.select
				
				sql.execute ('create or replace view ' + viewName + ' as ' + viewSelect)
			}
		}
	}
	
	changeSet(author: "jburgess", id: "1347934661760-37-2", runOnChange: true)
	{
		createView('''select *,
						scramblepoint(location) as public_location,
						case when isreleaseembargoed(animal_release_id) then 'embargoed'
							 else species_name
						end
						as public_species_name,
						case when isreleaseembargoed(animal_release_id) then 'embargoed'
							 else spcode
						end
						as public_spcode
						
						from detection_extract_view
						''', viewName: "public_detection_view")
	}

	changeSet(author: "jburgess", id: "1347934661760-37-3", runOnChange: true)
	{
		grailsChange
		{
			change
			{
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
																 ) t)) * 10 as relative_detection_count
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
								0 as releative_detection_count
							from installation_station
							left join installation on installation_station.installation_id = installation.id
							left join project on installation.project_id = project.id
							left join public_detection_view on installation_station.id = public_detection_view.station_id
							where public_detection_view.station_id is null;''')
			}
		}
	}

	// Refresh materialized view...
	changeSet(author: "jburgess", id: "1347934661760-37-4", runOnChange: true)
	{
		sql('''SELECT refresh_matview('detection_count_per_station_mv');''')
	}
}
