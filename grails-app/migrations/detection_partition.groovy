databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1347840115682-1") {
		createTable(tableName: "invalid_detection") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "invalid_detection_pkey")
			}

			column(name: "message", type: "VARCHAR(255)")

			column(name: "reason", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1347840115682-2") {
		createTable(tableName: "valid_detection") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "valid_detection_pkey")
			}

			column(name: "receiver_deployment_id", type: "int8")
		}
	}

	// Migrate detections in to either valid or invalid...
	changeSet(author: "jburgess (generated)", id: "1347840115682-3") {
		grailsChange
		{
			change
			{
				sql.eachRow('select * from raw_detection')
				{
					row ->
					
					if (row.class == 'au.org.emii.aatams.detection.ValidDetection')
					{
						sql.executeInsert('insert into valid_detection (id, receiver_deployment_id) values (' + row.id + ', ' + row.receiver_deployment_id + ')')
					}
					else if (row.class == 'au.org.emii.aatams.detection.InvalidDetection')
					{
						sql.executeInsert('insert into invalid_detection (id, message, reason) values (' + row.id + ', \'' + row.message + '\', \'' + row.reason + '\')')
					}
					else
					{
						assert(false): "Unknown detection class."
					}
				}
			}
		}
	}
	
	// Drop views.
	changeSet(author: "jburgess", id: "1347840115682-3-2")
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

	
	changeSet(author: "jburgess (generated)", id: "1347840115682-6") {
		dropForeignKeyConstraint(baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3bea110b34")
	}

	changeSet(author: "jburgess (generated)", id: "1347840115682-7") {
		dropForeignKeyConstraint(baseTableName: "raw_detection", baseTableSchemaName: "public", constraintName: "fk8f206b2eb30f8f32")
	}

	changeSet(author: "jburgess (generated)", id: "1347840115682-8") {
		addForeignKeyConstraint(baseColumnNames: "detection_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3b55219160", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "valid_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1347840115682-9") {
		addForeignKeyConstraint(baseColumnNames: "receiver_deployment_id", baseTableName: "valid_detection", baseTableSchemaName: "public", constraintName: "fk28ce8a02b30f8f32", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}
	
	changeSet(author: "jburgess (generated)", id: "1347840115682-10") {
		dropColumn(columnName: "class", tableName: "raw_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1347840115682-11") {
		dropColumn(columnName: "message", tableName: "raw_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1347840115682-12") {
		dropColumn(columnName: "reason", tableName: "raw_detection")
	}

	changeSet(author: "jburgess (generated)", id: "1347840115682-13") {
		dropColumn(columnName: "receiver_deployment_id", tableName: "raw_detection")
	}

	// Update views "detection_extract_view"...
	changeSet(author: "jburgess", id: "1347840115682-14")
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
	
	changeSet(author: "jburgess", id: "1347840115682-14-1", runOnChange: true)
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

	changeSet(author: "jburgess", id: "1347840115682-14-2", runOnChange: true)
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
	changeSet(author: "jburgess", id: "1347840115682-15", runOnChange: true)
	{
		sql('''SELECT refresh_matview('detection_count_per_station_mv');''')
	}
}
