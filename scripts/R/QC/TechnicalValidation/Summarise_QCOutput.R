rm(list = ls());

##### Load up configuration file
setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('config_workdir.R'); # for working directories
setwd(paste(data_dir,'/Processed/', sep= ''));

##### Merge summary statistics and tag metadata
dat <- read.csv(paste(wd,'/Outcomes/QC_OutputSummary.csv', sep = ''), header=T, sep = ';');
metadata <- read.csv('TagMetadata.txt', header= T, sep =';');

for (i in 1:nrow(dat)){
	if (length(strsplit(as.character(dat$transmitter_id[i]),'/')[[1]]) == 1) {s <- grep(paste(dat$transmitter_id[i],'_', dat$tag_id[i], '_', dat$release_id[i], sep =''), paste(metadata$transmitter_id,'_',metadata$tag_id,'_',metadata$release_id, sep =''));} else {
		s <- grep(paste(strsplit(as.character(dat$transmitter_id[i]),'/')[[1]][1],'_', dat$tag_id[i], '_', dat$release_id[i], sep =''), paste(metadata$transmitter_id,'_',metadata$tag_id,'_',metadata$release_id, sep =''));
	}
	if (i == 1) {dat_all <- cbind(dat[i,], metadata[s,c(4:6,8:22)])} else {dat_all <- rbind(dat_all, dat_all <- cbind(dat[i,], metadata[s,c(4:6,8:22)]))}
}
dat <- dat_all; rm(dat_all);
dat$scientific_name <- as.character(dat$scientific_name); dat$common_name <- as.character(dat$common_name); metadata$scientific_name <- as.character(metadata$scientific_name); metadata$common_name <- as.character(metadata$common_name);
write.table(dat, paste(wd,'/Outcomes/QC_OutputSummary.csv', sep = ''), row.names=F, col.names=T, sep = ';');

##### Invalid species names - delete data files and corresponding records in QC_OutputSummary.csv and TagMetadata.txt
invalid_species <- which(is.na(dat$common_name))
if (length(invalid_species) > 0){
	for (i in 1:length(invalid_species)){
		unlink(dir()[grep(paste(dat$tag_id[invalid_species[i]],'_',dat$release_id[invalid_species[i]],sep=''),dir())])
	}
	dat <- dat[which(is.na(dat$common_name) == F),]; metadata <- metadata[which(is.na(metadata$common_name) == F),];
}

##### Exclude data for tags that are currently protected or embargoed
## Update summary statistics file
dat$embargo_date <- strptime(as.character(dat$embargo_date), '%Y-%m-%d %H:%M:%S', tz = 'UTC');
dat <- dat[which(dat$is_protected == F & (as.Date(dat$embargo_date) <= Sys.Date() | is.na(dat$embargo_date) == T)),]; nrow(dat); sum(dat$nb_detections);
write.table(dat, paste(wd,'/Outcomes/QC_OutputSummary.csv', sep = ''), row.names=F, col.names=T, sep = ';', quote = F);

## Update tag metadata file
print(paste(nrow(metadata) - nrow(metadata[which(metadata$is_protected == F & (as.Date(metadata$embargo_date) <= Sys.Date() | is.na(metadata$embargo_date) == T)),]),' tag IDs embargoed/protected', sep =''));
metadata$embargo_date <- strptime(as.character(metadata$embargo_date), '%Y-%m-%d %H:%M:%S', tz = 'UTC');
metadata_2 <- metadata[which(metadata$is_protected == F & (as.Date(metadata$embargo_date) <= Sys.Date() | is.na(metadata$embargo_date) == T)),];
write.table(metadata_2, 'TagMetadata.txt', row.names=F, col.names=T, sep = ';', quote = F);

## Update list of data files by deleting those that are embargoed or protected
embprot <- metadata[-which(metadata$is_protected == F & (as.Date(metadata$embargo_date) <= Sys.Date() | is.na(metadata$embargo_date) == T)),]
if (nrow(embprot) > 0){
	embprot <- data.frame(embprot[,c(1:3,7:8)])
	for(i in 1:nrow(embprot)){
		unlink(dir()[grep(paste(embprot$tag_id[i],'_',embprot$release_id[i],sep=''),dir())])
	};	
}

##### Print summary statistics
print(paste('Number of registered tags = ', nrow(dat), '; Number of valid detections for registered tags = ', sum(dat$nb_detections[which(dat$nb_valid != 0)]), ' detections', sep = ''));
