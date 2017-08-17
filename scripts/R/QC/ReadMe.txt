##### How to run
1. Change working directory in terminal to where you've cloned the GitHub repository.
2. Modify the 'config_db.R' and 'config_workdir.R' configuration files to match your database credentials and folder hierarchy.
3. Place species distribution shapefiles into the 'ALA_Shapefile' folder (see example sub-folders in there).
4. Run sh -x QC.sh > LogFile.txt. This will execute the different R scripts mentioned in 'QC.sh' and will log all terminal activities in 'LogFile.txt'

###########################
##### Quality Control Tests

	##### False Detection Algorithm test - detection fails this test ('FDA_QC' = 2) if tag detected only once or if there were more long (> 12 hours) than short periods (< 30 minutes) between detections at a given installation.

	##### Velocity test - a given detection failed this test if the velocity with the previous and next receiver was greater than 10 m.s-1 (‘Velocity_QC’ = 2).

	##### Distance test - a given detection failed this test if the distance with the previous and next receiver was greater than 1000 km (‘Distance_QC’ = 2).

	##### Detection distribution test - detections failed this test if they occurred outside of a species’ known occurrence area (‘DetectionDistribution_QC’ = 2), allowing for uncertainties in compiled distributions and climate-induced species range shifts by extending the original area’s latitudinal range by 500 km.

	##### Distance from release - a given detection passed this test (‘DistanceRelease_QC’ = 1) if it occurred within a 500 km radius of where the tagged animal was released.

	##### Release date before detection date - detections failed this test ('ReleaseDate_QC’ = 2) if they occurred before a tag’s release date.

	##### Release location test - tag's release coordinates failed this test (‘ReleaseLocation’ = 2) if falling outside of a species distribution area and/or farther than 500 km of the first detection.

##### Quality Control Flag Values for the above tests
	## 0: no qc performed 
	## 1: good data
	## 2: likely invalid data
	## 3: missing data

##### Final Flag Values For 'Detection_QC' - this field summarises the output of the five first tests, i.e. ‘FDA_QC’, ‘Distance_QC’, ‘Velocity_QC’, ‘DetectionDistribution_QC’, and ‘DistanceRelease_QC’
	## 1: valid detection - All five tests passed
	## 2: probably valid detection - Four tests passed
	## 3: probably bad detection - Three tests passed
	## 4: bad detection - Two or less tests passed
