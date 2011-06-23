--
-- PostgreSQL database dump
--

-- Dumped from database version 8.4.8
-- Dumped by pg_dump version 9.0.1
-- Started on 2011-06-15 15:11:17 EST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET SESSION AUTHORIZATION 'aatams';

--
-- TOC entry 7 (class 2615 OID 1634998)
-- Name: aatams; Type: SCHEMA; Schema: -; Owner: aatams
--

CREATE SCHEMA aatams;


SET search_path = aatams, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 2321 (class 1259 OID 1636339)
-- Dependencies: 7
-- Name: address; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE address (
    id bigint NOT NULL,
    version bigint NOT NULL,
    country character varying(255) NOT NULL,
    postcode character varying(255) NOT NULL,
    state character varying(255) NOT NULL,
    street_address character varying(255) NOT NULL,
    suburb_town character varying(255) NOT NULL
);


--
-- TOC entry 2322 (class 1259 OID 1636347)
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
-- TOC entry 2323 (class 1259 OID 1636352)
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
-- TOC entry 2324 (class 1259 OID 1636357)
-- Dependencies: 7
-- Name: animal_measurement_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_measurement_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2325 (class 1259 OID 1636364)
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
-- TOC entry 2326 (class 1259 OID 1636372)
-- Dependencies: 7
-- Name: animal_release_animal_measurement; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE animal_release_animal_measurement (
    animal_release_measurements_id bigint,
    animal_measurement_id bigint
);


--
-- TOC entry 2327 (class 1259 OID 1636375)
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
    sensor_unit character varying(255),
    uncalibrated_value real
);


--
-- TOC entry 2328 (class 1259 OID 1636383)
-- Dependencies: 7
-- Name: detection_tag; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE detection_tag (
    detection_tags_id bigint,
    tag_id bigint
);


--
-- TOC entry 2329 (class 1259 OID 1636386)
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
-- TOC entry 2330 (class 1259 OID 1636394)
-- Dependencies: 7
-- Name: device_manufacturer; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device_manufacturer (
    id bigint NOT NULL,
    version bigint NOT NULL,
    manufacturer_name character varying(255) NOT NULL
);


--
-- TOC entry 2331 (class 1259 OID 1636401)
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
-- TOC entry 2332 (class 1259 OID 1636408)
-- Dependencies: 7
-- Name: device_status; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE device_status (
    id bigint NOT NULL,
    version bigint NOT NULL,
    status character varying(255) NOT NULL
);


--
-- TOC entry 2363 (class 1259 OID 1636864)
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
-- TOC entry 2333 (class 1259 OID 1636415)
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
-- TOC entry 2334 (class 1259 OID 1636420)
-- Dependencies: 7
-- Name: installation_configuration; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation_configuration (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2335 (class 1259 OID 1636427)
-- Dependencies: 7 980
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
-- TOC entry 2336 (class 1259 OID 1636435)
-- Dependencies: 7
-- Name: installation_station_receiver; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE installation_station_receiver (
    installation_station_receivers_id bigint,
    receiver_id bigint
);


--
-- TOC entry 2337 (class 1259 OID 1636438)
-- Dependencies: 7
-- Name: measurement_unit; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE measurement_unit (
    id bigint NOT NULL,
    version bigint NOT NULL,
    unit character varying(255) NOT NULL
);


--
-- TOC entry 2338 (class 1259 OID 1636445)
-- Dependencies: 7
-- Name: mooring_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE mooring_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2339 (class 1259 OID 1636452)
-- Dependencies: 7
-- Name: organisation; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE organisation (
    id bigint NOT NULL,
    version bigint NOT NULL,
    fax_number character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    phone_number character varying(255) NOT NULL,
    postal_address_id bigint,
    status character varying(255) NOT NULL,
    street_address_id bigint NOT NULL
);


--
-- TOC entry 2340 (class 1259 OID 1636462)
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
-- TOC entry 2341 (class 1259 OID 1636467)
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
-- TOC entry 2342 (class 1259 OID 1636472)
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
-- TOC entry 2343 (class 1259 OID 1636480)
-- Dependencies: 7
-- Name: person_system_role; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE person_system_role (
    person_system_roles_id bigint,
    system_role_id bigint
);


--
-- TOC entry 2344 (class 1259 OID 1636483)
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
-- TOC entry 2345 (class 1259 OID 1636491)
-- Dependencies: 7
-- Name: project; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE project (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    principal_investigator_id bigint
);


--
-- TOC entry 2346 (class 1259 OID 1636501)
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
-- TOC entry 2347 (class 1259 OID 1636506)
-- Dependencies: 7
-- Name: project_role_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE project_role_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    display_name character varying(255) NOT NULL
);


