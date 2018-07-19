rm(list=ls());
library(plyr, quietly= T); library(rgdal, quietly= T); library(sp, quietly= T); library(rgeos, quietly= T); library(mapdata); library(scales); library(gmt);

######################################
##### Load up configuration file
setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('QC_functions/ala_shp.R');
source('config_workdir.R'); # for working directories
setwd(paste(data_dir,'/Processed/', sep = ''));
dat <- read.csv(paste(wd,'/Outcomes/QC_OutputSummary.csv', sep = ''), header = T, sep= ';');
unlink(paste(wd,'/Outcomes/QC_Maps', sep = '')); dir.create(paste(wd,'/Outcomes/QC_Maps', sep = ''));

species <- ddply(dat, .(dat$scientific_name, dat$common_name), nrow); colnames(species) <- c('scientific_name', 'common_name', 'freq'); # Retrieve species list
rec <- read.csv('ReceiverMetadata.txt', header = T, sep= ';');

######################################
##### Produce map of detection geographical distribution for each species
start_time <- Sys.time();
for (i in 1:nrow(species)){
	
	## Prepare dataset and folder
	if(is.na(species[i,1]) == F) sel <- which(dat$scientific_name == species [i,1]);
	if(is.na(species[i,2]) == F) spe <- species[i,2] else spe <- species[i,1];
	dir.create(paste(wd,'/Outcomes/QC_Maps/', gsub(' ', '_',spe), sep=""))
	
	## Collate dataset
	for (j in 1:length(sel)){
# j<-which(dat$transmitter_id[sel] == 'A69-9004-1036')
		if (length(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]]) == 1) {s <- grep(paste(dat$transmitter_id[sel[j]],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());} else {
			s <- grep(paste(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]][1],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());
			if (length(s) == 0) s <- grep(paste(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]][2],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());
		}
		
		d <- read.csv(dir()[s], header=T, sep=';'); d$detection_timestamp <- strptime(as.character(d$detection_timestamp), '%Y-%m-%d %H:%M:%S', tz = 'UTC');
		d <- cbind(dat[s,], d, row.names = NULL); d <- d[, c(1:4, 32:37, 19:20, 46:47, 41)] ## Select following columns for plotting: transmitter_id, tag_id, release_id, scientific_name, installation_name, station_name, receiver_name, detection_timestamp, longitude, latitude, release_longitude, release_latitude, ReleaseLocation_QC, Detection_QC, Velocity_QC (not sure last one's needed though)
				
		##### 1st figure - Individual tag movements (only for dodgy tags?) - START
		releases <- unique(data.frame(d$release_longitude,d$release_latitude, d$ReleaseLocation_QC)); tmp <- d[which(d$Detection_QC <= 2),];
		dists <- geodist(d$latitude[2:nrow(d)], d$longitude[2:nrow(d)], d$latitude[1:(nrow(d) - 1)], d$longitude[1:(nrow(d) - 1)], units = 'km') * 1000; dists[which(is.nan(dists))] <- 0;
		times <- abs(as.numeric(difftime(d$detection_timestamp[2:nrow(d)], d$detection_timestamp[1:(nrow(d)-1)], units = 'secs'))); times[which(times == 0)] <- 1;
		v <- dists/times;
		dbd <- c(NA,FindBearingDelta(d$latitude[1:(nrow(d)-2)], d$longitude[1:(nrow(d)-2)], d$latitude[2:(nrow(d)-1)], d$longitude[2:(nrow(d)-1)], d$latitude[3:(nrow(d))], d$longitude[3:(nrow(d))]), NA) # 'Bearing delta' - minimum enclosed bearing between two successive bearing
			
		if (length(which(v > 10 & dists > 2000)) > 0){ ## Less sensitive than if any(tmp$Velocity_QC == 2) is added as the latter test doesn't account for spatially overlapping detections for receivers close to each other?
			png(file = paste(wd,'/Outcomes/QC_Maps/', gsub(' ', '_',spe), "/", unique(d$transmitter_id), ".png",sep=""), width = 1920, height = 800, units = "px", res=92, bg = "white");
				par(oma = c(0, 0, 2, 0));
				xr <- c(min(c(d$release_longitude,d$longitude), na.rm=T)-.1, max(c(d$release_longitude,d$longitude), na.rm=T) + .1);
				yr <- c(min(c(d$release_latitude,d$latitude), na.rm=T)-.1, max(c(d$release_latitude,d$latitude), na.rm=T) +.1);
				map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
				points(releases[,1:2], col = ifelse(releases[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(releases[,3] == 1, 1, 2.5));
				lines(tmp$longitude, tmp$latitude, col = 'red')
				points(rec$deployment_longitude, rec$deployment_latitude, col = 'dark red', pch = 4, cex = .5)
				mtext(paste(unique(d$transmitter_id), ', ', nrow(tmp), ' detections. Detection_QC <= 2', sep = ''), outer = TRUE, cex = 1.5)
			dev.off()

		print(which(v > 10 & dists > 2000)) # to delete afterwards - ideas: 1/ print out list of receivers associated with erroneous speeds			
		rec_er <- count(d[which(v > 10 & dists > 2000) + 1,c(1,5:7,9:10)]); rec_er <- rec_er[order(rec_er$freq, decreasing = T),];
		if(exists('rec_errors') == F) rec_errors <- rec_er else {rec_errors <- rbind(rec_errors, rec_er)}
		}
		##### 1st figure - Individual tag movements - END
			
		d <- count(d[,c(1:7,9:14)]); # Get rid of detection_timestamp for aggregating purposes
		
		if (j == 1) {data <- d} else data <- rbind(data,d);
	}
	releases <- unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))
	
	##### Load up ALA shapefile(s)
	sp <- as.character(unique(data$scientific_name));
	if(is.na(sp) == F) {
		shp_b <- ala_shp(sp)
		if(is.null(shp_b) == T) rm(shp_b);
	}
	
	##### 2nd figure - Spatial distribution of detections - START
	png(file = paste(wd,'/Outcomes/QC_Maps/',gsub(' ', '_',spe),".png",sep=""), width = 1920, height = 800, units = "px", res=92, bg = "white");
		par(mfrow=c(1,2), oma = c(0, 0, 2, 0));
		# 1st panel - data's spatial extent
			xr <- c(min(c(data$release_longitude,data$longitude), na.rm=T)-.2,max(c(data$release_longitude,data$longitude), na.rm=T) +.2);
			yr <- c(min(c(data$release_latitude,data$latitude), na.rm=T)-.2,max(c(data$release_latitude,data$latitude), na.rm=T) +.2);
			map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
			if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.25));
			if (length(which(data$Detection_QC < 3)) > 0) points(data$longitude[which(data$Detection_QC < 3)],data$latitude[which(data$Detection_QC < 3)], col = alpha('forestgreen', 0.7), pch = 19, cex = 1); # Plot valid detections
			if (length(which(data$Detection_QC >= 3)) > 0) points(data$longitude[which(data$Detection_QC >= 3)],data$latitude[which(data$Detection_QC >= 3)], col = 'red', pch = 3, cex = 3); # Plot invalid detections
			points(rec$deployment_longitude[which(!rec$station_name %in% unique(data$station_name))], rec$deployment_latitude[which(!rec$station_name %in% unique(data$station_name))], col = 'dark red', pch = 4, cex = .5); # Plot all receiver locations
			points(releases[,1:2], col = ifelse(releases[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(releases[,3] == 1, 1, 2.5));	 # Plot release locations
					
		# 2nd panel - shapefile's spatial extent
		if (exists('shp_b') == T) {
			xr <- c(min(c(data$release_longitude,data$longitude,shp_b@bbox[1,1]), na.rm=T)-2,max(c(data$release_longitude,data$longitude,shp_b@bbox[1,2]), na.rm=T) +2);
			yr <- c(min(c(data$release_latitude,data$latitude,shp_b@bbox[2,1]), na.rm=T)-2,max(c(data$release_latitude,data$latitude,shp_b@bbox[2,2]), na.rm=T) +2);
			map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
			if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.75));
			if (length(which(data$Detection_QC < 3)) > 0) points(data$longitude[which(data$Detection_QC < 3)],data$latitude[which(data$Detection_QC < 3)], col = alpha('forestgreen', 0.7), pch = 19, cex = 1); # Plot valid detections
			if (length(which(data$Detection_QC >= 3)) > 0) points(data$longitude[which(data$Detection_QC >= 3)],data$latitude[which(data$Detection_QC >= 3)], col = 'red', pch = 3, cex = 3); # Plot invalid detections
			points(releases[,1:2], col = ifelse(releases[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(releases[,3] == 1, 1, 2.5)); # Plot release locations
		}
		mtext(paste(spe, ', ', length(unique(data$transmitter_id)), ' tag IDs, ', sum(data$freq), ' detections. Detection_QC <= 2', sep = ''), outer = TRUE, cex = 1.5)
	dev.off();
	##### 2nd figure - Spatial distribution of detections - END

	if (exists('shp_b') == T) rm(shp_b);
};

st_rec <- unique(data.frame(rec_errors$installation_name, rec_errors$station_name, rec_errors$receiver_name)); colnames(st_rec) <- c('installation_name', 'station_name', 'receiver_name')
for (i in 1:nrow(st_rec)){
	sel <- rec_errors[which(rec_errors$installation_name == st_rec$installation_name[i] & rec_errors$station_name == st_rec$station_name[i] & rec_errors$receiver_name == st_rec$receiver_name[i]),]
	st_rec$nb_transmitters[i] <- length(unique(sel$transmitter_id))
	st_rec$freq[i] <- sum(sel$freq)
}
st_rec <- st_rec[order(st_rec$nb_transmitters, st_rec$freq, decreasing = T),]
##### Print diagnostic metrics
end_time <- Sys.time();
print(paste('Start time = ', start_time, '. End time = ', end_time, '. Number of hours to produce all maps =  ', round(as.numeric(difftime(end_time, start_time, units = 'hours')), 1), sep = ''));
