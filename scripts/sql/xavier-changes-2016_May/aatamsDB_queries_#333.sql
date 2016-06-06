
-- Fix #333 - Remove Andrew Boomer from all projects
UPDATE project_role SET person_id = 1605188 WHERE person_id = 9106 returning *;

---- Test results
-- SELECT pr.*, p.name AS project_name, prt.display_name
-- FROM project_role pr
-- JOIN project p ON p.id = pr.project_id
-- JOIN project_role_type prt ON prt.id = pr.role_type_id
-- WHERE person_id = 9106;

-- SELECT *
-- FROM sec_user s
-- WHERE name = 'Andrew Boomer'; -- sec_user.id = 9106

-- SELECT *
-- FROM sec_user s
-- WHERE name = 'Andre Steckenreuter'; -- sec_user.id = 1605188

