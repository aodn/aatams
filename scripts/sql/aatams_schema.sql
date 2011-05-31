--
-- PostgreSQL database dump
--

-- Dumped from database version 9.0.2
-- Dumped by pg_dump version 9.0.1
-- Started on 2011-05-31 16:27:48 EST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET SESSION AUTHORIZATION 'aatams';

SET search_path = aatams, pg_catalog;

ALTER TABLE ONLY aatams.organisation_project DROP CONSTRAINT fkf3b978b4bf505a21;
ALTER TABLE ONLY aatams.organisation_project DROP CONSTRAINT fkf3b978b499b5ecd3;
ALTER TABLE ONLY aatams.organisation_person DROP CONSTRAINT fkee60ce5ae985cdb3;
ALTER TABLE ONLY aatams.organisation_person DROP CONSTRAINT fkee60ce5a99b5ecd3;
ALTER TABLE ONLY aatams.project DROP CONSTRAINT fked904b19c34cf774;
ALTER TABLE ONLY aatams.detection_tag DROP CONSTRAINT fkeb71eee0ceab1a01;
ALTER TABLE ONLY aatams.detection_tag DROP CONSTRAINT fkeb71eee0ac39c713;
ALTER TABLE ONLY aatams.device_model DROP CONSTRAINT fkdcc4e40025f67029;
ALTER TABLE ONLY aatams.sensor DROP CONSTRAINT fkca0053baceab1a01;
ALTER TABLE ONLY aatams.sensor DROP CONSTRAINT fkca0053ba4b0c3bc4;
ALTER TABLE ONLY aatams.device DROP CONSTRAINT fkb06b1e56cdaf3227;
ALTER TABLE ONLY aatams.device DROP CONSTRAINT fkb06b1e56bf505a21;
ALTER TABLE ONLY aatams.device DROP CONSTRAINT fkb06b1e566b73e7c9;
ALTER TABLE ONLY aatams.device DROP CONSTRAINT fkb06b1e561c043beb;
ALTER TABLE ONLY aatams.receiver_download_receiver_download_file DROP CONSTRAINT fkadfe47cafa360683;
ALTER TABLE ONLY aatams.receiver_download_receiver_download_file DROP CONSTRAINT fkadfe47ca50da4c63;
ALTER TABLE ONLY aatams.animal DROP CONSTRAINT fkabc58dfccd365681;
ALTER TABLE ONLY aatams.system_role DROP CONSTRAINT fka47ecc86d06730af;
ALTER TABLE ONLY aatams.animal_release_animal_measurement DROP CONSTRAINT fka3b7554af520388;
ALTER TABLE ONLY aatams.animal_release_animal_measurement DROP CONSTRAINT fka3b75543290ccda;
ALTER TABLE ONLY aatams.surgery DROP CONSTRAINT fk918a71f5ceab1a01;
ALTER TABLE ONLY aatams.surgery DROP CONSTRAINT fk918a71f5b5c10085;
ALTER TABLE ONLY aatams.surgery DROP CONSTRAINT fk918a71f57480ac7b;
ALTER TABLE ONLY aatams.surgery DROP CONSTRAINT fk918a71f56d95e296;
ALTER TABLE ONLY aatams.surgery DROP CONSTRAINT fk918a71f54f2dd3af;
ALTER TABLE ONLY aatams.detection DROP CONSTRAINT fk90e7ca85f0bb6d33;
ALTER TABLE ONLY aatams.detection DROP CONSTRAINT fk90e7ca85d7d8b793;
ALTER TABLE ONLY aatams.installation_station DROP CONSTRAINT fk902c2c2f35e870d3;
ALTER TABLE ONLY aatams.animal_measurement DROP CONSTRAINT fk8eb3adf9e6ea591d;
ALTER TABLE ONLY aatams.animal_measurement DROP CONSTRAINT fk8eb3adf914018681;
ALTER TABLE ONLY aatams.person_system_role DROP CONSTRAINT fk8e3f6e9cbba8aa52;
ALTER TABLE ONLY aatams.person_system_role DROP CONSTRAINT fk8e3f6e9c244fac71;
ALTER TABLE ONLY aatams.animal_release DROP CONSTRAINT fk88d6a0c4e0347853;
ALTER TABLE ONLY aatams.animal_release DROP CONSTRAINT fk88d6a0c4bf505a21;
ALTER TABLE ONLY aatams.receiver_deployment DROP CONSTRAINT fk862aeb15f0bb6d33;
ALTER TABLE ONLY aatams.receiver_deployment DROP CONSTRAINT fk862aeb15cdaf3227;
ALTER TABLE ONLY aatams.receiver_deployment DROP CONSTRAINT fk862aeb1531eebdc;
ALTER TABLE ONLY aatams.receiver_recovery DROP CONSTRAINT fk82de83e593fa40a2;
ALTER TABLE ONLY aatams.receiver_recovery DROP CONSTRAINT fk82de83e579a96182;
ALTER TABLE ONLY aatams.receiver_recovery DROP CONSTRAINT fk82de83e56b73e7c9;
ALTER TABLE ONLY aatams.receiver_download DROP CONSTRAINT fk79accd8b8e509d3;
ALTER TABLE ONLY aatams.installation DROP CONSTRAINT fk796d5e3abf505a21;
ALTER TABLE ONLY aatams.installation DROP CONSTRAINT fk796d5e3a18d65d27;
ALTER TABLE ONLY aatams.project_role DROP CONSTRAINT fk37fff5dce985cdb3;
ALTER TABLE ONLY aatams.project_role DROP CONSTRAINT fk37fff5dcbf505a21;
ALTER TABLE ONLY aatams.project_role DROP CONSTRAINT fk37fff5dc51218487;
ALTER TABLE ONLY aatams.installation_station_receiver DROP CONSTRAINT fk2f7233fff0bb6d33;
ALTER TABLE ONLY aatams.installation_station_receiver DROP CONSTRAINT fk2f7233ffd6ed9307;
ALTER TABLE ONLY aatams.ufile DROP CONSTRAINT ufile_pkey;
ALTER TABLE ONLY aatams.transmitter_type DROP CONSTRAINT transmitter_type_transmitter_type_name_key;
ALTER TABLE ONLY aatams.transmitter_type DROP CONSTRAINT transmitter_type_pkey;
ALTER TABLE ONLY aatams.system_role_type DROP CONSTRAINT system_role_type_pkey;
ALTER TABLE ONLY aatams.system_role_type DROP CONSTRAINT system_role_type_display_name_key;
ALTER TABLE ONLY aatams.system_role DROP CONSTRAINT system_role_pkey;
ALTER TABLE ONLY aatams.surgery_type DROP CONSTRAINT surgery_type_type_key;
ALTER TABLE ONLY aatams.surgery_type DROP CONSTRAINT surgery_type_pkey;
ALTER TABLE ONLY aatams.surgery_treatment_type DROP CONSTRAINT surgery_treatment_type_type_key;
ALTER TABLE ONLY aatams.surgery_treatment_type DROP CONSTRAINT surgery_treatment_type_pkey;
ALTER TABLE ONLY aatams.surgery DROP CONSTRAINT surgery_pkey;
ALTER TABLE ONLY aatams.sex DROP CONSTRAINT sex_sex_key;
ALTER TABLE ONLY aatams.sex DROP CONSTRAINT sex_pkey;
ALTER TABLE ONLY aatams.sensor DROP CONSTRAINT sensor_pkey;
ALTER TABLE ONLY aatams.receiver_recovery DROP CONSTRAINT receiver_recovery_pkey;
ALTER TABLE ONLY aatams.receiver_download DROP CONSTRAINT receiver_download_pkey;
ALTER TABLE ONLY aatams.receiver_download_file DROP CONSTRAINT receiver_download_file_pkey;
ALTER TABLE ONLY aatams.receiver_deployment DROP CONSTRAINT receiver_deployment_pkey;
ALTER TABLE ONLY aatams.project_role_type DROP CONSTRAINT project_role_type_pkey;
ALTER TABLE ONLY aatams.project_role_type DROP CONSTRAINT project_role_type_display_name_key;
ALTER TABLE ONLY aatams.project_role DROP CONSTRAINT project_role_pkey;
ALTER TABLE ONLY aatams.project DROP CONSTRAINT project_pkey;
ALTER TABLE ONLY aatams.project DROP CONSTRAINT project_name_key;
ALTER TABLE ONLY aatams.person DROP CONSTRAINT person_pkey;
ALTER TABLE ONLY aatams.organisation_project DROP CONSTRAINT organisation_project_pkey;
ALTER TABLE ONLY aatams.organisation DROP CONSTRAINT organisation_pkey;
ALTER TABLE ONLY aatams.organisation_person DROP CONSTRAINT organisation_person_pkey;
ALTER TABLE ONLY aatams.organisation DROP CONSTRAINT organisation_name_key;
ALTER TABLE ONLY aatams.mooring_type DROP CONSTRAINT mooring_type_type_key;
ALTER TABLE ONLY aatams.mooring_type DROP CONSTRAINT mooring_type_pkey;
ALTER TABLE ONLY aatams.measurement_unit DROP CONSTRAINT measurement_unit_unit_key;
ALTER TABLE ONLY aatams.measurement_unit DROP CONSTRAINT measurement_unit_pkey;
ALTER TABLE ONLY aatams.installation_station DROP CONSTRAINT installation_station_pkey;
ALTER TABLE ONLY aatams.installation DROP CONSTRAINT installation_pkey;
ALTER TABLE ONLY aatams.installation_configuration DROP CONSTRAINT installation_configuration_type_key;
ALTER TABLE ONLY aatams.installation_configuration DROP CONSTRAINT installation_configuration_pkey;
ALTER TABLE ONLY aatams.device_status DROP CONSTRAINT device_status_status_key;
ALTER TABLE ONLY aatams.device_status DROP CONSTRAINT device_status_pkey;
ALTER TABLE ONLY aatams.device DROP CONSTRAINT device_pkey;
ALTER TABLE ONLY aatams.device_model DROP CONSTRAINT device_model_pkey;
ALTER TABLE ONLY aatams.device_model DROP CONSTRAINT device_model_model_name_key;
ALTER TABLE ONLY aatams.device_manufacturer DROP CONSTRAINT device_manufacturer_pkey;
ALTER TABLE ONLY aatams.device_manufacturer DROP CONSTRAINT device_manufacturer_manufacturer_name_key;
ALTER TABLE ONLY aatams.detection DROP CONSTRAINT detection_pkey;
ALTER TABLE ONLY aatams.animal_release DROP CONSTRAINT animal_release_pkey;
ALTER TABLE ONLY aatams.animal DROP CONSTRAINT animal_pkey;
ALTER TABLE ONLY aatams.animal_measurement_type DROP CONSTRAINT animal_measurement_type_type_key;
ALTER TABLE ONLY aatams.animal_measurement_type DROP CONSTRAINT animal_measurement_type_pkey;
ALTER TABLE ONLY aatams.animal_measurement DROP CONSTRAINT animal_measurement_pkey;
DROP TABLE aatams.ufile;
DROP TABLE aatams.transmitter_type;
DROP TABLE aatams.system_role_type;
DROP TABLE aatams.system_role;
DROP TABLE aatams.surgery_type;
DROP TABLE aatams.surgery_treatment_type;
DROP TABLE aatams.surgery;
DROP TABLE aatams.sex;
DROP TABLE aatams.sensor;
DROP TABLE aatams.receiver_recovery;
DROP TABLE aatams.receiver_download_receiver_download_file;
DROP TABLE aatams.receiver_download_file;
DROP TABLE aatams.receiver_download;
DROP TABLE aatams.receiver_deployment;
DROP TABLE aatams.project_role_type;
DROP TABLE aatams.project_role;
DROP TABLE aatams.project;
DROP TABLE aatams.person_system_role;
DROP TABLE aatams.person;
DROP TABLE aatams.organisation_project;
DROP TABLE aatams.organisation_person;
DROP TABLE aatams.organisation;
DROP TABLE aatams.mooring_type;
DROP TABLE aatams.measurement_unit;
DROP TABLE aatams.installation_station_receiver;
DROP TABLE aatams.installation_station;
DROP TABLE aatams.installation_configuration;
DROP TABLE aatams.installation;
DROP SEQUENCE aatams.hibernate_sequence;
DROP TABLE aatams.device_status;
DROP TABLE aatams.device_model;
DROP TABLE aatams.device_manufacturer;
DROP TABLE aatams.device;
DROP TABLE aatams.detection_tag;
DROP TABLE aatams.detection;
DROP TABLE aatams.animal_release_animal_measurement;
DROP TABLE aatams.animal_release;
DROP TABLE aatams.animal_measurement_type;
DROP TABLE aatams.animal_measurement;
DROP TABLE aatams.animal;
SET SESSION AUTHORIZATION 'aatams_group';

