
CREATE OR REPLACE VIEW aatams.parent_classification  AS 
SELECT classification_id, name, common_name, classification_level_id
FROM aatams.classification WHERE classification_level_id < 12
ORDER BY classification_level_id, name;

CREATE OR REPLACE VIEW aatams.classification_hierarchy AS
select s.classification_id as species_id, s.name as species_name, s._name as species_common_name,
g.classification_id as genus_id, g.name as genus_name, g.common_name as genus_common_name,
f.classification_id as family_id, f.name as family_name, f.common_name as family_common_name,
10 as family_level_id, 11 as genus_level_id, 12 as species_level_id
from aatams.classification s left outer join aatams.classification g on g.classification_id = s.parent_classification_id 
left outer join aatams.classification f on f.classification_id = g.parent_classification_id
where s.classification_level_id = 12
order by family_name, genus_name, species_name;

CREATE OR REPLACE VIEW aatams.detections_by_deployment AS 
select receiver_deployment.deployment_id, sum(detection_count) as count
from aatams.receiver_deployment, aatams.deployment_download, aatams.download_tag_summary
where deployment_download.deployment_id = receiver_deployment.deployment_id
and download_tag_summary.download_id = deployment_download.download_id
group by receiver_deployment.deployment_id;

CREATE OR REPLACE VIEW aatams.detections_by_deployment_b  AS 
select deployment_id, count(*) as count from aatams.detection group by deployment_id;

CREATE OR REPLACE VIEW aatams.detections_by_station AS 
select installation_station.station_id,
sum(download_tag_summary.detection_count) as count
from aatams.installation_station, aatams.receiver_deployment, aatams.download_tag_summary
where receiver_deployment.station_id = installation_station.station_id
and download_tag_summary.deployment_id = receiver_deployment.deployment_id
group by installation_station.station_id;

CREATE OR REPLACE VIEW aatams.detections_by_instln_station AS 
select installation.installation_id,
installation.name as installation_name,
installation_station.station_id,
installation_station.name as station_name,
installation_station.location,
detections_by_station.count
from aatams.installation, aatams.installation_station, aatams.detections_by_station
where installation_station.installation_id = installation.installation_id 
and detections_by_station.station_id = installation_station.station_id
order by installation_name, station_name;

insert into geometry_columns values ('', 'aatams', 'detections_by_instln_station', 'location',2, 4326, 'POINT');

CREATE OR REPLACE VIEW aatams.detections_by_instln_station_b AS 
select installation.installation_id,
installation.name as installation_name,
installation_station.station_id,
installation_station.name as station_name,
installation_station.location,
detections_by_station.count
from aatams.installation, aatams.installation_station, aatams.detections_by_station
where installation_station.installation_id = installation.installation_id and
detections_by_station.station_id = installation_station.station_id
order by installation_name, station_name;

insert into geometry_columns values ('', 'aatams', 'detections_by_instln_station_b', 'location',2, 4326, 'POINT');

CREATE OR REPLACE VIEW aatams.detections_by_project_tag  AS 
select project_tag.project_id, project_tag.device_id, device.code_name, project_tag.count
from (select project_role_person.project_id,
detection.tag_id as device_id,  
count(*) as count
from aatams.project_role_person, aatams.device, aatams.detection
where project_role_person.project_role_person_id = device.project_role_person_id
and detection.tag_id = device.device_id
group by  project_role_person.project_id, detection.tag_id ) project_tag, aatams.device
where device.device_id = project_tag.device_id
order by project_id, code_name;

CREATE OR REPLACE VIEW aatams.detections_by_station_b AS 
select installation_station.station_id, sum(count) as count 
from aatams.detections_by_deployment, aatams.installation_station, aatams.receiver_deployment
where detections_by_deployment.deployment_id = receiver_deployment.deployment_id
and receiver_deployment.station_id = installation_station.station_id
group by installation_station.station_id;

CREATE OR REPLACE VIEW aatams.detections_by_station_tag  AS 
select station_tag.station_id, station_tag.device_id, device.code_name, station_tag.count
from (select installation_station.station_id,
download_tag_summary.device_id as device_id,  
sum(download_tag_summary.detection_count) as count
from aatams.installation_station, aatams.receiver_deployment, aatams.download_tag_summary
where receiver_deployment.station_id = installation_station.station_id
and download_tag_summary.deployment_id = receiver_deployment.deployment_id
group by installation_station.station_id, download_tag_summary.device_id) station_tag, aatams.device
where device.device_id = station_tag.device_id
order by station_id, code_name;

CREATE OR REPLACE VIEW aatams.detections_by_station_tag_b AS 
select station_tag.station_id, station_tag.device_id, device.code_name, station_tag.count
from (select installation_station.station_id,
detection.tag_id as device_id,  
count(*) as count
from aatams.installation_station, aatams.receiver_deployment, aatams.detection
where receiver_deployment.station_id = installation_station.station_id
and detection.deployment_id = receiver_deployment.deployment_id
group by installation_station.station_id, detection.tag_id ) station_tag, aatams.device
where device.device_id = station_tag.device_id
order by station_id, code_name;

