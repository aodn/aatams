#!/bin/bash

# Reset
dropdb -h localhost -U postgres aatams
createdb -h localhost -U postgres -O aatams aatams
psql -h localhost -U postgres -c "create extension postgis;" aatams
psql -h localhost -U postgres -c "create schema authorization aatams;" aatams
cd ../../
grails dbm-update --defaultSchema=aatams
cd -
psql -h localhost -U postgres -f insert_admin_user.sql aatams