DROP SCHEMA aatams;
--
-- TOC entry 7 (class 2615 OID 121525)
-- Name: aatams; Type: SCHEMA; Schema: -; Owner: aatams_group
--

CREATE SCHEMA aatams;


SET SESSION AUTHORIZATION 'aatams';

SET search_path = aatams, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 2451 (class 1259 OID 122487)
-- Dependencies: 7
-- Name: animal; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal (
    id bigint NOT NULL,
    version bigint NOT NULL,
    classification character varying(255) NOT NULL,
    sex_id bigint NOT NULL
);


--
-- TOC entry 2452 (class 1259 OID 122492)
-- Dependencies: 7
-- Name: animal_measurement; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_measurement (
    id bigint NOT NULL,
    version bigint NOT NULL,
    comments character varying(255) NOT NULL,
    estimate boolean NOT NULL,
    type_id bigint NOT NULL,
    unit_id bigint NOT NULL,
    value real NOT NULL
);


--
-- TOC entry 2453 (class 1259 OID 122497)
-- Dependencies: 7
-- Name: animal_measurement_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_measurement_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2454 (class 1259 OID 122504)
-- Dependencies: 7
-- Name: animal_release; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_release (
    id bigint NOT NULL,
    version bigint NOT NULL,
    animal_id bigint NOT NULL,
    capture_date_time timestamp without time zone NOT NULL,
    capture_locality character varying(255) NOT NULL,
    capture_location bytea NOT NULL,
    comments character varying(255) NOT NULL,
    project_id bigint NOT NULL,
    release_date_time timestamp without time zone NOT NULL,
    release_locality character varying(255) NOT NULL,
    release_location bytea NOT NULL
);


