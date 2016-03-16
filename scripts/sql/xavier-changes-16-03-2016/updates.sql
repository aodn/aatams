set search_path = aatams, public;

--------------------
---- Inadvertent entry into the production db, needs to be deleted.
DELETE FROM sensor where transmitter_id = 'A69-1303-62731' AND transmitter_type_id = 34;




--------------------
---- VR2-AR device model inconsistency. In the device_model table the VR2AR receiver model is stored both as 'VR2-AR' and VR2AR', it should only be stored as 'VR2AR'.
UPDATE device SET model_id = 129558776 WHERE model_id = 117465295 -- Transforms all receivers registered as VR2-AR into VR2AR
DELETE FROM device_model WHERE model_name = 'VR2-AR'; -- Delete wrongly named receiver model

---- Test results
-- SELECT DISTINCT receiver_name FROM detection WHERE receiver_name LIKE 'VR2-AR%' -- Returns 0 row
-- SELECT DISTINCT receiver_name FROM detection WHERE receiver_name LIKE 'VR2AR%' -- Returns 10 rows
-- SELECT COUNT(*) FROM device WHERE model_id = 117465295 -- VR2-AR -- Count = 20
-- SELECT COUNT(*) FROM device WHERE model_id = 129558776 -- VR2AR -- Count = 0




--------------------
---- Deletion of receiver for JCU student 
DELETE FROM receiver_deployment 
USING device
WHERE device.id = receiver_deployment.receiver_id 
AND device.serial_number = '102044' AND initialisationdatetime_timestamp IS NULL;




--------------------
---- Add 'RANGE TEST' tag as a new type of transmitter in the transmitter_type table. TO DO: update pinger tags which we know are range test tags in the sensor table (Andre to provide list).
INSERT INTO transmitter_type (id, version, transmitter_type_name) VALUES
(37, 0, 'RANGE TEST');





--------------------
---- Clean up of animal table, 113 records will be deleted. Addresses one of the points made by Dave's second to last comment in #123 
WITH a AS (
SELECT a.id FROM animal a 
FULL JOIN animal_release ar ON ar.animal_id = a.id 
WHERE animal_id IS NULL 
ORDER BY id)
DELETE FROM animal
USING a
WHERE a.id = animal.id;

-- Test results
-- SELECT * FROM animal a 
-- FULL JOIN animal_release ar ON ar.animal_id = a.id 
-- WHERE animal_id IS NULL 
-- ORDER BY a.id;





--------------------
---- Capture and release location should be mandatory fields #268
-- ALTER TABLE animal_release ALTER COLUMN capture_location SET NOT NULL;
-- ALTER TABLE animal_release ALTER COLUMN release_location SET NOT NULL;
---- JA should be Liquibase?
-- 
-- -- Test results
-- SELECT * FROM animal_release WHERE capture_location IS NULL OR release_location IS NULL; -- Returns 256 rows, can't run the above code if those two fields have null values, CONTACT ANDRE

