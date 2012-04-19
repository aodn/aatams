databaseChangeLog = 
{
	changeSet(author: "jburgess", id: "1332135917000-1")
	{
		grailsChange
		{
			change
			{
				def viewName = application.config.rawDetection.extract.view.name
				def viewSelect = application.config.rawDetection.extract.view.select
				
				// Need to drop first, see: http://dba.stackexchange.com/questions/586/cant-rename-columns-in-postgresql-views-with-create-or-replace
				sql.execute ('drop view ' + viewName)
				sql.execute ('create or replace view ' + viewName + ' as ' + viewSelect)
			}
		}
	}
	
	changeSet(author: "jburgess", id: "1332135917000-2", runOnChange: true)
	{
		createProcedure('''CREATE OR REPLACE FUNCTION isreleaseembargoed(release_id bigint) RETURNS boolean AS
			$$
			DECLARE
				found_animal_release animal_release%ROWTYPE;
			BEGIN
				SELECT INTO found_animal_release * FROM animal_release WHERE id = release_id;
				RETURN found_animal_release.embargo_date > current_date;
			END;
			$$ LANGUAGE plpgsql STRICT;''')
	}
	
	changeSet(author: "jburgess", id: "1332135917000-3", runOnChange: true)
	{
		createProcedure('''CREATE OR REPLACE FUNCTION scramblepoint(point geometry) RETURNS geometry AS
			$$
			DECLARE
				scrambled_point geometry;
				scrambled_lat numeric;
				scrambled_lon numeric;
			BEGIN
				scrambled_lon = round(st_x(point)::NUMERIC, 2);
				scrambled_lat = round(st_y(point)::NUMERIC, 2);

				return st_point(scrambled_lon, scrambled_lat);
			END;
			$$ LANGUAGE plpgsql STRICT;''')
	}
	

	changeSet(author: "jburgess", id: "1332135917000-4", runOnChange: true)
	{
		sql("DROP VIEW IF EXISTS public_detection_view")
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

	changeSet(author: "jburgess", id: "1332135917000-5", runOnChange: true)
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
								''' + "'" + application.config.grails.serverURL + '''/report/extract?name=detection&formats=CSV&filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in=' || station as detection_download_url,
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
	
	// Preconditions: 1332309793000-1 1332309793000-2 1332309793000-3 1332309793000-4
	changeSet(author: "jburgess", id: "1332312087000-1", runOnChange: true)
	{
		preConditions 
		{
			changeSetExecuted(id: "1332309793000-1", author: "jburgess", changeLogFile: "materialized_views.groovy")
			changeSetExecuted(id: "1332309793000-2", author: "jburgess", changeLogFile: "materialized_views.groovy")
			changeSetExecuted(id: "1332309793000-3", author: "jburgess", changeLogFile: "materialized_views.groovy")
			changeSetExecuted(id: "1332309793000-4", author: "jburgess", changeLogFile: "materialized_views.groovy")
			changeSetExecuted(id: "1332135917000-5", author: "jburgess", changeLogFile: "geoserver_detections_view.groovy")
		}
		
		sql('''SELECT create_matview('detection_count_per_station_mv', 'detection_count_per_station');''')
		sql('''SELECT refresh_matview('detection_count_per_station_mv');''')
	}
}
