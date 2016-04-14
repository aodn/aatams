set search_path = aatams, public;

begin;

-- Fix #309 - Delete manually entered duplicate tag entry
DELETE FROM sensor WHERE transmitter_id = 'A69-1601-65026' AND transmitter_type_id = 33;

---- Test results
-- SELECT s.transmitter_id, s.transmitter_type_id, tt.transmitter_type_name
-- FROM sensor s
-- JOIN transmitter_type tt ON tt.id = s.transmitter_type_id
-- WHERE transmitter_id = 'A69-1601-65026';

commit;