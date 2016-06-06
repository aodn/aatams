
-- simple event log to record ad-hoc sql actions
-- relies on filename uniqueness to guarantee idempotency, easy to add md5


create or replace function exec_returning_string(text) returns text
language plpgsql volatile
as $$
    declare
      ret text;
    begin
      execute $1 into ret;
      return ret;
    end;
$$;


create table if not exists events (
  filename text not null,
  source text not null,
  md5 text not null,
  start timestamptz not null,
  finish timestamptz not null,
  result text not null 
);


create or replace function runner(_path text, _basename text) returns void 
language plpgsql volatile
as $$
  DECLARE _filename text;
  DECLARE _oid integer;
  DECLARE _exists boolean;
  DECLARE _source text;
  DECLARE _md5 text;
  DECLARE _result text;
  DECLARE _start timestamptz;
  DECLARE _finish timestamptz;
begin

  _filename := (select _path || '/' || _basename);
  -- _basename := (select regexp_replace( _filename, '^.*\/', '', 'g'));

  _oid := (select lo_import(_filename));
  _source := (select encode(lo_get(_oid), 'escape'));
  perform lo_unlink(_oid);
  _md5 := (select md5(_source));

  -- check if we already successfully executed the script
  _exists := (
    select exists (
      select 1 
      from events 
      where events.filename = _basename
      -- and events.md5 = _md5
  ));

  if _exists = false THEN
    -- no, so run it
    raise notice 'running: %', _filename;

    _start := now();
    _result := (select exec_returning_string(_source));
    _finish := now();

    -- update table 
    insert into events(filename,source, md5, start, finish, result) 
    values (_basename, _source, _md5, _start, _finish, _result);
  else
    -- just report
    raise notice 'skipping: %', _filename;
  end if;

end $$;

