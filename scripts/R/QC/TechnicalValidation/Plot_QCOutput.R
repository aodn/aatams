rm(list=ls());
library(plyr, quietly= T); library(rgdal, quietly= T); library(sp, quietly= T); library(rgeos, quietly= T); library(mapdata); library(scales);

##### Load up configuration file
source('config_workdir.R'); # for working directories
setwd(paste(data_dir,'/Processed/', sep = ''));
dat <- read.csv(paste(wd,'/Outcomes/QC_OutputSummary.csv', sep = ''), header = T, sep= ';');
unlink(paste(wd,'/Outcomes/QC_Maps', sep = '')); dir.create(paste(wd,'/Outcomes/QC_Maps', sep = ''));

##### Retrieve species list
sp <- count(dat$scientific_name)[,1];
sp <- as.data.frame(sp); colnames(sp) <- 'scientific_name';
sp <- merge(sp,dat[,c(4,12)],by='scientific_name');
sp <- unique(sp[order(sp[,2]),]); 

##### Find for each species the corresponding ALA expert map shapefile
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

##### Print diagnostic message for species missing expert map distribution
print(paste('Missing ALA expert map distribution for ', paste(sp[which(sp[,1] %in% fol_sel[which(is.na(fol_sel[,2]) == T & fol_sel[,1] != 'Myliobatus australis & Aetobatus narinari [Soviet Fishery Data, 1998]' & fol_sel[,1] != 'Lethrinus nebulosus & Lethrinus sp.'),1]),1],collapse=', '), sep=''));


######################################
##### Produce map of detection geographical distribution for each species
start_time <- Sys.time();

