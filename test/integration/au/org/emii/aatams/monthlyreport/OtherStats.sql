WITH tag_ids AS (
	  SELECT COUNT(DISTINCT transmitter_id) AS t,
		'no unique tag ids detected' AS statistics_type
	  FROM valid_detection),

tag_aatams_knows AS (
	  SELECT COUNT(*) AS t,
		'no unique registered tag ids' AS statistics_type
	  FROM device 
		WHERE class = 'au.org.emii.aatams.Tag'),
detected_tags_aatams_knows AS (
	  SELECT COUNT(DISTINCT sensor.transmitter_id) AS t,
		'no unique tag ids detected that aatams knows about' AS statistics_type
	  FROM valid_detection 
	  JOIN sensor ON valid_detection.transmitter_id = sensor.transmitter_id),
	  
tags_by_species AS (
	  SELECT COUNT(DISTINCT s.tag_id) AS t,
		'tags detected by species' AS statistics_type
	  FROM valid_detection vd
	  LEFT JOIN sensor on vd.transmitter_id = sensor.transmitter_id
	  LEFT JOIN device d on sensor.tag_id = d.id
	  JOIN surgery s ON s.tag_id = d.id)
  SELECT 'Species' AS type, t, statistics_type::text FROM tag_ids
UNION ALL
  SELECT 'Species' AS type, t, statistics_type::text FROM tag_aatams_knows
UNION ALL
  SELECT 'Species' AS type, t, statistics_type::text FROM detected_tags_aatams_knows
UNION ALL
  SELECT 'Species' AS type, t, statistics_type::text FROM tags_by_species;