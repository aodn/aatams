WITH a AS (SELECT project_id, COUNT(DISTINCT ar.id) AS no_releases FROM animal_release ar GROUP BY project_id)
SELECT funding_type,
project_name,
COUNT (DISTINCT(installation_name))::numeric AS no_installations,
COUNT (DISTINCT(station_name))::numeric AS no_stations,
SUM(no_deployments)::numeric AS no_deployments,
ROUND(AVG(a.no_releases),0) AS no_releases,
SUM(no_detections) AS no_detections,
min(first_deployment_date) AS earliest_deployment_date,
max(last_deployment_date) AS latest_deployment_date,
min(start_date) AS start_date,
max(end_date) AS end_date,
round((date_part('days', max(end_date) - min(start_date)) + date_part('hours', max(end_date) - min(start_date))/24)::numeric, 1) AS coverage_duration,
min(station_lat) AS min_lat,
max(station_lat) AS max_lat,
min(station_lon) AS min_lon,
max(station_lon) AS max_lon,
min(min_depth) AS min_depth,
max(max_depth) AS max_depth
FROM aatams_acoustic_project_all_deployments_view v
LEFT JOIN a ON a.project_id = v.project_id
GROUP BY funding_type,project_name
ORDER BY funding_type DESC,project_name;
