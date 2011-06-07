--
-- PostgreSQL database dump
--

-- Dumped from database version 9.0.2
-- Dumped by pg_dump version 9.0.1
-- Started on 2011-06-07 13:51:51 EST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET SESSION AUTHORIZATION 'aatams';

DROP SCHEMA aatams CASCADE;

--
-- TOC entry 7 (class 2615 OID 125950)
-- Name: aatams; Type: SCHEMA; Schema: -; Owner: aatams
--

CREATE SCHEMA aatams;


SET search_path = aatams, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 2456 (class 1259 OID 154413)
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
-- TOC entry 2457 (class 1259 OID 154418)
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
-- TOC entry 2458 (class 1259 OID 154423)
-- Dependencies: 7
-- Name: animal_measurement_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_measurement_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2459 (class 1259 OID 154430)
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
-- TOC entry 2460 (class 1259 OID 154438)
-- Dependencies: 7
-- Name: animal_release_animal_measurement; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_release_animal_measurement (
    animal_release_measurements_id bigint,
    animal_measurement_id bigint
);


--
-- TOC entry 2461 (class 1259 OID 154441)
-- Dependencies: 7
-- Name: detection; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE detection (
    id bigint NOT NULL,
    version bigint NOT NULL,
    location bytea,
    receiver_id bigint NOT NULL,
    station_name character varying(255),
    "timestamp" timestamp without time zone NOT NULL,
    transmitter_name character varying(255),
    transmitter_serial_number character varying(255),
    class character varying(255) NOT NULL,
    uncalibrated_value real
);


--
-- TOC entry 2462 (class 1259 OID 154449)
-- Dependencies: 7
-- Name: detection_tag; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE detection_tag (
    detection_tags_id bigint,
    tag_id bigint
);


--
-- TOC entry 2463 (class 1259 OID 154452)
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
    ping_code integer
);


--
-- TOC entry 2464 (class 1259 OID 154460)
-- Dependencies: 7
-- Name: device_manufacturer; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device_manufacturer (
    id bigint NOT NULL,
    version bigint NOT NULL,
    manufacturer_name character varying(255) NOT NULL
);


--
-- TOC entry 2465 (class 1259 OID 154467)
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
-- TOC entry 2466 (class 1259 OID 154474)
-- Dependencies: 7
-- Name: device_status; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device_status (
    id bigint NOT NULL,
    version bigint NOT NULL,
    status character varying(255) NOT NULL
);


--
-- TOC entry 2497 (class 1259 OID 154920)
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
-- TOC entry 2467 (class 1259 OID 154481)
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
-- TOC entry 2468 (class 1259 OID 154486)
-- Dependencies: 7
-- Name: installation_configuration; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation_configuration (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2469 (class 1259 OID 154493)
-- Dependencies: 1161 7
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
-- TOC entry 2470 (class 1259 OID 154501)
-- Dependencies: 7
-- Name: installation_station_receiver; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation_station_receiver (
    installation_station_receivers_id bigint,
    receiver_id bigint
);


--
-- TOC entry 2471 (class 1259 OID 154504)
-- Dependencies: 7
-- Name: measurement_unit; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE measurement_unit (
    id bigint NOT NULL,
    version bigint NOT NULL,
    unit character varying(255) NOT NULL
);


--
-- TOC entry 2472 (class 1259 OID 154511)
-- Dependencies: 7
-- Name: mooring_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE mooring_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2473 (class 1259 OID 154518)
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
-- TOC entry 2474 (class 1259 OID 154528)
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
-- TOC entry 2475 (class 1259 OID 154533)
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
-- TOC entry 2476 (class 1259 OID 154538)
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
-- TOC entry 2477 (class 1259 OID 154546)
-- Dependencies: 7
-- Name: person_system_role; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE person_system_role (
    person_system_roles_id bigint,
    system_role_id bigint
);


--
-- TOC entry 2478 (class 1259 OID 154549)
-- Dependencies: 7
-- Name: processed_upload_file; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE processed_upload_file (
    id bigint NOT NULL,
    version bigint NOT NULL,
    err_msg character varying(255),
    status character varying(255) NOT NULL,
    u_file_id bigint NOT NULL
);


--
-- TOC entry 2479 (class 1259 OID 154557)
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
-- TOC entry 2480 (class 1259 OID 154567)
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
-- TOC entry 2481 (class 1259 OID 154572)
-- Dependencies: 7
-- Name: project_role_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE project_role_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    display_name character varying(255) NOT NULL
);


