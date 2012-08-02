-- Designed for SQLite 3.7.13 -------------------------------------------------
-- Begin schema ---------------------------------------------------------------

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS VS_schema
(
  table_name TEXT,
  major_ver INTEGER,
  minor_ver INTEGER,
  patch_ver INTEGER,
  extension_table TEXT,
  UNIQUE(table_name)
);
INSERT INTO VS_schema VALUES(NULL,1,0,0,NULL);
INSERT INTO VS_schema VALUES('VS_schema',1,0,0,NULL);

CREATE TABLE IF NOT EXISTS VS_device_model
(
  model INTEGER NOT NULL,
  class INTEGER NOT NULL,
  name TEXT,
  description TEXT,
  PRIMARY KEY(model)
);
INSERT INTO VS_schema VALUES('VS_device_model',1,0,0,NULL);

CREATE TABLE IF NOT EXISTS VS_recorder
(
  rowid INTEGER NOT NULL,
  model INTEGER NOT NULL,
  serial INTEGER NOT NULL,
  PRIMARY KEY(rowid),
  FOREIGN KEY(model) REFERENCES VS_device_model(model),
  UNIQUE(model,serial)
);
INSERT INTO VS_schema VALUES('VS_recorder',1,0,0,NULL);

CREATE TABLE IF NOT EXISTS VS_transmitter
(
  rowid INTEGER NOT NULL,
  tagid INTEGER NOT NULL,
  code_space INTEGER NOT NULL,
  frequency INTEGER NOT NULL,
  model INTEGER, -- usually NULL
  name TEXT, --usually NULL
  PRIMARY KEY(rowid)
);
INSERT INTO VS_schema VALUES('VS_transmitter',1,0,0,NULL);

CREATE TABLE IF NOT EXISTS VS_recording_context
(
  rowid INTEGER NOT NULL,
  rxr INTEGER NOT NULL,
  rxr_fw_ver INTEGER,
  PRIMARY KEY(rowid),
  FOREIGN KEY(rxr) REFERENCES VS_recorder(rowid),
  UNIQUE(rxr, rxr_fw_ver)
);
INSERT INTO VS_schema VALUES('VS_recording_context',1,0,0,NULL);

CREATE TABLE IF NOT EXISTS VS_offload_context
(
  rowid INTEGER NOT NULL,
  time INTEGER NOT NULL,
  uuid TEXT NOT NULL,
  offloader_app_name TEXT,
  offloader_app_ver INTEGER,
  offloader_module_name TEXT,
  offloader_module_ver INTEGER,
  PRIMARY KEY(rowid),
  UNIQUE(uuid)
);
INSERT OR REPLACE INTO VS_schema VALUES('VS_offload_context',1,1,0,'VS_offload_context_ex');

CREATE TABLE IF NOT EXISTS VS_offload_context_ex
(
  rowid INTEGER NOT NULL,
  phase_of_the_moon TEXT,
  FOREIGN KEY(rowid) REFERENCES VS_offload_context(rowid)
);
INSERT INTO VS_schema VALUES('VS_offload_context_ex',1,0,0,NULL);

CREATE TABLE IF NOT EXISTS VS_event_context
(
  rowid INTEGER NOT NULL,
  recording_context INTEGER NOT NULL,
  offload_context INTEGER NOT NULL,
  PRIMARY KEY(rowid),
  UNIQUE(recording_context, offload_context)
);
INSERT INTO VS_schema VALUES('VS_event_context',1,0,0,NULL);

CREATE TABLE IF NOT EXISTS VS_detection
(
  time INTEGER NOT NULL,
  txr INTEGER NOT NULL,
  rxr INTEGER NOT NULL,
  context INTEGER NOT NULL,
  PRIMARY KEY(time,txr,rxr),
  FOREIGN KEY(txr) REFERENCES VS_transmitter(rowid),
  FOREIGN KEY(rxr) REFERENCES VS_recorder(rowid),
  FOREIGN KEY(context) REFERENCES VS_event_context(rowid),
  UNIQUE(time,txr,rxr)
);
INSERT INTO VS_schema VALUES('VS_detection',1,0,0,NULL);


-- Begin data -----------------------------------------------------------------

INSERT INTO VS_device_model VALUES(514,1,'VR2W','VR2W Acoustic receiver');
INSERT INTO VS_recorder(rowid,model,serial) VALUES(1,514,12345);
INSERT INTO VS_recording_context VALUES(1,1,123);
INSERT INTO VS_offload_context VALUES(1,20120613123456000,'550e8400e29b41d4a716446655440000','VUE',123,'VR2W',456);
INSERT INTO VS_event_context VALUES(1,1,1);
INSERT INTO VS_transmitter(rowid,tagid,code_space,frequency) VALUES(1,123,1303,69000);
INSERT INTO VS_transmitter(rowid,tagid,code_space,frequency) VALUES(2,456,1303,69000);

INSERT INTO VS_detection VALUES (20120613123356000,1,1,1);
INSERT INTO VS_detection VALUES (20120613123456000,1,1,1);
INSERT INTO VS_detection VALUES (20120613123556000,2,1,1);

INSERT INTO VS_transmitter(rowid,tagid,code_space,frequency) VALUES(3,789,1303,69000);

INSERT INTO VS_detection VALUES (20120613123656000,3,1,1);
INSERT INTO VS_detection VALUES (20120613123756000,1,1,1);
INSERT INTO VS_detection VALUES (20120613123856000,2,1,1);


-- Output Example -----------------------------------------------------------------

--SELECT 'Date/Time','Tag ID';
--SELECT
--  (
--    substr(time,1,4)||'-'||substr(time,5,2)||'-'||substr(time,7,2)||' '||
--    substr(time,9,2)||':'||substr(time,11,2)||':'||substr(time,13,2)
--  ),
--  tagid
--FROM VS_Detection JOIN VS_Transmitter ON txr=rowid;
