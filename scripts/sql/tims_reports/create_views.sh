#!/bin/bash

HOST=$1
DB=$2

#FILENAMES=('project_list.sql' 'releases_by_species.sql' 'summary_statistics.sql' 'detections_by_project.sql' 'detections_by_species.sql' 'installation_list.sql')
#VIEWNAMES=('project_list' 'releases_by_species' 'summary_statistics' 'detections_by_project' 'detections_by_species' 'installation_list')

FILENAMES=('project_list.sql' 'detections_by_project.sql' 'detections_by_species.sql' 'installation_list.sql' 'releases_by_species.sql')
VIEWNAMES=('project_list' 'detections_by_project' 'detections_by_species' 'installation_list' 'releases_by_species')

COUNT=0

for filename in "${FILENAMES[@]}"; do
	echo "Creating view: ${VIEWNAMES[$COUNT]}...";
	psql -h $HOST -d $DB -c "CREATE OR REPLACE VIEW ${VIEWNAMES[$COUNT]} AS `cat $filename`"
    echo "";
	let COUNT+=1
done;

