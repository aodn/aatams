WITH a AS (
SELECT common_name,
COUNT(DISTINCT transmitter_id) AS no_individuals_public,
SUM(no_detections) AS no_detections_public
FROM aatams_acoustic_detections_map
WHERE common_name IS NOT NULL
GROUP BY common_name)
SELECT v.phylum,
v.order_name,
v.common_name,
COUNT(DISTINCT v.project_name) AS no_projects,
COUNT(DISTINCT v.release_locality) AS no_localities,
COUNT(v.animal_release_id) AS no_releases,
SUM (CASE WHEN v.embargo_date > now() THEN 1 ELSE 0 END) AS no_embargo,
SUM (CASE WHEN v.no_detections = 0 THEN 0 ELSE 1 END) AS no_releases_with_detections,
CASE WHEN no_individuals_public IS NULL THEN 0 ELSE no_individuals_public END AS no_public_releases_with_detections,
max(v.embargo_date) AS latest_embargo_date,
SUM(v.no_detections) AS total_no_detections,
CASE WHEN no_detections_public IS NULL THEN 0 ELSE no_detections_public END AS no_detections_public,
min(v.first_detection) AS earliest_detection,
max(v.last_detection) AS latest_detection,
round(avg(v.coverage_duration)::numeric, 1) AS mean_coverage_duration
FROM aatams_acoustic_species_all_deployments_view v
FULL JOIN a ON v.common_name = a.common_name
GROUP BY v.phylum, v.order_name, v.common_name, no_individuals_public, no_detections_public
ORDER BY phylum, order_name, common_name;