--
-- TOC entry 2455 (class 1259 OID 122512)
-- Dependencies: 7
-- Name: animal_release_animal_measurement; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_release_animal_measurement (
    animal_release_measurements_id bigint,
    animal_measurement_id bigint
);


--
-- TOC entry 2456 (class 1259 OID 122515)
-- Dependencies: 7
-- Name: detection; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE detection (
    id bigint NOT NULL,
    version bigint NOT NULL,
    location bytea NOT NULL,
    receiver_id bigint NOT NULL,
    station_name character varying(255) NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    transmitter_name character varying(255) NOT NULL,
    class character varying(255) NOT NULL,
    sensor_id bigint,
    uncalibrated_value real
);


--
-- TOC entry 2457 (class 1259 OID 122523)
-- Dependencies: 7
-- Name: detection_tag; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE detection_tag (
    detection_tags_id bigint,
    tag_id bigint
);


--
-- TOC entry 2458 (class 1259 OID 122526)
-- Dependencies: 7
-- Name: device; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device (
    id bigint NOT NULL,
    version bigint NOT NULL,
    code_name character varying(255) NOT NULL,
    embargo_date timestamp without time zone,
    model_id bigint NOT NULL,
    project_id bigint NOT NULL,
    serial_number character varying(255) NOT NULL,
    status_id bigint NOT NULL,
    class character varying(255) NOT NULL,
    code_map character varying(255),
    ping_code integer,
    station_id bigint
);


--
-- TOC entry 2459 (class 1259 OID 122534)
-- Dependencies: 7
-- Name: device_manufacturer; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device_manufacturer (
    id bigint NOT NULL,
    version bigint NOT NULL,
    manufacturer_name character varying(255) NOT NULL
);


--
-- TOC entry 2460 (class 1259 OID 122541)
-- Dependencies: 7
-- Name: device_model; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device_model (
    id bigint NOT NULL,
    version bigint NOT NULL,
    manufacturer_id bigint NOT NULL,
    model_name character varying(255) NOT NULL
);


--
-- TOC entry 2461 (class 1259 OID 122548)
-- Dependencies: 7
-- Name: device_status; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device_status (
    id bigint NOT NULL,
    version bigint NOT NULL,
    status character varying(255) NOT NULL
);


--
-- TOC entry 2489 (class 1259 OID 122966)
-- Dependencies: 7
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: aatams; Owner: aatams
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2462 (class 1259 OID 122555)
-- Dependencies: 7
-- Name: installation; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation (
    id bigint NOT NULL,
    version bigint NOT NULL,
    configuration_id bigint NOT NULL,
    lat_offset real NOT NULL,
    lon_offset real NOT NULL,
    name character varying(255) NOT NULL,
    project_id bigint NOT NULL
);