--
-- TOC entry 2482 (class 1259 OID 154579)
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
-- TOC entry 2483 (class 1259 OID 154587)
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
-- TOC entry 2484 (class 1259 OID 154592)
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
-- TOC entry 2485 (class 1259 OID 154600)
-- Dependencies: 7
-- Name: receiver_download_receiver_download_file; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE receiver_download_receiver_download_file (
    receiver_download_download_files_id bigint,
    receiver_download_file_id bigint
);


--
-- TOC entry 2486 (class 1259 OID 154603)
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
-- TOC entry 2487 (class 1259 OID 154611)
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
-- TOC entry 2488 (class 1259 OID 154619)
-- Dependencies: 7
-- Name: sensor_detection_sensor; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE sensor_detection_sensor (
    sensor_detection_sensors_id bigint,
    sensor_id bigint
);


--
-- TOC entry 2489 (class 1259 OID 154622)
-- Dependencies: 7
-- Name: sex; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE sex (
    id bigint NOT NULL,
    version bigint NOT NULL,
    sex character varying(255) NOT NULL
);


--
-- TOC entry 2490 (class 1259 OID 154629)
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
-- TOC entry 2491 (class 1259 OID 154634)
-- Dependencies: 7
-- Name: surgery_treatment_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE surgery_treatment_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2492 (class 1259 OID 154641)
-- Dependencies: 7
-- Name: surgery_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE surgery_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2493 (class 1259 OID 154648)
-- Dependencies: 7
-- Name: system_role; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE system_role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    role_type_id bigint NOT NULL
);


--
-- TOC entry 2494 (class 1259 OID 154653)
-- Dependencies: 7
-- Name: system_role_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE system_role_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    display_name character varying(255) NOT NULL
);


--
-- TOC entry 2495 (class 1259 OID 154660)
-- Dependencies: 7
-- Name: transmitter_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE transmitter_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    transmitter_type_name character varying(255) NOT NULL
);


--
-- TOC entry 2496 (class 1259 OID 154667)
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
-- TOC entry 2798 (class 2606 OID 154422)
-- Dependencies: 2457 2457
-- Name: animal_measurement_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT animal_measurement_pkey PRIMARY KEY (id);


--
-- TOC entry 2800 (class 2606 OID 154427)
-- Dependencies: 2458 2458
-- Name: animal_measurement_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement_type
    ADD CONSTRAINT animal_measurement_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2802 (class 2606 OID 154429)
-- Dependencies: 2458 2458
-- Name: animal_measurement_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement_type
    ADD CONSTRAINT animal_measurement_type_type_key UNIQUE (type);


--
-- TOC entry 2796 (class 2606 OID 154417)
-- Dependencies: 2456 2456
-- Name: animal_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_pkey PRIMARY KEY (id);


--
-- TOC entry 2804 (class 2606 OID 154437)
-- Dependencies: 2459 2459
-- Name: animal_release_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT animal_release_pkey PRIMARY KEY (id);


--
-- TOC entry 2806 (class 2606 OID 154448)
-- Dependencies: 2461 2461
-- Name: detection_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY detection
    ADD CONSTRAINT detection_pkey PRIMARY KEY (id);


--
-- TOC entry 2810 (class 2606 OID 154466)
-- Dependencies: 2464 2464
-- Name: device_manufacturer_manufacturer_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_manufacturer
    ADD CONSTRAINT device_manufacturer_manufacturer_name_key UNIQUE (manufacturer_name);


--
-- TOC entry 2812 (class 2606 OID 154464)
-- Dependencies: 2464 2464
-- Name: device_manufacturer_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_manufacturer
    ADD CONSTRAINT device_manufacturer_pkey PRIMARY KEY (id);


