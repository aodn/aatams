rm(list=ls());
library(RPostgres); library(RPostgreSQL);
options(warn=2); Sys.setenv(TZ='GMT');

##### Load up configuration files and set working directory
setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('config_db_dev.R'); # for db credentials, change to 'config_db.R' once all dev work completed
source('config_workdir.R'); # for working directories
setwd(data_dir); dir.create('Raw',showWarnings=F); setwd('Raw');
if (length(dir()) > 0) {unlink(dir())} # Remove all files in Raw folder if some are already present

##### Get list of all tag deployments
con <- dbConnect(RPostgres::Postgres(), host = server_address, dbname = db_name, user = db_user, port = db_port, password = db_password);
query <- paste("SELECT DISTINCT project.name AS tag_project_name, transmitter_id, surgery.tag_id, surgery.release_id,
	string_agg(su.name, ', ') AS tag_project_principal_investigator_names,
	string_agg(su.email_address, ', ') AS tag_project_principal_investigator_email_addresses
  	FROM aatams.sensor
  	JOIN aatams.surgery ON surgery.tag_id = sensor.tag_id
  	JOIN aatams.transmitter_type ON transmitter_type.id = sensor.transmitter_type_id
  	JOIN aatams.device ON device.id = sensor.tag_id
  	JOIN aatams.project ON project.id = device.project_id
	JOIN aatams.project_role pr ON project.id = pr.project_id
	JOIN aatams.project_role_type prt ON prt.id = pr.role_type_id
	JOIN aatams.sec_user su ON su.id = pr.person_id
  	WHERE prt.id = 8 AND transmitter_type_name != 'RANGE TEST'", ifelse(projects == '', " ", paste(" AND project.name IN (", projects ,") ", sep = '')), 
  	"GROUP BY project.name, transmitter_id, surgery.tag_id, surgery.release_id", sep = '')
data <- dbGetQuery(con, query)

colna <- c("tag_id","transmitter_id","release_id","scientific_name","releasedatetime_timestamp","release_longitude","release_latitude", "detection_id",
"tag_detection_timestamp","installation_name","station_name","receiver_name","receiver_deployment_id","latitude","longitude","sensor_value");
dbDisconnect(con);

