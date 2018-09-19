rm(list=ls());
library(plyr, quietly= T); library(rgdal, quietly= T); library(sp, quietly= T); library(rgeos, quietly= T); library(mapdata, quietly = TRUE); library(scales, quietly = TRUE); library(gmt, quietly = TRUE); library(grid, quietly = TRUE); library(gridBase, quietly = TRUE); library(gridExtra, quietly = TRUE);

######################################
##### Load up configuration file
# setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('config_workdir.R'); # for working directories
setwd(paste(data_dir,'/Processed/', sep = ''));
dat <- read.csv('TagMetadata.txt', header = T, sep= ';');
unlink(paste(wd,'/Outcomes/QC_Maps', sep = ''), recursive = T); dir.create(paste(wd,'/Outcomes/QC_Maps', sep = ''));

species <- ddply(dat, .(dat$scientific_name, dat$common_name), nrow); colnames(species) <- c('scientific_name', 'common_name', 'freq'); # Retrieve species list
rec <- read.csv('ReceiverMetadata.txt', header = T, sep= ';');

######################################
##### Produce map of detection geographical distribution for each species
start_time <- Sys.time();
for (i in 1:nrow(species)){
	
	## Prepare dataset and folder
	if(is.na(species[i,1]) == F) sel <- which(dat$scientific_name == species[i,1]);
	if(is.na(species[i,2]) == F) spe <- species[i,2] else spe <- species[i,1];
	
	## Collate datasets
	for (j in 1:length(sel)){
				
		## Load up dataset
		# if (length(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]]) == 1) {s <- grep(paste(dat$transmitter_id[sel[j]],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());} else {
			# s <- grep(paste(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]][1],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());
			# if (length(s) == 0) s <- grep(paste(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]][2],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());
		# }
		# d <- read.csv(dir()[s], header=T, sep=';'); 
		d <- read.csv(dir()[grep(dat$tag_id[sel[j]],dir())], header=T, sep=';'); 
		d$detection_timestamp <- strptime(as.character(d$detection_timestamp), '%Y-%m-%d %H:%M:%S', tz = 'UTC');

		## Select columns for plotting: transmitter_id, tag_id, release_id, scientific_name, installation_name, station_name, receiver_name, detection_timestamp, longitude, latitude, release_longitude, release_latitude, ReleaseLocation_QC, Detection_QC, Velocity_QC
		d <- cbind(dat[sel[j],], d, row.names = NULL); 	d <- d[, c(1:3, 7, 26:28, 30:32, 11:12, 41:42, 36)] ## Uncomment if no issue with precedent scripts, and comment out line below
		
		## Compute metrics
		releases <- unique(data.frame(d$release_longitude,d$release_latitude, d$ReleaseLocation_QC)); tmp <- d[which(d$Detection_QC <= 2),];
		dists_d <- c(NA, geodist(d$latitude[2:nrow(d)], d$longitude[2:nrow(d)], d$latitude[1:(nrow(d) - 1)], d$longitude[1:(nrow(d) - 1)], units = 'km') * 1000); dists_d[which(is.nan(dists_d))] <- 0;
		times_d <- c(NA, abs(as.numeric(difftime(d$detection_timestamp[2:nrow(d)], d$detection_timestamp[1:(nrow(d)-1)], units = 'secs')))); times_d[which(times_d == 0)] <- 1;
		v_d <- dists_d/times_d;
		if (j == 1) {vel <- v_d} else {vel <- c(vel, v_d)}

		## For tags with dubious movements - START
		dubious <- which(v_d > 10 & dists_d > 2000);
		if (length(dubious) > 0){
			d_d <- cbind(d[, 5:8], round(d$longitude, 2), round(d$latitude, 2), round(dists_d/1000, 1), round(v_d, 1)); colnames(d_d)[5:8] <- c('long','lat','distprev_km', 'v_ms');
			dir.create(paste(wd,'/Outcomes/QC_Maps/', gsub(' ', '_', species$common_name[i]), sep=""), showWarnings = F);
			
			for (kk in 1:length(dubious)){
				dub <- dubious[kk] + c(-2: 2)
				if(kk == 1) dubs <- dub else {dubs <- c(dubs, dub)}
			}; dubs <- unique(dubs);
			
			trip_id <- 1; d$trip_id[1] <- trip_id
			for (kk in 2:nrow(d)){
				if(dists_d[kk] > 0) {d$trip_id[kk] <- trip_id + 1; trip_id <- trip_id + 1;} else {d$trip_id[kk] <- trip_id}
			}
			
			## Summary per trip
			for (kk in 1:length(unique(d$trip_id))){
				subsel <- which(d$trip_id == unique(d$trip_id)[kk]);
				dat.sum <- data.frame(unique(d[subsel,1:3]), as.character(unique(d$installation_name[subsel])), as.character(unique(d$station_name[subsel])),  as.character(unique(d$receiver_name[subsel])), d$detection_timestamp[min(subsel)], d$detection_timestamp[max(subsel)], unique(d$longitude[subsel]), unique(d$latitude[subsel]), nrow(d[subsel,]), row.names = NULL)
				if(kk == 1) dat.sum.all <- dat.sum else dat.sum.all <- rbind(dat.sum.all, dat.sum)
			}
			colnames(dat.sum.all) <- c('transmitter_id','tag_id','release_id','installation_name','station_name','receiver_name','date_arrival','date_departure','longitude','latitude','nb_locations');
			
			dists <- c(NA, geodist(dat.sum.all$latitude[2:nrow(dat.sum.all)], dat.sum.all$longitude[2:nrow(dat.sum.all)], dat.sum.all$latitude[1:(nrow(dat.sum.all) - 1)], dat.sum.all$longitude[1:(nrow(dat.sum.all) - 1)], units = 'km') * 1000); dists[which(is.nan(dists))] <- 0;
			times <- c(NA, abs(as.numeric(difftime(dat.sum.all$date_arrival[2:nrow(dat.sum.all)], dat.sum.all$date_departure[1:(nrow(dat.sum.all)-1)], units = 'secs')))); times[which(times == 0)] <- 1;
			v <- dists/times;
			rec_er <- count(dat.sum.all[which(v > 10 & dists > 2000),c(1,4:6)])
			if(exists('rec_errors') == F) rec_errors <- rec_er else {rec_errors <- rbind(rec_errors, rec_er)}
			
			## 1st figure - Individual tag movements - START
			png(file = paste(wd,'/Outcomes/QC_Maps/', gsub(' ', '_',species$common_name[i]), "/", gsub('/', '_', unique(d$transmitter_id)), ".png",sep=""), width = 1920, height = 800, units = "px", res=92, bg = "white");
				par(mfrow=c(1,2), oma = c(0, 0, 2, 0));
				diff_long <- .1 * (max(c(d$release_longitude,d$longitude), na.rm=T) - min(c(d$release_longitude,d$longitude), na.rm=T));
				diff_lat <- .1 * (max(c(d$release_latitude,d$latitude), na.rm=T) - min(c(d$release_latitude,d$latitude), na.rm=T));
				xr <- c(min(c(d$release_longitude,d$longitude), na.rm=T) - diff_long, max(c(d$release_longitude,d$longitude), na.rm=T) + diff_long);
				yr <- c(min(c(d$release_latitude,d$latitude), na.rm=T) - diff_lat, max(c(d$release_latitude,d$latitude), na.rm=T) + diff_lat);
				
				# 1st panel
				map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
				points(releases[,1:2], col = ifelse(releases[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(releases[,3] == 1, 1, 2.5));
				lines(dat.sum.all$longitude, dat.sum.all$latitude, col = 'red')
				points(dat.sum.all$longitude[which(v > 10 & dists > 2000)], dat.sum.all$latitude[which(v > 10 & dists > 2000)], pch = 1, cex = 2, col = 'orange', lwd = 2)
				text(dat.sum.all$longitude[which(v > 10 & dists > 2000)], dat.sum.all$latitude[which(v > 10 & dists > 2000)], labels = as.character(dat.sum.all$station_name[which(v > 10 & dists > 2000)]), pos = 3)
				points(rec$deployment_longitude, rec$deployment_latitude, col = 'dark red', pch = 4, cex = .5)
				legend('topleft', legend = c('Dubious receiver(s)', 'Tag release location', 'Receiver location'), col = c('orange', 'blue', 'dark red'), pch = c(1, 4, 4), cex = 1, bg="transparent", bty = 'n')
				
				# 2nd panel
				frame()
				vps <- baseViewports()
				pushViewport(vps$inner, vps$figure, vps$plot)
				grob <-  tableGrob(d_d[head(dubs, 20),])  
				grid.draw(grob)
				
				mtext(paste(species$scientific_name[i], ', ', unique(d$transmitter_id), ', ', nrow(tmp), ' detections', sep = ''), outer = TRUE, cex = 1.5)
			dev.off()
			## 1st figure - Individual tag movements - END		
		}
		## For tags with dubious movements - START

		d <- count(d[,c(1:7,9:14)]); # Get rid of detection_timestamp for aggregating purposes
		if (j == 1) {data <- d} else data <- rbind(data,d);
	}
	releases <- unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))
	
	##### Load up ALA shapefile(s) - START
	sp <- as.character(species$scientific_name[i]); spe <- gsub(' ', '_', sp);		
	if(length(grep('&',spe)) == 0) {
		if(length(dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe,dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)]) == 0) s <- NA else 
			s <- dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe,dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)];
	} else {
		spe <- strsplit(spe,'_&_')[[1]]; s <- matrix(ncol=1, nrow= length(spe));
		for (j in 1:length(spe)){
		if(length(dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe[j],dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)]) == 0) s[j] <- NA else 
			s[j] <- dir(paste(wd,'/ALA_Shapefile/', sep =''))[which(grepl(spe[j],dir(paste(wd,'/ALA_Shapefile/', sep =''))) == T)];
		}
	}

	if (length(s) == 1){
		if (is.na(s) == F){
		shp <- readOGR(dsn = path.expand(paste(paste(wd,'/ALA_Shapefile/', sep =''),s,'/',gsub('_SHP','',s),'_Shapefile.shp',sep='')), paste(gsub('_SHP','',s),'_Shapefile',sep=''), verbose = F);
		shp_b <- spTransform(shp, CRS=CRS("+proj=merc +ellps=GRS80"));
		shp_b <- gBuffer(shp_b, width= 500000); ## 500 km buffer area
		shp_b <- gSimplify(shp_b, tol=0.01, topologyPreserve=TRUE);
		shp_b <- spTransform(shp_b, CRS=CRS("+proj=longlat +ellps=WGS84"));
		}
	}
	
	if (length(s) > 1){
		if (is.na(s[1]) == F) shp_1 <- readOGR(dsn = path.expand(paste(paste(wd,'/ALA_Shapefile/', sep =''),s[1],'/',gsub('_SHP','',s[1]),'_Shapefile.shp',sep='')), paste(gsub('_SHP','',s[1]),'_Shapefile',sep=''), verbose = F);
		if (is.na(s[2]) == F) shp_2 <- readOGR(dsn = path.expand(paste(paste(wd,'/ALA_Shapefile/', sep =''),s[2],'/',gsub('_SHP','',s[2]),'_Shapefile.shp',sep='')), paste(gsub('_SHP','',s[2]),'_Shapefile',sep=''), verbose = F);
		if (is.na(s[1]) == T & is.na(s[2]) == F) shp <- shp_2 else if(is.na(s[1]) == F & is.na(s[2]) == T) shp <- shp_1;
		if (is.na(s[1]) == F & is.na(s[2]) == F) {
			shp_1 <- spTransform(shp_1, CRS=CRS("+proj=merc +ellps=GRS80")); shp_2 <- spTransform(shp_2, CRS=CRS("+proj=merc +ellps=GRS80"));
			shp_1 <- gBuffer(shp_1, width=0, byid =T); shp_2 <- gBuffer(shp_2, width=0, byid =T);
			shp <- gUnion(shp_1,shp_2);
		}
		shp <- spTransform(shp, CRS=CRS("+proj=longlat +ellps=WGS84"));
		shp_b <- spTransform(shp, CRS=CRS("+proj=merc +ellps=GRS80"));
		shp_b <- gBuffer(shp_b, width= 500000); ## 500 km buffer area
		shp_b <- gSimplify(shp_b, tol=0.01, topologyPreserve=TRUE);
		shp_b <- spTransform(shp_b, CRS=CRS("+proj=longlat +ellps=WGS84"));
		if (exists('shp_1') == T) rm(shp_1); if (exists('shp_2') == T) rm(shp_2);
	}
	##### Load up ALA shapefile(s) - END	
	
	##### 2nd figure - Spatial distribution of detections - START
	binned_detects <- data.frame(data$freq, bin=cut(data$freq, unique(quantile(data$freq, seq(0,1,.25))), include.lowest=TRUE))
	data$binned_detections <- as.numeric(binned_detects$bin)
	png(file = paste(wd,'/Outcomes/QC_Maps/',gsub(' ', '_', species$common_name[i]),".png",sep=""), width = 1920, height = 800, units = "px", res=92, bg = "white");
		par(mfrow=c(1,2), oma = c(0, 0, 2, 0));
		## First panel - Australia's spatial extent
			map('worldHires',xlim=c(100, 165), ylim = c(-45, -5), fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
			if (exists('shp') == T) plot(shp, add = T, col = alpha('dark blue', 0.25)); if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.25));
			points(rec$deployment_longitude[which(!rec$station_name %in% unique(data$station_name))], rec$deployment_latitude[which(!rec$station_name %in% unique(data$station_name))], col = 'dark red', pch = 4, cex = .5); # Plot receiver locations
			if (length(which(data$Detection_QC == 1)) > 0) points(data$longitude[which(data$Detection_QC == 1)],data$latitude[which(data$Detection_QC == 1)], col = alpha('#1A9641', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 1)], pch = 19); # Plot valid detections
			if (length(which(data$Detection_QC == 2)) > 0) points(data$longitude[which(data$Detection_QC == 2)],data$latitude[which(data$Detection_QC == 2)], col = alpha('#A6D96A', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 2)], pch = 19); # Plot invalid detections
			if (length(which(data$Detection_QC == 3)) > 0) points(data$longitude[which(data$Detection_QC == 3)],data$latitude[which(data$Detection_QC == 3)], col = alpha('#FDAE61', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 3)], pch = 19); # Plot valid detections
			if (length(which(data$Detection_QC == 4)) > 0) points(data$longitude[which(data$Detection_QC == 4)],data$latitude[which(data$Detection_QC == 4)], col = alpha('#D7191C', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 4)], pch = 19); # Plot invalid detections
			points(releases[,1:2], col = 'blue', pch = 4, cex = 2, lwd = 2); # Plot release locations
			legend('bottomleft', legend = c('QC flag 1','QC flag 2', 'QC flag 3', 'QC flag 4', 'Tag release location', 'Receiver location'), col = c('#1A9641', '#A6D96A', '#FDAE61', '#D7191C', 'blue', 'dark red'), pch = c(19, 19, 19, 19, 4, 4), cex = 1, bg="transparent", bty = 'n')
					
		## Second panel - data's spatial extent + 10 %
			diff_long <- .1 * (max(c(data$release_longitude,data$longitude), na.rm=T) - min(c(data$release_longitude,data$longitude), na.rm=T));
			diff_lat <- .1 * (max(c(data$release_latitude,data$latitude), na.rm=T) - min(c(data$release_latitude,data$latitude), na.rm=T));
			xr <- c(min(c(data$release_longitude,data$longitude), na.rm=T) - diff_long,max(c(data$release_longitude,data$longitude), na.rm=T) + diff_long);
			yr <- c(min(c(data$release_latitude,data$latitude), na.rm=T) - diff_lat,max(c(data$release_latitude,data$latitude), na.rm=T) + diff_lat);
			map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
			if (exists('shp') == T) plot(shp, add = T, col = alpha('dark blue', 0.25)); if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.25));
			points(rec$deployment_longitude[which(!rec$station_name %in% unique(data$station_name))], rec$deployment_latitude[which(!rec$station_name %in% unique(data$station_name))], col = 'dark red', pch = 4, cex = .5); # Plot receiver locations
			if (length(which(data$Detection_QC == 1)) > 0) points(data$longitude[which(data$Detection_QC == 1)],data$latitude[which(data$Detection_QC == 1)], col = alpha('#1A9641', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 1)], pch = 19); # Plot valid detections
			if (length(which(data$Detection_QC == 2)) > 0) points(data$longitude[which(data$Detection_QC == 2)],data$latitude[which(data$Detection_QC == 2)], col = alpha('#A6D96A', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 2)], pch = 19); # Plot invalid detections
			if (length(which(data$Detection_QC == 3)) > 0) points(data$longitude[which(data$Detection_QC == 3)],data$latitude[which(data$Detection_QC == 3)], col = alpha('#FDAE61', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 3)], pch = 19); # Plot valid detections
			if (length(which(data$Detection_QC == 4)) > 0) points(data$longitude[which(data$Detection_QC == 4)],data$latitude[which(data$Detection_QC == 4)], col = alpha('#D7191C', 0.65), 
			cex = data$binned_detections[which(data$Detection_QC == 4)], pch = 19); # Plot invalid detections
			points(releases[,1:2], col = 'blue', pch = 4, cex = 2, lwd = 2); # Plot release locations
			
		mtext(paste(species$scientific_name[i], ', ', length(unique(data$transmitter_id)), ' tags.', sep = ''), outer = TRUE, cex = 1.5)
		mtext(paste('Total # detections = ', sum(data$freq), ', # invalid detections = ', sum(data$freq[which(data$Detection_QC > 2)]), sep = ''), outer = TRUE, cex = 1.5, line = -2)
	dev.off();
	##### 2nd figure - Spatial distribution of detections - END
	
	rm(data);
	if (exists('shp_b') == T) {rm(shp_b);}
};
end_time <- Sys.time();

