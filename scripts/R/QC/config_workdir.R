#file:config_workdir.R
# wd <- '~/QC'; ## On Nectar machine
# data_dir <- '/mnt/data'; ## On Nectar machine
# home_dir <- '/home/xhoenner'; ## On Nectar machine
wd <- '~/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC'; ## On local machine
data_dir <- '~/Work/AATAMS_AcousticTagging/Data'; ## On local machine
home_dir <- '/Users/xhoenner/Work'; ## On local machine

## To QC detections for all, one, or multiple projects, AND/OR for all, one, or multiple species
	# All tags (default) - leave as projects <- ""
	# For a given project, note the use of the double and single quotes <- e.g. "'CSIRO: Animal Tagging'"
	# For multiple projects, note the use of the double and single quotes <- e.g. "'CSIRO: Animal Tagging', 'JCU Low Isles'"

	# All species (default) - leave as species_list <- ""
	# For a given species, note the use of the double and single quotes <- e.g. "'Bigeye Snapper'"
	# For multiple species, note the use of the double and single quotes <- e.g. "'Dusky Flathead', 'Black Bream'"

projects <- ""
species_list <- "'Bigeye Snapper', 'Snapper', 'Yellowtail Kingfish', 'sand flathead (mixed)', 'Southern Sand Flathead', 'Bluespotted Flathead', 'Dusky Flathead', 'Yellowfin Bream', 'Spanish Mackerel', 'Southern Bluefin Tuna', 'Tiger Shark', 'White Shark', 'Bull Shark', 'School Shark'" ## For FRDC workshop
# species_list <- ""
