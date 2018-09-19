rm(list=ls()); options(warn = 1);
library(gmt); library(rgeos, quietly= T); library(sp, quietly= T); library(rgdal, quietly= T); library(rworldmap, quietly= T); library(rworldxtra, quietly= T); library(gdistance, quietly= T); library(raster, quietly= T); library(doParallel); library(foreach);

##### Load up configuration file and set working directory
# setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('config_workdir.R'); # for working directories
setwd(wd);
options(digits=10)

##### Empty and re-create folders
# unlink(dir(paste(data_dir,'/Processed/',sep=''),full.names=T)); dir.create(paste(data_dir,'/Processed/',sep=''),showWarnings=F); 
unlink('Outcomes', recursive = T); dir.create("Outcomes");

#### Load up all QC functions
source('QC_functions/ala_shp.R'); source('QC_functions/shortest_dist.R'); source('QC_functions/qc.R');

#### Prepare high resolution raster map for shortest path distance calculations
Aust <- crop(getMap(resolution="high"), extent(110,155,-45,-5));
r <- raster(ncol=2000, nrow=2000); extent(r) <- extent(Aust); Aust_raster <- rasterize(Aust, r); rm(Aust);
Aust_raster[is.na(Aust_raster)] <- 1; Aust_raster[Aust_raster>1] <- NA;
tr <- transition(Aust_raster,function(x) 1/mean(x) ,directions=8);
tr <- geoCorrection(tr,type="c",scl=FALSE);

##### Extract transmitter, tag and release IDs from filenames
files <- grep(list.files(path=paste(data_dir,'/Raw',sep=''), full.names=F, recursive=FALSE),pattern="NA",invert=T,value=T)

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
to_process <- which(!ttr_files %in% proc_files); rm(list = c('proc_files', 'ttr_files'));

##### Configure parallel processing
comb <- function(x, ...) {  
      mapply(rbind,x,...,SIMPLIFY=FALSE)
}
cl <- makeCluster(detectCores()); registerDoParallel(cl);

######################
##### QC tests - START
start_time <- Sys.time(); print(paste('Start processing ', length(to_process), ' files', sep = ''))
setwd(paste(data_dir,'/Raw',sep='')); 
tag_metadata <- read.csv('../Processed/TagMetadata.txt', header = T, sep = ';');

iteration_number <- foreach(i = to_process, .packages = c('gmt','raster','rgeos','sp','rgdal','gdistance')) %dopar% {

	##### Read raw data file
	s <- which(ttr$tag_id == ttr$tag_id[i] & ttr$release_id == ttr$release_id[i]); s_m <- which(tag_metadata$release_id == ttr$release_id[i] & tag_metadata$release_id == ttr$release_id[i])
	if (length(s) == 1) {data <- read.csv(files[i],header=T,sep=';')} else {
		if ((ttr$release_id[s[1]] == ttr$release_id[s[2]] & is.na(ttr$release_id[s[1]]) == F) | 
			(is.na(ttr$release_id[s[1]]) == T & is.na(ttr$release_id[s[2]]) == T)) {
			data <- rbind(read.csv(files[i],header=T,sep=';'), read.csv(files[which(ttr$tag_id == ttr$tag_id[i] & (ttr$release_id == ttr$release_id[i] | is.na(ttr$release_id[i] == T)))[2]],header=T,sep=';'))
		} #else {data <- read.csv(files[i],header=T,sep=';')}
	}
	
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
	write.table(temporal_outcome, paste(data_dir,'/Processed/',files[i],sep=''), col.names=T, row.names=F, sep=';', quote=F);	
	if (exists('shp_b') == T) rm(shp_b); if (exists('shp') == T) rm(shp); # Remove ALA shapefiles from environment
	
	i
};
stopCluster(cl);

##### Print diagnostic metrics
end_time <- Sys.time();
print(paste('Start time = ', start_time, '. End time = ', end_time, '. Number of hours to extract and process all detections =  ', round(as.numeric(difftime(end_time, start_time, units = 'hours')), 1), sep = ''));
print(paste('Number of files processed = ', length(iteration_number), sep = ''))