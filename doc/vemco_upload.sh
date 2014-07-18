#!/bin/bash

AATAMS_URL="http://aatams-rc.aodn.org.au/aatams"
COOKIE_FILE=`mktemp`
CURL_AUTH_CMD="curl -b ${COOKIE_FILE} -i"

# Authenticate.
curl "${AATAMS_URL}/auth/signIn" --data "username=vemco&password=vemco"  -c ${COOKIE_FILE}

# Upload data.
${CURL_AUTH_CMD} -L "${AATAMS_URL}/receiverDownloadFile/save?format=xml" \
    -F "type=VUE_XML_ZIPPED" \
    -F "example.zip=@example.zip"

# Get status.
# ${CURL_AUTH_CMD} "${AATAMS_URL}/receiverDownloadFile/show/1247?format=xml" \
#     -H "Accept: application/xml"

# Cleanup.
rm -f ${COOKIE_FILE}