##### Aggregate info about tags with dubious movements, due to erroneous receiver deployment coordinates - START
	st_rec <- unique(data.frame(rec_errors$installation_name, rec_errors$station_name, rec_errors$receiver_name)); colnames(st_rec) <- c('installation_name', 'station_name', 'receiver_name')
	for (i in 1:nrow(st_rec)){
		sel <- rec_errors[which(rec_errors$installation_name == st_rec$installation_name[i] & rec_errors$station_name == st_rec$station_name[i] & rec_errors$receiver_name == st_rec$receiver_name[i]),]
		st_rec$nb_transmitters[i] <- length(unique(sel$transmitter_id))
		st_rec$freq[i] <- sum(sel$freq)
	}
	st_rec <- st_rec[order(st_rec$freq, st_rec$nb_transmitters, decreasing = T),];
	write.table(st_rec, paste(wd,'/Outcomes/ReceiverMetadataErrors_Summary.csv', sep = ''), row.names=F, col.names=T, sep = ';', quote = F);
	write.table(rec_errors, paste(wd,'/Outcomes/ReceiverMetadataErrors.csv', sep = ''), row.names=F, col.names=T, sep = ';', quote = F);
##### Aggregate info about tags with dubious movements, due to erroneous receiver deployment coordinates - END

##### Print diagnostic metrics
print(paste('Start time = ', start_time, '. End time = ', end_time, '. Number of hours to produce all maps =  ', round(as.numeric(difftime(end_time, start_time, units = 'hours')), 1), sep = ''));
print(paste('# tags with dubious movements = ', length(unique(rec_errors$transmitter_id)), ', # of tags processed = ', nrow(dat), sep = ''));
rm(rec_errors);