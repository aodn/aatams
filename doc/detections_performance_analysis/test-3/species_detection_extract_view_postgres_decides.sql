set role to aatams;

drop view if exists species_detection_extract_test_view;

CREATE OR REPLACE VIEW species_detection_extract_test_view AS 
 SELECT valid_detection."timestamp",
    to_char(timezone('00:00'::text, valid_detection."timestamp"), 'YYYY-MM-DD HH24:MI:SS'::text) AS formatted_timestamp,
    installation_station.name AS station,
    installation_station.id AS station_id,
    installation_station.location,
    st_y(installation_station.location) AS latitude,
    st_x(installation_station.location) AS longitude,
    (device_model.model_name::text || '-'::text) || receiver.serial_number::text AS receiver_name,
    COALESCE(sensor.transmitter_id, ''::character varying) AS sensor_id,
    COALESCE(((((species.spcode::text || ' - '::text) || species.scientific_name::text) || ' ('::text) || species.common_name::text) || ')'::text, ''::text) AS species_name,
    sec_user.name AS uploader,
    valid_detection.transmitter_id,
    organisation.name AS organisation,
    receiver_project.name AS project,
    installation.name AS installation,
    COALESCE(species.spcode, ''::character varying) AS spcode,
    animal_release.id AS animal_release_id,
    animal_release.embargo_date,
    receiver_project.id AS project_id,
    valid_detection.id AS detection_id,
    animal_release.project_id AS release_project_id,
    valid_detection.sensor_value,
    valid_detection.sensor_unit,
    valid_detection.provisional
   FROM valid_detection
     JOIN receiver_deployment ON valid_detection.receiver_deployment_id = receiver_deployment.id
     JOIN installation_station ON receiver_deployment.station_id = installation_station.id
     JOIN installation ON installation_station.installation_id = installation.id
     JOIN project receiver_project ON installation.project_id = receiver_project.id
     JOIN device receiver ON receiver_deployment.receiver_id = receiver.id
     JOIN device_model ON receiver.model_id = device_model.id
     JOIN receiver_download_file ON valid_detection.receiver_download_id = receiver_download_file.id
     JOIN sec_user ON receiver_download_file.requesting_user_id = sec_user.id
     JOIN organisation ON receiver.organisation_id = organisation.id
     JOIN sensor ON valid_detection.transmitter_id = sensor.transmitter_id
     JOIN device tag ON sensor.tag_id = tag.id
     JOIN surgery ON tag.id = surgery.tag_id
     JOIN animal_release ON surgery.release_id = animal_release.id
     JOIN animal ON animal_release.animal_id = animal.id
     JOIN species ON animal.species_id = species.id;
