--------------------------------------------------------
--  File created - Monday-December-21-2009   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for View CLASSIFICATION_HIERARCHY
--------------------------------------------------------

  CREATE OR REPLACE VIEW "CLASSIFICATION_HIERARCHY" ("SPECIES_ID", "SPECIES_NAME", "SPECIES_COMMON_NAME", "GENUS_ID", "GENUS_NAME", "GENUS_COMMON_NAME", "FAMILY_ID", "FAMILY_NAME", "FAMILY_COMMON_NAME", "FAMILY_LEVEL_ID", "GENUS_LEVEL_ID", "SPECIES_LEVEL_ID") AS 
  select s.classification_id as species_id, s.name as species_name, s.common_name as species_common_name,
g.classification_id as genus_id, g.name as genus_name, g.common_name as genus_common_name,
f.classification_id as family_id, f.name as family_name, f.common_name as family_common_name,
10 as family_level_id, 11 as genus_level_id, 12 as species_level_id
from classification s left outer join classification g on g.classification_id = s.parent_classification_id 
left outer join classification f on f.classification_id = g.parent_classification_id
where s.classification_level_id = 12
order by family_name, genus_name, species_name;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_CLASSTN_TAG
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_CLASSTN_TAG" ("TAG_ID", "TAG_NAME", "FAMILY", "GENUS", "SPECIES", "COUNT") AS 
  select display_tag_release.tag_id,
device.code_name as tag_name,
(select name from classification where classification_id = display_tag_release.family_id) as family,
(select name from classification where classification_id = display_tag_release.genus_id) as genus,
(select name from classification where classification_id = display_tag_release.species_id) as species,
detections_by_tag_2.count
from device, display_tag_release, detections_by_tag_2
where display_tag_release.tag_id = device.device_id 
and display_tag_release.tag_id = detections_by_tag_2.tag_id
order by family, genus, species;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_DEPLOYMENT
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_DEPLOYMENT" ("DEPLOYMENT_ID", "COUNT") AS 
  select receiver_deployment.deployment_id, sum(detection_count) as count
from receiver_deployment, deployment_download, download_tag_summary
where deployment_download.deployment_id = receiver_deployment.deployment_id
and download_tag_summary.download_id = deployment_download.download_id
group by receiver_deployment.deployment_id;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_DEPLOYMENT_B
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_DEPLOYMENT_B" ("DEPLOYMENT_ID", "COUNT") AS 
  select deployment_id, count(*) as count from detection group by deployment_id;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_INSTLN_STATION
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_INSTLN_STATION" ("INSTALLATION_ID", "INSTALLATION_NAME", "STATION_ID", "STATION_NAME", "LONGITUDE", "LATITUDE", "COUNT") AS 
  select installation.installation_id,
installation.name as installation_name,
installation_station.station_id,
installation_station.name as station_name,
installation_station.longitude,
installation_station.latitude,
detections_by_station.count
from installation, installation_station, detections_by_station
where installation_station.installation_id = installation.installation_id 
and detections_by_station.station_id = installation_station.station_id
order by installation_name, station_name;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_INSTLN_STATION_B
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_INSTLN_STATION_B" ("INSTALLATION_ID", "INSTALLATION_NAME", "STATION_ID", "STATION_NAME", "LONGITUDE", "LATITUDE", "COUNT") AS 
  select installation.installation_id,
installation.name as installation_name,
installation_station.station_id,
installation_station.name as station_name,
installation_station.longitude,
installation_station.latitude,
detections_by_station.count
from installation, installation_station, detections_by_station
where installation_station.installation_id = installation.installation_id and
detections_by_station.station_id = installation_station.station_id
order by installation_name, station_name;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_PROJECT_TAG
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_PROJECT_TAG" ("PROJECT_ID", "TAG_ID", "TAG_NAME", "COUNT") AS 
  select project_tag.project_id, project_tag.device_id, device.code_name, project_tag.count
from (select project_role_person.project_id,
detection.tag_id as device_id,  
count(*) as count
from project_role_person, device, detection
where project_role_person.project_role_person_id = device.project_role_person_id
and detection.tag_id = device.device_id
group by  project_role_person.project_id, detection.tag_id ) project_tag, device
where device.device_id = project_tag.device_id
order by project_id, code_name;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_STATION
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_STATION" ("STATION_ID", "COUNT") AS 
  select installation_station.station_id,
sum(download_tag_summary.detection_count) as count
from installation_station, receiver_deployment, download_tag_summary
where receiver_deployment.station_id = installation_station.station_id
and download_tag_summary.deployment_id = receiver_deployment.deployment_id
group by installation_station.station_id;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_STATION_B
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_STATION_B" ("STATION_ID", "COUNT") AS 
  select installation_station.station_id, sum(count) as count from detections_by_deployment, installation_station, receiver_deployment
