#!/bin/bash

set -ex

# Override these variables as required.
NODE_NAME=${NODE_NAME:-"4-nec-mel.emii.org.au"}
IGNORE_TABLE=${IGNORE_TABLE:-"databasechangelog|receiver_event|valid_detection|invalid_detection"}
DUMP_FILE=${DUMP_FILE:-"/vagrant/aatams.dump"}

guest_command() {
    vagrant ssh $NODE_NAME -c "$@"
}

if `guest_command "[ ! -f $DUMP_FILE ]"`; then
    echo "Backup dump file '$DUMP_FILE' not found!"
    exit 1
fi

DB_TOC=`guest_command mktemp`

vagrant up $NODE_NAME
vagrant provision $NODE_NAME

guest_command "sudo service tomcat7_aatams_rc start"
read -p "Press [Enter] key when app has started..."   # TODO: automate this?

guest_command "sudo service tomcat7_aatams_rc stop"

guest_command "sudo -u postgres pg_restore -l $DUMP_FILE | grep DATA | egrep -v '$IGNORE_TABLE' > $DB_TOC"

guest_command "sudo -u postgres time pg_restore -d aatams_rc --no-owner --use-list=$DB_TOC --disable-triggers $DUMP_FILE"

guest_command "sudo -u postgres psql -d aatams_rc -c \"update aatams.statistics set value=0 where key='numValidDetections';\""

guest_command "sudo service tomcat7_aatams_rc start"

guest_command "rm -rf $DB_TOC"
