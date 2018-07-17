#file:config_workdir.R
# wd <- '~/QC'; ## On Nectar machine
# data_dir <- '/mnt/data'; ## On Nectar machine
wd <- '~/Work/AATAMS_AcousticTagging/aatams/scripts/R/QC'; ## On local machine
data_dir <- '~/Work/AATAMS_AcousticTagging/Data'; ## On local machine

## To QC detections for all, one, or multiple projects
# All tags (default) - leave as projects <- ""
# For a given project, note the use of the double and single quotes <- e.g. "'CSIRO: Animal Tagging'"
# For multiple projects, note the use of the double and single quotes <- e.g. "'CSIRO: Animal Tagging', 'JCU Low Isles'"
projects <- ""