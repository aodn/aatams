rm(list=ls());
library(RPostgres); library(RPostgreSQL); library(plyr)
options(warn=2); Sys.setenv(TZ='GMT');

##### Load up configuration files and set working directory
# setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('config_db_dev.R'); # for db credentials, change to 'config_db.R' once all dev work completed
source('config_workdir.R'); # for working directories
setwd(data_dir); dir.create('Raw',showWarnings=F); unlink(dir('Processed',full.names=T)); dir.create('Processed',showWarnings=F); setwd('Raw');
if (length(dir()) > 0) {unlink(dir())} # Remove all files in Raw folder if some are already present

##### Get list of all registered tag deployments, associated PIs' details and corresponding species names. Note that range test tags are excluded, but embargoed and protected tags are included at this point
con <- dbConnect(RPostgres::Postgres(), host = server_address, dbname = db_name, user = db_user, port = db_port, password = db_password);

u <- dbSendStatement(con, "UPDATE aatams.animal_release SET release_location = ST_SetSRID(release_location, 4326), capture_location = ST_SetSRID(capture_location, 4326);"); dbClearResult(u); rm(u);

query <- paste("SELECT DISTINCT project.name AS tag_project_name, transmitter_id, surgery.tag_id, surgery.release_id,
	string_agg(su.name, ', ') AS tag_project_principal_investigator_names,
	string_agg(su.email_address, ', ') AS tag_project_principal_investigator_email_addresses,
	scientific_name, common_name, embargo_date, is_protected,
	CASE WHEN ST_X(ar.release_location) IS NULL THEN ST_X(ar.capture_location) ELSE ST_X(ar.release_location) END AS release_longitude,
	CASE WHEN ST_Y(ar.release_location) IS NULL THEN ST_Y(ar.capture_location) ELSE ST_Y(ar.release_location) END AS release_latitude,
	CASE WHEN ar.releasedatetime_timestamp IS NULL THEN ar.capturedatetime_timestamp AT TIME ZONE 'UTC' AT TIME ZONE capturedatetime_zone
		ELSE ar.releasedatetime_timestamp AT TIME ZONE 'UTC' AT TIME ZONE releasedatetime_zone END AS release_date,
	sensor.slope AS sensor_slope,
	sensor.intercept AS sensor_intercept,
	transmitter_type.transmitter_type_name,
	sensor.unit AS transmitter_unit,
	device_model.model_name AS transmitter_model_name,
	device.serial_number AS transmitter_serial_number,
	device.expected_life_time_days AS tag_expected_life_time_days,
	device.status AS tag_status
  	FROM aatams.sensor
  	JOIN aatams.surgery ON surgery.tag_id = sensor.tag_id
  	JOIN aatams.transmitter_type ON transmitter_type.id = sensor.transmitter_type_id
  	JOIN aatams.device ON device.id = sensor.tag_id
  	JOIN aatams.device_model ON device_model.id = device.model_id
  	JOIN aatams.project ON project.id = device.project_id
	JOIN aatams.project_role pr ON project.id = pr.project_id
	JOIN aatams.project_role_type prt ON prt.id = pr.role_type_id
	JOIN aatams.sec_user su ON su.id = pr.person_id
	JOIN aatams.animal_release ar ON ar.id = surgery.release_id
	JOIN aatams.animal a ON a.id = ar.animal_id
	JOIN aatams.species sp ON sp.id = a.species_id
  	WHERE prt.id = 8 AND ar.release_location IS NOT NULL and ar.capture_location IS NOT NULL AND transmitter_type_name != 'RANGE TEST'", ifelse(projects == '', " ", paste(" AND project.name IN (", projects ,") ", sep = '')), ifelse(species_list == '', " ", paste(" AND sp.common_name IN (", species_list ,") ", sep = '')),
  	"GROUP BY project.name, transmitter_id, surgery.tag_id, surgery.release_id, scientific_name, common_name, embargo_date, is_protected, releasedatetime_timestamp, capturedatetime_timestamp, release_location, capture_location,
  	slope, intercept, transmitter_type_name, model_name, unit, serial_number, expected_life_time_days, device.status, capturedatetime_zone, releasedatetime_zone", sep = '')
data <- dbGetQuery(con, query)
data <- data[order(data$transmitter_id, data$tag_id, data$release_id),]; print(paste('Total number of tags = ', nrow(data), sep = ''));

dbDisconnect(con);

##### Find for each species the corresponding ALA expert map shapefile
# Retrieve species list 
sp <- ddply(data, .(data$scientific_name, data$common_name), nrow)
colnames(sp) <- c('scientific_name', 'common_name', 'freq'); 

# Retrieve corresponding shapefiles
for (i in 1:nrow(sp)){
	spe <- gsub(' ', '_', sp[i,1]);
	if(length(grep('&',spe)) == 0) {
		if(length(dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe,dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)]) == 0) s <- data.frame(sp[i,1], NA) else 
			s <- data.frame(sp[i,1], dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe,dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)]);
	} else {
		spe <- strsplit(spe,'_&_')[[1]]; s <- matrix(ncol=2, nrow= length(spe));
		for (j in 1:length(spe)){
			s[j,1] <- as.character(sp[i,1]);
			# if(length(dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe[j],dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)]) == 0) next else sel[i] <- dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe[j],dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)]
		if(length(dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe[j],dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)]) == 0) s[j,2] <- NA else 
			s[j,2] <- dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe[j],dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)];
		}
	}
	
	colnames(s) <- c('scientific_name', 'folder_name');
	if(i == 1) fol_sel <- s else fol_sel <- rbind(fol_sel, s)
};

