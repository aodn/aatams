-- num unique transmitter_ids
select count(distinct transmitter_id) as "num unique tag IDs detected" from valid_detection;

-- num tags
select count (*) as "tags AATAMS knows about" from device where class = 'au.org.emii.aatams.Tag';

-- num known detected tag count
select count(distinct sensor.transmitter_id) as "num detected tags AATAMS knows about" from valid_detection
join sensor on valid_detection.transmitter_id = sensor.transmitter_id;

-- unique tags detected by species
select count(distinct tag_id) as "tags detected by species" from valid_detection
join detection_surgery on valid_detection.id = detection_surgery.detection_id
join surgery on detection_surgery.surgery_id = surgery.id;


select count (*) release_count from animal_release;

select count(*) as detection_count from valid_detection;

select count(*) as deployment_count from receiver_deployment;

select count(*) as installation_count from installation;

select count(*) as station_count from installation_station;

