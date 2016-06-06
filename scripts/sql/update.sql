
-- invoke with psql
-- sudo -u postgres psql -d aatams -f update.sql -v path="'/home/meteo/content'"

set search_path=aatams,public;

\i runner.sql

-- IMPORTANT - must be wrapped in a transaction block to bail-out on the first error

begin;
-- select runner(:path, 'y.sql');

select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#123.sql');



-- select runner(:path, 'y.sql');
-- select runner(:path, 'y1.sql');
-- select runner(:path, 'y1.sql');
-- select runner(:path, 'y1.sql');
-- select runner(:path, 'y1.sql');
-- select runner(:path, 'y1.sql');

select * from events;

rollback;
end;

