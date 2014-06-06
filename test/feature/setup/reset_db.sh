#!/bin/bash

# Reset
dropdb -h localhost -U postgres aatams
createdb -h localhost -U postgres -O aatams aatams
psql -h localhost -U postgres -c "create extension postgis;" aatams
psql -h localhost -U postgres -c "create schema authorization aatams;" aatams
cd ../../..

# Note: this does throw up some exceptions but they can be ignored.
grails dbm-update --defaultSchema=aatams
cd -

psql -h localhost -U postgres -f reference_data.sql aatams
psql -h localhost -U postgres -f insert_admin_user.sql aatams

psql -h localhost -U postgres -c "COPY aatams.species (ID, SPCODE, COMMON_NAME, SCIENTIFIC_NAME, AUTHORITY, FAMILY, FAMILY_SEQUENCE, ASSIGNED_FAMILY_CODE, ASSIGNED_FAMILY_SEQUENCE, RECENT_SYNONYMS, COMMON_NAMES_LIST, GENUS, SPECIES, SCINAME_INFORMAL, DATE_LAST_MODIFIED, SUBSPECIES, VARIETY, UNDESCRIBED_SP_FLAG, HABITAT_CODE, OBIS_CLASSIFICATION_CODE, SUBGENUS, KINGDOM, PHYLUM, SUBPHYLUM, SPCLASS, SUBCLASS, ORDER_NAME, SUBORDER, INFRAORDER, VERSION, CLASS) FROM '`pwd`/../../../resources/caab/caab_dump_latest.txt.cleaned' DELIMITER E'\t' CSV HEADER;" aatams
