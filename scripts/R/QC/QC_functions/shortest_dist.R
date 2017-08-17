##### Function to compute the least-cost distance between two locations, if a land mass was found between two receivers. For tags detected more than once.

shortest_dist <- function(inst1, pts1, inst2, pts2, position, pts1_o, pts2_o){

	mvmts <- unique(cbind(inst1, pts1, inst2, pts2)); pts1 <- cbind(as.numeric(mvmts[,2]),as.numeric(mvmts[,3])); pts2 <- cbind(as.numeric(mvmts[,5]),as.numeric(mvmts[,6]));
	xr <- c(min(position$longitude, na.rm = T) - 1, max(position$longitude, na.rm = T) + 1)
	yr <- c(min(position$latitude, na.rm = T) - 1, max(position$latitude, na.rm = T) + 1)

	dist <- matrix(ncol=1, nrow = nrow(pts1_o)); dist_mvmts <- matrix(ncol=1,nrow=nrow(mvmts));
	for (u in 1:nrow(dist_mvmts)){

		pts <- rbind(pts1[u,],pts2[u,]); pts_o <- pts
		
		##### If detection lat/long = subsequent detection lat/long then dist = 0 km
		if (length(which(pts1[u,] == pts2[u,])) == 2) {dist_mvmts[u] <- 0} else {

			##### If there's a point on land and the other offshore approximate river point by closest point on coastline, OR if there are two points on land belonging to two distinct installations
			if (u == 1 & length(which(is.na(extract(Aust_raster,pts)) == T)) >= 1){ 
				Aust_sub <- crop(Aust_raster, extent(min(pts[,1]) - 2, max(pts[,1]) + 2, min(pts[,2]) - 2, max(pts[,2]) + 2));
				Aust_sub <- cbind(coordinates(Aust_sub), Aust_sub@data@values); Aust_sub <- Aust_sub[which(Aust_sub[,3] == 1),]
				if (length(which(is.na(extract(Aust_raster,pts)) == T)) == 1) {
					dist_sub <- which.min(geodist(Aust_sub[,2], Aust_sub[,1], rep(pts[which(is.na(extract(Aust_raster,pts)) == T),2],nrow(Aust_sub)), rep(pts[which(is.na(extract(Aust_raster,pts)) == T),1],nrow(Aust_sub)), units='km'));
					pts[which(is.na(extract(Aust_raster,pts)) == T),] <- Aust_sub[dist_sub, 1:2]; ## Find closest point on coast for river system
				}
				if (length(which(is.na(extract(Aust_raster,pts)) == T)) > 1) {
					for (k in 1:2){
						dist_sub <- which.min(geodist(Aust_sub[,2], Aust_sub[,1], rep(pts[k,2],nrow(Aust_sub)), rep(pts[k,1],nrow(Aust_sub)), units='km'));
						pts[k,] <- Aust_sub[dist_sub, 1:2]; ## Find closest point on coast for river system
					}
				}
				rm(Aust_sub);
			} else {
			
			if (length(which(is.na(extract(Aust_raster,pts)) == T)) == 1 | (length(which(is.na(extract(Aust_raster,pts)) == T)) == 2 & inst1[u] != inst2[u])){ 
				Aust_sub <- crop(Aust_raster, extent(min(pts[,1]) - 2, max(pts[,1]) + 2, min(pts[,2]) - 2, max(pts[,2]) + 2));
				Aust_sub <- cbind(coordinates(Aust_sub), Aust_sub@data@values); Aust_sub <- Aust_sub[which(Aust_sub[,3] == 1),]
				if (length(which(is.na(extract(Aust_raster,pts)) == T)) == 1) {
					dist_sub <- which.min(geodist(Aust_sub[,2], Aust_sub[,1], rep(pts[which(is.na(extract(Aust_raster,pts)) == T),2],nrow(Aust_sub)), rep(pts[which(is.na(extract(Aust_raster,pts)) == T),1],nrow(Aust_sub)), units='km'));
					pts[which(is.na(extract(Aust_raster,pts)) == T),] <- Aust_sub[dist_sub, 1:2]; ## Find closest point on coast for river system
				}
				if (length(which(is.na(extract(Aust_raster,pts)) == T)) > 1) {
					for (k in 1:2){
						dist_sub <- which.min(geodist(Aust_sub[,2], Aust_sub[,1], rep(pts[k,2],nrow(Aust_sub)), rep(pts[k,1],nrow(Aust_sub)), units='km'));
						pts[k,] <- Aust_sub[dist_sub, 1:2]; ## Find closest point on coast for river system
					}
				}
				rm(Aust_sub);
			}
			}
			
			##### Linear interpolation between positions to determine the presence of a land mass
			if (length(which(is.na(extract(Aust_raster,pts)) == T)) < 2 & length(which(pts[1,] == pts[2,])) == 0){
				interp <- cbind(approx(c(pts[1,1],pts[2,1]), c(pts[1,2],pts[2,2]),n=200)$x,approx(c(pts[1,1],pts[2,1]), c(pts[1,2],pts[2,2]),n=200)$y);
				int <- extract(Aust_raster,interp); rm(interp);
			} else int <- c(NA, NA);
			
			##### Determine how distances are calculated
			## None of the two positions were on land
			if (length(which(pts == pts_o) == T) == 4) {
				dist_mvmts[u] <- ifelse((length(which(int == 1)) == length(int) | length(which(is.na(int))) == length(int) | length(which(is.na(extract(Aust_raster,pts)) == T)) > 0), geodist(pts[1,2],pts[1,1],pts[2,2],pts[2,1], units='km'), costDistance(tr,pts[1,],pts[2,]) / 1000);
			}
			## One of the two positions was on land
			if (length(which(pts == pts_o) == T) == 2) {
				d <- which(rowSums(pts) != rowSums(pts_o))
				dist_mvmts[u] <- ifelse((length(which(int == 1)) == length(int) | length(which(is.na(int))) == length(int) | length(which(is.na(extract(Aust_raster,pts)) == T)) > 0), geodist(pts[1,2],pts[1,1],pts[2,2],pts[2,1], units='km') + geodist(pts_o[d,2],pts_o[d,1],pts[d,2],pts[d,1], units='km'), costDistance(tr,pts[1,],pts[2,]) / 1000 + geodist(pts_o[d,2],pts_o[d,1],pts[d,2],pts[d,1], units='km')) ;
			}
			## The two positions were on land and transformed points have different coordinates
			if (length(which(pts == pts_o) == T) == 0 & length(which(pts[1,] == pts[2,])) < 2) {
				dist_mvmts[u] <- ifelse((length(which(int == 1)) == length(int) | length(which(is.na(int))) == length(int) | length(which(is.na(extract(Aust_raster,pts)) == T)) > 0), geodist(pts[1,2],pts[1,1],pts[2,2],pts[2,1], units='km') + geodist(pts_o[1,2],pts_o[1,1],pts[1,2],pts[1,1], units='km') + geodist(pts_o[2,2],pts_o[2,1],pts[2,2],pts[2,1], units='km'), costDistance(tr,pts[1,],pts[2,]) / 1000 + geodist(pts_o[1,2],pts_o[1,1],pts[1,2],pts[1,1], units='km') + geodist(pts_o[2,2],pts_o[2,1],pts[2,2],pts[2,1], units='km')) ;
			}
			## The two positions were on land and transformed points have same coordinates
			if (length(which(pts == pts_o) == T) == 0 & length(which(pts[1,] == pts[2,])) == 2) {
				dist_mvmts[u] <- geodist(pts_o[1,2],pts_o[1,1],pts_o[2,2],pts_o[2,1], units='km');
			}
			rm(int);
		}
		dist[which(pts1_o[,1] == pts1[u,1] & pts1_o[,2] == pts1[u,2] & pts2_o[,1] == pts2[u,1] & pts2_o[,2] == pts2[u,2])] <- dist_mvmts[u]
	}
	
	return(dist)
}
