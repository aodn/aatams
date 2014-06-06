#!/bin/bash

AATAMS_URL="http://localhost:8080/aatams"
COOKIE_FILE=/tmp/cookies.txt
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
${CURL_AUTH_CMD} "${AATAMS_URL}/person/save" --data "name=Non+Project+User&username=non_project_user&password=password&passwordConfirm=password&organisation.id=0&unlistedOrganisationName=&phoneNumber=1234&emailAddress=non_project_user%40email.com&defaultTimeZone=Australia%2FHobart&create=Create"

# Project User
${CURL_AUTH_CMD} "${AATAMS_URL}/person/save" --data "name=Project+User&username=project_user&password=password&passwordConfirm=password&organisation.id=0&unlistedOrganisationName=&phoneNumber=1234&emailAddress=project_user%40email.com&defaultTimeZone=Australia%2FHobart&create=Create"

# Project
${CURL_AUTH_CMD} "${AATAMS_URL}/project/save" --data "name=Study+all+the+Fishes&organisation.id=0&person.id=3&create=Create"

# Installation
${CURL_AUTH_CMD} "${AATAMS_URL}/installation/save" --data "name=Some+Curtain&configuration.id=2&project.id=5&create=Create"

# Station
${CURL_AUTH_CMD} "${AATAMS_URL}/installationStation/save" --data "name=Station+1&curtainPosition=&location_pointInputTextField=10%C2%B0S+10%C2%B0E+%28datum%3AWGS+84%29&location=POINT%2810+-10%29+%2C+4326&location_lon=10&location_lat=-10&location_srid=4326&installation.id=9&create=Create"

# Receiver
${CURL_AUTH_CMD} "${AATAMS_URL}/receiver/save" --data "organisation.id=0&model.id=0&serialNumber=1111&comment=&create=Create"

# Deployment
${CURL_AUTH_CMD} "${AATAMS_URL}/receiverDeployment/save" --data "station.id=11&receiver.id=13&deploymentDateTime=struct&deploymentDateTime_day=6&deploymentDateTime_month=6&deploymentDateTime_year=2000&deploymentDateTime_hour=14&deploymentDateTime_minute=57&deploymentDateTime_second=28&deploymentDateTime_zone=Australia%2FMelbourne&location_pointInputTextField=10%C2%B0S+10%C2%B0+E+%28datum%3AWGS+84%29&location=POINT%2810+-10%29+%2C+4326&location_lon=10&location_lat=-10&location_srid=4326&recoveryDate=date.struct&recoveryDate_day=6&recoveryDate_month=6&recoveryDate_year=2040&acousticReleaseID=&mooringType.id=1&mooringDescriptor=&bottomDepthM=&depthBelowSurfaceM=&receiverOrientation=UP&batteryLifeDays=&comments=&project.id=5&create=Create"

# Recovery
${CURL_AUTH_CMD} "${AATAMS_URL}/receiverRecovery/save" --data "deploymentId=15&projectId=5&recoverer.id=8&location_pointInputTextField=10%C2%B0S+10%C2%B0E+%28datum%3AWGS+84%29&location=POINT%2810.0+-10.0%29+%2C+4326&location_lon=10.0&location_lat=-10.0&location_srid=4326&deployment.initialisationDateTime=struct&deployment.initialisationDateTime_day=6&deployment.initialisationDateTime_month=6&deployment.initialisationDateTime_year=2000&deployment.initialisationDateTime_hour=14&deployment.initialisationDateTime_minute=57&deployment.initialisationDateTime_second=45&deployment.initialisationDateTime_zone=Australia%2FMelbourne&recoveryDateTime=struct&recoveryDateTime_day=6&recoveryDateTime_month=6&recoveryDateTime_year=2040&recoveryDateTime_hour=14&recoveryDateTime_minute=57&recoveryDateTime_second=53&recoveryDateTime_zone=Australia%2FMelbourne&status.id=3&comments=&create=Create"

# Detections
${CURL_AUTH_CMD} "${AATAMS_URL}/receiverDownloadFile/save" -F type=DETECTIONS_CSV -F filedata=@detections.csv

# Tags
${CURL_AUTH_CMD} "${AATAMS_URL}/sensor/save" --data 'tag.serialNumber=11111111&tag.project.id=5&tag.model.id=2&tag.codeMap.id=0&tag.expectedLifeTimeDays=&tag.status.id=1&transmitterType.id=1&pingCode=1111&slope=&intercept=&unit=&create=Create'

# Release
${CURL_AUTH_CMD} "${AATAMS_URL}/animalRelease/save" --data 'project.id=5&speciesId=10029746&speciesName=41112004+-+Balaenoptera+musculus+%28blue+whale%29&sex.id=1&animal.id=&captureLocality=Derwent+River&captureLocation_pointInputTextField=42%C2%B0S+138%C2%B0E+%28datum%3AWGS+84%29&captureLocation=POINT%28138+-42%29+%2C+4326&captureLocation_lon=138&captureLocation_lat=-42&captureLocation_srid=4326&captureDateTime=struct&captureDateTime_day=6&captureDateTime_month=6&captureDateTime_year=2014&captureDateTime_hour=16&captureDateTime_minute=25&captureDateTime_second=23&captureDateTime_zone=Australia%2FMelbourne&captureMethod.id=0&releaseLocality=Derwent+River&releaseLocation_pointInputTextField=42%C2%B0S+138%C2%B0E+%28datum%3AWGS+84%29&releaseLocation=POINT%28138+-42%29+%2C+4326&releaseLocation_lon=138&releaseLocation_lat=-42&releaseLocation_srid=4326&releaseDateTime=struct&releaseDateTime_day=6&releaseDateTime_month=6&releaseDateTime_year=2014&releaseDateTime_hour=16&releaseDateTime_minute=25&releaseDateTime_second=23&releaseDateTime_zone=Australia%2FMelbourne&comments=&embargoPeriod=36&create=Create&surgery.0.timestamp_day=6&surgery.0.timestamp_month=6&surgery.0.timestamp_year=2014&surgery.0.timestamp_hour=16&surgery.0.timestamp_minute=25&surgery.0.timestamp_second=23&surgery.0.timestamp_zone=Australia%2FMelbourne&surgery.0.type.id=1&surgery.0.treatmentType.id=1&surgery.0.comments=&surgery.0.tag.codeMap.id=0&surgery.0.tag.pingCode=1111&surgery.0.tag.serialNumber=11111111&surgery.0.tag.model.id=2&measurement.0.type.id=1&measurement.0.value=26000&measurement.0.unit.id=2&measurement.0.estimate=false&measurement.0.comments='
