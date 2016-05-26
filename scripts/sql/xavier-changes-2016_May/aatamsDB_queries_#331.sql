set search_path = aatams, public;

begin;

-- Fix #331 - Accidentally registered a person in the database twice
DELETE FROM sec_user WHERE username = 'jmcallister';

---- Test results
-- SELECT *
-- FROM sec_user s
-- WHERE username = 'jmcallister';

commit;