--
-- TOC entry 2463 (class 1259 OID 122560)
-- Dependencies: 7
-- Name: installation_configuration; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation_configuration (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2464 (class 1259 OID 122567)
-- Dependencies: 1133 7
-- Name: installation_station; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation_station (
    id bigint NOT NULL,
    version bigint NOT NULL,
    curtain_position integer NOT NULL,
    installation_id bigint NOT NULL,
    location public.geometry NOT NULL,
    name character varying(255) NOT NULL
);


--
-- TOC entry 2490 (class 1259 OID 122969)
-- Dependencies: 7
-- Name: installation_station_receiver; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation_station_receiver (
    installation_station_receivers_id bigint,
    receiver_id bigint
);


--
-- TOC entry 2465 (class 1259 OID 122576)
-- Dependencies: 7
-- Name: measurement_unit; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE measurement_unit (
    id bigint NOT NULL,
    version bigint NOT NULL,
    unit character varying(255) NOT NULL
);


--
-- TOC entry 2466 (class 1259 OID 122583)
-- Dependencies: 7
-- Name: mooring_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE mooring_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2467 (class 1259 OID 122590)
-- Dependencies: 7
-- Name: organisation; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE organisation (
    id bigint NOT NULL,
    version bigint NOT NULL,
    fax_number character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    phone_number character varying(255) NOT NULL,
    postal_address character varying(255) NOT NULL,
    status character varying(255) NOT NULL
);


--
-- TOC entry 2468 (class 1259 OID 122600)
-- Dependencies: 7
-- Name: organisation_person; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE organisation_person (
    id bigint NOT NULL,
    version bigint NOT NULL,
    organisation_id bigint NOT NULL,
    person_id bigint NOT NULL
);


--
-- TOC entry 2469 (class 1259 OID 122605)
-- Dependencies: 7
-- Name: organisation_project; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE organisation_project (
    id bigint NOT NULL,
    version bigint NOT NULL,
    organisation_id bigint NOT NULL,
    project_id bigint NOT NULL
);


--
-- TOC entry 2470 (class 1259 OID 122610)
-- Dependencies: 7
-- Name: person; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE person (
    id bigint NOT NULL,
    version bigint NOT NULL,
    email_address character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    phone_number character varying(255) NOT NULL
);


--
-- TOC entry 2471 (class 1259 OID 122618)
-- Dependencies: 7
-- Name: person_system_role; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE person_system_role (
    person_system_roles_id bigint,
    system_role_id bigint
);


--
-- TOC entry 2472 (class 1259 OID 122621)
-- Dependencies: 7
-- Name: project; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE project (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    principal_investator_id bigint
);


--
-- TOC entry 2473 (class 1259 OID 122631)
-- Dependencies: 7
-- Name: project_role; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE project_role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    person_id bigint NOT NULL,
    project_id bigint NOT NULL,
    role_type_id bigint NOT NULL
);


--
-- TOC entry 2474 (class 1259 OID 122636)
-- Dependencies: 7
-- Name: project_role_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE project_role_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    display_name character varying(255) NOT NULL
);


--
-- TOC entry 2475 (class 1259 OID 122643)
-- Dependencies: 7
-- Name: receiver_deployment; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE receiver_deployment (
    id bigint NOT NULL,
    version bigint NOT NULL,
    acoustic_releaseid character varying(255) NOT NULL,
    bottom_depthm real NOT NULL,
    comments character varying(255) NOT NULL,
    deployment_date timestamp without time zone NOT NULL,
    deployment_number integer NOT NULL,
    depth_below_surfacem real NOT NULL,
    location bytea NOT NULL,
    mooring_type_id bigint NOT NULL,
    receiver_id bigint NOT NULL,
    receiver_orientation character varying(255) NOT NULL,
    recovery_date timestamp without time zone NOT NULL,
    station_id bigint NOT NULL
);


--
-- TOC entry 2476 (class 1259 OID 122651)
-- Dependencies: 7
-- Name: receiver_download; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE receiver_download (
    id bigint NOT NULL,
    version bigint NOT NULL,
    battery_days integer NOT NULL,
    battery_voltage real NOT NULL,
    clock_drift real NOT NULL,
    comments character varying(255) NOT NULL,
    detection_count integer NOT NULL,
    download_date timestamp without time zone NOT NULL,
    downloader_id bigint NOT NULL,
    ping_count integer NOT NULL
);


--
-- TOC entry 2477 (class 1259 OID 122656)
-- Dependencies: 7
-- Name: receiver_download_file; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE receiver_download_file (
    id bigint NOT NULL,
    version bigint NOT NULL,
    path bytea NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2478 (class 1259 OID 122664)
-- Dependencies: 7
-- Name: receiver_download_receiver_download_file; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE receiver_download_receiver_download_file (
    receiver_download_download_files_id bigint,
    receiver_download_file_id bigint
);


--
-- TOC entry 2479 (class 1259 OID 122667)
-- Dependencies: 7
-- Name: receiver_recovery; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE receiver_recovery (
    id bigint NOT NULL,
    version bigint NOT NULL,
    battery_life real,
    battery_voltage real,
    deployment_id bigint NOT NULL,
    download_id bigint,
    location bytea NOT NULL,
    recovery_date timestamp without time zone NOT NULL,
    status_id bigint NOT NULL
);


--
-- TOC entry 2480 (class 1259 OID 122675)
-- Dependencies: 7
-- Name: sensor; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE sensor (
    id bigint NOT NULL,
    version bigint NOT NULL,
    code_map character varying(255) NOT NULL,
    intercept integer NOT NULL,
    ping_code integer NOT NULL,
    slope integer NOT NULL,
    tag_id bigint NOT NULL,
    transmitter_type_id bigint NOT NULL,
    unit character varying(255) NOT NULL
);


--
-- TOC entry 2481 (class 1259 OID 122683)
-- Dependencies: 7
-- Name: sex; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE sex (
    id bigint NOT NULL,
    version bigint NOT NULL,
    sex character varying(255) NOT NULL
);


--
-- TOC entry 2482 (class 1259 OID 122690)
-- Dependencies: 7
-- Name: surgery; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE surgery (
    id bigint NOT NULL,
    version bigint NOT NULL,
    comments character varying(255) NOT NULL,
    release_id bigint NOT NULL,
    surgeon_id bigint NOT NULL,
    sutures boolean NOT NULL,
    tag_id bigint NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    treatment_type_id bigint NOT NULL,
    type_id bigint NOT NULL
);


--
-- TOC entry 2483 (class 1259 OID 122695)
-- Dependencies: 7
-- Name: surgery_treatment_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE surgery_treatment_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2484 (class 1259 OID 122702)
-- Dependencies: 7
-- Name: surgery_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE surgery_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2485 (class 1259 OID 122709)
-- Dependencies: 7
-- Name: system_role; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE system_role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    role_type_id bigint NOT NULL
);


