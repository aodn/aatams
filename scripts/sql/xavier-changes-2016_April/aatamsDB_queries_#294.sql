set search_path = aatams, public;

begin;

-- Change releases with positive latitudes
UPDATE animal_release SET capture_location = ST_POINT(ST_X(capture_location), ST_Y(capture_location) * (-1))
WHERE ST_Y(capture_location) >= 0;
UPDATE animal_release SET release_location = ST_POINT(ST_X(release_location), ST_Y(release_location) * (-1))
WHERE ST_Y(release_location) >= 0;

---- Test results
SELECT ST_ASTEXT(capture_location), ST_ASTEXT(release_location) FROM animal_release
WHERE ST_Y(capture_location) >= 0 OR ST_Y(release_location) >= 0;


-- Change releases with negative longitudes
UPDATE animal_release SET capture_location = ST_POINT(ST_X(capture_location) * (-1), ST_Y(capture_location))
WHERE ST_X(capture_location) <= 0;
UPDATE animal_release SET release_location = ST_POINT(ST_X(release_location) * (-1), ST_Y(release_location))
WHERE ST_X(release_location) <= 0;

---- Test results
SELECT ST_ASTEXT(capture_location), ST_ASTEXT(release_location) FROM animal_release
WHERE ST_X(capture_location) <= 0 OR ST_X(release_location) <= 0;

commit;