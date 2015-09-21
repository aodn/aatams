  SELECT DISTINCT p.name AS project_name,
	i.name AS installation_name,
	ist.name AS station_name,
	ist.location AS geom
  FROM installation_station ist
  LEFT JOIN installation i ON i.id = ist.installation_id
  LEFT JOIN project p ON i.project_id = p.id
	ORDER BY i.name;