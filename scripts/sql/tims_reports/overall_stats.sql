-- num unique transmitter_ids
select count(distinct transmitter_id) as "num unique tag IDs detected" from raw_detection where class = 'au.org.emii.aatams.detection.ValidDetection';

-- num tags
select count (*) as "tags AATAMS knows about" from device where class = 'au.org.emii.aatams.Tag';

-- num known detected tag count
select count(distinct sensor.transmitter_id) as "num detected tags AATAMS knows about" from raw_detection
join sensor on raw_detection.transmitter_id = sensor.transmitter_id
where raw_detection.class = 'au.org.emii.aatams.detection.ValidDetection';

-- unique tags detected by species
select count(distinct tag_id) as "tags detected by species" from raw_detection
join detection_surgery on raw_detection.id = detection_surgery.id
join surgery on detection_surgery.surgery_id = surgery.id
where raw_detection.class = 'au.org.emii.aatams.detection.ValidDetection';


select count (*) release_count from animal_release;

select count(*) as detection_count from raw_detection where class = 'au.org.emii.aatams.detection.ValidDetection';

select count(*) as deployment_count from receiver_deployment;

select count(*) as installation_count from installation;

select count(*) as station_count from installation_station;