CREATE OR REPLACE VIEW aatams.detections_by_tag AS 
select device_id as tag_id, sum(detection_count) as count
from aatams.download_tag_summary
group by device_id;

CREATE OR REPLACE VIEW aatams.detections_by_tag_b AS 
select tag_id, count(*) as count from aatams.detection group by tag_id;

CREATE OR REPLACE VIEW aatams.display_tag_release AS 
select tr.release_id,
tr.device_id as tag_id,
ch.family_id,
case when tr.displayed_level_id > 10 then ch.genus_id else null end as genus_id,
case when tr.displayed_level_id > 11 then ch.species_id else null end as species_id,
tr.project_role_person_id,
tr.release_timestamp,
tr.release_location,
tr.capture_timestamp,
tr.capture_location,
tr.implant_type_id 
from aatams.tag_release tr, aatams.classification_hierarchy ch
where tr.classification_id = ch.species_id;

insert into geometry_columns values ('', 'aatams', 'display_tag_release', 'capture_location',2, 4326, 'POINT');
insert into geometry_columns values ('', 'aatams', 'display_tag_release', 'release_location',2, 4326, 'POINT');

CREATE OR REPLACE VIEW aatams.family  AS 
SELECT classification_id, parent_classification_id, name || CASE WHEN LENGTH(common_name) > 0 THEN '(' || common_name ||')' ELSE '' END AS name
FROM aatams.classification
WHERE classification_LEVEL_id = 10
ORDER BY name;

CREATE OR REPLACE VIEW aatams.genus AS 
SELECT classification_id, parent_classification_id, name || CASE WHEN LENGTH(common_name) > 0 THEN '(' || common_name ||')' ELSE '' END AS name
FROM aatams.classification
WHERE classification_LEVEL_id = 11
ORDER BY name;

CREATE OR REPLACE VIEW aatams.parent_classification  AS 
SELECT classification_id, name, common_name, classification_level_id
FROM aatams.classification WHERE classification_level_id < 12
ORDER BY classification_level_id, name;

CREATE OR REPLACE VIEW aatams.project_person  AS 
select project_role_person.project_role_person_id,
'aatams.project.' || project.project_id as project_fid, 
project.name as project_name,
'aatams.person.' || person.person_id as person_fid, 
person.name || ' (' || role.name || ')' as person_role
from aatams.project_role_person, aatams.project, aatams.role, aatams.person
where project.project_id = project_role_person.project_id and
role.role_id = project_role_person.role_id and
person.person_id = project_role_person.person_id;

CREATE OR REPLACE VIEW aatams.receiver_device  AS 
select device.device_id as device_id, device.code_name as name, device.code_name, device_model.name as model_name,
device_manufacturer.name as manufacturer_name 
from aatams.device, aatams.device_model, aatams.device_manufacturer
where device.model_id = device_model.model_id
and device_model.manufacturer_id = device_manufacturer.manufacturer_id
and device.device_type_id = 1
order by device.code_name;

CREATE OR REPLACE VIEW aatams.species  AS 
SELECT classification_id, parent_classification_id, name || CASE WHEN LENGTH(name) > 0 THEN ' (' || common_name || ')' ELSE '' END AS name
FROM aatams.classification
WHERE classification_level_id = 12
ORDER BY name;

CREATE OR REPLACE VIEW aatams.stations  AS 
select
'aatams.installation.' || installation_id as installation_fid,
station_id,
name,
location
from aatams.installation_station;

insert into geometry_columns values ('', 'aatams', 'stations', 'location', 2, 4326, 'POINT');

CREATE OR REPLACE VIEW aatams.tag_device  AS 
select * from aatams.device where device_type_id = 2;

CREATE OR REPLACE VIEW aatams.tag_release_min  AS 
select display_tag_release.release_id, project.project_id, project.name as project_name,
device.device_id as tag_id, device.code_name,
family.name as family, genus.name as genus, species.name as species,
display_tag_release.release_location, display_tag_release.release_timestamp
from aatams.display_tag_release inner join aatams.project_role_person 
on project_role_person.project_role_person_id = display_tag_release.project_role_person_id
inner join aatams.project on project.project_id = project_role_person.project_id
inner join aatams.device on device.device_id = display_tag_release.tag_id
left outer join aatams.family on display_tag_release.family_id = family.classification_id
left outer join aatams.genus on display_tag_release.genus_id = genus.classification_id
left outer join aatams.species on display_tag_release.species_id = species.classification_id
order by family, genus, species;

insert into geometry_columns values ('', 'aatams', 'tag_release_min', 'release_location', 2, 4326, 'POINT');


