set search_path = aatams, public;

begin;

---- Fix issues listed in Andre's spreadsheet, Sheet 1

--------------------
-- 1. Delete 'mq' ahd 'Stradbroke Workshop' projects
DELETE FROM sensor WHERE id IN (77640673, 77640675, 77640701);
DELETE FROM device WHERE id IN (77640671, 77640699);
DELETE FROM project_role WHERE id = 129352710;
DELETE FROM organisation_project WHERE id = 129352708;
DELETE FROM project WHERE name = 'mq' OR name = 'Stradbroke Workshop';

---- Test results
-- SELECT * FROM sensor WHERE transmitter_id = 'A180-1105-1111' OR transmitter_id = 'A180-1105-2222' OR transmitter_id = 'A180-1105-3333';
-- SELECT * FROM project_role WHERE project_id IN (77640666, 129352707);
-- SELECT * FROM organisation_project WHERE project_id IN (77640666, 129352707);
-- SELECT * FROM project WHERE name = 'mq' OR name = 'Stradbroke Workshop';

--------------------
-- 2. Delete deployment 2015-01-19, 10:02:06, Coral Bay BB6, receiver 114574
DELETE FROM receiver_deployment WHERE id = 129352973;

---- Test results
-- SELECT receiver_deployment.id, serial_number, initialisationdatetime_timestamp, deploymentdatetime_timestamp, deploymentdatetime_zone 
-- FROM receiver_deployment
-- JOIN device ON device.id = receiver_deployment.receiver_id 
-- WHERE serial_number = '114574';
-- SELECT COUNT(*) FROM aatams.valid_detection WHERE receiver_deployment_id = 129352973; -- 0 record associated with this deployment

--------------------
-- 3. Delete deployment CL1, 101848, 23/08/08, 15:30
DELETE FROM receiver_deployment WHERE id = 4479625;

---- Test results
-- SELECT receiver_deployment.id, serial_number, initialisationdatetime_timestamp, deploymentdatetime_timestamp, deploymentdatetime_zone 
-- FROM receiver_deployment
-- JOIN device ON device.id = receiver_deployment.receiver_id 
-- WHERE serial_number = '101848';
-- SELECT COUNT(*) FROM aatams.valid_detection WHERE receiver_deployment_id = 4479625; -- 0 record associated with this deployment

--------------------
-- 4. Delete deployments NL1, 101764, 22/01/08, 23:30
DELETE FROM receiver_deployment WHERE id IN (2917441, 2917440);

---- Test results
-- SELECT receiver_deployment.id, serial_number, initialisationdatetime_timestamp, deploymentdatetime_timestamp, deploymentdatetime_zone 
-- FROM receiver_deployment
-- JOIN device ON device.id = receiver_deployment.receiver_id 
-- WHERE serial_number = '101764';
-- SELECT COUNT(*) FROM aatams.valid_detection WHERE receiver_deployment_id IN (2917441, 2917440); -- 0 record associated with this deployment

-------------------- Andre to advise which need to be removed
-- 5. A69-1601-12987: 3 releases, delete 2 of those 
-- DELETE FROM surgery WHERE id = 128688734;
-- 
-- DELETE FROM surgery WHERE id = 92990592; 
-- DELETE FROM animal_release WHERE id = 92990590;
-- DELETE FROM animal WHERE id = 92990589;

---- Test results
-- SELECT su.id AS surgery_id, s.transmitter_id, su.release_id, ar.*, a.*
-- FROM sensor s
-- JOIN surgery_with_end_date su ON s.tag_id = su.tag_id
-- JOIN animal_release ar ON ar.id = su.release_id
-- JOIN animal a ON a.id = ar.animal_id
-- WHERE s.transmitter_id = 'A69-1601-12987';
-- SELECT COUNT(*) FROM aatams.valid_detection WHERE release_id IN (128840074, 128840152,128840148); -- 0 record associated with these releases

--------------------
-- 6. 101951: delete receiver and associated deployment
DELETE FROM receiver_deployment WHERE id = 3084877;
DELETE FROM device WHERE serial_number = '101951';

---- Test results
-- SELECT receiver_deployment.id, serial_number, initialisationdatetime_timestamp, deploymentdatetime_timestamp, deploymentdatetime_zone 
-- FROM receiver_deployment
-- JOIN device ON device.id = receiver_deployment.receiver_id 
-- WHERE serial_number = '101951';
-- SELECT COUNT(*) FROM aatams.valid_detection WHERE receiver_deployment_id = 101951; -- 0 record associated with this deployment

--------------------
-- 7. Take Kate Lee off Rowley/Scotts projects
DELETE FROM project_role WHERE id IN (117606732, 117606733);

