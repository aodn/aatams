SELECT ar.id AS animal_release_id,
p.name AS project_name,
s.phylum,
s.order_name,
s.common_name,
ar.release_locality,
date(ar.embargo_date) AS embargo_date,
COUNT(vd.timestamp) AS no_detections,
date(min(vd.timestamp)) AS first_detection,
date(max(vd.timestamp)) AS last_detection,
round((date_part('days', max(vd.timestamp) - min(vd.timestamp)) + date_part('hours', max(vd.timestamp) - min(vd.timestamp))/24)::numeric, 1) AS coverage_duration
FROM valid_detection vd
LEFT JOIN sensor on vd.transmitter_id = sensor.transmitter_id
LEFT JOIN device d on sensor.tag_id = d.id
JOIN surgery su ON su.tag_id = d.id
FULL JOIN animal_release ar ON su.release_id = ar.id
JOIN animal a ON ar.animal_id = a.id
JOIN species s ON a.species_id = s.id
JOIN project p ON p.id = ar.project_id
GROUP BY ar.id, p.name, s.phylum, s.order_name, s.common_name, ar.embargo_date
ORDER BY animal_release_id, phylum,order_name,common_name;