where detections_by_deployment.deployment_id = receiver_deployment.deployment_id
and receiver_deployment.station_id = installation_station.station_id
group by installation_station.station_id;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_STATION_TAG
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_STATION_TAG" ("STATION_ID", "TAG_ID", "TAG_NAME", "COUNT") AS 
  select station_tag.station_id, station_tag.device_id, device.code_name, station_tag.count
from (select installation_station.station_id,
download_tag_summary.device_id as device_id,  
sum(download_tag_summary.detection_count) as count
from installation_station, receiver_deployment, download_tag_summary
where receiver_deployment.station_id = installation_station.station_id
and download_tag_summary.deployment_id = receiver_deployment.deployment_id
group by installation_station.station_id, download_tag_summary.device_id) station_tag, device
where device.device_id = station_tag.device_id
order by station_id, code_name;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_STATION_TAG_B
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_STATION_TAG_B" ("STATION_ID", "TAG_ID", "TAG_NAME", "COUNT") AS 
  select station_tag.station_id, station_tag.device_id, device.code_name, station_tag.count
from (select installation_station.station_id,
detection.tag_id as device_id,  
count(*) as count
from installation_station, receiver_deployment, detection
where receiver_deployment.station_id = installation_station.station_id
and detection.deployment_id = receiver_deployment.deployment_id
group by installation_station.station_id, detection.tag_id ) station_tag, device
where device.device_id = station_tag.device_id
order by station_id, code_name;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_TAG
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_TAG" ("TAG_ID", "COUNT") AS 
  select tag_id, count(*) as count from detection group by tag_id;
--------------------------------------------------------
--  DDL for View DETECTIONS_BY_TAG_2
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTIONS_BY_TAG_2" ("TAG_ID", "COUNT") AS 
  select device_id as tag_id, sum(detection_count) as count
from download_tag_summary
group by device_id;
--------------------------------------------------------
--  DDL for View DETECTION_MAX
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTION_MAX" ("INSTALLATION_ID", "STATION_ID", "DEPLOYMENT_ID", "DETECTION_ID", "DETECTION_TIMESTAMP", "LONGITUDE", "LATITUDE", "TAG_ID", "TAG_NAME", "RECEIVER_ID", "RECEIVER_NAME", "RELEASE_LONGITUDE", "RELEASE_LATITUDE", "RELEASE_TIMESTAMP", "FAMILY_ID", "GENUS_ID", "SPECIES_ID") AS 
  select
rd.installation_id, rd.station_id, rd.deployment_id, d.detection_id, d.detection_timestamp, rd.longitude, rd.latitude, d.tag_id, t.code_name, rd.device_id, r.code_name, dtr.release_longitude, dtr.release_latitude, dtr.release_timestamp, dtr.family_id, dtr.genus_id, dtr.species_id
from detection d 
inner join receiver_deployment rd on rd.deployment_id = d.deployment_id
inner join tag_device t on t.device_id = d.tag_id
inner join receiver_device r on r.device_id = rd.device_id
left outer join display_tag_release dtr on dtr.tag_id = d.tag_id;
--------------------------------------------------------
--  DDL for View DETECTION_MIN
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DETECTION_MIN" ("INSTALLATION_ID", "STATION_ID", "DEPLOYMENT_ID", "DETECTION_ID", "DETECTION_TIMESTAMP", "LONGITUDE", "LATITUDE", "TAG_ID", "TAG_NAME", "RECEIVER_ID", "RECEIVER_NAME") AS 
  select
rd.installation_id, rd.station_id, rd.deployment_id, d.detection_id, d.detection_timestamp, rd.longitude, rd.latitude, d.tag_id, t.code_name, rd.device_id, r.code_name
from detection d, receiver_deployment rd, tag_device t, receiver_device r 
where rd.deployment_id = d.deployment_id
and d.tag_id = t.device_id
and rd.device_id = r.device_id;
--------------------------------------------------------
--  DDL for View DISPLAY_TAG_RELEASE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "DISPLAY_TAG_RELEASE" ("RELEASE_ID", "TAG_ID", "FAMILY_ID", "GENUS_ID", "SPECIES_ID", "PROJECT_ROLE_PERSON_ID", "SURGERY_ID", "RELEASE_LONGITUDE", "RELEASE_LATITUDE", "RELEASE_GEOGRAPHIC_AREA_ID", "RELEASE_TIMESTAMP", "CAPTURE_LONGITUDE", "CAPTURE_LATITUDE", "CAPTURE_GEOGRAPHIC_AREA_ID", "CAPTURE_TIMESTAMP", "TETHER_TYPE_ID", "IMPLANT_TYPE_ID") AS 
  select tr.RELEASE_ID,
