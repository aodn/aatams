--------------------------------------------------------
--  File created - Thursday-November-19-2009   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for View DETECTION_MIN
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTION_MIN" ("INSTALLATION_ID", "STATION_ID", "DEPLOYMENT_ID", "DETECTION_ID", "DETECTION_TIMESTAMP", "LONGITUDE", "LATITUDE", "TAG_ID", "TAG_NAME", "RECEIVER_ID", "RECEIVER_NAME") AS 
  select
rd.installation_id, rd.station_id, rd.deployment_id, d.detection_id, d.detection_timestamp, rd.longitude, rd.latitude, d.tag_id, t.code_name, rd.device_id, r.code_name
from detection d, receiver_deployment rd, tag_device t, receiver_device r 
where rd.deployment_id = d.deployment_id
and d.tag_id = t.device_id
and rd.device_id = r.device_id 
order by d.detection_timestamp asc;
--------------------------------------------------------
--  DDL for View INSTALLATION_DEPLOYMENT
--------------------------------------------------------

  CREATE OR REPLACE VIEW "INSTALLATION_DEPLOYMENT" ("DEPLOYMENT_ID", "INSTALLATION_FID", "INSTALLATION_NAME", "STATION_FID", "STATION_NAME", "DEVICE_FID", "DEVICE_NAME", "DEPLOYMENT_FID", "NAME", "DEPLOYMENT_TIMESTAMP") AS 
  select
receiver_deployment.deployment_id,  
concat('aatams.installation.',to_char(installation.installation_id)) as installation_fid,
installation.name as installation_name,
concat('aatams.station.',to_char(installation_station.station_id)) as station_fid,
installation_station.name as station_name,
concat('aatams.device.', to_char(receiver_device.device_id)) as device_fid,
receiver_device.name as device_name,
concat('aatams.receiver_deployment.',to_char(receiver_deployment.deployment_id)) as deployment_fid,
concat(concat(receiver_device.name,'-'),to_char(receiver_deployment.deployment_timestamp,'YYYY/MM/DD')) as name,
receiver_deployment.deployment_timestamp
from receiver_deployment left outer join installation_station on installation_station.station_id = receiver_deployment.station_id
inner join installation on installation.installation_id = receiver_deployment.installation_id
inner join receiver_device on receiver_device.device_id = receiver_deployment.device_id
order by installation.name, receiver_device.name, receiver_deployment.deployment_timestamp;
--------------------------------------------------------
--  DDL for View PROJECT_PERSON
--------------------------------------------------------

  CREATE OR REPLACE VIEW "PROJECT_PERSON" ("PROJECT_ROLE_PERSON_ID", "PROJECT_FID", "PROJECT_NAME", "PERSON_FID", "PERSON_ROLE") AS 
  select project_role_person.project_role_person_id,
concat('aatams.project.',to_char(project.project_id)) as project_fid, 
project.name as project_name,
concat('aatams.person.',to_char(person.person_id)) as person_fid, 
concat(person.name,concat(' (',concat(role.name,')'))) as person_role
from project_role_person, project, role, person
where project.project_id = project_role_person.project_id and
role.role_id = project_role_person.role_id and
person.person_id = project_role_person.person_id;
--------------------------------------------------------
--  DDL for View RECEIVER_DEVICE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "RECEIVER_DEVICE" ("DEVICE_ID", "NAME", "CODE_NAME", "MODEL_NAME", "MANUFACTURER_NAME") AS 
  select device.device_id as device_id, concat(concat(concat(concat(device.code_name,'-'),device_manufacturer.name),'-'),device_model.name) as name, device.code_name, device_model.name as model_name, device_manufacturer.name as manufacturer_name from
device, device_model, device_manufacturer
where device.model_id = device_model.model_id
and device_model.manufacturer_id = device_manufacturer.manufacturer_id
and device.device_type_id = 1
order by device.code_name;
--------------------------------------------------------
--  DDL for View RECEIVER_MODEL
--------------------------------------------------------

  CREATE OR REPLACE VIEW "RECEIVER_MODEL" ("MODEL_ID", "DEVICE_TYPE_ID", "MANUFACTURER_ID", "NAME", "DISABLED", "CREATED", "MODIFIED") AS 
  SELECT  "MODEL_ID","DEVICE_TYPE_ID","MANUFACTURER_ID","NAME","DISABLED","CREATED","MODIFIED" FROM DEVICE_MODEL
WHERE DEVICE_TYPE_ID = 1
ORDER BY NAME;
--------------------------------------------------------
--  DDL for View STATION
--------------------------------------------------------

  CREATE OR REPLACE VIEW "STATION" ("INSTALLATION_FID", "STATION_ID", "NAME", "LONGITUDE", "LATITUDE") AS 
  select
concat('aatams.installation.',to_char(installation_id)) as installation_fid,
station_id,
name,
longitude,
latitude
from installation_station;
--------------------------------------------------------
--  DDL for View TAG_DEVICE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "TAG_DEVICE" ("DEVICE_ID", "CODE_NAME", "DEVICE_TYPE_ID", "MODEL_ID", "SERIAL_NUMBER", "STATUS_ID", "PROJECT_ROLE_PERSON_ID", "DISABLED", "CREATED", "MODIFIED") AS 
  select "DEVICE_ID","CODE_NAME","DEVICE_TYPE_ID","MODEL_ID","SERIAL_NUMBER","STATUS_ID","PROJECT_ROLE_PERSON_ID","DISABLED","CREATED","MODIFIED" from device where device_type_id = 2;
--------------------------------------------------------
--  DDL for View TRANSMITTER_DEVICE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "TRANSMITTER_DEVICE" ("DEVICE_ID", "NAME", "CODE_NAME", "MODEL_NAME", "MANUFACTURER_NAME") AS 
  select device.device_id as device_id, concat(concat(concat(concat(device.code_name,'-'),device_model.name),(case when device_manufacturer.name is null then '' else '-' end)),device_manufacturer.name) as name, device.code_name, device_model.name as model_name, device_manufacturer.name as manufacturer_name from
device inner join device_model on device.model_id = device_model.model_id
left outer join device_manufacturer on device_model.manufacturer_id = device_manufacturer.manufacturer_id
where device.device_type_id = 2
order by device.code_name;
--------------------------------------------------------
--  DDL for View TRANSMITTER_MODEL
--------------------------------------------------------

  CREATE OR REPLACE VIEW "TRANSMITTER_MODEL" ("MODEL_ID", "DEVICE_TYPE_ID", "MANUFACTURER_ID", "NAME", "DISABLED", "CREATED", "MODIFIED") AS 
  SELECT  "MODEL_ID","DEVICE_TYPE_ID","MANUFACTURER_ID","NAME","DISABLED","CREATED","MODIFIED" FROM DEVICE_MODEL
WHERE DEVICE_TYPE_ID = 2
ORDER BY NAME;
