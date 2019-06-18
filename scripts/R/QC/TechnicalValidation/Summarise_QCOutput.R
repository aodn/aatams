rm(list = ls());

##### Load up configuration file
# setwd('/Users/xhoenner/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC/'); # comment out once all dev work completed
source('config_workdir.R'); # for working directories
setwd(paste(data_dir,'/Processed/', sep= ''));

dat <- read.csv('TagMetadata.txt', header= T, sep =';');

##### Invalid species names - delete data files and corresponding records in TagMetadata.txt
invalid_species <- which(is.na(dat$common_name))
if (length(invalid_species) > 0){
	for (i in 1:length(invalid_species)){
		unlink(dir()[grep(paste(dat$tag_id[invalid_species[i]],'_',dat$release_id[invalid_species[i]],sep=''),dir())])
	}
	dat <- dat[which(is.na(dat$common_name) == F),]; metadata <- metadata[which(is.na(metadata$common_name) == F),];
}

##### Exclude data/metadata for protected and embargoed tags. Only apply when running QC on entire dataset
if (projects == ''){
	
	## Update tag metadata file
	print(paste(nrow(dat) - nrow(dat[which(dat$is_protected == F & (as.Date(dat$embargo_date) <= Sys.Date() | is.na(dat$embargo_date) == T)),]),' tag IDs embargoed/protected', sep =''));
	dat$embargo_date <- strptime(as.character(dat$embargo_date), '%Y-%m-%d %H:%M:%S', tz = 'UTC');
	
	if (species_list == "'Bigeye Snapper', 'Snapper', 'Yellowtail Kingfish', 'sand flathead (mixed)', 'Southern Sand Flathead', 'Bluespotted Flathead', 'Dusky Flathead', 'Yellowfin Bream', 'Spanish Mackerel', 'Southern Bluefin Tuna', 'Tiger Shark', 'White Shark', 'Bull Shark', 'School Shark'"){ ## For FRDC only	
		dat_2 <- dat[which(!dat$tag_project_name %in% c('CSIRO: Animal Tagging 2', 'Ningaloo Shark Nursery')),]; ## For FRDC only
	} else {
		dat_2 <- dat[which(dat$is_protected == F & (as.Date(dat$embargo_date) <= Sys.Date() | is.na(dat$embargo_date) == T)),];	
	}
	write.table(dat_2, 'TagMetadata.txt', row.names=F, col.names=T, sep = ';', quote = F);
	
	## Update list of data files by deleting those that are embargoed or protected
	if (species_list == "'Bigeye Snapper', 'Snapper', 'Yellowtail Kingfish', 'sand flathead (mixed)', 'Southern Sand Flathead', 'Bluespotted Flathead', 'Dusky Flathead', 'Yellowfin Bream', 'Spanish Mackerel', 'Southern Bluefin Tuna', 'Tiger Shark', 'White Shark', 'Bull Shark', 'School Shark'"){ ## For FRDC only	
		embprot <- dat[which(dat$tag_project_name %in% c('CSIRO: Animal Tagging 2', 'Ningaloo Shark Nursery')),]; ## For FRDC only
	} else {
		embprot <- dat[-which(dat$is_protected == F & (as.Date(dat$embargo_date) <= Sys.Date() | is.na(dat$embargo_date) == T)),];
	}
	if (nrow(embprot) > 0){
		embprot <- data.frame(embprot[,c(1:3,7:8)])
		for (i in 1:nrow(embprot)){
			unlink(dir()[grep(paste(embprot$tag_id[i],'_',embprot$release_id[i],sep=''),dir())])
		};	
	}

}