--
-- TOC entry 2348 (class 1259 OID 1636513)
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
-- TOC entry 2349 (class 1259 OID 1636521)
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
-- TOC entry 2350 (class 1259 OID 1636526)
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
-- TOC entry 2351 (class 1259 OID 1636534)
-- Dependencies: 7
-- Name: receiver_download_receiver_download_file; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE receiver_download_receiver_download_file (
    receiver_download_download_files_id bigint,
    receiver_download_file_id bigint
);


--
-- TOC entry 2352 (class 1259 OID 1636537)
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
-- TOC entry 2353 (class 1259 OID 1636545)
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
-- TOC entry 2354 (class 1259 OID 1636553)
-- Dependencies: 7
-- Name: sensor_detection_sensor; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE sensor_detection_sensor (
    sensor_detection_sensors_id bigint,
    sensor_id bigint
);


--
-- TOC entry 2355 (class 1259 OID 1636556)
-- Dependencies: 7
-- Name: sex; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE sex (
    id bigint NOT NULL,
    version bigint NOT NULL,
    sex character varying(255) NOT NULL
);


--
-- TOC entry 2356 (class 1259 OID 1636563)
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
-- TOC entry 2357 (class 1259 OID 1636568)
-- Dependencies: 7
-- Name: surgery_treatment_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE surgery_treatment_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2358 (class 1259 OID 1636575)
-- Dependencies: 7
-- Name: surgery_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE surgery_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    type character varying(255) NOT NULL
);


--
-- TOC entry 2359 (class 1259 OID 1636582)
-- Dependencies: 7
-- Name: system_role; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE system_role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    role_type_id bigint NOT NULL
);


--
-- TOC entry 2360 (class 1259 OID 1636587)
-- Dependencies: 7
-- Name: system_role_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE system_role_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    display_name character varying(255) NOT NULL
);


--
-- TOC entry 2361 (class 1259 OID 1636594)
-- Dependencies: 7
-- Name: transmitter_type; Type: TABLE; Schema: aatams; Owner: aatams; Tablespace: 
--

CREATE TABLE transmitter_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    transmitter_type_name character varying(255) NOT NULL
);


--
-- TOC entry 2362 (class 1259 OID 1636601)
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
-- TOC entry 2655 (class 2606 OID 1636346)
-- Dependencies: 2321 2321
-- Name: address_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- TOC entry 2659 (class 2606 OID 1636356)
-- Dependencies: 2323 2323
-- Name: animal_measurement_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT animal_measurement_pkey PRIMARY KEY (id);


--
-- TOC entry 2661 (class 2606 OID 1636361)
-- Dependencies: 2324 2324
-- Name: animal_measurement_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement_type
    ADD CONSTRAINT animal_measurement_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2663 (class 2606 OID 1636363)
-- Dependencies: 2324 2324
-- Name: animal_measurement_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_measurement_type
    ADD CONSTRAINT animal_measurement_type_type_key UNIQUE (type);


--
-- TOC entry 2657 (class 2606 OID 1636351)
-- Dependencies: 2322 2322
-- Name: animal_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_pkey PRIMARY KEY (id);


