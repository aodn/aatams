WITH a AS (
  SELECT DISTINCT common_name
  FROM aatams_acoustic_detections_data),
  b AS (
  SELECT a.common_name,
	 COALESCE('#'||''||lpad(to_hex(trunc(random() * 16777215)::integer),6,'0')) AS colour
  FROM a),
  c AS (
  SELECT station_name, transmitter_id,
	 COUNT(DISTINCT detection_id) AS no_detections
  FROM aatams_acoustic_detections_data
	GROUP BY station_name, transmitter_id)
  SELECT project_name,
	installation_name,
	d.station_name,
	d.common_name,
	d.transmitter_id,
	release_locality,
	date(min(detection_date)) AS first_detection_date,
	date(max(detection_date)) AS last_detection_date,
	sex,
	scientific_name,
	replace(ST_AsEWKT(ST_SIMPLIFY(ST_MAKELINE(geom),0.01)), 'LINESTRING','MULTIPOINT')::geometry AS geom,
	b.colour AS colour,
	c.no_detections,
	bool_or(no_detections > 0) AS detected
  FROM aatams_acoustic_detections_data d
  LEFT JOIN b ON b.common_name = d.common_name
  LEFT JOIN c ON c.station_name = d.station_name AND c.transmitter_id = d.transmitter_id
  	GROUP BY project_name, installation_name, d.station_name, d.common_name, d.transmitter_id, release_locality, sex, scientific_name, colour, no_detections
UNION ALL
  SELECT p.name AS project_name, 
	i.name AS installation_name, 
	ist.name AS station_name,
	NULL AS common_name,
	NULL AS transmitter_id,
	NULL AS release_locality,
	NULL AS first_detection_date,
	NULL AS last_detection_date,
	NULL AS sex,
	NULL AS scientific_name,
	ist.location AS geom,
	'#FF0000' AS colour,
	0 AS no_detections,
	'FALSE' AS detected
  FROM installation_station ist
  LEFT JOIN installation i ON i.id = ist.installation_id
  LEFT JOIN project p ON p.id = i.project_id 
	WHERE ist.name NOT IN (SELECT DISTINCT station_name FROM aatams_acoustic_detections_data)
	ORDER BY detected, common_name, transmitter_id, first_detection_date;