--
-- TOC entry 2486 (class 1259 OID 122714)
-- Dependencies: 7
-- Name: system_role_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE system_role_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    display_name character varying(255) NOT NULL
);


--
-- TOC entry 2487 (class 1259 OID 122721)
-- Dependencies: 7
-- Name: transmitter_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE transmitter_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    transmitter_type_name character varying(255) NOT NULL
);


--
-- TOC entry 2488 (class 1259 OID 122728)
-- Dependencies: 7
-- Name: ufile; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE ufile (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_uploaded timestamp without time zone NOT NULL,
    downloads integer NOT NULL,
    extension character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    path character varying(255) NOT NULL,
    size bigint NOT NULL
);


--
-- TOC entry 2791 (class 2606 OID 122496)
-- Dependencies: 2452 2452
-- Name: animal_measurement_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT animal_measurement_pkey PRIMARY KEY (id);


--
-- TOC entry 2793 (class 2606 OID 122501)
-- Dependencies: 2453 2453
-- Name: animal_measurement_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement_type
    ADD CONSTRAINT animal_measurement_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2795 (class 2606 OID 122503)
-- Dependencies: 2453 2453
-- Name: animal_measurement_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement_type
    ADD CONSTRAINT animal_measurement_type_type_key UNIQUE (type);


--
-- TOC entry 2789 (class 2606 OID 122491)
-- Dependencies: 2451 2451
-- Name: animal_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_pkey PRIMARY KEY (id);


--
-- TOC entry 2797 (class 2606 OID 122511)
-- Dependencies: 2454 2454
-- Name: animal_release_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT animal_release_pkey PRIMARY KEY (id);


--
-- TOC entry 2799 (class 2606 OID 122522)
-- Dependencies: 2456 2456
-- Name: detection_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY detection
    ADD CONSTRAINT detection_pkey PRIMARY KEY (id);


--
-- TOC entry 2803 (class 2606 OID 122540)
-- Dependencies: 2459 2459
-- Name: device_manufacturer_manufacturer_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_manufacturer
    ADD CONSTRAINT device_manufacturer_manufacturer_name_key UNIQUE (manufacturer_name);


--
-- TOC entry 2805 (class 2606 OID 122538)
-- Dependencies: 2459 2459
-- Name: device_manufacturer_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_manufacturer
    ADD CONSTRAINT device_manufacturer_pkey PRIMARY KEY (id);


--
-- TOC entry 2807 (class 2606 OID 122547)
-- Dependencies: 2460 2460
-- Name: device_model_model_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT device_model_model_name_key UNIQUE (model_name);


--
-- TOC entry 2809 (class 2606 OID 122545)
-- Dependencies: 2460 2460
-- Name: device_model_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT device_model_pkey PRIMARY KEY (id);


--
-- TOC entry 2801 (class 2606 OID 122533)
-- Dependencies: 2458 2458
-- Name: device_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);


--
-- TOC entry 2811 (class 2606 OID 122552)
-- Dependencies: 2461 2461
-- Name: device_status_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_status
    ADD CONSTRAINT device_status_pkey PRIMARY KEY (id);


--
-- TOC entry 2813 (class 2606 OID 122554)
-- Dependencies: 2461 2461
-- Name: device_status_status_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_status
    ADD CONSTRAINT device_status_status_key UNIQUE (status);


--
-- TOC entry 2817 (class 2606 OID 122564)
-- Dependencies: 2463 2463
-- Name: installation_configuration_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_configuration
    ADD CONSTRAINT installation_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 2819 (class 2606 OID 122566)
-- Dependencies: 2463 2463
-- Name: installation_configuration_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_configuration
    ADD CONSTRAINT installation_configuration_type_key UNIQUE (type);


--
-- TOC entry 2815 (class 2606 OID 122559)
-- Dependencies: 2462 2462
-- Name: installation_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT installation_pkey PRIMARY KEY (id);


--
-- TOC entry 2821 (class 2606 OID 122575)
-- Dependencies: 2464 2464
-- Name: installation_station_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_station
    ADD CONSTRAINT installation_station_pkey PRIMARY KEY (id);


--
-- TOC entry 2823 (class 2606 OID 122580)
-- Dependencies: 2465 2465
-- Name: measurement_unit_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY measurement_unit
    ADD CONSTRAINT measurement_unit_pkey PRIMARY KEY (id);


--
-- TOC entry 2825 (class 2606 OID 122582)
-- Dependencies: 2465 2465
-- Name: measurement_unit_unit_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY measurement_unit
    ADD CONSTRAINT measurement_unit_unit_key UNIQUE (unit);


--
-- TOC entry 2827 (class 2606 OID 122587)
-- Dependencies: 2466 2466
-- Name: mooring_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY mooring_type
    ADD CONSTRAINT mooring_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2829 (class 2606 OID 122589)
