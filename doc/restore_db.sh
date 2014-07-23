#!/bin/bash

set -ex

# Override these variables as required.
IGNORE_TABLE=${IGNORE_TABLE:-"databasechangelog|receiver_event|valid_detection|invalid_detection"}
DUMP_FILE=${DUMP_FILE:-"/vagrant/aatams.dump"}

if [ ! -f $DUMP_FILE ]; then
    echo "Backup dump file '$DUMP_FILE' not found!"
    exit 1
fi

DB_TOC=`mktemp`
chmod 644 $DB_TOC

# The app takes care of creating tables etc, via liquibase.
sudo service tomcat7_aatams_rc start
read -p "Press [Enter] key when app has started..."   # TODO: automate this?

sudo service tomcat7_aatams_rc stop

# Create a table of contents which exludes our "ignore tables".
sudo -u postgres pg_restore -l $DUMP_FILE | grep DATA | egrep -v $IGNORE_TABLE > $DB_TOC

# Load the data...
sudo -u postgres time pg_restore -d aatams_rc --no-owner --use-list=$DB_TOC --disable-triggers $DUMP_FILE

# ... and reset the stats.
sudo -u postgres psql -d aatams_rc -c "update aatams.statistics set value=(select count(*) from aatams.valid_detection) where key='numValidDetections';"

sudo service tomcat7_aatams_rc start

rm -rf $DB_TOC
