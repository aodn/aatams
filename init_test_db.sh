PSQL_ARGS=${1:--U postgres}
psql $PSQL_ARGS -c "drop database aatams_test"
psql $PSQL_ARGS -c "drop user aatams_test"
psql $PSQL_ARGS -c "create user aatams_test unencrypted password 'aatams_test'"
psql $PSQL_ARGS -c "create database aatams_test owner aatams_test"
psql $PSQL_ARGS -c "create extension postgis" aatams_test
psql $PSQL_ARGS -c "create schema aatams authorization aatams_test" aatams_test
psql $PSQL_ARGS -c "alter database aatams_test set search_path to 'aatams', 'public'" aatams_test
psql $PSQL_ARGS -c "alter database aatams_test set timezone to 'Australia/Hobart'" aatams_test
