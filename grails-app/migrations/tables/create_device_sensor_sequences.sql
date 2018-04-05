--liquibase formatted sql

--changeset xhoenner:baseline dbms:postgres
create SEQUENCE device_id_seq;
select setval('"aatams"."device_id_seq"'::regclass, (select max("id") + 1 FROM "aatams"."device"));
ALTER TABLE device ALTER COLUMN id SET DEFAULT nextval('device_id_seq');
ALTER SEQUENCE device_id_seq OWNED BY device.id;

create SEQUENCE sensor_id_seq;
select setval('"aatams"."sensor_id_seq"'::regclass, (select max("id") + 1 FROM "aatams"."sensor"));
ALTER TABLE sensor ALTER COLUMN id SET DEFAULT nextval('sensor_id_seq');
ALTER SEQUENCE sensor_id_seq OWNED BY sensor.id;