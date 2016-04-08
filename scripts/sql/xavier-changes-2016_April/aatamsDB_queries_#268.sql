﻿set search_path = aatams, public;

begin;

-- 1. Clean up animal_release table by deleting animal_release records with no corresponding surgery
DELETE FROM animal_release ar
WHERE NOT EXISTS (SELECT release_id FROM surgery s WHERE s.release_id = ar.id);

---- Test results
-- SELECT * FROM animal_release ar
-- WHERE NOT EXISTS (SELECT release_id FROM surgery s WHERE s.release_id = ar.id);

-- 2. When capture_location is null, assign value of release_location
UPDATE animal_release
SET capture_location = release_location
WHERE capture_location IS NULL;

---- Test results
-- SELECT p.name AS project_name,
-- transmitter_id,
-- capture_locality,
-- release_locality,
-- ar.status AS release_status,
-- ar.capture_location,
-- ar.release_location
-- FROM animal_release ar
-- JOIN project p ON p.id = ar.project_id
-- JOIN surgery s ON s.release_id = ar.id 
-- JOIN sensor se ON se.tag_id = s.tag_id
-- WHERE capture_location IS NULL
-- ORDER BY project_name;

-- 3. When release_location is null, assign value of capture_location
UPDATE animal_release
SET release_location = capture_location
WHERE release_location IS NULL;

---- Test results
-- SELECT p.name AS project_name,
-- transmitter_id,
-- capture_locality,
-- release_locality,
-- ar.status AS release_status,
-- ar.capture_location,
-- ar.release_location
-- FROM animal_release ar
-- JOIN project p ON p.id = ar.project_id
-- JOIN surgery s ON s.release_id = ar.id 
-- JOIN sensor se ON se.tag_id = s.tag_id
-- WHERE release_location IS NULL
-- ORDER BY project_name;

commit;