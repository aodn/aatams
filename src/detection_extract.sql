select timestamp, transmitter_id, 
installation_station.name, installation_station.location_geometry,
(device_model.model_name || '-' || device.serial_number) as receiver_name,
sec_user.name, organisation.name
from raw_detection

left join receiver_deployment on receiver_deployment_id = receiver_deployment.id
left join installation_station on receiver_deployment.station_id = installation_station.id
left join device on receiver_deployment.receiver_id = device.id
left join device_model on device.model_id = device_model.id
left join receiver_download_file on receiver_download_id = receiver_download_file.id
left join sec_user on receiver_download_file.requesting_user_id = sec_user.id
left join organisation on device.organisation_id = organisation.id
where raw_detection.class = 'au.org.emii.aatams.detection.ValidDetection'
