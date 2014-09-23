set search_path to 'aatams', 'public';

COPY (
    select distinct
        installation,
        station,
        species_name,
        count(distinct dets.transmitter_id) as distinct_tag_count,
        latitude, longitude, depth_below_surfacem, deploymentdatetime_timestamp as deployment_datetime
    from (select * from detection_extract_view_mv) as dets
    join valid_detection on detection_id = valid_detection.id
    join receiver_deployment on valid_detection.receiver_deployment_id = receiver_deployment.id
    group by installation, station, species_name, latitude, longitude, depth_below_surfacem, deploymentdatetime_timestamp
    order by installation, station, species_name
) TO stdout WITH CSV HEADER;