--
-- TOC entry 2814 (class 2606 OID 154473)
-- Dependencies: 2465 2465
-- Name: device_model_model_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT device_model_model_name_key UNIQUE (model_name);


--
-- TOC entry 2816 (class 2606 OID 154471)
-- Dependencies: 2465 2465
-- Name: device_model_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT device_model_pkey PRIMARY KEY (id);


--
-- TOC entry 2808 (class 2606 OID 154459)
-- Dependencies: 2463 2463
-- Name: device_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);


--
-- TOC entry 2818 (class 2606 OID 154478)
-- Dependencies: 2466 2466
-- Name: device_status_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_status
    ADD CONSTRAINT device_status_pkey PRIMARY KEY (id);


--
-- TOC entry 2820 (class 2606 OID 154480)
-- Dependencies: 2466 2466
-- Name: device_status_status_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_status
    ADD CONSTRAINT device_status_status_key UNIQUE (status);


--
-- TOC entry 2824 (class 2606 OID 154490)
-- Dependencies: 2468 2468
-- Name: installation_configuration_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_configuration
    ADD CONSTRAINT installation_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 2826 (class 2606 OID 154492)
-- Dependencies: 2468 2468
-- Name: installation_configuration_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_configuration
    ADD CONSTRAINT installation_configuration_type_key UNIQUE (type);


--
-- TOC entry 2822 (class 2606 OID 154485)
-- Dependencies: 2467 2467
-- Name: installation_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT installation_pkey PRIMARY KEY (id);


--
-- TOC entry 2828 (class 2606 OID 154500)
-- Dependencies: 2469 2469
-- Name: installation_station_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_station
    ADD CONSTRAINT installation_station_pkey PRIMARY KEY (id);


--
-- TOC entry 2830 (class 2606 OID 154508)
-- Dependencies: 2471 2471
-- Name: measurement_unit_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY measurement_unit
    ADD CONSTRAINT measurement_unit_pkey PRIMARY KEY (id);


--
-- TOC entry 2832 (class 2606 OID 154510)
-- Dependencies: 2471 2471
-- Name: measurement_unit_unit_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY measurement_unit
    ADD CONSTRAINT measurement_unit_unit_key UNIQUE (unit);


--
-- TOC entry 2834 (class 2606 OID 154515)
-- Dependencies: 2472 2472
-- Name: mooring_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY mooring_type
    ADD CONSTRAINT mooring_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2836 (class 2606 OID 154517)
-- Dependencies: 2472 2472
-- Name: mooring_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY mooring_type
    ADD CONSTRAINT mooring_type_type_key UNIQUE (type);


--
-- TOC entry 2838 (class 2606 OID 154527)
-- Dependencies: 2473 2473
-- Name: organisation_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT organisation_name_key UNIQUE (name);


--
-- TOC entry 2842 (class 2606 OID 154532)
-- Dependencies: 2474 2474
-- Name: organisation_person_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT organisation_person_pkey PRIMARY KEY (id);


--
-- TOC entry 2840 (class 2606 OID 154525)
-- Dependencies: 2473 2473
-- Name: organisation_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT organisation_pkey PRIMARY KEY (id);


--
-- TOC entry 2844 (class 2606 OID 154537)
-- Dependencies: 2475 2475
-- Name: organisation_project_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT organisation_project_pkey PRIMARY KEY (id);


--
-- TOC entry 2846 (class 2606 OID 154545)
-- Dependencies: 2476 2476
-- Name: person_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 2848 (class 2606 OID 154556)
-- Dependencies: 2478 2478
-- Name: processed_upload_file_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY processed_upload_file
    ADD CONSTRAINT processed_upload_file_pkey PRIMARY KEY (id);


--
-- TOC entry 2850 (class 2606 OID 154566)
-- Dependencies: 2479 2479
-- Name: project_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_name_key UNIQUE (name);


--
-- TOC entry 2852 (class 2606 OID 154564)
-- Dependencies: 2479 2479
-- Name: project_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 2854 (class 2606 OID 154571)
-- Dependencies: 2480 2480
-- Name: project_role_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT project_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2856 (class 2606 OID 154578)
-- Dependencies: 2481 2481
-- Name: project_role_type_display_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role_type
    ADD CONSTRAINT project_role_type_display_name_key UNIQUE (display_name);


