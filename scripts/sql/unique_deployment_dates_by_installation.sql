select distinct date_trunc('day', deploymentdatetime_timestamp), installation.name as installation_name, project.name as project_name from receiver_deployment
join installation_station on installation_station.id = station_id
join installation on installation.id = installation_station.installation_id
join project on project.id = installation.project_id
order by project_name, installation.name, date_trunc('day', deploymentdatetime_timestamp);

