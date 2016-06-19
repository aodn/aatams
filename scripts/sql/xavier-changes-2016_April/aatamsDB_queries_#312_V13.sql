set search_path = aatams, public;

begin;

-- Fix #312 - V13
DELETE FROM sensor WHERE id = 128427911; 
DELETE FROM surgery WHERE id = 128427913;
DELETE FROM device WHERE id = 128427910;

---- Test results
-- SELECT d.*, s.*, su.*
-- FROM surgery su
-- JOIN device d ON su.tag_id = d.id
-- JOIN sensor s ON s.tag_id = d.id
-- WHERE s.transmitter_id = 'A69-1303-34797';

commit;