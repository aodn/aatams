WITH a AS (SELECT DISTINCT transmitter_id FROM valid_detection
  UNION ALL 
  SELECT DISTINCT transmitter_id FROM sensor),
 sub AS (SELECT DISTINCT transmitter_id FROM a)
  SELECT DISTINCT sub.transmitter_id, 
	d.id AS tag_id,
	su.release_id,
	p.name AS project_name,
	sp.common_name,
	CASE WHEN d.id IS NULL THEN FALSE ELSE TRUE END AS registered,
	p.is_protected AS protected,
	CASE WHEN capture_location IS NULL AND release_location IS NULL THEN FALSE ELSE TRUE END AS releaselocation_info,
	date(ar.embargo_date) AS embargo_date,
	COUNT(vd.timestamp) AS no_detections,
	date(min(vd.timestamp)) AS first_detection,
	date(max(vd.timestamp)) AS last_detection,
	round((date_part('days', max(vd.timestamp) - min(vd.timestamp)) + date_part('hours', max(vd.timestamp) - min(vd.timestamp))/24)::numeric, 1) AS coverage_duration
  FROM sub
  LEFT JOIN valid_detection vd ON sub.transmitter_id = vd.transmitter_id
  LEFT JOIN sensor s ON s.transmitter_id = sub.transmitter_id
  LEFT JOIN surgery su ON s.tag_id = su.tag_id
  LEFT JOIN device d ON d.id = s.tag_id OR d.id = su.tag_id
  LEFT JOIN animal_release ar ON ar.id = su.release_id
  LEFT JOIN project p ON p.id = ar.project_id
  LEFT JOIN animal a ON a.id = ar.animal_id
  LEFT JOIN species sp ON sp.id = a.species_id
	WHERE sub.transmitter_id != ''
	GROUP BY sub.transmitter_id, p.name, d.id, su.release_id, ar.animal_id, sp.common_name, p.is_protected, capturedatetime_timestamp, releasedatetime_timestamp, capture_location, release_location, ar.embargo_date
	ORDER BY registered, transmitter_id, common_name, tag_id, release_id;