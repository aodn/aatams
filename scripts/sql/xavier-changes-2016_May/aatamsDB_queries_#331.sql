
-- Fix #331 - Accidentally registered a person in the database twice
DELETE FROM sec_user WHERE username = 'jmcallister' returning *;

---- Test results
-- SELECT *
-- FROM sec_user s
-- WHERE username = 'jmcallister';