tr.DEVICE_ID as TAG_ID,
ch.family_id,
case when tr.displayed_level_id > 10 then ch.genus_id else null end as genus_id,
case when tr.displayed_level_id > 11 then ch.species_id else null end as species_id,
tr.PROJECT_ROLE_PERSON_ID,
tr.SURGERY_ID,
tr.RELEASE_LONGITUDE,
tr.RELEASE_LATITUDE,
tr.RELEASE_GEOGRAPHIC_AREA_ID,
tr.RELEASE_TIMESTAMP,
tr.CAPTURE_LONGITUDE,
tr.CAPTURE_LATITUDE,
tr.CAPTURE_GEOGRAPHIC_AREA_ID,
tr.CAPTURE_TIMESTAMP,
tr.TETHER_TYPE_ID,
tr.IMPLANT_TYPE_ID from tag_release tr, classification_hierarchy ch
where tr.classification_id = ch.species_id;
--------------------------------------------------------
--  DDL for View FAMILY
--------------------------------------------------------

  CREATE OR REPLACE VIEW "FAMILY" ("CLASSIFICATION_ID", "PARENT_CLASSIFICATION_ID", "NAME") AS 
  SELECT CLASSIFICATION_ID, PARENT_CLASSIFICATION_ID, CONCAT(NAME,CASE  WHEN LENGTH(COMMON_NAME) > 0 THEN CONCAT(CONCAT('(',COMMON_NAME),')') ELSE '' END) AS NAME
FROM CLASSIFICATION
WHERE CLASSIFICATION_LEVEL_ID = 10
ORDER BY NAME;
--------------------------------------------------------
--  DDL for View GENUS
--------------------------------------------------------

  CREATE OR REPLACE VIEW "GENUS" ("CLASSIFICATION_ID", "PARENT_CLASSIFICATION_ID", "NAME") AS 
  SELECT CLASSIFICATION_ID, PARENT_CLASSIFICATION_ID, CONCAT(NAME,CASE  WHEN LENGTH(COMMON_NAME) > 0 THEN CONCAT(CONCAT('(',COMMON_NAME),')') ELSE '' END) AS NAME
FROM CLASSIFICATION
WHERE CLASSIFICATION_LEVEL_ID = 11
ORDER BY NAME;
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
--  DDL for View PARENT_CLASSIFICATION
--------------------------------------------------------

  CREATE OR REPLACE VIEW "PARENT_CLASSIFICATION" ("CLASSIFICATION_ID", "NAME", "COMMON_NAME", "CLASSIFICATION_LEVEL_ID") AS 
  SELECT CLASSIFICATION_ID, NAME, COMMON_NAME, CLASSIFICATION_LEVEL_ID
FROM CLASSIFICATION WHERE CLASSIFICATION_LEVEL_ID < 12
ORDER BY CLASSIFICATION_LEVEL_ID, NAME;
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
--  DDL for View SPECIES
--------------------------------------------------------

  CREATE OR REPLACE VIEW "SPECIES" ("CLASSIFICATION_ID", "PARENT_CLASSIFICATION_ID", "NAME") AS 
  SELECT CLASSIFICATION_ID, PARENT_CLASSIFICATION_ID, CONCAT(NAME,CASE  WHEN LENGTH(COMMON_NAME) > 0 THEN CONCAT(CONCAT(' (',COMMON_NAME),')') ELSE '' END) AS NAME
FROM CLASSIFICATION
WHERE CLASSIFICATION_LEVEL_ID = 12
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
--  DDL for View TAG_RELEASE_MIN
--------------------------------------------------------

  CREATE OR REPLACE VIEW "TAG_RELEASE_MIN" ("RELEASE_ID", "PROJECT_ID", "PROJECT_NAME", "DEVICE_ID", "CODE_NAME", "FAMILY", "GENUS", "SPECIES", "RELEASE_LONGITUDE", "RELEASE_LATITUDE", "RELEASE_TIMESTAMP") AS 
  select display_tag_release.release_id, project.project_id, project.name as project_name, device.device_id, device.code_name,
family.name as family, genus.name as genus, species.name as species,
display_tag_release.release_longitude, display_tag_release.release_latitude, display_tag_release.release_timestamp
from display_tag_release inner join project_role_person 
on project_role_person.project_role_person_id = display_tag_release.project_role_person_id
inner join project on project.project_id = project_role_person.project_id
inner join device on device.device_id = display_tag_release.tag_id
left outer join family on display_tag_release.family_id = family.classification_id
left outer join genus on display_tag_release.genus_id = genus.classification_id
left outer join species on display_tag_release.species_id = species.classification_id
order by family, genus, species;
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