--
-- TOC entry 2858 (class 2606 OID 154576)
-- Dependencies: 2481 2481
-- Name: project_role_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role_type
    ADD CONSTRAINT project_role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2860 (class 2606 OID 154586)
-- Dependencies: 2482 2482
-- Name: receiver_deployment_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT receiver_deployment_pkey PRIMARY KEY (id);


--
-- TOC entry 2864 (class 2606 OID 154599)
-- Dependencies: 2484 2484
-- Name: receiver_download_file_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_download_file
    ADD CONSTRAINT receiver_download_file_pkey PRIMARY KEY (id);


--
-- TOC entry 2862 (class 2606 OID 154591)
-- Dependencies: 2483 2483
-- Name: receiver_download_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_download
    ADD CONSTRAINT receiver_download_pkey PRIMARY KEY (id);


--
-- TOC entry 2866 (class 2606 OID 154610)
-- Dependencies: 2486 2486
-- Name: receiver_recovery_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT receiver_recovery_pkey PRIMARY KEY (id);


--
-- TOC entry 2868 (class 2606 OID 154618)
-- Dependencies: 2487 2487
-- Name: sensor_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT sensor_pkey PRIMARY KEY (id);


--
-- TOC entry 2870 (class 2606 OID 154626)
-- Dependencies: 2489 2489
-- Name: sex_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_pkey PRIMARY KEY (id);


--
-- TOC entry 2872 (class 2606 OID 154628)
-- Dependencies: 2489 2489
-- Name: sex_sex_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_sex_key UNIQUE (sex);


--
-- TOC entry 2874 (class 2606 OID 154633)
-- Dependencies: 2490 2490
-- Name: surgery_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT surgery_pkey PRIMARY KEY (id);


--
-- TOC entry 2876 (class 2606 OID 154638)
-- Dependencies: 2491 2491
-- Name: surgery_treatment_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_treatment_type
    ADD CONSTRAINT surgery_treatment_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2878 (class 2606 OID 154640)
-- Dependencies: 2491 2491
-- Name: surgery_treatment_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_treatment_type
    ADD CONSTRAINT surgery_treatment_type_type_key UNIQUE (type);


--
-- TOC entry 2880 (class 2606 OID 154645)
-- Dependencies: 2492 2492
-- Name: surgery_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_type
    ADD CONSTRAINT surgery_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2882 (class 2606 OID 154647)
-- Dependencies: 2492 2492
-- Name: surgery_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_type
    ADD CONSTRAINT surgery_type_type_key UNIQUE (type);


--
-- TOC entry 2884 (class 2606 OID 154652)
-- Dependencies: 2493 2493
-- Name: system_role_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT system_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2886 (class 2606 OID 154659)
-- Dependencies: 2494 2494
-- Name: system_role_type_display_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role_type
    ADD CONSTRAINT system_role_type_display_name_key UNIQUE (display_name);


--
-- TOC entry 2888 (class 2606 OID 154657)
-- Dependencies: 2494 2494
-- Name: system_role_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role_type
    ADD CONSTRAINT system_role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2890 (class 2606 OID 154664)
-- Dependencies: 2495 2495
-- Name: transmitter_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY transmitter_type
    ADD CONSTRAINT transmitter_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2892 (class 2606 OID 154666)
-- Dependencies: 2495 2495
-- Name: transmitter_type_transmitter_type_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY transmitter_type
    ADD CONSTRAINT transmitter_type_transmitter_type_name_key UNIQUE (transmitter_type_name);


--
-- TOC entry 2894 (class 2606 OID 154674)
-- Dependencies: 2496 2496
-- Name: ufile_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY ufile
    ADD CONSTRAINT ufile_pkey PRIMARY KEY (id);


--
-- TOC entry 2913 (class 2606 OID 154765)
-- Dependencies: 2470 2469 2827
-- Name: fk2f7233ffd6ed9307; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station_receiver
    ADD CONSTRAINT fk2f7233ffd6ed9307 FOREIGN KEY (installation_station_receivers_id) REFERENCES installation_station(id);


