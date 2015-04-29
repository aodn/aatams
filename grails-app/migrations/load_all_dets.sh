#!/bin/bash

# TODO: dos2unix

DOWNLOAD_ID_QUERY="select id from aatams.receiver_download_file order by id"
PSQL_CMD="psql aatams3 -t -c"

for receiver_download_id in `$PSQL_CMD "$DOWNLOAD_ID_QUERY"`; do

  FILENAME="/mnt/ebs/aatams/fileimports/$receiver_download_id/*.csv"
  echo "Loading $FILENAME..."

  if grep -q "Date and Time (UTC)" $FILENAME; then

    cat $FILENAME | dos2unix | awk -F',' -v id=$receiver_download_id ' { print $1","$2","$3","$4","$5","$6","$7","$8","$9","$10","id} ' | \
      psql -d aatams3 -c "COPY aatams.detection (timestamp, receiver_name, transmitter_id, transmitter_name, transmitter_serial_number, sensor_value, sensor_unit, station_name, latitude, longitude, receiver_download_id) FROM STDIN WITH CSV HEADER;" && \
    echo "File loading succeeded: ${FILENAME}" || \
    echo "File loading failed: ${FILENAME}"

  else
    echo "Incorrect format: ${FILENAME}"
  fi

  echo

done