--
-- TOC entry 2665 (class 2606 OID 1636371)
-- Dependencies: 2325 2325
-- Name: animal_release_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT animal_release_pkey PRIMARY KEY (id);


--
-- TOC entry 2667 (class 2606 OID 1636382)
-- Dependencies: 2327 2327
-- Name: detection_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY detection
    ADD CONSTRAINT detection_pkey PRIMARY KEY (id);


--
-- TOC entry 2671 (class 2606 OID 1636400)
-- Dependencies: 2330 2330
-- Name: device_manufacturer_manufacturer_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_manufacturer
    ADD CONSTRAINT device_manufacturer_manufacturer_name_key UNIQUE (manufacturer_name);


--
-- TOC entry 2673 (class 2606 OID 1636398)
-- Dependencies: 2330 2330
-- Name: device_manufacturer_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_manufacturer
    ADD CONSTRAINT device_manufacturer_pkey PRIMARY KEY (id);


--
-- TOC entry 2675 (class 2606 OID 1636407)
-- Dependencies: 2331 2331
-- Name: device_model_model_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT device_model_model_name_key UNIQUE (model_name);


--
-- TOC entry 2677 (class 2606 OID 1636405)
-- Dependencies: 2331 2331
-- Name: device_model_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT device_model_pkey PRIMARY KEY (id);


--
-- TOC entry 2669 (class 2606 OID 1636393)
-- Dependencies: 2329 2329
-- Name: device_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);


--
-- TOC entry 2679 (class 2606 OID 1636412)
-- Dependencies: 2332 2332
-- Name: device_status_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_status
    ADD CONSTRAINT device_status_pkey PRIMARY KEY (id);


--
-- TOC entry 2681 (class 2606 OID 1636414)
-- Dependencies: 2332 2332
-- Name: device_status_status_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY device_status
    ADD CONSTRAINT device_status_status_key UNIQUE (status);


--
-- TOC entry 2685 (class 2606 OID 1636424)
-- Dependencies: 2334 2334
-- Name: installation_configuration_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_configuration
    ADD CONSTRAINT installation_configuration_pkey PRIMARY KEY (id);


--
-- TOC entry 2687 (class 2606 OID 1636426)
-- Dependencies: 2334 2334
-- Name: installation_configuration_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_configuration
    ADD CONSTRAINT installation_configuration_type_key UNIQUE (type);


--
-- TOC entry 2683 (class 2606 OID 1636419)
-- Dependencies: 2333 2333
-- Name: installation_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT installation_pkey PRIMARY KEY (id);


--
-- TOC entry 2689 (class 2606 OID 1636434)
-- Dependencies: 2335 2335
-- Name: installation_station_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY installation_station
    ADD CONSTRAINT installation_station_pkey PRIMARY KEY (id);


--
-- TOC entry 2691 (class 2606 OID 1636442)
-- Dependencies: 2337 2337
-- Name: measurement_unit_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY measurement_unit
    ADD CONSTRAINT measurement_unit_pkey PRIMARY KEY (id);


--
-- TOC entry 2693 (class 2606 OID 1636444)
-- Dependencies: 2337 2337
-- Name: measurement_unit_unit_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY measurement_unit
    ADD CONSTRAINT measurement_unit_unit_key UNIQUE (unit);


--
-- TOC entry 2695 (class 2606 OID 1636449)
-- Dependencies: 2338 2338
-- Name: mooring_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY mooring_type
    ADD CONSTRAINT mooring_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2697 (class 2606 OID 1636451)
-- Dependencies: 2338 2338
-- Name: mooring_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY mooring_type
    ADD CONSTRAINT mooring_type_type_key UNIQUE (type);


--
-- TOC entry 2699 (class 2606 OID 1636461)
-- Dependencies: 2339 2339
-- Name: organisation_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT organisation_name_key UNIQUE (name);


--
-- TOC entry 2703 (class 2606 OID 1636466)
-- Dependencies: 2340 2340
-- Name: organisation_person_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT organisation_person_pkey PRIMARY KEY (id);


