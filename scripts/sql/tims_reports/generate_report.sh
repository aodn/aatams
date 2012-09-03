#!/bin/bash

HOST=$1
DB=$2

FILENAMES=('project_list.sql' 'releases_by_species.sql' 'overall_stats.sql' 'detections_by_project.sql' 'detections_by_species.sql' 'installation_list.sql')

for filename in "${FILENAMES[@]}"; do
	echo "Executing file: $filename ...";
    psql -h $HOST -d $DB -f $filename;
    echo "";
done;

