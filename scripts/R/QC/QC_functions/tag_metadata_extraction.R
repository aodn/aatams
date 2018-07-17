#### Function to extract tag, tag deployment and animal metadata from raw data files 

tag_metadata_extraction <- function(data){

	tag_metadata[i,1] <- as.character(unique(data$transmitter_id));
	tag_metadata[i,2] <- as.character(unique(data$tag_id));
	tag_metadata[i,3] <- as.character(unique(data$release_id));
	tag_metadata[i,4] <- as.character(unique(data$tag_project_name));
	tag_metadata[i,5] <- as.character(unique(data$tag_project_principal_investigator_names));
	tag_metadata[i,6] <- as.character(unique(data$tag_project_principal_investigator_email_addresses));
	tag_metadata[i,7] <- as.character(unique(data$scientific_name));
	tag_metadata[i,8] <- as.character(unique(data$common_name));
	tag_metadata[i,9] <- as.character(unique(data$embargo_date));
	tag_metadata[i,10] <- as.character(unique(data$is_protected));
	tag_metadata[i,11] <- unique(round(data$release_longitude,4));
	tag_metadata[i,12] <- unique(round(data$release_latitude,4));
	tag_metadata[i,13] <- ifelse((is.na(strptime(as.character(data$releasedatetime_timestamp[1]),'%Y-%m-%d %H:%M:%S', tz= 'UTC')) == F), as.character(unique(data$releasedatetime_timestamp)), format(strptime(as.character(unique(data$releasedatetime_timestamp)),'%Y-%m-%d', tz = 'UTC'),'%Y-%m-%d %H:%M:%S'));
	tag_metadata[i,14] <- ifelse(length(unique(data$sensor_slope)) == 1, unique(data$sensor_slope), unique(data$sensor_slope)[which(is.na(unique(data$sensor_slope)) == F)]);
	tag_metadata[i,15] <- ifelse(length(unique(data$sensor_intercept)) == 1, unique(data$sensor_intercept), unique(data$sensor_intercept)[which(is.na(unique(data$sensor_intercept)) == F)]);
	tag_metadata[i,16] <- ifelse(length(as.character(unique(data$transmitter_type_name))) == 1, as.character(unique(data$transmitter_type_name)), paste(as.character(unique(data$transmitter_type_name)),collapse = ', '));
	tag_metadata[i,17] <- ifelse(length(unique(data$transmitter_unit)) == 1, as.character(unique(data$transmitter_unit)), as.character(unique(data$transmitter_unit)[which(is.na(unique(data$transmitter_unit)) == F)]));
	tag_metadata[i,18] <- as.character(unique(data$transmitter_model_name));
	tag_metadata[i,19] <- unique(data$transmitter_serial_number);
	tag_metadata[i,20] <- unique(data$tag_expected_life_time_days);
	tag_metadata[i,21] <- as.character(unique(data$tag_status));
	tag_metadata[i,22] <- as.character(unique(data$sex));
	tag_metadata[i,23] <- as.character(unique(data$measurement));

	return(tag_metadata);
	
}