--
-- TOC entry 2701 (class 2606 OID 1636459)
-- Dependencies: 2339 2339
-- Name: organisation_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT organisation_pkey PRIMARY KEY (id);


--
-- TOC entry 2705 (class 2606 OID 1636471)
-- Dependencies: 2341 2341
-- Name: organisation_project_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT organisation_project_pkey PRIMARY KEY (id);


--
-- TOC entry 2707 (class 2606 OID 1636479)
-- Dependencies: 2342 2342
-- Name: person_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 2709 (class 2606 OID 1636490)
-- Dependencies: 2344 2344
-- Name: processed_upload_file_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY processed_upload_file
    ADD CONSTRAINT processed_upload_file_pkey PRIMARY KEY (id);


--
-- TOC entry 2711 (class 2606 OID 1636500)
-- Dependencies: 2345 2345
-- Name: project_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_name_key UNIQUE (name);


--
-- TOC entry 2713 (class 2606 OID 1636498)
-- Dependencies: 2345 2345
-- Name: project_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (id);


--
-- TOC entry 2715 (class 2606 OID 1636505)
-- Dependencies: 2346 2346
-- Name: project_role_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT project_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2717 (class 2606 OID 1636512)
-- Dependencies: 2347 2347
-- Name: project_role_type_display_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role_type
    ADD CONSTRAINT project_role_type_display_name_key UNIQUE (display_name);


--
-- TOC entry 2719 (class 2606 OID 1636510)
-- Dependencies: 2347 2347
-- Name: project_role_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY project_role_type
    ADD CONSTRAINT project_role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2721 (class 2606 OID 1636520)
-- Dependencies: 2348 2348
-- Name: receiver_deployment_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT receiver_deployment_pkey PRIMARY KEY (id);


--
-- TOC entry 2725 (class 2606 OID 1636533)
-- Dependencies: 2350 2350
-- Name: receiver_download_file_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_download_file
    ADD CONSTRAINT receiver_download_file_pkey PRIMARY KEY (id);


--
-- TOC entry 2723 (class 2606 OID 1636525)
-- Dependencies: 2349 2349
-- Name: receiver_download_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_download
    ADD CONSTRAINT receiver_download_pkey PRIMARY KEY (id);


--
-- TOC entry 2727 (class 2606 OID 1636544)
-- Dependencies: 2352 2352
-- Name: receiver_recovery_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT receiver_recovery_pkey PRIMARY KEY (id);


--
-- TOC entry 2729 (class 2606 OID 1636552)
-- Dependencies: 2353 2353
-- Name: sensor_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT sensor_pkey PRIMARY KEY (id);


--
-- TOC entry 2731 (class 2606 OID 1636560)
-- Dependencies: 2355 2355
-- Name: sex_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_pkey PRIMARY KEY (id);


--
-- TOC entry 2733 (class 2606 OID 1636562)
-- Dependencies: 2355 2355
-- Name: sex_sex_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_sex_key UNIQUE (sex);


--
-- TOC entry 2735 (class 2606 OID 1636567)
-- Dependencies: 2356 2356
-- Name: surgery_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT surgery_pkey PRIMARY KEY (id);


--
-- TOC entry 2737 (class 2606 OID 1636572)
-- Dependencies: 2357 2357
-- Name: surgery_treatment_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_treatment_type
    ADD CONSTRAINT surgery_treatment_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2739 (class 2606 OID 1636574)
-- Dependencies: 2357 2357
-- Name: surgery_treatment_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_treatment_type
    ADD CONSTRAINT surgery_treatment_type_type_key UNIQUE (type);


--
-- TOC entry 2741 (class 2606 OID 1636579)
-- Dependencies: 2358 2358
-- Name: surgery_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_type
    ADD CONSTRAINT surgery_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2743 (class 2606 OID 1636581)
-- Dependencies: 2358 2358
-- Name: surgery_type_type_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY surgery_type
    ADD CONSTRAINT surgery_type_type_key UNIQUE (type);


