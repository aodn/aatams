SELECT funding_type,
COUNT(DISTINCT(project_name)) AS no_projects,
SUM(no_installations) AS no_installations,
SUM(no_stations) AS no_stations,
SUM(no_deployments) AS no_deployments,
SUM(no_releases) AS no_releases,
SUM(no_detections) AS no_detections
FROM aatams_acoustic_project_data_summary_view
GROUP BY funding_type
UNION ALL
SELECT 'TOTAL' AS funding_type,
COUNT(DISTINCT(project_name)) AS no_projects,
SUM(no_installations) AS no_installations,
SUM(no_stations) AS no_stations,
SUM(no_deployments) AS no_deployments,
SUM(no_releases) AS no_releases,
SUM(no_detections) AS no_detections
FROM aatams_acoustic_project_data_summary_view
ORDER BY funding_type ASC;
