select count(project.name), project.name from valid_detection
join receiver_deployment on valid_detection.receiver_deployment_id = receiver_deployment.id
join installation_station on receiver_deployment.station_id = installation_station.id
join installation on installation_station.installation_id = installation.id
join project on installation.project_id = project.id
group by project.name
order by count(project.name) desc;

