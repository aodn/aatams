##### Function to match species names with ALA shapefiles (http://www.ala.org.au/). 
## For a few species acoustically tagged no shapefile exists, for a few others multiple shapefiles need to be merged into a single one (line 29 onwards).
## gBuffer extends the boundary of the original shapefile.

ala_shp <- function(species_name){
	
		spe <- gsub(' ', '_', sp);
		
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
		
		return(shp_b)
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

		return(shp_b)
	}

}
