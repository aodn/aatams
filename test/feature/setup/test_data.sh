#!/bin/bash
set -x

AATAMS_URL="http://localhost:8080/aatams"
COOKIE_FILE=`mktemp`
CURL_AUTH_CMD="curl --compressed -b ${COOKIE_FILE}"

curl "${AATAMS_URL}/auth/signIn" --data "targetUri=%2F%3Fnull&username=admin&password=password"  -c ${COOKIE_FILE}

#
# The following is a bit brittle, in that the commands reference record IDs - which could change if things aren't
# input in the same order.
#
# To rectify this, the app would need to change somewhat to allow string-like keys, rather than IDs.  e.g.
# reference an organisation by its name rather than ID.
#

# Non Project User
${CURL_AUTH_CMD} "${AATAMS_URL}/person/save" -d "name=Non+Project+User" -d "username=non_project_user" -d "password=password" -d "passwordConfirm=password" -d "organisation.id=0" -d "unlistedOrganisationName=" -d "phoneNumber=1234" -d "emailAddress=non_project_user%40email.com" -d "defaultTimeZone=Australia%2FHobart" -d "create=Create"

# Project User
${CURL_AUTH_CMD} "${AATAMS_URL}/person/save" -d "name=Project+User" -d "username=project_user" -d "password=password" -d "passwordConfirm=password" -d "organisation.id=0" -d "unlistedOrganisationName=" -d "phoneNumber=1234" -d "emailAddress=project_user%40email.com" -d "defaultTimeZone=Australia%2FHobart" -d "create=Create"

# Project
${CURL_AUTH_CMD} "${AATAMS_URL}/project/save" -d "name=Study+all+the+Fishes" -d "organisation.id=0" -d "person.id=3" -d "create=Create"

# Installation
${CURL_AUTH_CMD} "${AATAMS_URL}/installation/save" -d "name=Some+Curtain" -d "configuration.id=2" -d "project.id=5" -d "create=Create"

# Station
${CURL_AUTH_CMD} "${AATAMS_URL}/installationStation/save" -d "name=Station+1" -d "curtainPosition=" -d "location_pointInputTextField=10%C2%B0S+10%C2%B0E+%28datum%3AWGS+84%29" -d "location=POINT%2810+-10%29+%2C+4326" -d "location_lon=10" -d "location_lat=-10" -d "location_srid=4326" -d "installation.id=9" -d "create=Create"

# Receiver
${CURL_AUTH_CMD} "${AATAMS_URL}/receiver/save" -d "organisation.id=0" -d "model.id=0" -d "serialNumber=1111" -d "comment=" -d "create=Create"

# Deployment
${CURL_AUTH_CMD} "${AATAMS_URL}/receiverDeployment/save" -d "station.id=11" -d "receiver.id=13" -d "deploymentDateTime=struct" -d "deploymentDateTime_day=6" -d "deploymentDateTime_month=6" -d "deploymentDateTime_year=2000" -d "deploymentDateTime_hour=14" -d "deploymentDateTime_minute=57" -d "deploymentDateTime_second=28" -d "deploymentDateTime_zone=Australia%2FMelbourne" -d "location_pointInputTextField=10%C2%B0S+10%C2%B0+E+%28datum%3AWGS+84%29" -d "location=POINT%2810+-10%29+%2C+4326" -d "location_lon=10" -d "location_lat=-10" -d "location_srid=4326" -d "recoveryDate=date.struct" -d "recoveryDate_day=6" -d "recoveryDate_month=6" -d "recoveryDate_year=2040" -d "acousticReleaseID=" -d "mooringType.id=1" -d "mooringDescriptor=" -d "bottomDepthM=" -d "depthBelowSurfaceM=" -d "receiverOrientation=UP" -d "batteryLifeDays=" -d "comments=" -d "project.id=5" -d "create=Create"

