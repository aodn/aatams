--
-- event log to play ad-hoc sql
-- relies on filename uniqueness to guarantee idempotency
-- Usage: sudo -u postgres psql -d aatams -f update.sql -v path="'/home/meteo/aatams/scripts/sql'"
--


create or replace function exec_returning_string(s text)
returns text
language plpgsql volatile
as $$
  declare
    ret text;
  begin
    execute s into ret;
    return ret;
  end;
$$;


create or replace function strip_comments(s text)
returns text
language plpgsql
as $$
  declare
    ret text;
  begin
    select regexp_replace(s, '^--[ \t-]+.*', '', 'gn') into ret;
    return ret;
  end;
$$;


create or replace function split_stmts(s text)
returns setof text
language plpgsql
as $$
  declare
    ret text;
  begin
    return query (select * from unnest(regexp_split_to_array(s, ';')));
  end;
$$;


create or replace function trim_whitespace(s text)
returns text
language plpgsql
as $$
  declare
    ret text;
  begin
    select regexp_replace(s, '^\s*(.*)\s*$', '\1', 'g') into ret;
    return ret;
  end;
$$;


create or replace function file_read(filename text)
returns text
language plpgsql volatile
as $$
  declare
    ret     text;
    _oid    integer;
  begin
    select lo_import(filename) into _oid;
    select encode(lo_get(_oid), 'escape') into ret;
    perform lo_unlink(_oid);
    return ret;
  end
$$;


create or replace function runner(_path text, _basename text)
returns void
language plpgsql volatile
as $$
  declare _filename     text;
  declare _previously_run boolean;
  declare _source_text  text;
  declare _source_md5   text;
  declare _event_id     bigint;
  declare _sql          text;
  declare _result       text;
  declare _start        timestamptz;

begin
  select exists (
    select 1
      from event
      where event.filename = _basename
    ) into _previously_run;

  if _previously_run = false then
    -- run it
    raise notice 'running file: %', _basename;

    select _path || '/' || _basename into _filename;
    select file_read(_filename) into _source_text;
    select md5(_source_text) into _source_md5;

    insert
      into event(filename, source, md5)
      values (_basename, _source_text, _source_md5)
      returning id
      into _event_id;

    for _sql in select trim_whitespace(sql) from split_stmts(strip_comments(_source_text)) as sql
    loop

      if _sql <> '' then
        raise notice 'running sql: %', _sql;

        select now() into _start;
        select exec_returning_string(_sql) into _result;

        insert into action(event_id, sql, result, start, finish)
        values (_event_id, _sql, _result, _start, now());

      end if;
    end loop;

  else
    -- report only
    raise notice 'skipping: %', _basename;
  end if;
end
$$;


begin;

set search_path=aatams,public;

create table if not exists event
(
  id        bigserial primary key,
  filename  text not null,
  source    text not null,
  md5       text not null
);

create table if not exists action
(
  id        bigserial primary key,
  event_id  bigint not null references event(id),
  sql       text not null,
  result    text not null,
  start     timestamptz not null,
  finish    timestamptz not null
);


select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#123.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#270.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#312_V13.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#312_V13.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#319.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#326.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_#329.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_AndresSpreadsheet_Sheet2#240-1.sql');
select runner(:path, 'xavier-changes-2016_April/aatamsDB_queries_AndresSpreadsheet_DuplicateTagID_#240-2.sql');

select runner(:path, 'xavier-changes-2016_May/aatamsDB_queries_#331.sql');
select runner(:path, 'xavier-changes-2016_May/aatamsDB_queries_#333.sql');


-- Testing
-- select filename from event;
-- select * from action;
-- rollback;

end;

