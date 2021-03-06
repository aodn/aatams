rm(list=ls()); options(warn = 1);
library(gmt, quietly= T); library(rgeos, quietly= T); library(sp, quietly= T); library(rgdal, quietly= T); library(rworldmap, quietly= T); library(rworldxtra, quietly= T); library(gdistance, quietly= T); library(raster, quietly= T); library(doParallel, quietly= T); library(foreach, quietly= T);

##### Load up configuration file and set working directory
# setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('config_workdir.R'); # for working directories
setwd(wd);
options(digits=10)

##### Empty and re-create folders, copy metadata files from Raw to Processed folder
unlink(dir(paste(data_dir,'/Processed/',sep=''),full.names=T)); dir.create(paste(data_dir,'/Processed/',sep=''),showWarnings=F); ## Create Processed folder
file.copy(from= paste(data_dir,'/Raw/TagMetadata_AllTagsWithDetections.txt',sep=''), to= paste(data_dir,'/Processed/TagMetadata.txt',sep='')); ## Copy text file listing tag metadata
file.copy(from= paste(data_dir,'/Raw/ReceiverMetadata_AllReceiversWithDetectionsForTags.txt',sep=''), to= paste(data_dir,'/Processed/ReceiverMetadata.txt',sep='')); ## Copy text file listing receiver metadata
unlink('Outcomes', recursive = T); dir.create("Outcomes"); ## Create Outcomes folder

#### Load up all QC functions
source('QC_functions/ala_shp.R'); source('QC_functions/shortest_dist.R'); source('QC_functions/qc.R');

#### Prepare high resolution raster map for shortest path distance calculations
Aust <- crop(getMap(resolution="high"), extent(110,155,-45,-5));
r <- raster(ncol=2000, nrow=2000); extent(r) <- extent(Aust); Aust_raster <- rasterize(Aust, r); rm(Aust);
Aust_raster[is.na(Aust_raster)] <- 1; Aust_raster[Aust_raster>1] <- NA;
tr <- transition(Aust_raster,function(x) 1/mean(x) ,directions=8);
tr <- geoCorrection(tr,type="c",scl=FALSE);

##### Extract transmitter, tag and release IDs from filenames
files <- grep(list.files(path=paste(data_dir,'/Raw',sep=''), pattern = '.csv', full.names=F, recursive=FALSE),pattern="NA",invert=T,value=T)

ttr <- matrix(ncol=3, nrow=length(files)); colnames(ttr) <- c('transmitter_id','tag_id','release_id')
for (i in 1:length(files)){
	ttr[i,] <- strsplit(gsub('.csv','',files[i]),'_')[[1]]	
}
ttr[which(ttr == 'NA')] <- NA; ttr <- data.frame(ttr);

##### Identify dual sensor tags, i.e. transmitters with same tag ID and release ID
for (i in 1:nrow(ttr)){
	s <- which(ttr$tag_id == ttr$tag_id[i] & ttr$release_id == ttr$release_id[i]);
	if (length(s) == 1) ttr$keep[i] <- 1 else {
		if ((ttr$release_id[s[1]] == ttr$release_id[s[2]] & is.na(ttr$release_id[s[1]]) == F) | 
			(is.na(ttr$release_id[s[1]]) == T & is.na(ttr$release_id[s[2]]) == T)) {
			ttr$keep[i] <- 1; ttr$keep[which(ttr$tag_id == ttr$tag_id[i] & (ttr$release_id == ttr$release_id[i] | is.na(ttr$release_id[i] == T)))[2]] <- 0
		} else {ttr$keep[i] <- 1;}
	}
	rm(s);
}

##### Identify files to process
proc_files <- dir(paste(data_dir, '/Processed', sep = ''), pattern = '.csv')
ttr_files <- paste(ttr$transmitter_id[which(ttr$keep == 1)], '_', ttr$tag_id[which(ttr$keep == 1)], '_', ttr$release_id[which(ttr$keep == 1)], '.csv', sep = '')
to_process <- which(!ttr_files %in% proc_files); rm(list = c('proc_files', 'ttr_files', 'i'));

##### Configure output datasets
outcome <- data.frame(matrix(ncol=11, nrow = length(to_process))); 
colnames(outcome) <- c('transmitter_id','tag_id','release_id','scientific_name','nb_detections', 'nb_detections_releasedate_1','invalid_releaseloc','nb_detections_distribution_2','nb_valid', 'timediff_1stlastdetections_days','nb_detections_velocity_2');

## ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ##
##### QC tests - START
start_time <- Sys.time(); print(paste('Start processing ', length(to_process), ' files', sep = ''))
setwd(paste(data_dir,'/Raw',sep='')); 
tag_metadata <- read.csv('../Processed/TagMetadata.txt', header = T, sep = ';');

##### Configure parallel processing
comb <- function(x, ...) {  
      mapply(rbind,x,...,SIMPLIFY=FALSE)
}
cl <- makeCluster(detectCores()); registerDoParallel(cl);