-- Dependencies: 2466 2466
-- Name: mooring_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY mooring_type
    ADD CONSTRAINT mooring_type_type_key UNIQUE (type);


--
-- TOC entry 2831 (class 2606 OID 122599)
-- Dependencies: 2467 2467
-- Name: organisation_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT organisation_name_key UNIQUE (name);


--
-- TOC entry 2835 (class 2606 OID 122604)
-- Dependencies: 2468 2468
-- Name: organisation_person_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT organisation_person_pkey PRIMARY KEY (id);


--
-- TOC entry 2833 (class 2606 OID 122597)
-- Dependencies: 2467 2467
-- Name: organisation_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT organisation_pkey PRIMARY KEY (id);


--
-- TOC entry 2837 (class 2606 OID 122609)
-- Dependencies: 2469 2469
-- Name: organisation_project_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT organisation_project_pkey PRIMARY KEY (id);


--
-- TOC entry 2839 (class 2606 OID 122617)
-- Dependencies: 2470 2470
-- Name: person_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 2841 (class 2606 OID 122630)
-- Dependencies: 2472 2472
-- Name: project_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_name_key UNIQUE (name);


--
-- TOC entry 2843 (class 2606 OID 122628)
-- Dependencies: 2472 2472
-- Name: project_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 2845 (class 2606 OID 122635)
-- Dependencies: 2473 2473
-- Name: project_role_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT project_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2847 (class 2606 OID 122642)
-- Dependencies: 2474 2474
-- Name: project_role_type_display_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role_type
    ADD CONSTRAINT project_role_type_display_name_key UNIQUE (display_name);


--
-- TOC entry 2849 (class 2606 OID 122640)
-- Dependencies: 2474 2474
-- Name: project_role_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role_type
    ADD CONSTRAINT project_role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2851 (class 2606 OID 122650)
-- Dependencies: 2475 2475
-- Name: receiver_deployment_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT receiver_deployment_pkey PRIMARY KEY (id);


--
-- TOC entry 2855 (class 2606 OID 122663)
-- Dependencies: 2477 2477
-- Name: receiver_download_file_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_download_file
    ADD CONSTRAINT receiver_download_file_pkey PRIMARY KEY (id);


--
-- TOC entry 2853 (class 2606 OID 122655)
-- Dependencies: 2476 2476
-- Name: receiver_download_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_download
    ADD CONSTRAINT receiver_download_pkey PRIMARY KEY (id);


--
-- TOC entry 2857 (class 2606 OID 122674)
-- Dependencies: 2479 2479
-- Name: receiver_recovery_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT receiver_recovery_pkey PRIMARY KEY (id);


--
-- TOC entry 2859 (class 2606 OID 122682)
-- Dependencies: 2480 2480
-- Name: sensor_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT sensor_pkey PRIMARY KEY (id);


--
-- TOC entry 2861 (class 2606 OID 122687)
-- Dependencies: 2481 2481
-- Name: sex_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_pkey PRIMARY KEY (id);


--
-- TOC entry 2863 (class 2606 OID 122689)
-- Dependencies: 2481 2481
-- Name: sex_sex_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_sex_key UNIQUE (sex);


--
-- TOC entry 2865 (class 2606 OID 122694)
-- Dependencies: 2482 2482
-- Name: surgery_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT surgery_pkey PRIMARY KEY (id);


--
-- TOC entry 2867 (class 2606 OID 122699)
-- Dependencies: 2483 2483
-- Name: surgery_treatment_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_treatment_type
    ADD CONSTRAINT surgery_treatment_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2869 (class 2606 OID 122701)
-- Dependencies: 2483 2483
-- Name: surgery_treatment_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_treatment_type
    ADD CONSTRAINT surgery_treatment_type_type_key UNIQUE (type);


--
-- TOC entry 2871 (class 2606 OID 122706)
-- Dependencies: 2484 2484
-- Name: surgery_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_type
    ADD CONSTRAINT surgery_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2873 (class 2606 OID 122708)
-- Dependencies: 2484 2484
-- Name: surgery_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_type
    ADD CONSTRAINT surgery_type_type_key UNIQUE (type);


--
-- TOC entry 2875 (class 2606 OID 122713)
-- Dependencies: 2485 2485
-- Name: system_role_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT system_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2877 (class 2606 OID 122720)
-- Dependencies: 2486 2486
-- Name: system_role_type_display_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role_type
    ADD CONSTRAINT system_role_type_display_name_key UNIQUE (display_name);


--
-- TOC entry 2879 (class 2606 OID 122718)
-- Dependencies: 2486 2486
-- Name: system_role_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role_type
    ADD CONSTRAINT system_role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2881 (class 2606 OID 122725)
-- Dependencies: 2487 2487
-- Name: transmitter_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY transmitter_type
    ADD CONSTRAINT transmitter_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2883 (class 2606 OID 122727)
-- Dependencies: 2487 2487
-- Name: transmitter_type_transmitter_type_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY transmitter_type
    ADD CONSTRAINT transmitter_type_transmitter_type_name_key UNIQUE (transmitter_type_name);


--
-- TOC entry 2885 (class 2606 OID 122735)
-- Dependencies: 2488 2488
-- Name: ufile_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY ufile
    ADD CONSTRAINT ufile_pkey PRIMARY KEY (id);


--
-- TOC entry 2933 (class 2606 OID 122977)
-- Dependencies: 2490 2464 2820
-- Name: fk2f7233ffd6ed9307; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station_receiver
    ADD CONSTRAINT fk2f7233ffd6ed9307 FOREIGN KEY (installation_station_receivers_id) REFERENCES installation_station(id);


--
-- TOC entry 2932 (class 2606 OID 122972)
-- Dependencies: 2800 2490 2458
-- Name: fk2f7233fff0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station_receiver
    ADD CONSTRAINT fk2f7233fff0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2914 (class 2606 OID 122876)
