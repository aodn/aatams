--liquibase formatted sql

--changeset ankit:baseline dbms:postgres
INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id)    
VALUES (1, 0, 33, 2);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (2, 0, 33, 3);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (3, 0, 129582657, 2);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (4, 0, 129582657, 3);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (5, 0, 35, 7);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (6, 0, 35, 8);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (7, 0, 35, 17);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (8, 0, 34, 7);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (9, 0, 34, 8);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (10, 0, 34, 17);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (11, 0, 36, 7);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (12, 0, 36, 8);

INSERT INTO valid_code_map_transmitter_type(id, version, transmitter_type_id, code_map_id) 
VALUES (13, 0, 36, 17);
