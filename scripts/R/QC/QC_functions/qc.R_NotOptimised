##### Function to test the validity of individual detections for tags detected more than once.

qc <- function(data){

		temporal_outcome[,1] <- as.character(data$transmitter_id);
		temporal_outcome[,2] <- as.character(data$installation_name);
		temporal_outcome[,3] <- as.character(data$station_name);
		temporal_outcome[,4] <- as.character(data$receiver_name);
		temporal_outcome[,5] <- as.character(data$tag_detection_timestamp);
		temporal_outcome[,6] <- data$longitude;
		temporal_outcome[,7] <- data$latitude;
		temporal_outcome[,8] <- data$sensor_value;
		temporal_outcome[,9] <- data$sensor_unit;

		##### False Detection Algorithm test
		sta_rec <- unique(data$installation_name); sta_rec <- sta_rec[order(sta_rec)];
		for (j in 1:length(sta_rec)){
			sel <- which(data$installation_name == sta_rec[j]);
			sub <- data[sel,];

			##### Calculate time differences between detections (in minutes)
			time_diff <- as.numeric(difftime(sub$tag_detection_timestamp[2:nrow(sub)],sub$tag_detection_timestamp[1:(nrow(sub)-1)],units='mins'));	
			temporal_outcome[sel,10] <- ifelse(length(which(time_diff <= 30)) > length(which((time_diff) >= 720)) & nrow(sub) > 1, 1, 2);
		}
		
		##### Distance and Velocity tests
		position <- data.frame(c(data$release_longitude[1], data$longitude),c(data$release_latitude[1],data$latitude)); colnames(position) <- c('longitude','latitude')
		pts2 <- cbind(x = position$longitude[2:nrow(position)], y = position$latitude[2:nrow(position)]); inst2 <- c(NA, as.character(data$installation_name))[2:length(c(NA, as.character(data$installation_name)))]; pts2_o <- pts2
		pts1 <- cbind(x = position$longitude[1:(nrow(position)-1)], y = position$latitude[1:(nrow(position)-1)]); inst1 <- c(NA, as.character(data$installation_name))[1:(length(c(NA, as.character(data$installation_name)))-1)]; pts1_o <- pts1
		
		dist <- shortest_dist(inst1,pts1,inst2,pts2, position, pts1_o, pts2_o);

		if (length(dist) == 1){

				timediff <- as.numeric(difftime(data$releasedatetime_timestamp, data$tag_detection_timestamp, tz = 'UTC', units='secs'));
				velocity <- (dist * 1000)/timediff;
				
				temporal_outcome[11] <- ifelse(velocity <= 10, 1, 2);
				temporal_outcome[12] <- ifelse(dist <= 1000, 1, 2);

			} else {

				dist_next <- c(dist[2:nrow(dist)], NA);

				time <- c(data$releasedatetime_timestamp[1], data$tag_detection_timestamp);
				timediff <- abs(as.numeric(difftime(time[1:(length(time)-1)], time[2:length(time)], tz = 'UTC', units='secs')));
				timediff_next <- c(timediff[2:length(timediff)], NA);
				
				##### Exception to overcome the fact that a same tag may be detected by two neighbouring stations at the exact same time, thus creating infinite velocity values
				timediff[which(timediff == 0)] <- 1; timediff_next[which(timediff_next == 0)] <- 1;
				velocity <- (dist * 1000)/timediff;	velocity_next <- (dist_next * 1000)/timediff_next;
				
				## Velocity test
				temporal_outcome[,11] <- ifelse(velocity > 10 & velocity_next > 10, 2, 1); 
				temporal_outcome[1,11] <- ifelse(velocity[1] > 10, 2, 1);
				temporal_outcome[nrow(data),11] <- ifelse(velocity[nrow(data)] > 10, 2, 1);
				
				## Distance test	
				temporal_outcome[,12] <- ifelse(dist > 1000 & dist_next > 1000, 2, 1); 
				temporal_outcome[1,12] <- ifelse(dist[1] > 1000, 2, 1);
				temporal_outcome[nrow(data),12] <- ifelse(dist[nrow(data)] > 1000, 2, 1);
		}

		
		##### Detection distribution test
		temporal_outcome[,13] <- ifelse(exists('shp_b') == F, 3, 1);
		if(exists('shp_b') == T) {
			out <- which(is.na(over(ll,shp_b)) == T);
			if(length(out) > 0) temporal_outcome[which(data$longitude %in% ll@coords[out,1] & data$latitude %in% ll@coords[out,2]),13] <- 2;
			rm(out);
		}

		##### Distance from release
		dist_r <- geodist(data$release_latitude[rep(1,nrow(data))],data$release_longitude[rep(1,nrow(data))],data$latitude,data$longitude,units='km');
		temporal_outcome[,14] <- ifelse(dist_r > 500, 2, 1);

		##### Release date before detection date
		release_timediff <- as.numeric(difftime(data$tag_detection_timestamp,data$releasedatetime_timestamp, tz = 'UTC', units = 'mins'))
		temporal_outcome[which(release_timediff >= (-720)),15] <- 1; ## -720 minutes, i.e. 12 hours, to take into account potential time zone differences 
		temporal_outcome[which(release_timediff < (-720)),15] <- 2;  ## -720 minutes, i.e. 12 hours, to take into account potential time zone differences
		
		##### Release location test
		if(exists('shp_b') == T) {
			temporal_outcome[,16] <- ifelse(dist[1] > 500 & length(which(is.na(over(ll_r,shp_b)) == T)) > 0, 2, 1)
		} else {
			temporal_outcome[,16] <- ifelse(dist[1] > 500, 2, 1);
		} 
	
		##### Detection QC
		ones <- as.numeric(rowSums(temporal_outcome[,c(10:14)] == 1));
		temporal_outcome[which(ones <= 2),17] <- 4;
		temporal_outcome[which(ones == 3),17] <- 3;		
		temporal_outcome[which(ones == 4),17] <- 2;
		temporal_outcome[which(ones == 5),17] <- 1;
		rm(ones);

	return(temporal_outcome);

}
