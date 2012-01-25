select to_char((timestamp::timestamp with time zone) at time zone '00:00', 'YYYY-MM-DD HH24:MI:SS') as formatted_timestamp, installation_station.name as "station_name", 
	st_y(installation_station.location) as latitude, st_x(installation_station.location) as longitude,
	(device_model.model_name || '-' || device.serial_number) as receiver_name,
	COALESCE(sensor.transmitter_id, '') as sensor_id,
	COALESCE((species.spcode || ' - ' || species.scientific_name || ' (' || species.common_name || ')'), '') as species_name,

	sec_user.name as uploader,
	raw_detection.transmitter_id as "transmitter_id",
	organisation.name as organisation_name

	from raw_detection

	left join receiver_deployment on receiver_deployment_id = receiver_deployment.id
	left join installation_station on receiver_deployment.station_id = installation_station.id
	left join installation on installation_station.installation_id = installation.id
	left join project on installation.project_id = project.id
	left join device on receiver_deployment.receiver_id = device.id
	left join device_model on device.model_id = device_model.id
	left join receiver_download_file on receiver_download_id = receiver_download_file.id
	left join sec_user on receiver_download_file.requesting_user_id = sec_user.id
	left join organisation on device.organisation_id = organisation.id
	left join detection_surgery on raw_detection.id = detection_surgery.detection_id
	left join sensor on detection_surgery.sensor_id = sensor.id

	left join surgery on detection_surgery.surgery_id = surgery.id
	left join animal_release on surgery.release_id = animal_release.id
	left join animal on animal_release.animal_id = animal.id
	left join species on animal.species_id = species.id

	where     raw_detection.class = 'au.org.emii.aatams.detection.ValidDetection'
	order by installation_station.name, raw_detection.transmitter_id, timestamp