SELECT CASE WHEN substring(p.name,'AATAMS')='AATAMS' THEN 'IMOS funded and co-invested'
WHEN p.name = 'Coral Sea Nautilus tracking project'
OR p.name = 'Seven Gill Tracking in Coastal Tasmania'
OR substring(p.name,'Yongala')='Yongala'
OR p.name = 'Rowley Shoald reef shark tracking 2007'
OR p.name = 'Wenlock River Array, Gulf of Carpentaria' THEN 'IMOS Receiver Pool'
ELSE 'Fully Co-Invested' END AS funding_type,
p.id AS project_id,
p.name AS project_name,
i.name AS installation_name,
ist.name AS station_name,
COUNT(DISTINCT rd.id) AS no_deployments,
COUNT(vd.timestamp) AS no_detections,
min(rd.deploymentdatetime_timestamp) AS first_deployment_date,
max(rd.deploymentdatetime_timestamp) AS last_deployment_date,
min(vd.timestamp) AS start_date,
max(vd.timestamp) AS end_date,
round((date_part('days', max(vd.timestamp) - min(vd.timestamp)) + date_part('hours', max(vd.timestamp) - min(vd.timestamp))/24)::numeric, 1) AS coverage_duration,
ROUND(st_y(ist.location)::numeric,1) AS station_lat,
ROUND(st_x(ist.location)::numeric,1) AS station_lon,
ROUND(min(rd.depth_below_surfacem::integer),1) AS min_depth,
ROUND(max(rd.depth_below_surfacem::integer),1) AS max_depth
FROM project p
FULL JOIN installation i ON i.project_id = p.id
FULL JOIN installation_station ist ON ist.installation_id = i.id
FULL JOIN receiver_deployment rd ON rd.station_id = ist.id
FULL JOIN valid_detection vd ON vd.receiver_deployment_id = rd.id
GROUP BY p.id, p.name,i.name,ist.name,ist.location
ORDER BY project_name,installation_name,station_name;