--
-- TOC entry 2745 (class 2606 OID 1636586)
-- Dependencies: 2359 2359
-- Name: system_role_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT system_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2747 (class 2606 OID 1636593)
-- Dependencies: 2360 2360
-- Name: system_role_type_display_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role_type
    ADD CONSTRAINT system_role_type_display_name_key UNIQUE (display_name);


--
-- TOC entry 2749 (class 2606 OID 1636591)
-- Dependencies: 2360 2360
-- Name: system_role_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY system_role_type
    ADD CONSTRAINT system_role_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2751 (class 2606 OID 1636598)
-- Dependencies: 2361 2361
-- Name: transmitter_type_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY transmitter_type
    ADD CONSTRAINT transmitter_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2753 (class 2606 OID 1636600)
-- Dependencies: 2361 2361
-- Name: transmitter_type_transmitter_type_name_key; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY transmitter_type
    ADD CONSTRAINT transmitter_type_transmitter_type_name_key UNIQUE (transmitter_type_name);


--
-- TOC entry 2755 (class 2606 OID 1636608)
-- Dependencies: 2362 2362
-- Name: ufile_pkey; Type: CONSTRAINT; Schema: aatams; Owner: aatams; Tablespace: 
--

ALTER TABLE ONLY ufile
    ADD CONSTRAINT ufile_pkey PRIMARY KEY (id);


--
-- TOC entry 2774 (class 2606 OID 1636699)
-- Dependencies: 2335 2336 2688
-- Name: fk2f7233ffd6ed9307; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station_receiver
    ADD CONSTRAINT fk2f7233ffd6ed9307 FOREIGN KEY (installation_station_receivers_id) REFERENCES installation_station(id);


--
-- TOC entry 2773 (class 2606 OID 1636694)
-- Dependencies: 2329 2668 2336
-- Name: fk2f7233fff0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station_receiver
    ADD CONSTRAINT fk2f7233fff0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2787 (class 2606 OID 1636764)
-- Dependencies: 2346 2718 2347
-- Name: fk37fff5dc51218487; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dc51218487 FOREIGN KEY (role_type_id) REFERENCES project_role_type(id);


--
-- TOC entry 2785 (class 2606 OID 1636754)
-- Dependencies: 2712 2346 2345
-- Name: fk37fff5dcbf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dcbf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2786 (class 2606 OID 1636759)
-- Dependencies: 2706 2342 2346
-- Name: fk37fff5dce985cdb3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project_role
    ADD CONSTRAINT fk37fff5dce985cdb3 FOREIGN KEY (person_id) REFERENCES person(id);


--
-- TOC entry 2775 (class 2606 OID 1636704)
-- Dependencies: 2654 2321 2339
-- Name: fk3a5300dabc5dddfd; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT fk3a5300dabc5dddfd FOREIGN KEY (street_address_id) REFERENCES address(id);


--
-- TOC entry 2776 (class 2606 OID 1636709)
-- Dependencies: 2321 2339 2654
-- Name: fk3a5300dac7e435; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation
    ADD CONSTRAINT fk3a5300dac7e435 FOREIGN KEY (postal_address_id) REFERENCES address(id);


--
-- TOC entry 2771 (class 2606 OID 1636684)
-- Dependencies: 2684 2334 2333
-- Name: fk796d5e3a18d65d27; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT fk796d5e3a18d65d27 FOREIGN KEY (configuration_id) REFERENCES installation_configuration(id);


--
-- TOC entry 2770 (class 2606 OID 1636679)
-- Dependencies: 2712 2345 2333
-- Name: fk796d5e3abf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation
    ADD CONSTRAINT fk796d5e3abf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2791 (class 2606 OID 1636784)
-- Dependencies: 2706 2342 2349
-- Name: fk79accd8b8e509d3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download
    ADD CONSTRAINT fk79accd8b8e509d3 FOREIGN KEY (downloader_id) REFERENCES person(id);