--
-- TOC entry 2912 (class 2606 OID 154760)
-- Dependencies: 2463 2807 2470
-- Name: fk2f7233fff0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station_receiver
    ADD CONSTRAINT fk2f7233fff0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2924 (class 2606 OID 154820)
-- Dependencies: 2480 2481 2857
-- Name: fk37fff5dc51218487; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dc51218487 FOREIGN KEY (role_type_id) REFERENCES project_role_type(id);


--
-- TOC entry 2922 (class 2606 OID 154810)
-- Dependencies: 2851 2480 2479
-- Name: fk37fff5dcbf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dcbf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2923 (class 2606 OID 154815)
-- Dependencies: 2480 2476 2845
-- Name: fk37fff5dce985cdb3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dce985cdb3 FOREIGN KEY (person_id) REFERENCES person(id);


--
-- TOC entry 2910 (class 2606 OID 154750)
-- Dependencies: 2823 2468 2467
-- Name: fk796d5e3a18d65d27; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT fk796d5e3a18d65d27 FOREIGN KEY (configuration_id) REFERENCES installation_configuration(id);


--
-- TOC entry 2909 (class 2606 OID 154745)
-- Dependencies: 2467 2479 2851
-- Name: fk796d5e3abf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT fk796d5e3abf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2928 (class 2606 OID 154840)
-- Dependencies: 2845 2483 2476
-- Name: fk79accd8b8e509d3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download
    ADD CONSTRAINT fk79accd8b8e509d3 FOREIGN KEY (downloader_id) REFERENCES person(id);


--
-- TOC entry 2937 (class 2606 OID 154885)
-- Dependencies: 2461 2488 2805
-- Name: fk8165509916b1b072; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor_detection_sensor
    ADD CONSTRAINT fk8165509916b1b072 FOREIGN KEY (sensor_detection_sensors_id) REFERENCES detection(id);


--
-- TOC entry 2936 (class 2606 OID 154880)
-- Dependencies: 2487 2488 2867
-- Name: fk81655099d7d8b793; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor_detection_sensor
    ADD CONSTRAINT fk81655099d7d8b793 FOREIGN KEY (sensor_id) REFERENCES sensor(id);


--
-- TOC entry 2932 (class 2606 OID 154860)
-- Dependencies: 2466 2486 2817
-- Name: fk82de83e56b73e7c9; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e56b73e7c9 FOREIGN KEY (status_id) REFERENCES device_status(id);


--
-- TOC entry 2933 (class 2606 OID 154865)
-- Dependencies: 2482 2486 2859
-- Name: fk82de83e579a96182; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e579a96182 FOREIGN KEY (deployment_id) REFERENCES receiver_deployment(id);


--
-- TOC entry 2931 (class 2606 OID 154855)
-- Dependencies: 2483 2486 2861
-- Name: fk82de83e593fa40a2; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e593fa40a2 FOREIGN KEY (download_id) REFERENCES receiver_download(id);


--
-- TOC entry 2926 (class 2606 OID 154830)
-- Dependencies: 2472 2833 2482
-- Name: fk862aeb1531eebdc; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb1531eebdc FOREIGN KEY (mooring_type_id) REFERENCES mooring_type(id);


--
-- TOC entry 2927 (class 2606 OID 154835)
-- Dependencies: 2482 2469 2827
-- Name: fk862aeb15cdaf3227; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb15cdaf3227 FOREIGN KEY (station_id) REFERENCES installation_station(id);


--
-- TOC entry 2925 (class 2606 OID 154825)
-- Dependencies: 2807 2463 2482
-- Name: fk862aeb15f0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb15f0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2898 (class 2606 OID 154690)
-- Dependencies: 2459 2851 2479
-- Name: fk88d6a0c4bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT fk88d6a0c4bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2899 (class 2606 OID 154695)
-- Dependencies: 2456 2795 2459
-- Name: fk88d6a0c4e0347853; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT fk88d6a0c4e0347853 FOREIGN KEY (animal_id) REFERENCES animal(id);


