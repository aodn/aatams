WITH vd AS (
  SELECT DISTINCT p.id AS project_id,
	p.name AS project_name,
	i.name AS installation_name,
	ist.name AS station_name,
	vd.transmitter_id, 
	CASE WHEN vd.receiver_deployment_id IS NULL THEN rd.id ELSE vd.receiver_deployment_id END AS receiver_deployment_id, 
	registered, 
	COUNT(timestamp) AS no_detections,
	min(timestamp) AS start_date,
	max(timestamp) AS end_date,
	round((date_part('days', max(timestamp) - min(timestamp)) + date_part('hours', max(timestamp) - min(timestamp))/24)::numeric/365.25, 1) AS coverage_duration
  FROM valid_detection vd
  LEFT JOIN aatams_acoustic_species_all_deployments_view a ON a.transmitter_id = vd.transmitter_id
  FULL JOIN receiver_deployment rd ON rd.id = vd.receiver_deployment_id
  FULL JOIN installation_station ist ON ist.id = rd.station_id
  FULL JOIN installation i ON i.id = ist.installation_id
  FULL JOIN project p ON p.id = i.project_id
	GROUP BY vd.transmitter_id, vd.receiver_deployment_id, registered,rd.id,p.id, p.name,i.name,ist.name)
  SELECT CASE WHEN substring(p.name,'AATAMS')='AATAMS' THEN 'IMOS funded and co-invested' 
	      WHEN p.name = 'Coral Sea  Nautilus tracking project' 
	      OR p.name = 'Seven Gill tracking in Coastal Tasmania' 
	      OR substring(p.name,'Yongala')='Yongala'
	      OR p.name = 'Rowley Shoals reef shark tracking 2007' 
	      OR p.name = 'Wenlock River Array, Gulf of Carpentaria' THEN 'IMOS Receiver Pool' 
	      ELSE 'Fully Co-Invested' END AS funding_type,
	p.id AS project_id,
	p.name AS project_name,
	i.name AS installation_name,
	ist.name AS station_name,
	COUNT(DISTINCT receiver_deployment_id) AS no_deployments,
	CASE WHEN SUM(no_detections) IS NULL THEN 0 ELSE SUM(no_detections) END AS no_detections,
	min(rd.deploymentdatetime_timestamp) AS first_deployment_date,
	max(rd.deploymentdatetime_timestamp) AS last_deployment_date,
	min(vd.start_date) AS start_date,
	max(vd.end_date) AS end_date,
	round((date_part('days', max(vd.end_date) - min(vd.start_date)) + date_part('hours', max(vd.end_date) - min(vd.start_date))/24)::numeric/365.25, 1) AS coverage_duration,
	ROUND(st_y(ist.location)::numeric,1) AS station_lat,
	ROUND(st_x(ist.location)::numeric,1) AS station_lon,
	ROUND(min(rd.depth_below_surfacem::integer),1) AS min_depth,
	ROUND(max(rd.depth_below_surfacem::integer),1) AS max_depth,
	p.is_protected,
	COUNT(DISTINCT CASE WHEN registered = FALSE THEN transmitter_id ELSE NULL END) AS no_unreg_transmitters,
	SUM(CASE WHEN registered = FALSE THEN no_detections ELSE 0 END) AS no_unreg_detections,
	COUNT(DISTINCT transmitter_id) AS no_transmitters
  FROM project p
  FULL JOIN installation i ON i.project_id = p.id
  FULL JOIN installation_station ist ON ist.installation_id = i.id
  FULL JOIN receiver_deployment rd ON rd.station_id = ist.id
  FULL JOIN vd ON vd.receiver_deployment_id = rd.id
	WHERE p.id IS NOT NULL
	GROUP BY p.id, p.name,i.name,ist.name,p.is_protected,ist.location
	ORDER BY project_name,installation_name,station_name;