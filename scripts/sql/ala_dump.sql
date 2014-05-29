--
-- This script dumps data in a format that would be useful for Atlas of Living Australia integration.
--
-- The idea being that rather than exporting *all* our data (too detailed for ALA), we give them
-- a summary of records - one record for each date/station/species, with a count of the number of
-- unique individuals correspondingly.

-- Probably, the data will eventually be delivered using WFS and the actual data sent across may
-- be refined.  The main motivation for this was to get an idea of the number of records - but since
-- it took me a while to get the query correct, I wanted to check it in so it's there for reference
-- later on.
--
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
