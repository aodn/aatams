--liquibase formatted sql

--changeset ankit:baseline dbms:postgres
CREATE TABLE valid_code_map_transmitter_type
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  code_map_id bigint NOT NULL,
  transmitter_type_id bigint NOT NULL,
  CONSTRAINT valid_code_map_transmitter_type_pkey PRIMARY KEY (id),
  CONSTRAINT code_map FOREIGN KEY (code_map_id)
      REFERENCES code_map (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT transmitter_type FOREIGN KEY (transmitter_type_id)
      REFERENCES transmitter_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
