select project.name as "project name", count(project.name) as "number of stations" 
from installation_station
join installation on installation_station.installation_id = installation.id
join project on installation.project_id = project.id
group by project.name
order by project.name;

