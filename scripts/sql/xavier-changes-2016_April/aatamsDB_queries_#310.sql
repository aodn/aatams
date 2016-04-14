set search_path = aatams, public;

begin;

-- Fix #310 - Change of Range Test Tag specs
UPDATE sensor SET transmitter_type_id = 129582657 WHERE sensor.id IN (93016148,93016074,93016078,93016082,93016086,93016090,93016138,93016142,93016118,93016114,93016102,93016094,93016110,93016126,93016106,93016134,93016130,93016122,93016098);
DELETE FROM sensor WHERE sensor.id = 93016146;

---- Test results
-- SELECT s.id, p.name, s.transmitter_id, s.transmitter_type_id, tt.transmitter_type_name
-- FROM sensor s
-- JOIN device d ON s.tag_id = d.id
-- JOIN transmitter_type tt ON tt.id = s.transmitter_type_id
-- JOIN project p ON p.id = d.project_id
-- WHERE p.name = 'AATAMS Bondi range experiment 2009';

commit;