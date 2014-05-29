
    select timestamp::date, latitude, longitude, species_name, count(transmitter_id) from (
        select timestamp::date, latitude, longitude, species_name, transmitter_id from (
            select * from aatams.detection_extract_view_mv
              where species_name != ''
              and (embargo_date is null or embargo_date < current_timestamp)
              --limit 100000
          ) as subset
          where species_name != ''
          group by timestamp::date, latitude, longitude, species_name, transmitter_id
          order by timestamp::date, latitude, longitude, species_name
    ) as grouped
    group by timestamp::date, latitude, longitude, species_name;
