select
    sensor.transmitter_id,
    releasedatetime_timestamp as "release timestamp",
    common_name

    from sensor
    join surgery on sensor.tag_id = surgery.tag_id
    join animal_release on surgery.release_id = animal_release.id
    join animal on animal_release.animal_id = animal.id
    join species on animal.species_id = species.id

    join valid_detection on sensor.transmitter_id = valid_detection.transmitter_id

    where valid_detection.timestamp >= releasedatetime_timestamp

    -- example filters
    and common_name = 'White Shark'

    limit 10;