-- Dependencies: 2473 2474 2848
-- Name: fk37fff5dc51218487; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dc51218487 FOREIGN KEY (role_type_id) REFERENCES project_role_type(id);


--
-- TOC entry 2912 (class 2606 OID 122866)
-- Dependencies: 2473 2472 2842
-- Name: fk37fff5dcbf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dcbf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2913 (class 2606 OID 122871)
-- Dependencies: 2473 2470 2838
-- Name: fk37fff5dce985cdb3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dce985cdb3 FOREIGN KEY (person_id) REFERENCES person(id);


--
-- TOC entry 2903 (class 2606 OID 122821)
-- Dependencies: 2462 2463 2816
-- Name: fk796d5e3a18d65d27; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT fk796d5e3a18d65d27 FOREIGN KEY (configuration_id) REFERENCES installation_configuration(id);


--
-- TOC entry 2902 (class 2606 OID 122816)
-- Dependencies: 2462 2472 2842
-- Name: fk796d5e3abf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT fk796d5e3abf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2918 (class 2606 OID 122896)
-- Dependencies: 2476 2470 2838
-- Name: fk79accd8b8e509d3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download
    ADD CONSTRAINT fk79accd8b8e509d3 FOREIGN KEY (downloader_id) REFERENCES person(id);


--
-- TOC entry 2922 (class 2606 OID 122916)
-- Dependencies: 2479 2461 2810
-- Name: fk82de83e56b73e7c9; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e56b73e7c9 FOREIGN KEY (status_id) REFERENCES device_status(id);


--
-- TOC entry 2923 (class 2606 OID 122921)
-- Dependencies: 2479 2475 2850
-- Name: fk82de83e579a96182; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e579a96182 FOREIGN KEY (deployment_id) REFERENCES receiver_deployment(id);


--
-- TOC entry 2921 (class 2606 OID 122911)
-- Dependencies: 2479 2476 2852
-- Name: fk82de83e593fa40a2; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e593fa40a2 FOREIGN KEY (download_id) REFERENCES receiver_download(id);


--
-- TOC entry 2916 (class 2606 OID 122886)
-- Dependencies: 2475 2466 2826
-- Name: fk862aeb1531eebdc; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb1531eebdc FOREIGN KEY (mooring_type_id) REFERENCES mooring_type(id);


--
-- TOC entry 2917 (class 2606 OID 122891)
-- Dependencies: 2475 2464 2820
-- Name: fk862aeb15cdaf3227; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb15cdaf3227 FOREIGN KEY (station_id) REFERENCES installation_station(id);


--
-- TOC entry 2915 (class 2606 OID 122881)
-- Dependencies: 2475 2458 2800
-- Name: fk862aeb15f0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb15f0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2889 (class 2606 OID 122751)
-- Dependencies: 2842 2454 2472
-- Name: fk88d6a0c4bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT fk88d6a0c4bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2890 (class 2606 OID 122756)
-- Dependencies: 2454 2451 2788
-- Name: fk88d6a0c4e0347853; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT fk88d6a0c4e0347853 FOREIGN KEY (animal_id) REFERENCES animal(id);


--
-- TOC entry 2910 (class 2606 OID 122856)
-- Dependencies: 2471 2470 2838
-- Name: fk8e3f6e9c244fac71; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY person_system_role
    ADD CONSTRAINT fk8e3f6e9c244fac71 FOREIGN KEY (person_system_roles_id) REFERENCES person(id);


--
-- TOC entry 2909 (class 2606 OID 122851)
-- Dependencies: 2471 2485 2874
-- Name: fk8e3f6e9cbba8aa52; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY person_system_role
    ADD CONSTRAINT fk8e3f6e9cbba8aa52 FOREIGN KEY (system_role_id) REFERENCES system_role(id);


--
-- TOC entry 2888 (class 2606 OID 122746)
-- Dependencies: 2452 2453 2792
-- Name: fk8eb3adf914018681; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT fk8eb3adf914018681 FOREIGN KEY (type_id) REFERENCES animal_measurement_type(id);


--
-- TOC entry 2887 (class 2606 OID 122741)
-- Dependencies: 2452 2465 2822
-- Name: fk8eb3adf9e6ea591d; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT fk8eb3adf9e6ea591d FOREIGN KEY (unit_id) REFERENCES measurement_unit(id);


--
-- TOC entry 2904 (class 2606 OID 122826)
-- Dependencies: 2464 2814 2462
-- Name: fk902c2c2f35e870d3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station
    ADD CONSTRAINT fk902c2c2f35e870d3 FOREIGN KEY (installation_id) REFERENCES installation(id);


--
-- TOC entry 2894 (class 2606 OID 122776)
-- Dependencies: 2456 2480 2858
-- Name: fk90e7ca85d7d8b793; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection
    ADD CONSTRAINT fk90e7ca85d7d8b793 FOREIGN KEY (sensor_id) REFERENCES sensor(id);


--
-- TOC entry 2893 (class 2606 OID 122771)
-- Dependencies: 2456 2458 2800
-- Name: fk90e7ca85f0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection
    ADD CONSTRAINT fk90e7ca85f0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2927 (class 2606 OID 122941)
-- Dependencies: 2866 2482 2483
-- Name: fk918a71f54f2dd3af; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f54f2dd3af FOREIGN KEY (treatment_type_id) REFERENCES surgery_treatment_type(id);


--
-- TOC entry 2929 (class 2606 OID 122951)
-- Dependencies: 2484 2870 2482
-- Name: fk918a71f56d95e296; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f56d95e296 FOREIGN KEY (type_id) REFERENCES surgery_type(id);


--
-- TOC entry 2926 (class 2606 OID 122936)
-- Dependencies: 2470 2838 2482
-- Name: fk918a71f57480ac7b; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f57480ac7b FOREIGN KEY (surgeon_id) REFERENCES person(id);