##### SQL query: for each tag deployment download all detections and metadata, then produce a CSV file
start_time <- Sys.time();
for (i in 1:nrow(data)){
	
	con <- dbConnect(RPostgres::Postgres(), host = server_address, dbname = db_name, user = db_user, port = db_port, password = db_password);
  	
	query <- paste("SELECT DISTINCT se.tag_id,
  		vd.transmitter_id AS transmitter_id,
	  	vd.release_id,
	    CASE WHEN vd.scientific_name IS NULL THEN sp.name ELSE vd.scientific_name END AS scientific_name,
	    sp.common_name,
		CASE WHEN ar.releasedatetime_timestamp IS NULL THEN ar.capturedatetime_timestamp AT TIME ZONE 'UTC' 
			ELSE ar.releasedatetime_timestamp AT TIME ZONE 'UTC' END AS releasedatetime_timestamp,
		CASE WHEN ST_X(ar.release_location) IS NULL THEN ST_X(ar.capture_location) ELSE ST_X(ar.release_location) END AS release_longitude,
		CASE WHEN ST_Y(ar.release_location) IS NULL THEN ST_Y(ar.capture_location) ELSE ST_Y(ar.release_location) END AS release_latitude,
		ar.embargo_date,
		p.is_protected,
		vd.timestamp AT TIME ZONE 'UTC' AS tag_detection_timestamp,
		vd.installation_name,
		vd.station_station_name AS station_name,
		vd.receiver_name,
		rd.initialisationdatetime_timestamp AT TIME ZONE 'UTC'AS receiverinitialisationdatetime_timestamp,
		rd.deploymentdatetime_timestamp AT TIME ZONE 'UTC' AS receiverdeploymentdatetime_timestamp,
		rr.recoverydatetime_timestamp AT TIME ZONE 'UTC' AS receiverrecoverydatetime_timestamp,
		CASE WHEN ST_Y(rd.location) IS NOT NULL THEN ST_Y(rd.location) ELSE vd.station_latitude END AS latitude,
		CASE WHEN ST_X(rd.location) IS NOT NULL THEN ST_X(rd.location) ELSE vd.station_longitude END AS longitude,
		rd.depth_below_surfacem AS receiver_depth,
		vd.sensor_value,
		vd.sensor_unit,
		se.slope AS sensor_slope,
		se.intercept AS sensor_intercept,
		tt.transmitter_type_name,
		dm.model_name AS transmitter_model_name,
		se.unit AS transmitter_unit,
		d.serial_number AS transmitter_serial_number,
		d.expected_life_time_days AS tag_expected_life_time_days,
		d.status AS tag_status
	  FROM aatams.valid_detection vd 
	  FULL JOIN aatams.sensor se ON vd.transmitter_id = se.transmitter_id
	  FULL JOIN aatams.device d ON d.id = se.tag_id
	  FULL JOIN aatams.device_model dm ON dm.id = d.model_id
	  FULL JOIN aatams.project p ON p.id = d.project_id
	  FULL JOIN aatams.transmitter_type tt ON tt.id = se.transmitter_type_id
	  FULL JOIN aatams.animal_release ar ON ar.id = vd.release_id
	  FULL JOIN aatams.animal a ON a.id = ar.animal_id
	  FULL JOIN aatams.species sp ON sp.id = a.species_id
	  JOIN aatams.receiver_deployment rd ON rd.id = vd.receiver_deployment_id
	  JOIN aatams.receiver_recovery rr ON rr.deployment_id = rd.id
	  WHERE vd.transmitter_id = '",as.character(data$transmitter_id[i]),"' AND se.tag_id ",ifelse(is.na(as.character(data$tag_id[i]))==T,'IS NULL', paste('=',as.character(data$tag_id[i]),sep=''))," AND vd.release_id ",ifelse(is.na(as.character(data$release_id[i]))==T,'IS NULL', paste('=',as.character(data$release_id[i]),sep='')),";",sep='');
	  
	rs <- dbGetQuery(con, query);
	
	## Go to next iteration if tag has not been detected
	if(nrow(rs)==0) next
	  	
  	## Extract measurement(s) and sex information
	query <- paste("SELECT ar.id AS release_id, 
			sex.sex,
			STRING_AGG(COALESCE(amt.type || ' = ' || am.value || ' ' || mu.unit), ', ') AS measurement 
			FROM animal_release ar
			LEFT JOIN animal a ON a.id = ar.animal_id
			LEFT JOIN sex ON a.sex_id = sex.id
			LEFT JOIN animal_measurement am ON am.release_id = ar.id
			LEFT JOIN animal_measurement_type amt ON amt.id = am.type_id
			LEFT JOIN measurement_unit mu ON mu.id = am.unit_id
			WHERE ar.id" , paste('=',as.character(data$release_id[i]),sep=''), " GROUP BY ar.id, sex;", sep='');
	
	m <- dbGetQuery(con, query);

  	## Extract receiver projects PIs	
  	rxr_pr <- unique(rs$installation_name)
	query <- paste("SELECT i.name AS installation_name, p.name AS receiver_project_name, 
			string_agg(su.name, ', ') AS receiver_project_principal_investigator_names,
			string_agg(su.email_address, ', ') AS receiver_project_principal_investigator_email_addresses
			FROM aatams.installation i
			JOIN aatams.project p on p.id = i.project_id
			JOIN aatams.project_role pr ON p.id = pr.project_id
			JOIN aatams.project_role_type prt ON prt.id = pr.role_type_id
			JOIN aatams.sec_user su ON su.id = pr.person_id
			WHERE prt.id = 8 AND i.name IN ('", paste(rxr_pr, collapse = "','"), "') GROUP BY i.name, p.name;", sep='');
	
	rxr_pis <- dbGetQuery(con, query);

	dbDisconnect(con);
				  		
	## Produce a CSV file for each tag deployment
	rs <- cbind(rs, m[,2:3])
	rs <- merge(rs, rxr_pis, by = 'installation_name');
	rs <- merge(rs, data, by = c('transmitter_id', 'tag_id','release_id'))
	write.table(rs, paste(data$transmitter_id[i],'_',data$tag_id[i],'_',data$release_id[i],'.csv',sep=''),col.names=T,row.names=F,quote=F,sep=';');
}
end_time <- Sys.time();

##### Print diagnostic metrics
print(paste("Total number of raw data files = ", length(dir()), " data files. Number of registered tags without detection = ", nrow(data) - length(dir()), ' tags', sep = ''));
print(paste('Start time = ', start_time, '. End time = ', end_time, '. Number of hours to extract and process all detections =  ', round(as.numeric(difftime(end_time, start_time, units = 'hours')), 1), sep = ''));