# Recovery
${CURL_AUTH_CMD} "${AATAMS_URL}/receiverRecovery/save" -d "deploymentId=15" -d "projectId=5" -d "recoverer.id=8" -d "location_pointInputTextField=10%C2%B0S+10%C2%B0E+%28datum%3AWGS+84%29" -d "location=POINT%2810.0+-10.0%29+%2C+4326" -d "location_lon=10.0" -d "location_lat=-10.0" -d "location_srid=4326" -d "deployment.initialisationDateTime=struct" -d "deployment.initialisationDateTime_day=6" -d "deployment.initialisationDateTime_month=6" -d "deployment.initialisationDateTime_year=2000" -d "deployment.initialisationDateTime_hour=14" -d "deployment.initialisationDateTime_minute=57" -d "deployment.initialisationDateTime_second=45" -d "deployment.initialisationDateTime_zone=Australia%2FMelbourne" -d "recoveryDateTime=struct" -d "recoveryDateTime_day=6" -d "recoveryDateTime_month=6" -d "recoveryDateTime_year=2040" -d "recoveryDateTime_hour=14" -d "recoveryDateTime_minute=57" -d "recoveryDateTime_second=53" -d "recoveryDateTime_zone=Australia%2FMelbourne" -d "status.id=3" -d "comments=" -d "create=Create"

# Detections
${CURL_AUTH_CMD} "${AATAMS_URL}/receiverDownloadFile/save" -F type=DETECTIONS_CSV -F filedata=@detections.csv

# Tags
${CURL_AUTH_CMD} "${AATAMS_URL}/sensor/save" -d "tag.serialNumber=11111111" -d "tag.project.id=5" -d "tag.model.id=2" -d "tag.codeMap.id=0" -d "tag.expectedLifeTimeDays=" -d "tag.status.id=1" -d "transmitterType.id=1" -d "pingCode=1111" -d "slope=" -d "intercept=" -d "unit=" -d "create=Create"

# Release
${CURL_AUTH_CMD} "${AATAMS_URL}/animalRelease/save" -d "project.id=5" -d "speciesId=10029746" -d "speciesName=41112004+-+Balaenoptera+musculus+%28blue+whale%29" -d "sex.id=1" -d "animal.id=" -d "captureLocality=Derwent+River" -d "captureLocation_pointInputTextField=42%C2%B0S+138%C2%B0E+%28datum%3AWGS+84%29" -d "captureLocation=POINT%28138+-42%29+%2C+4326" -d "captureLocation_lon=138" -d "captureLocation_lat=-42" -d "captureLocation_srid=4326" -d "captureDateTime=struct" -d "captureDateTime_day=6" -d "captureDateTime_month=6" -d "captureDateTime_year=2014" -d "captureDateTime_hour=16" -d "captureDateTime_minute=25" -d "captureDateTime_second=23" -d "captureDateTime_zone=Australia%2FMelbourne" -d "captureMethod.id=0" -d "releaseLocality=Derwent+River" -d "releaseLocation_pointInputTextField=42%C2%B0S+138%C2%B0E+%28datum%3AWGS+84%29" -d "releaseLocation=POINT%28138+-42%29+%2C+4326" -d "releaseLocation_lon=138" -d "releaseLocation_lat=-42" -d "releaseLocation_srid=4326" -d "releaseDateTime=struct" -d "releaseDateTime_day=6" -d "releaseDateTime_month=6" -d "releaseDateTime_year=2014" -d "releaseDateTime_hour=16" -d "releaseDateTime_minute=25" -d "releaseDateTime_second=23" -d "releaseDateTime_zone=Australia%2FMelbourne" -d "comments=" -d "embargoPeriod=36" -d "create=Create" -d "surgery.0.timestamp_day=6" -d "surgery.0.timestamp_month=6" -d "surgery.0.timestamp_year=2014" -d "surgery.0.timestamp_hour=16" -d "surgery.0.timestamp_minute=25" -d "surgery.0.timestamp_second=23" -d "surgery.0.timestamp_zone=Australia%2FMelbourne" -d "surgery.0.type.id=1" -d "surgery.0.treatmentType.id=1" -d "surgery.0.comments=" -d "surgery.0.tag.codeMap.id=0" -d "surgery.0.tag.pingCode=1111" -d "surgery.0.tag.serialNumber=11111111" -d "surgery.0.tag.model.id=2" -d "measurement.0.type.id=1" -d "measurement.0.value=26000" -d "measurement.0.unit.id=2" -d "measurement.0.estimate=false" -d "measurement.0.comments="

rm ${COOKIE_FILE}