--
-- TOC entry 2800 (class 2606 OID 1636829)
-- Dependencies: 2666 2354 2327
-- Name: fk8165509916b1b072; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor_detection_sensor
    ADD CONSTRAINT fk8165509916b1b072 FOREIGN KEY (sensor_detection_sensors_id) REFERENCES detection(id);


--
-- TOC entry 2799 (class 2606 OID 1636824)
-- Dependencies: 2728 2353 2354
-- Name: fk81655099d7d8b793; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor_detection_sensor
    ADD CONSTRAINT fk81655099d7d8b793 FOREIGN KEY (sensor_id) REFERENCES sensor(id);


--
-- TOC entry 2795 (class 2606 OID 1636804)
-- Dependencies: 2678 2352 2332
-- Name: fk82de83e56b73e7c9; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e56b73e7c9 FOREIGN KEY (status_id) REFERENCES device_status(id);


--
-- TOC entry 2796 (class 2606 OID 1636809)
-- Dependencies: 2348 2352 2720
-- Name: fk82de83e579a96182; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e579a96182 FOREIGN KEY (deployment_id) REFERENCES receiver_deployment(id);


--
-- TOC entry 2794 (class 2606 OID 1636799)
-- Dependencies: 2352 2722 2349
-- Name: fk82de83e593fa40a2; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_recovery
    ADD CONSTRAINT fk82de83e593fa40a2 FOREIGN KEY (download_id) REFERENCES receiver_download(id);


--
-- TOC entry 2789 (class 2606 OID 1636774)
-- Dependencies: 2338 2348 2694
-- Name: fk862aeb1531eebdc; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb1531eebdc FOREIGN KEY (mooring_type_id) REFERENCES mooring_type(id);


--
-- TOC entry 2790 (class 2606 OID 1636779)
-- Dependencies: 2348 2688 2335
-- Name: fk862aeb15cdaf3227; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb15cdaf3227 FOREIGN KEY (station_id) REFERENCES installation_station(id);


--
-- TOC entry 2788 (class 2606 OID 1636769)
-- Dependencies: 2329 2348 2668
-- Name: fk862aeb15f0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_deployment
    ADD CONSTRAINT fk862aeb15f0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2759 (class 2606 OID 1636624)
-- Dependencies: 2712 2345 2325
-- Name: fk88d6a0c4bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT fk88d6a0c4bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2760 (class 2606 OID 1636629)
-- Dependencies: 2656 2325 2322
-- Name: fk88d6a0c4e0347853; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release
    ADD CONSTRAINT fk88d6a0c4e0347853 FOREIGN KEY (animal_id) REFERENCES animal(id);


--
-- TOC entry 2782 (class 2606 OID 1636739)
-- Dependencies: 2706 2343 2342
-- Name: fk8e3f6e9c244fac71; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY person_system_role
    ADD CONSTRAINT fk8e3f6e9c244fac71 FOREIGN KEY (person_system_roles_id) REFERENCES person(id);


--
-- TOC entry 2781 (class 2606 OID 1636734)
-- Dependencies: 2359 2744 2343
-- Name: fk8e3f6e9cbba8aa52; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY person_system_role
    ADD CONSTRAINT fk8e3f6e9cbba8aa52 FOREIGN KEY (system_role_id) REFERENCES system_role(id);


--
-- TOC entry 2758 (class 2606 OID 1636619)
-- Dependencies: 2323 2324 2660
-- Name: fk8eb3adf914018681; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT fk8eb3adf914018681 FOREIGN KEY (type_id) REFERENCES animal_measurement_type(id);


--
-- TOC entry 2757 (class 2606 OID 1636614)
-- Dependencies: 2323 2690 2337
-- Name: fk8eb3adf9e6ea591d; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_measurement
    ADD CONSTRAINT fk8eb3adf9e6ea591d FOREIGN KEY (unit_id) REFERENCES measurement_unit(id);


