set search_path = aatams, public;

begin;

-- Fix #326 - Delete manually entered sensor entry
DELETE FROM sensor WHERE transmitter_id IN ('A69-9001-65177','A69-9001-65178','A69-9001-65179','A69-9001-65180','A69-9001-65181','A69-9001-65182');

---- Test results
-- SELECT s.transmitter_id
-- FROM sensor s
-- WHERE transmitter_id IN ('A69-9001-65177','A69-9001-65178','A69-9001-65179','A69-9001-65180','A69-9001-65181','A69-9001-65182');

commit;