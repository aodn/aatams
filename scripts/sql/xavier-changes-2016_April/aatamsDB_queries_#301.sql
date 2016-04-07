set search_path = aatams, public;

begin;

DELETE FROM device 
USING device_model
WHERE device.model_id = device_model.id 
AND device.serial_number IN ('114543','114535','120741','102026','105863','113118') AND device_model.model_name = 'VR2AR';

---- Test results
-- SELECT * FROM device 
-- JOIN device_model ON device.model_id = device_model.id
-- WHERE serial_number IN ('114543','114535','120741','102026','105863','113118') AND model_name = 'VR2AR';

commit;