--
-- TOC entry 2772 (class 2606 OID 1636689)
-- Dependencies: 2333 2335 2682
-- Name: fk902c2c2f35e870d3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY installation_station
    ADD CONSTRAINT fk902c2c2f35e870d3 FOREIGN KEY (installation_id) REFERENCES installation(id);


--
-- TOC entry 2763 (class 2606 OID 1636644)
-- Dependencies: 2327 2668 2329
-- Name: fk90e7ca85f0bb6d33; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection
    ADD CONSTRAINT fk90e7ca85f0bb6d33 FOREIGN KEY (receiver_id) REFERENCES device(id);


--
-- TOC entry 2802 (class 2606 OID 1636839)
-- Dependencies: 2356 2357 2736
-- Name: fk918a71f54f2dd3af; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f54f2dd3af FOREIGN KEY (treatment_type_id) REFERENCES surgery_treatment_type(id);


--
-- TOC entry 2804 (class 2606 OID 1636849)
-- Dependencies: 2740 2356 2358
-- Name: fk918a71f56d95e296; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f56d95e296 FOREIGN KEY (type_id) REFERENCES surgery_type(id);


--
-- TOC entry 2801 (class 2606 OID 1636834)
-- Dependencies: 2356 2342 2706
-- Name: fk918a71f57480ac7b; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f57480ac7b FOREIGN KEY (surgeon_id) REFERENCES person(id);


--
-- TOC entry 2803 (class 2606 OID 1636844)
-- Dependencies: 2325 2356 2664
-- Name: fk918a71f5b5c10085; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f5b5c10085 FOREIGN KEY (release_id) REFERENCES animal_release(id);


--
-- TOC entry 2805 (class 2606 OID 1636854)
-- Dependencies: 2356 2329 2668
-- Name: fk918a71f5ceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY surgery
    ADD CONSTRAINT fk918a71f5ceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2761 (class 2606 OID 1636634)
-- Dependencies: 2325 2664 2326
-- Name: fka3b75543290ccda; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release_animal_measurement
    ADD CONSTRAINT fka3b75543290ccda FOREIGN KEY (animal_release_measurements_id) REFERENCES animal_release(id);


--
-- TOC entry 2762 (class 2606 OID 1636639)
-- Dependencies: 2326 2658 2323
-- Name: fka3b7554af520388; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal_release_animal_measurement
    ADD CONSTRAINT fka3b7554af520388 FOREIGN KEY (animal_measurement_id) REFERENCES animal_measurement(id);


--
-- TOC entry 2806 (class 2606 OID 1636859)
-- Dependencies: 2360 2748 2359
-- Name: fka47ecc86d06730af; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY system_role
    ADD CONSTRAINT fka47ecc86d06730af FOREIGN KEY (role_type_id) REFERENCES system_role_type(id);


--
-- TOC entry 2756 (class 2606 OID 1636609)
-- Dependencies: 2730 2355 2322
-- Name: fkabc58dfccd365681; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fkabc58dfccd365681 FOREIGN KEY (sex_id) REFERENCES sex(id);


--
-- TOC entry 2793 (class 2606 OID 1636794)
-- Dependencies: 2351 2350 2724
-- Name: fkadfe47ca50da4c63; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download_receiver_download_file
    ADD CONSTRAINT fkadfe47ca50da4c63 FOREIGN KEY (receiver_download_file_id) REFERENCES receiver_download_file(id);


--
-- TOC entry 2792 (class 2606 OID 1636789)
-- Dependencies: 2722 2349 2351
-- Name: fkadfe47cafa360683; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY receiver_download_receiver_download_file
    ADD CONSTRAINT fkadfe47cafa360683 FOREIGN KEY (receiver_download_download_files_id) REFERENCES receiver_download(id);


--
-- TOC entry 2768 (class 2606 OID 1636669)
-- Dependencies: 2676 2331 2329
-- Name: fkb06b1e561c043beb; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e561c043beb FOREIGN KEY (model_id) REFERENCES device_model(id);


