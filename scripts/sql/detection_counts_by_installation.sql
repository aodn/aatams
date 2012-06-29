select 
	count(installation.name), installation.name as installation_name
	
from raw_detection
join receiver_deployment on receiver_deployment.id = raw_detection.receiver_deployment_id
join installation_station on installation_station.id = station_id
join installation on installation.id = installation_station.installation_id

group by installation.name
order by installation.name;

