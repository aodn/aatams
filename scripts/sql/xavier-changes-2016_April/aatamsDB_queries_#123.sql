

-- Fix #123
DELETE FROM animal a
WHERE a.id NOT IN (SELECT animal_id FROM animal_release) RETURNING *;

---- Test results
-- SELECT a.* FROM animal a 
-- FULL JOIN animal_release ar ON ar.animal_id = a.id 
-- WHERE animal_id IS NULL 
-- ORDER BY a.id;

