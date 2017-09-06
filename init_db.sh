PSQL_ARGS=${1:--U postgres}
psql $PSQL_ARGS -c "drop database if exists aatams" || exit
psql $PSQL_ARGS -c "drop user if exists aatams" || exit
psql $PSQL_ARGS -c "create user aatams unencrypted password 'aatams'" || exit
psql $PSQL_ARGS -c "create database aatams owner aatams" || exit
psql $PSQL_ARGS -c "create extension postgis" aatams || exit
psql $PSQL_ARGS -c "create schema aatams authorization aatams" aatams || exit
psql $PSQL_ARGS -c "alter database aatams set search_path to 'aatams', 'public'" aatams || exit
psql $PSQL_ARGS -c "alter database aatams set timezone to 'Australia/Hobart'" aatams || exit