--
-- TOC entry 2767 (class 2606 OID 1636664)
-- Dependencies: 2329 2678 2332
-- Name: fkb06b1e566b73e7c9; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e566b73e7c9 FOREIGN KEY (status_id) REFERENCES device_status(id);


--
-- TOC entry 2766 (class 2606 OID 1636659)
-- Dependencies: 2345 2329 2712
-- Name: fkb06b1e56bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fkb06b1e56bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


--
-- TOC entry 2783 (class 2606 OID 1636744)
-- Dependencies: 2754 2344 2362
-- Name: fkc1735d892224770f; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY processed_upload_file
    ADD CONSTRAINT fkc1735d892224770f FOREIGN KEY (u_file_id) REFERENCES ufile(id);


--
-- TOC entry 2797 (class 2606 OID 1636814)
-- Dependencies: 2353 2750 2361
-- Name: fkca0053ba4b0c3bc4; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT fkca0053ba4b0c3bc4 FOREIGN KEY (transmitter_type_id) REFERENCES transmitter_type(id);


--
-- TOC entry 2798 (class 2606 OID 1636819)
-- Dependencies: 2353 2668 2329
-- Name: fkca0053baceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY sensor
    ADD CONSTRAINT fkca0053baceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2769 (class 2606 OID 1636674)
-- Dependencies: 2672 2331 2330
-- Name: fkdcc4e40025f67029; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY device_model
    ADD CONSTRAINT fkdcc4e40025f67029 FOREIGN KEY (manufacturer_id) REFERENCES device_manufacturer(id);


--
-- TOC entry 2764 (class 2606 OID 1636649)
-- Dependencies: 2328 2327 2666
-- Name: fkeb71eee0ac39c713; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection_tag
    ADD CONSTRAINT fkeb71eee0ac39c713 FOREIGN KEY (detection_tags_id) REFERENCES detection(id);


--
-- TOC entry 2765 (class 2606 OID 1636654)
-- Dependencies: 2328 2668 2329
-- Name: fkeb71eee0ceab1a01; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY detection_tag
    ADD CONSTRAINT fkeb71eee0ceab1a01 FOREIGN KEY (tag_id) REFERENCES device(id);


--
-- TOC entry 2784 (class 2606 OID 1636749)
-- Dependencies: 2346 2714 2345
-- Name: fked904b19ab94ee16; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY project
    ADD CONSTRAINT fked904b19ab94ee16 FOREIGN KEY (principal_investigator_id) REFERENCES project_role(id);


--
-- TOC entry 2778 (class 2606 OID 1636719)
-- Dependencies: 2339 2340 2700
-- Name: fkee60ce5a99b5ecd3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT fkee60ce5a99b5ecd3 FOREIGN KEY (organisation_id) REFERENCES organisation(id);


--
-- TOC entry 2777 (class 2606 OID 1636714)
-- Dependencies: 2706 2340 2342
-- Name: fkee60ce5ae985cdb3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_person
    ADD CONSTRAINT fkee60ce5ae985cdb3 FOREIGN KEY (person_id) REFERENCES person(id);


--
-- TOC entry 2780 (class 2606 OID 1636729)
-- Dependencies: 2339 2341 2700
-- Name: fkf3b978b499b5ecd3; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT fkf3b978b499b5ecd3 FOREIGN KEY (organisation_id) REFERENCES organisation(id);


--
-- TOC entry 2779 (class 2606 OID 1636724)
-- Dependencies: 2341 2712 2345
-- Name: fkf3b978b4bf505a21; Type: FK CONSTRAINT; Schema: aatams; Owner: aatams
--

ALTER TABLE ONLY organisation_project
    ADD CONSTRAINT fkf3b978b4bf505a21 FOREIGN KEY (project_id) REFERENCES project(id);


-- Completed on 2011-06-15 15:11:24 EST

--
-- PostgreSQL database dump complete
--

