#### Function to extract tag, tag deployment and animal metadata from raw data files 

receiver_metadata_extraction <- function(data){

	receiver_metadata <- unique(data.frame(
		data$installation_name, 
		data$station_name, 
		data$receiver_name, 
		data$receiver_project_name, 
		data$tag_project_principal_investigator_names, 
		data$tag_project_principal_investigator_email_addresses,
		data$receiverinitialisationdatetime_timestamp, 
		data$receiverdeploymentdatetime_timestamp, 
		data$receiverrecoverydatetime_timestamp,
		data$longitude,
		data$latitude,
		data$receiver_depth))

	return(receiver_metadata);
	
}