---- Test results
-- SELECT username, project.name, project_role.id
-- FROM sec_user
-- JOIN project_role ON project_role.person_id = sec_user.id
-- JOIN project ON project.id = project_role.project_id
-- WHERE sec_user.username = 'kalee';

-------------------- 
-- 8. A69-9004-588 pinger: to delete
DELETE FROM sensor WHERE sensor.id = 128190383;

---- Test results
-- SELECT s.id AS sensor_id, s.transmitter_id, d.id AS tag_id, tt.id, transmitter_type_name
-- FROM sensor s
-- JOIN device d ON d.id = s.tag_id
-- JOIN transmitter_type tt ON s.transmitter_type_id = tt.id
-- WHERE s.transmitter_id = 'A69-9004-588';

-------------------- 
-- 9. 2 OAR installation: OAR (Sydney) and Offshore Artificial Reef	Migrate the OAR installation that has only a single deployment of receiver VR2W-121036
UPDATE receiver_deployment SET station_id = 51061077 WHERE id = 80747688;
DELETE FROM installation_station WHERE id = 19974539;

---- Test results
-- SELECT * FROM installation_station
-- JOIN installation ON installation.id = installation_station.installation_id 
-- WHERE installation_station.name = 'OAR';
-- SELECT rd.*, ist.*
-- FROM receiver_deployment rd
-- JOIN installation_station ist ON rd.station_id = ist.id
-- JOIN device ON device.id = rd.receiver_id 
-- WHERE serial_number = '121036';

--------------------
-- 10. receiver 1068916 doesn’t exist
DELETE FROM device WHERE serial_number = '1068916';

---- Test results
-- SELECT * FROM device
-- WHERE serial_number = '1068916';

-------------------- 
-- 11. A69-1601-29724: delete double entry with embargo until 30/01/2016 
DELETE FROM surgery WHERE id = 39504964; 
DELETE FROM animal_release WHERE id = 39504959;
DELETE FROM animal WHERE id = 39504958;
DELETE FROM sensor WHERE tag_id = 39504961;
DELETE FROM device WHERE id = 39504961;

---- Test results
-- SELECT d.id AS tag_id, su.id AS surgery_id, s.transmitter_id, su.release_id, ar.*, a.*
-- FROM sensor s
-- JOIN device d ON d.id = s.tag_id
-- JOIN surgery_with_end_date su ON s.tag_id = su.tag_id
-- JOIN animal_release ar ON ar.id = su.release_id
-- JOIN animal a ON a.id = ar.animal_id
-- WHERE s.transmitter_id = 'A69-1601-29724';

-------------------- 
-- 12. A69-9002-8304: delete entry with SN 1147846, and pinger entry
DELETE FROM sensor WHERE id = 56773717;
DELETE FROM sensor WHERE id = 56773823;

---- Test results
-- SELECT d.*, s.*, tt.*
-- FROM sensor s
-- JOIN device d ON d.id = s.tag_id
-- JOIN transmitter_type tt ON s.transmitter_type_id = tt.id
-- WHERE s.transmitter_id = 'A69-9002-8304';

-------------------- 
-- 13. A69-9002-8305: delete entry with SN 1147846 (new)
DELETE FROM sensor WHERE id = 56773719; DELETE FROM device WHERE id = 56773715;

---- Test results
-- SELECT d.*, s.*, tt.*
-- FROM sensor s
-- JOIN device d ON d.id = s.tag_id
-- JOIN transmitter_type tt ON s.transmitter_type_id = tt.id
-- WHERE s.transmitter_id = 'A69-9002-8305';

-------------------- 
-- 14. A69-9002-84838482: wrong ID
DELETE FROM sensor WHERE id = 107795331;

---- Test results
-- SELECT d.*, s.*, tt.*
-- FROM sensor s
-- JOIN device d ON d.id = s.tag_id
-- JOIN transmitter_type tt ON s.transmitter_type_id = tt.id
-- WHERE s.transmitter_id = 'A69-9002-84838482';

-------------------- 
-- 15. A69-9001-65178, A69-9001-65177, A69-9001-65176, A69-9001-65175, A69-9001-65174: change from pinger to range test transmitter type
UPDATE sensor SET transmitter_type_id = 129582657 WHERE transmitter_id IN ('A69-9001-65178', 'A69-9001-65177', 'A69-9001-65176', 'A69-9001-65175', 'A69-9001-65174');

---- Test results
-- SELECT s.*, tt.*
-- FROM sensor s
-- JOIN transmitter_type tt ON s.transmitter_type_id = tt.id
-- WHERE s.transmitter_id IN ('A69-9001-65178', 'A69-9001-65177', 'A69-9001-65176', 'A69-9001-65175', 'A69-9001-65174');

commit;