for (i in 1:nrow(sp)){
	## Prepare dataset
	if(is.na(sp[i,1]) == F) sel <- which(dat$scientific_name == sp [i,1]);
	if(is.na(sp[i,2]) == F) spe <- sp[i,2] else spe <- sp[i,1];
	for (j in 1:length(sel)){
		if (length(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]]) == 1) {s <- grep(paste(dat$transmitter_id[sel[j]],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());} else {
			s <- grep(paste(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]][1],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());
			if (length(s) == 0) s <- grep(paste(strsplit(as.character(dat$transmitter_id[sel[j]]),'/')[[1]][2],'_', dat$tag_id[sel[j]], '_', dat$release_id[sel[j]], sep =''), dir());
		}
		
		d <- read.csv(dir()[s], header=T, sep=';');
		d <- cbind(d, dat[s,c(2:4,6:20)], row.names = NULL)[,c(1,18:20,2:17,21:35)]
		d <- count(d[,c(1:3,5:7,9:10,19:20,30:31)]);
		
		if (j == 1) {data <- d} else data <- rbind(data,d);
	}
	
	##### Load up shapefile(s)
	f <- which(fol_sel[,1] == sp[i,1]);
	
	if (length(f) == 1){
		if (is.na(fol_sel[f,2]) == F){
		shp <- readOGR(dsn = path.expand(paste(paste(wd,'/ALA_Shapefile/', sep =''),fol_sel[f,2],'/',gsub('_SHP','',fol_sel[f,2]),'_Shapefile.shp',sep='')), paste(gsub('_SHP','',fol_sel[f,2]),'_Shapefile',sep=''), verbose = F);
		shp_b <- spTransform(shp, CRS=CRS("+proj=merc +ellps=GRS80"));
		shp_b <- gBuffer(shp_b, width= 500000); ## 500 km buffer area
		shp_b <- gSimplify(shp_b, tol=0.01, topologyPreserve=TRUE);
		shp_b <- spTransform(shp_b, CRS=CRS("+proj=longlat +ellps=WGS84"));
		}
	}
	
	if (length(f) > 1){
		if (is.na(fol_sel[f[1],2]) == F) shp_1 <- readOGR(dsn = path.expand(paste(paste(wd,'/ALA_Shapefile/', sep =''),fol_sel[f[1],2],'/',gsub('_SHP','',fol_sel[f[1],2]),'_Shapefile.shp',sep='')), paste(gsub('_SHP','',fol_sel[f[1],2]),'_Shapefile',sep=''), verbose = F);
		if (is.na(fol_sel[f[2],2]) == F) shp_2 <- readOGR(dsn = path.expand(paste(paste(wd,'/ALA_Shapefile/', sep =''),fol_sel[f[2],2],'/',gsub('_SHP','',fol_sel[f[2],2]),'_Shapefile.shp',sep='')), paste(gsub('_SHP','',fol_sel[f[2],2]),'_Shapefile',sep=''), verbose = F);
		if (is.na(fol_sel[f[1],2]) == T & is.na(fol_sel[f[2],2]) == F) shp <- shp_2 else if(is.na(fol_sel[f[1],2]) == F & is.na(fol_sel[f[2],2]) == T) shp <- shp_1;
		if (is.na(fol_sel[f[1],2]) == F & is.na(fol_sel[f[2],2]) == F) {
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
	
	##### Represent detection geographical distribution
	xr <- c(min(c(data$release_longitude,data$longitude), na.rm=T)-2,max(c(data$release_longitude,data$longitude), na.rm=T) +2);
	yr <- c(min(c(data$release_latitude,data$latitude), na.rm=T)-2,max(c(data$release_latitude,data$latitude), na.rm=T) +2);

	png(file = paste(wd,'/Outcomes/QC_Maps/',gsub(' ', '_',spe),".png",sep=""), width = 1920, height = 800, units = "px", res=92, bg = "white");
	par(mfrow=c(1,2));
	
	map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
	if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.25));
	title(paste(spe, ', ', length(unique(data$transmitter_id)), ' tag IDs, ', sum(data$freq), ' detections. Detection_QC <= 2', sep = ''));
	# Plot release locations
	points(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,1], unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,2], 
		col = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1, 1, 2.5));
	# Plot valid detections
	if (length(which(data$Detection_QC < 3)) > 0) points(data$longitude[which(data$Detection_QC < 3)],data$latitude[which(data$Detection_QC < 3)], col = alpha('forestgreen', 0.7), pch = 19, cex = 1);
	# Plot invalid detections
	if (length(which(data$Detection_QC >= 3)) > 0) points(data$longitude[which(data$Detection_QC >= 3)],data$latitude[which(data$Detection_QC >= 3)], col = 'red', pch = 3, cex = 3);
	
	map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
	if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.25));
	title(paste(spe, ', ', length(unique(data$transmitter_id)), ' tag IDs, ', sum(data$freq), ' detections. Detection_QC == 1 ', sep = ''));
	# Plot release locations
	points(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,1], unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,2], 
		col = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1, 1, 2.5));
	# Plot valid detections
	if (length(which(data$Detection_QC < 2)) > 0) points(data$longitude[which(data$Detection_QC < 2)],data$latitude[which(data$Detection_QC < 2)], col = alpha('forestgreen', 0.7), pch = 19, cex = 1);
	# Plot invalid detections
	if (length(which(data$Detection_QC >= 2)) > 0) points(data$longitude[which(data$Detection_QC >= 2)],data$latitude[which(data$Detection_QC >= 2)], col = 'red', pch = 3, cex = 3);

	dev.off(); 
	
	## Represent detection geographical distribution along with expert distribution extent
	if (exists('shp_b') == T) {
	xr <- c(min(c(data$release_longitude,data$longitude,shp_b@bbox[1,1]), na.rm=T)-2,max(c(data$release_longitude,data$longitude,shp_b@bbox[1,2]), na.rm=T) +2);
	yr <- c(min(c(data$release_latitude,data$latitude,shp_b@bbox[2,1]), na.rm=T)-2,max(c(data$release_latitude,data$latitude,shp_b@bbox[2,2]), na.rm=T) +2);
	
	png(file = paste(wd,'/Outcomes/QC_Maps/',gsub(' ', '_',spe),"_ExpertExtent.png",sep=""), width = 1920, height = 800, units = "px", res=92, bg = "white");
	par(mfrow=c(1,2));
		
	map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
	if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.75));
	title(paste(spe, ', ', length(unique(data$transmitter_id)), ' tag IDs, ', sum(data$freq), ' detections. Detection_QC <= 2', sep = ''));
	# Plot release locations
	points(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,1], unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,2], 
		col = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1, 1, 2.5));	
	# Plot valid detections
	if (length(which(data$Detection_QC < 3)) > 0) points(data$longitude[which(data$Detection_QC < 3)],data$latitude[which(data$Detection_QC < 3)], col = alpha('forestgreen', 0.7), pch = 19, cex = 1);
	# Plot invalid detections
	if (length(which(data$Detection_QC >= 3)) > 0) points(data$longitude[which(data$Detection_QC >= 3)],data$latitude[which(data$Detection_QC >= 3)], col = 'red', pch = 3, cex = 3);


	map('worldHires',xlim=xr,ylim=yr, fill = T, col = 'grey'); box(); axis(1, cex.axis = 0.8); axis(2, cex.axis = 0.8);
	if (exists('shp_b') == T) plot(shp_b, add = T, col = alpha('light blue', 0.75));
	title(paste(spe, ', ', length(unique(data$transmitter_id)), ' tag IDs, ', sum(data$freq), ' detections. Detection_QC == 1', sep = ''));
	# Plot release locations
	points(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,1], unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,2], 
		col = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1,'blue','darkorange3'), pch = 4, cex = 2, lwd = ifelse(unique(data.frame(data$release_longitude,data$release_latitude, data$ReleaseLocation_QC))[,3] == 1, 1, 2.5));
	# Plot valid detections
	if (length(which(data$Detection_QC < 2)) > 0) points(data$longitude[which(data$Detection_QC < 2)],data$latitude[which(data$Detection_QC < 2)], col = alpha('forestgreen', 0.7), pch = 19, cex = 1);
	# Plot invalid detections
	if (length(which(data$Detection_QC >= 2)) > 0) points(data$longitude[which(data$Detection_QC >= 2)],data$latitude[which(data$Detection_QC >= 2)], col = 'red', pch = 3, cex = 3);

	dev.off();}
	if (exists('shp_b') == T) rm(shp_b);
};

##### Print diagnostic metrics
end_time <- Sys.time();
print(paste('Start time = ', start_time, '. End time = ', end_time, '. Number of hours to produce all maps =  ', round(as.numeric(difftime(end_time, start_time, units = 'hours')), 1), sep = ''));
