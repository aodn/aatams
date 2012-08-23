-- num unique transmitter_ids
select count(distinct transmitter_id) as unique_detected_tag_id_count from raw_detection where class = 'au.org.emii.aatams.detection.ValidDetection';

-- num known detected tag count
select count(distinct sensor.transmitter_id) as known_tags_detected from raw_detection
join sensor on raw_detection.transmitter_id = sensor.transmitter_id
where raw_detection.class = 'au.org.emii.aatams.detection.ValidDetection';

-- num tags
select count (*) as tags_count from device where class = 'au.org.emii.aatams.Tag';

select count (*) release_count from animal_release;

select count(*) as detection_count from raw_detection where class = 'au.org.emii.aatams.detection.ValidDetection';

select count(*) as deployment_count from receiver_deployment;

select count(*) as installation_count from installation;

select count(*) as station_count from installation_station;


