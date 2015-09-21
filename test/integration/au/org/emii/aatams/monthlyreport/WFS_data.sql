SELECT DISTINCT vd.detection_id AS detection_id,
	p.name AS project_name,
	i.name AS installation_name,
	ist.name AS station_name,
	vd.receiver_name,
	rd.bottom_depthm AS bottom_depth,
	sp.common_name AS common_name,
	vd.transmitter_id AS transmitter_id,
	ar.releasedatetime_timestamp AT TIME ZONE 'UTC' AS release_date,
	ar.release_locality AS release_locality,
	vd.timestamp AT TIME ZONE 'UTC' AS detection_date,
	ST_X(ist.location) AS longitude,
	ST_Y(ist.location) AS latitude,
	sex.sex AS sex,
	sp.scientific_name AS scientific_name,
	ist.location AS geom,
	'TRUE' AS detected
  FROM valid_detection vd
  LEFT JOIN receiver_deployment rd ON vd.receiver_deployment_id = rd.id
  LEFT JOIN installation_station ist ON ist.id = rd.station_id
  LEFT JOIN installation i ON i.id = ist.installation_id
  LEFT JOIN project p ON p.id = i.project_id  
  LEFT JOIN sensor on vd.transmitter_id = sensor.transmitter_id
  LEFT JOIN device d on sensor.tag_id = d.id
  LEFT JOIN surgery s ON s.tag_id = d.id
  LEFT JOIN animal_release ar ON ar.id = s.release_id
  LEFT JOIN animal a ON a.id = ar.animal_id
  LEFT JOIN species sp ON sp.id = a.species_id 
  LEFT JOIN sex ON a.sex_id = sex.id
	WHERE date_part('day', ar.embargo_date - now()) < 0 AND p.is_protected = FALSE AND
	ar.releasedatetime_timestamp AT TIME ZONE 'UTC' < vd.timestamp AT TIME ZONE 'UTC' AND
	sp.common_name IS NOT NULL AND sp.scientific_name IS NOT NULL
	ORDER BY transmitter_id, detection_date;