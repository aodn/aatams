set search_path = aatams, public;

begin;

-- Fix #312 - Double tag release entries
DELETE FROM surgery WHERE id = 128688734;

DELETE FROM surgery WHERE id = 92990592; 
DELETE FROM animal_release WHERE id = 92990590;
DELETE FROM animal WHERE id = 92990589;

---- Test results
-- SELECT su.id, s.transmitter_id, su.release_id
-- FROM sensor s
-- JOIN surgery_with_end_date su ON s.tag_id = su.tag_id
-- WHERE release_id = 92990542;

-- SELECT su.id AS surgery_id, s.transmitter_id, su.release_id, ar.*, a.*
-- FROM sensor s
-- JOIN surgery_with_end_date su ON s.tag_id = su.tag_id
-- JOIN animal_release ar ON ar.id = su.release_id
-- JOIN animal a ON a.id = ar.animal_id
-- WHERE s.transmitter_id = 'A69-1303-32588';

commit;