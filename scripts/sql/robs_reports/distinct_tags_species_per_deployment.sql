set search_path to 'aatams', 'public';

COPY (
    select distinct
        installation.name as installation_name,
        installation_station.name as station_name,
        species.common_name,
        count(distinct transmitter_id) as distinct_tag_count,
        array_to_string(array_agg(distinct transmitter_id), ',') as tag_ids,
        st_y(receiver_deployment.location) as latitude, st_x(receiver_deployment.location) as longitude,
        depth_below_surfacem, deploymentdatetime_timestamp as deployment_datetime

    from receiver_deployment
    join valid_detection on valid_detection.receiver_deployment_id = receiver_deployment.id
    join installation_station on installation_station.id = receiver_deployment.station_id
    join installation on installation.id = installation_station.installation_id
    left join detection_surgery on detection_surgery.detection_id = valid_detection.id
    left join surgery on surgery.id = detection_surgery.surgery_id
    left join animal_release on animal_release.id = surgery.release_id
    left join animal on animal.id = animal_release.animal_id
    left join species on species.id = animal.species_id

    group by installation.name, installation_station.name, species.common_name, latitude, longitude, depth_below_surfacem, deploymentdatetime_timestamp
    order by installation.name, installation_station.name, species.common_name
) TO stdout WITH CSV HEADER;
