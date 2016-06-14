set search_path = aatams, public;

begin;

-- Fix #339 - Delete erroneous entry for receiver VR2W-102023
DELETE FROM receiver_deployment WHERE id = 117606505;

---- Test results
--   SELECT (dm.model_name::text || '-'::text) || d.serial_number::text AS receiver_name, 
-- 	rd.id AS receiver_deployment_id, 
-- 	rd.deploymentdatetime_timestamp, 
-- 	rd.initialisationdatetime_timestamp
--   FROM device ds
--   JOIN device_model dm ON dm.id = d.model_id
--   JOIN receiver_deployment rd ON rd.receiver_id = d.id
-- 	WHERE (dm.model_name::text || '-'::text) || d.serial_number::text = 'VR2W-102023';

commit;