# Print diagnostic message for species missing expert map distribution
if (nrow(sp[which(sp[,1] %in% fol_sel[which(is.na(fol_sel[,2]) == T),1]),1:2]) > 0){
	for (i in 1:nrow(sp[which(sp[,1] %in% fol_sel[which(is.na(fol_sel[,2]) == T),1]),1:2])){
		if (i == 1){
			cat(paste('Missing ALA expert map distribution for :\n', 
			paste('\t', sp[which(sp[,1] %in% fol_sel[which(is.na(fol_sel[,2]) == T),1]),1:2][i,],collapse = ', '), '\n'));
		} else {
			cat(paste('\t', sp[which(sp[,1] %in% fol_sel[which(is.na(fol_sel[,2]) == T),1]),1:2][i,],collapse = ', '),'\n')
		}
	}
}


##### SQL query: for each tag deployment download all detections and metadata, then produce a CSV file
start_time <- Sys.time();
for (i in 1:nrow(data)){
	
	con <- dbConnect(RPostgres::Postgres(), host = server_address, dbname = db_name, user = db_user, port = db_port, password = db_password);
  	
	query <- paste("SELECT DISTINCT se.tag_id,
  		vd.transmitter_id AS transmitter_id,
	  	vd.release_id,
	  	vd.receiver_deployment_id,
	    CASE WHEN vd.scientific_name IS NULL THEN sp.name ELSE vd.scientific_name END AS scientific_name,
		vd.timestamp AT TIME ZONE 'UTC' AS tag_detection_timestamp,
		vd.installation_name,
		vd.station_station_name AS station_name,
		vd.receiver_name,
		CASE WHEN ST_Y(rd.location) IS NOT NULL THEN ST_Y(rd.location) ELSE vd.station_latitude END AS latitude,
		CASE WHEN ST_X(rd.location) IS NOT NULL THEN ST_X(rd.location) ELSE vd.station_longitude END AS longitude,
		vd.sensor_value,
		vd.sensor_unit
	  FROM aatams.valid_detection vd 
	  FULL JOIN aatams.sensor se ON vd.transmitter_id = se.transmitter_id
	  FULL JOIN aatams.animal_release ar ON ar.id = vd.release_id
	  FULL JOIN aatams.animal a ON a.id = ar.animal_id
	  FULL JOIN aatams.species sp ON sp.id = a.species_id
	  JOIN aatams.receiver_deployment rd ON rd.id = vd.receiver_deployment_id
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
	
	t_metadata <- dbGetQuery(con, query);
	t_metadata <- merge(data, t_metadata, by = 'release_id'); t_metadata <- data.frame(t_metadata[,3:4], t_metadata[,c(1:2,5:ncol(t_metadata))])
	if(nrow(t_metadata) > 1) {t_metadata$dual_sensor_tag <- TRUE} else {t_metadata$dual_sensor_tag <- FALSE}
	if (exists('tag_metadata') == F) {tag_metadata <- t_metadata} else {tag_metadata <- rbind(tag_metadata, t_metadata)}

  	## Extract receiver projects PIs	
  	rxr_pr <- unique(data.frame(rs$installation_name, rs$receiver_deployment_id)); colnames(rxr_pr) <- c('installation_name', 'receiver_deployment_id')
	query <- paste("SELECT rd.id AS receiver_deployment_id,
			i.name AS installation_name, 
			ist.name AS station_name,
			rec.receiver_name,
			p.name AS receiver_project_name, 
			string_agg(su.name, ', ') AS receiver_project_principal_investigator_names,
			string_agg(su.email_address, ', ') AS receiver_project_principal_investigator_email_addresses,				
			rd.initialisationdatetime_timestamp AT TIME ZONE 'UTC' AT TIME ZONE initialisationdatetime_zone AS initialisation_date,
			rd.deploymentdatetime_timestamp AT TIME ZONE 'UTC' AT TIME ZONE deploymentdatetime_zone AS deployment_date,
			rr.recoverydatetime_timestamp AT TIME ZONE 'UTC' AT TIME ZONE recoverydatetime_zone AS recovery_date,
			CASE WHEN ST_X(rd.location) IS NOT NULL THEN ST_X(rd.location) ELSE ST_X(ist.location) END AS deployment_longitude,
			CASE WHEN ST_Y(rd.location) IS NOT NULL THEN ST_Y(rd.location) ELSE ST_Y(ist.location) END AS deployment_latitude,
			rd.depth_below_surfacem AS receiver_depth			
			FROM aatams.installation i
			JOIN aatams.installation_station ist on ist.installation_id = i.id
			JOIN aatams.project p on p.id = i.project_id
			JOIN aatams.project_role pr ON p.id = pr.project_id
			JOIN aatams.project_role_type prt ON prt.id = pr.role_type_id
			JOIN aatams.sec_user su ON su.id = pr.person_id
			JOIN aatams.receiver_deployment rd ON rd.station_id = ist.id
            JOIN aatams.receiver_recovery rr ON rr.deployment_id = rd.id
            JOIN aatams.receiver rec ON rd.receiver_id = rec.id
			WHERE prt.id = 8 AND i.name IN ('", paste(unique(rxr_pr$installation_name), collapse = "','"), "') AND rd.id IN (", paste(rxr_pr$receiver_deployment_id, collapse = ","), ") 
			GROUP BY rd.id, rd.initialisationdatetime_timestamp, rd. initialisationdatetime_zone, rd.deploymentdatetime_timestamp, rd. deploymentdatetime_zone, rr.recoverydatetime_timestamp, rr. recoverydatetime_zone, rd.depth_below_surfacem, rd.location, ist.location, i.name, ist.name, p.name, rec.receiver_name;", sep='');
	
	rxr_metadata <- dbGetQuery(con, query);
	if (exists('receiver_metadata') == F) {receiver_metadata <- rxr_metadata} else {receiver_metadata <- rbind(receiver_metadata, rxr_metadata)}

	dbDisconnect(con);

	write.table(rs, paste(data$transmitter_id[i],'_',data$tag_id[i],'_',data$release_id[i],'.csv',sep=''),col.names=T,row.names=F,quote=F,sep=';');
}
end_time <- Sys.time();

##### Export tag metadata
tag_metadata <- tag_metadata[which(is.na(tag_metadata$transmitter_id) == F & duplicated(tag_metadata) == F),];
tag_metadata <- tag_metadata[order(tag_metadata$release_id, tag_metadata$transmitter_id, tag_metadata$tag_id),];
write.table(tag_metadata, '../Processed/TagMetadata.txt', row.names = F, col.names = T, sep= ';', quote = F);

##### Receiver metadata
receiver_metadata <- receiver_metadata[which(duplicated(receiver_metadata$receiver_deployment_id) == F),];
receiver_metadata <- receiver_metadata[order(receiver_metadata$installation_name, receiver_metadata$station_name, receiver_metadata$receiver_name, receiver_metadata$deployment_date),];
write.table(receiver_metadata, '../Processed/ReceiverMetadata.txt', row.names = F, col.names = T, sep= ';', quote = F);

##### Print diagnostic metrics
print(paste("Total number of raw data files = ", length(dir()), " data files. Number of registered tags without detection = ", nrow(data) - length(dir()), ' tags', sep = ''));
print(paste('Start time = ', start_time, '. End time = ', end_time, '. Number of minutes to extract and process all detections =  ', round(as.numeric(difftime(end_time, start_time, units = 'mins')), 1), sep = ''));