result <- foreach(i= to_process, .combine = 'comb', .multicombine = T, .packages = c('gmt','raster','rgeos','sp','rgdal','gdistance')) %dopar% {

	##### Read raw data file
	s <- which(ttr$tag_id == ttr$tag_id[which(ttr$keep == 1)[i]] & ttr$release_id == ttr$release_id[which(ttr$keep == 1)[i]]); 
	s_m <- which(tag_metadata$release_id == ttr$release_id[which(ttr$keep == 1)[i]] & tag_metadata$release_id == ttr$release_id[which(ttr$keep == 1)[i]]);
	if (length(s) == 1) {data <- read.csv(files[s],header=T,sep=';')} else {
		if ((ttr$release_id[s[1]] == ttr$release_id[s[2]] & is.na(ttr$release_id[s[1]]) == F) | 
			(is.na(ttr$release_id[s[1]]) == T & is.na(ttr$release_id[s[2]]) == T)) {
			data <- rbind(read.csv(files[s[1]],header=T,sep=';'), read.csv(files[s[2]],header=T,sep=';'))
		}
	}
	if (unique(data$release_id) != unique(tag_metadata$release_id[s_m])) {print(paste('Error for release_id = ', ttr$release_id[which(ttr$keep == 1)[1]], sep = ''))};
	
	##### Configure output processed data file
	temporal_outcome <- data.frame(matrix(ncol = 18, nrow = nrow(data)));
	colnames(temporal_outcome) <- c('transmitter_id','installation_name','station_name','receiver_name', 'receiver_deployment_id', 'detection_timestamp','longitude','latitude', 'sensor_value', 'sensor_unit', 'FDA_QC', 'Velocity_QC', 'Distance_QC', 'DetectionDistribution_QC','DistanceRelease_QC', 'ReleaseDate_QC', 'ReleaseLocation_QC','Detection_QC');

	##### Order by detection date, fix up potential timestamp without time for release dates, extract species name
	data$tag_detection_timestamp <- strptime(as.character(data$tag_detection_timestamp),'%Y-%m-%d %H:%M:%S', tz= 'UTC');
	if (is.na(strptime(as.character(unique(tag_metadata$release_date[s_m])),'%Y-%m-%d %H:%M:%S', tz= 'UTC')) == T){ 
		data$release_date <- strptime(as.character(unique(tag_metadata$release_date[s_m])),'%Y-%m-%d', tz= 'UTC')} else {
		data$release_date <- strptime(as.character(unique(tag_metadata$release_date[s_m])),'%Y-%m-%d %H:%M:%S', tz= 'UTC')};
	data <- data[order(data$tag_detection_timestamp),]; data$release_longitude <- unique(tag_metadata$release_longitude[s_m]); data$release_latitude <- unique(tag_metadata$release_latitude[s_m]);
	sp <- unique(data$scientific_name);
	
 	#### Find corresponding ALA shapefile based on species name
	if(is.na(sp) == F) {
		shp_b <- ala_shp(sp)
		if(is.null(shp_b) == T) rm(shp_b);
	}
	
	#### Converts unique sets of lat/long detection coordinates and release lat/long coordinates to SpatialPoints to test subsequently whether detections fall or not within distribution range 
	if (exists('shp_b') == T){
		ll <- unique(data.frame(data$longitude,data$latitude));
		coordinates(ll) <- ~ data.longitude + data.latitude;
		proj4string(ll) <- proj4string(shp_b);
	
		if (is.na(data$release_longitude[1]) == F){
			ll_r <- data.frame(data$release_longitude[1],data$release_latitude[1]);
			coordinates(ll_r) <- ~ data.release_longitude.1. + data.release_latitude.1.;
			proj4string(ll_r) <- proj4string(shp_b);	
		}
	}
	
	##### Apply QC tests on detections
	temporal_outcome <- qc(data);
		
	##### Produce a 'processed' CSV file for each tag deployment
	write.table(temporal_outcome, paste(data_dir,'/Processed/',files[which(ttr$keep == 1)[i]],sep=''), col.names=T, row.names=F, sep=';', quote=F);	
	if (exists('shp_b') == T) rm(shp_b); if (exists('shp') == T) rm(shp); # Remove ALA shapefiles from environment
	
	##### Extract summary statistics for each tag
	outcome[i,1] <- ifelse(length(as.character(unique(as.character(data$transmitter_id)))) == 1, as.character(unique(data$transmitter_id)), paste(as.character(unique(data$transmitter_id)),collapse = '/'));
	outcome[i,2] <- unique(as.character(data$tag_id));
	outcome[i,3] <- unique(as.character(data$release_id));
	outcome[i,4] <- as.character(unique(data$scientific_name));
	outcome[i,5] <- nrow(temporal_outcome); # Total number of detections
	outcome[i,6] <- length(which(temporal_outcome$ReleaseDate_QC == 2)); ## Number of detections prior to release date
	outcome[i,7] <- temporal_outcome$ReleaseLocation_QC[1] == 2; ## Is tag release outside ALA/FishMap's expert distribution map or farther than 500 km from first detection location?
	outcome[i,8] <- length(which(temporal_outcome$DetectionDistribution_QC == 2)); ## Number of detections outside the ALA/FishMap's expert distribution map
	outcome[i,9] <- length(which(temporal_outcome$Detection_QC < 3)); ## Total number of 'valid' or 'likely valid' detections
	outcome[i,10] <- as.numeric(difftime(temporal_outcome$detection_timestamp[nrow(temporal_outcome)], temporal_outcome$detection_timestamp[1], units = 'days')) ## Time difference, in days, between 1st and last detections
	outcome[i,11] <- length(which(temporal_outcome$Velocity_QC == 2)); ## Number of detections associated with unrealistic velocities

	list(outcome);
	
};
stopCluster(cl); closeAllConnections();
## ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ##

##### Print diagnostic metrics
end_time <- Sys.time();
print(paste('Start time = ', start_time, '. End time = ', end_time, '. Number of hours to extract and process all detections =  ', round(as.numeric(difftime(end_time, start_time, units = 'hours')), 1), sep = ''));

##### Export summary statistics
outcome <- result[[1]]; outcome <- outcome[which(is.na(outcome$transmitter_id) == F & duplicated(outcome) == F),]
write.table(outcome, paste(wd, '/Outcomes/QC_OutputSummary.csv', sep = ''), row.names=F, col.names=T, sep = ';', quote = F);