--
-- TOC entry 2919 (class 2606 OID 154795)
-- Dependencies: 2477 2845 2476
-- Name: fk8e3f6e9c244fac71; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY person_system_role
    ADD CONSTRAINT fk8e3f6e9c244fac71 FOREIGN KEY (person_system_roles_id) REFERENCES person(id);


--
-- TOC entry 2918 (class 2606 OID 154790)
-- Dependencies: 2883 2493 2477
-- Name: fk8e3f6e9cbba8aa52; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY person_system_role
    ADD CONSTRAINT fk8e3f6e9cbba8aa52 FOREIGN KEY (system_role_id) REFERENCES system_role(id);


--
-- TOC entry 2897 (class 2606 OID 154685)
-- Dependencies: 2457 2799 2458
-- Name: fk8eb3adf914018681; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT fk8eb3adf914018681 FOREIGN KEY (type_id) REFERENCES animal_measurement_type(id);


--
-- TOC entry 2896 (class 2606 OID 154680)
-- Dependencies: 2829 2457 2471
-- Name: fk8eb3adf9e6ea591d; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT fk8eb3adf9e6ea591d FOREIGN KEY (unit_id) REFERENCES measurement_unit(id);


--
-- TOC entry 2911 (class 2606 OID 154755)
-- Dependencies: 2821 2467 2469
-- Name: fk902c2c2f35e870d3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station
    ADD CONSTRAINT fk902c2c2f35e870d3 FOREIGN KEY (installation_id) REFERENCES installation(id);


--
-- TOC entry 2902 (class 2606 OID 154710)
-- Dependencies: 2807 2461 2463
-- Name: fk90e7ca85f0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection
    ADD CONSTRAINT fk90e7ca85f0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2939 (class 2606 OID 154895)
-- Dependencies: 2491 2490 2875
-- Name: fk918a71f54f2dd3af; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f54f2dd3af FOREIGN KEY (treatment_type_id) REFERENCES surgery_treatment_type(id);


--
-- TOC entry 2941 (class 2606 OID 154905)
-- Dependencies: 2879 2492 2490
-- Name: fk918a71f56d95e296; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f56d95e296 FOREIGN KEY (type_id) REFERENCES surgery_type(id);


--
-- TOC entry 2938 (class 2606 OID 154890)
-- Dependencies: 2845 2490 2476
-- Name: fk918a71f57480ac7b; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f57480ac7b FOREIGN KEY (surgeon_id) REFERENCES person(id);


--
-- TOC entry 2940 (class 2606 OID 154900)
-- Dependencies: 2803 2490 2459
-- Name: fk918a71f5b5c10085; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f5b5c10085 FOREIGN KEY (release_id) REFERENCES animal_release(id);


--
-- TOC entry 2942 (class 2606 OID 154910)
-- Dependencies: 2807 2490 2463
-- Name: fk918a71f5ceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f5ceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2900 (class 2606 OID 154700)
-- Dependencies: 2460 2803 2459
-- Name: fka3b75543290ccda; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release_animal_measurement
    ADD CONSTRAINT fka3b75543290ccda FOREIGN KEY (animal_release_measurements_id) REFERENCES animal_release(id);


--
-- TOC entry 2901 (class 2606 OID 154705)
-- Dependencies: 2460 2457 2797
-- Name: fka3b7554af520388; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release_animal_measurement
    ADD CONSTRAINT fka3b7554af520388 FOREIGN KEY (animal_measurement_id) REFERENCES animal_measurement(id);


--
-- TOC entry 2943 (class 2606 OID 154915)
-- Dependencies: 2887 2493 2494
-- Name: fka47ecc86d06730af; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT fka47ecc86d06730af FOREIGN KEY (role_type_id) REFERENCES system_role_type(id);


--
-- TOC entry 2895 (class 2606 OID 154675)
-- Dependencies: 2456 2869 2489
-- Name: fkabc58dfccd365681; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fkabc58dfccd365681 FOREIGN KEY (sex_id) REFERENCES sex(id);


