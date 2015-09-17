SELECT id, sex FROM aatams.sex;
SELECT id, unit FROM aatams.measurement_unit;
SELECT id, type FROM aatams.animal_measurement_type;
SELECT id, release_id, type_id, unit_id, value FROM aatams.animal_measurement;
SELECT id, release_id, tag_id FROM aatams.surgery;
SELECT id, 
animal_id, 
capturedatetime_timestamp, 
capture_locality,
embargo_date, 
project_id, 
releasedatetime_timestamp,
release_locality,
 ST_SetSRID(capture_location,4326),
 ST_SetSRID(release_location,4326)
FROM aatams.animal_release;
SELECT id, sex_id, species_id FROM aatams.animal;
SELECT id, common_name, order_name, phylum, scientific_name FROM aatams.species;
SELECT id, class FROM aatams.device;
SELECT id, tag_id, transmitter_id FROM aatams.sensor;
SELECT id, bottom_depthm, deploymentdatetime_timestamp, depth_below_surfacem, station_id FROM aatams.receiver_deployment;
SELECT id, 
  installation_id, 
  name, 
  ST_SetSRID(ST_POINT(trunc(ST_X(location)::numeric,2),trunc(ST_Y(location)::numeric,2)), 4326) AS location 
  FROM aatams.installation_station;
SELECT id, name, project_id FROM aatams.installation;
SELECT id, name, is_protected FROM aatams.project;
SELECT detection.id, receiver_deployment_id, timestamp, transmitter_id, detection.receiver_name 
FROM aatams.detection
LEFT JOIN aatams.receiver ON detection.receiver_name::text = receiver.receiver_name
LEFT JOIN aatams.deployment_and_recovery deployment_and_recovery ON receiver.id = deployment_and_recovery.receiver_id AND detection.timestamp >= deployment_and_recovery.initialisationdatetime_timestamp AND detection.timestamp <= deployment_and_recovery.recoverydatetime_timestamp
WHERE aatams.invalid_reason(detection.*, receiver.*, deployment_and_recovery.*) IS NULL;