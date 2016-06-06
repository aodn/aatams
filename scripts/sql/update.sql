
-- invoke with psql
-- sudo -u postgres psql -d aatams -f update.sql -v path="'/home/meteo/aatams/scripts/sql'"

set search_path=aatams,public;

\i runner.sql

-- IMPORTANT - we wrap in a transaction block in order to bail-out at the first error
begin;

select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#123.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#270.sql');



-- for testing
select result from events;
rollback;
end;