CREATE OR REPLACE VIEW aatams.deployment_download_tag AS 
SELECT ts.download_id || '_' || ts.device_id AS pkey, ts.download_id, ts.deployment_id,
'aatams.tag_device.' || ts.device_id AS tag_fid, d.code_name, ts.detection_count,
CASE WHEN tr.release_id IS NULL THEN NULL ELSE 'aatams.tag_release.' || tr.release_id END AS release_fid,
tr.family, tr.genus, tr.species 
FROM aatams.download_tag_summary ts INNER JOIN aatams.device d ON ts.device_id = d.device_id 
LEFT OUTER JOIN aatams.tag_release_min tr ON tr.tag_id = ts.device_id;

CREATE OR REPLACE VIEW aatams.detections_by_classn_tag AS 
select display_tag_release.tag_id,
device.code_name as tag_name,
(select name from aatams.classification where classification_id = display_tag_release.family_id) as family,
(select name from aatams.classification where classification_id = display_tag_release.genus_id) as genus,
(select name from aatams.classification where classification_id = display_tag_release.species_id) as species,
detections_by_tag.count
from aatams.device, aatams.display_tag_release, aatams.detections_by_tag
where display_tag_release.tag_id = device.device_id 
and display_tag_release.tag_id = detections_by_tag.tag_id
order by family, genus, species;

CREATE OR REPLACE VIEW aatams.detection_max AS 
select
rd.installation_id, rd.station_id, rd.deployment_id, d.detection_id, d.detection_timestamp, rd.location,
d.tag_id, t.code_name as tag_code_name, rd.device_id, r.code_name as receiver_code_name, dtr.release_location,
dtr.release_timestamp, dtr.family_id, dtr.genus_id, dtr.species_id
from aatams.detection d 
inner join aatams.receiver_deployment rd on rd.deployment_id = d.deployment_id
inner join aatams.device t on t.device_id = d.tag_id
inner join aatams.device r on r.device_id = rd.device_id
left outer join aatams.display_tag_release dtr on dtr.tag_id = d.tag_id;

insert into geometry_columns values ('', 'aatams', 'detection_max', 'location', 2, 4326, 'POINT');

CREATE OR REPLACE VIEW aatams.detection_min  AS 
select
rd.installation_id, rd.station_id, rd.deployment_id, d.detection_id, d.detection_timestamp, rd.location, 
d.tag_id, t.code_name as tag_code_name, rd.device_id, r.code_name as receiver_code_name
from aatams.detection d, aatams.receiver_deployment rd, aatams.device t, aatams.device r 
where rd.deployment_id = d.deployment_id
and d.tag_id = t.device_id
and rd.device_id = r.device_id;

insert into geometry_columns values ('', 'aatams', 'detection_min', 'location', 2, 4326, 'POINT');

CREATE OR REPLACE VIEW aatams.installation_deployment AS 
select
receiver_deployment.deployment_id,  
'aatams.installation.' || installation.installation_id as installation_fid,
installation.name as installation_name,
'aatams.station.' || installation_station.station_id as station_fid,
installation_station.name as station_name,
'aatams.device.' || device.device_id as device_fid,
device.code_name as device_name,
'aatams.receiver_deployment.' || receiver_deployment.deployment_id as deployment_fid,
case when installation_station.name is not null then installation_station.name || '-' else '' end || device.code_name || '-' || to_char(receiver_deployment.deployment_timestamp,'YYYY/MM/DD') as name,
receiver_deployment.deployment_timestamp, 
receiver_deployment.scheduled_recovery_date,
receiver_deployment.location
from aatams.receiver_deployment left outer join aatams.installation_station on installation_station.station_id = receiver_deployment.station_id
inner join aatams.installation on installation.installation_id = receiver_deployment.installation_id
inner join aatams.device on device.device_id = receiver_deployment.device_id
order by installation.name, installation_station.name, device.code_name, receiver_deployment.deployment_timestamp;

insert into geometry_columns values ('', 'aatams', 'installation_deployment', 'location', 2, 4326, 'POINT');

CREATE OR REPLACE VIEW aatams.receiver_model AS 
SELECT * FROM aatams.device_model
WHERE device_type_id = 1
ORDER BY name;

CREATE OR REPLACE VIEW aatams.transmitter_model  AS 
SELECT * FROM aatams.device_model
WHERE device_type_id = 2
ORDER BY name;

CREATE OR REPLACE VIEW aatams.transmitter_device  AS 
select device.device_id as device_id, device.code_name || '-' || device_model.name || case when device_manufacturer.name is null then '' else '-' end || device_manufacturer.name as name, 
device.code_name, device_model.name as model_name, device_manufacturer.name as manufacturer_name
from aatams.device inner join aatams.device_model on device.model_id = device_model.model_id
left outer join aatams.device_manufacturer on device_model.manufacturer_id = device_manufacturer.manufacturer_id
where device.device_type_id = 2
order by device.code_name;