--
-- TOC entry 2930 (class 2606 OID 154850)
-- Dependencies: 2484 2485 2863
-- Name: fkadfe47ca50da4c63; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download_receiver_download_file
    ADD CONSTRAINT fkadfe47ca50da4c63 FOREIGN KEY (receiver_download_file_id) REFERENCES receiver_download_file(id);


--
-- TOC entry 2929 (class 2606 OID 154845)
-- Dependencies: 2483 2485 2861
-- Name: fkadfe47cafa360683; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download_receiver_download_file
    ADD CONSTRAINT fkadfe47cafa360683 FOREIGN KEY (receiver_download_download_files_id) REFERENCES receiver_download(id);


--
-- TOC entry 2907 (class 2606 OID 154735)
-- Dependencies: 2463 2465 2815
-- Name: fkb06b1e561c043beb; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e561c043beb FOREIGN KEY (model_id) REFERENCES device_model(id);


--
-- TOC entry 2906 (class 2606 OID 154730)
-- Dependencies: 2817 2466 2463
-- Name: fkb06b1e566b73e7c9; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e566b73e7c9 FOREIGN KEY (status_id) REFERENCES device_status(id);


--
-- TOC entry 2905 (class 2606 OID 154725)
-- Dependencies: 2851 2479 2463
-- Name: fkb06b1e56bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e56bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2920 (class 2606 OID 154800)
-- Dependencies: 2893 2478 2496
-- Name: fkc1735d892224770f; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY processed_upload_file
    ADD CONSTRAINT fkc1735d892224770f FOREIGN KEY (u_file_id) REFERENCES ufile(id);


--
-- TOC entry 2934 (class 2606 OID 154870)
-- Dependencies: 2495 2487 2889
-- Name: fkca0053ba4b0c3bc4; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT fkca0053ba4b0c3bc4 FOREIGN KEY (transmitter_type_id) REFERENCES transmitter_type(id);


--
-- TOC entry 2935 (class 2606 OID 154875)
-- Dependencies: 2463 2487 2807
-- Name: fkca0053baceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT fkca0053baceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2908 (class 2606 OID 154740)
-- Dependencies: 2465 2464 2811
-- Name: fkdcc4e40025f67029; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT fkdcc4e40025f67029 FOREIGN KEY (manufacturer_id) REFERENCES device_manufacturer(id);


--
-- TOC entry 2903 (class 2606 OID 154715)
-- Dependencies: 2461 2462 2805
-- Name: fkeb71eee0ac39c713; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection_tag
    ADD CONSTRAINT fkeb71eee0ac39c713 FOREIGN KEY (detection_tags_id) REFERENCES detection(id);


--
-- TOC entry 2904 (class 2606 OID 154720)
-- Dependencies: 2462 2463 2807
-- Name: fkeb71eee0ceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection_tag
    ADD CONSTRAINT fkeb71eee0ceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2921 (class 2606 OID 154805)
-- Dependencies: 2480 2479 2853
-- Name: fked904b19c34cf774; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project
    ADD CONSTRAINT fked904b19c34cf774 FOREIGN KEY (principal_investator_id) REFERENCES project_role(id);


--
-- TOC entry 2915 (class 2606 OID 154775)
-- Dependencies: 2474 2839 2473
-- Name: fkee60ce5a99b5ecd3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT fkee60ce5a99b5ecd3 FOREIGN KEY (organisation_id) REFERENCES organisation(id);


--
-- TOC entry 2914 (class 2606 OID 154770)
-- Dependencies: 2474 2845 2476
-- Name: fkee60ce5ae985cdb3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT fkee60ce5ae985cdb3 FOREIGN KEY (person_id) REFERENCES person(id);


--
-- TOC entry 2917 (class 2606 OID 154785)
-- Dependencies: 2839 2475 2473
-- Name: fkf3b978b499b5ecd3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT fkf3b978b499b5ecd3 FOREIGN KEY (organisation_id) REFERENCES organisation(id);


--
-- TOC entry 2916 (class 2606 OID 154780)
-- Dependencies: 2475 2479 2851
-- Name: fkf3b978b4bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT fkf3b978b4bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


-- Completed on 2011-06-07 13:51:51 EST

--
-- PostgreSQL database dump complete
--