--
-- TOC entry 2928 (class 2606 OID 122946)
-- Dependencies: 2482 2796 2454
-- Name: fk918a71f5b5c10085; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f5b5c10085 FOREIGN KEY (release_id) REFERENCES animal_release(id);


--
-- TOC entry 2930 (class 2606 OID 122956)
-- Dependencies: 2482 2458 2800
-- Name: fk918a71f5ceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f5ceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2891 (class 2606 OID 122761)
-- Dependencies: 2455 2454 2796
-- Name: fka3b75543290ccda; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release_animal_measurement
    ADD CONSTRAINT fka3b75543290ccda FOREIGN KEY (animal_release_measurements_id) REFERENCES animal_release(id);


--
-- TOC entry 2892 (class 2606 OID 122766)
-- Dependencies: 2455 2452 2790
-- Name: fka3b7554af520388; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release_animal_measurement
    ADD CONSTRAINT fka3b7554af520388 FOREIGN KEY (animal_measurement_id) REFERENCES animal_measurement(id);


--
-- TOC entry 2931 (class 2606 OID 122961)
-- Dependencies: 2485 2878 2486
-- Name: fka47ecc86d06730af; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT fka47ecc86d06730af FOREIGN KEY (role_type_id) REFERENCES system_role_type(id);


--
-- TOC entry 2886 (class 2606 OID 122736)
-- Dependencies: 2860 2451 2481
-- Name: fkabc58dfccd365681; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fkabc58dfccd365681 FOREIGN KEY (sex_id) REFERENCES sex(id);


--
-- TOC entry 2920 (class 2606 OID 122906)
-- Dependencies: 2478 2477 2854
-- Name: fkadfe47ca50da4c63; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download_receiver_download_file
    ADD CONSTRAINT fkadfe47ca50da4c63 FOREIGN KEY (receiver_download_file_id) REFERENCES receiver_download_file(id);


--
-- TOC entry 2919 (class 2606 OID 122901)
-- Dependencies: 2478 2476 2852
-- Name: fkadfe47cafa360683; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download_receiver_download_file
    ADD CONSTRAINT fkadfe47cafa360683 FOREIGN KEY (receiver_download_download_files_id) REFERENCES receiver_download(id);


--
-- TOC entry 2900 (class 2606 OID 122806)
-- Dependencies: 2458 2460 2808
-- Name: fkb06b1e561c043beb; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e561c043beb FOREIGN KEY (model_id) REFERENCES device_model(id);


--
-- TOC entry 2899 (class 2606 OID 122801)
-- Dependencies: 2458 2461 2810
-- Name: fkb06b1e566b73e7c9; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e566b73e7c9 FOREIGN KEY (status_id) REFERENCES device_status(id);


--
-- TOC entry 2897 (class 2606 OID 122791)
-- Dependencies: 2458 2472 2842
-- Name: fkb06b1e56bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e56bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2898 (class 2606 OID 122796)
-- Dependencies: 2458 2464 2820
-- Name: fkb06b1e56cdaf3227; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e56cdaf3227 FOREIGN KEY (station_id) REFERENCES installation_station(id);


--
-- TOC entry 2924 (class 2606 OID 122926)
-- Dependencies: 2480 2880 2487
-- Name: fkca0053ba4b0c3bc4; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT fkca0053ba4b0c3bc4 FOREIGN KEY (transmitter_type_id) REFERENCES transmitter_type(id);


--
-- TOC entry 2925 (class 2606 OID 122931)
-- Dependencies: 2480 2458 2800
-- Name: fkca0053baceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT fkca0053baceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2901 (class 2606 OID 122811)
-- Dependencies: 2460 2459 2804
-- Name: fkdcc4e40025f67029; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT fkdcc4e40025f67029 FOREIGN KEY (manufacturer_id) REFERENCES device_manufacturer(id);


--
-- TOC entry 2895 (class 2606 OID 122781)
-- Dependencies: 2457 2456 2798
-- Name: fkeb71eee0ac39c713; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection_tag
    ADD CONSTRAINT fkeb71eee0ac39c713 FOREIGN KEY (detection_tags_id) REFERENCES detection(id);


--
-- TOC entry 2896 (class 2606 OID 122786)
-- Dependencies: 2457 2458 2800
-- Name: fkeb71eee0ceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection_tag
    ADD CONSTRAINT fkeb71eee0ceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2911 (class 2606 OID 122861)
-- Dependencies: 2472 2473 2844
-- Name: fked904b19c34cf774; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project
    ADD CONSTRAINT fked904b19c34cf774 FOREIGN KEY (principal_investator_id) REFERENCES project_role(id);


--
-- TOC entry 2906 (class 2606 OID 122836)
-- Dependencies: 2468 2467 2832
-- Name: fkee60ce5a99b5ecd3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT fkee60ce5a99b5ecd3 FOREIGN KEY (organisation_id) REFERENCES organisation(id);


--
-- TOC entry 2905 (class 2606 OID 122831)
-- Dependencies: 2468 2470 2838
-- Name: fkee60ce5ae985cdb3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT fkee60ce5ae985cdb3 FOREIGN KEY (person_id) REFERENCES person(id);


--
-- TOC entry 2908 (class 2606 OID 122846)
-- Dependencies: 2469 2467 2832
-- Name: fkf3b978b499b5ecd3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT fkf3b978b499b5ecd3 FOREIGN KEY (organisation_id) REFERENCES organisation(id);


--
-- TOC entry 2907 (class 2606 OID 122841)
-- Dependencies: 2469 2472 2842
-- Name: fkf3b978b4bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT fkf3b978b4bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


-- Completed on 2011-05-31 16:27:48 EST

--
-- PostgreSQL database dump complete
--

