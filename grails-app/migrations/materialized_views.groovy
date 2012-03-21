databaseChangeLog = 
{
	// See: http://tech.jonathangardner.net/wiki/PostgreSQL/Materialized_Views
	changeSet(author: "jburgess", id: "1332309793000-1")
	{
		createTable(tableName: "matviews") 
		{
			column(name: "mv_name", type: "NAME") 
			{
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "matviews_pkey")
			}

			column(name: "v_name", type: "NAME") 
			{
				constraints(nullable: "false")
			}

			column(name: "last_refresh", type: "TIMESTAMP WITH TIME ZONE")
		}
	}
	
	changeSet(author: "jburgess", id: "1332309793000-2", runOnChange: true)
	{
		createProcedure('''
						CREATE OR REPLACE FUNCTION create_matview(NAME, NAME)
						RETURNS VOID
						SECURITY DEFINER
						LANGUAGE plpgsql AS
						$$
						DECLARE
							matview ALIAS FOR $1;
							view_name ALIAS FOR $2;
							entry matviews%ROWTYPE;
						BEGIN
							SELECT * INTO entry FROM matviews WHERE mv_name = matview;
						
							IF FOUND THEN
								RAISE EXCEPTION 'Materialized view % already exists.', matview;
							END IF;
						
							EXECUTE 'REVOKE ALL ON ' || view_name || ' FROM PUBLIC';
						
							EXECUTE 'GRANT SELECT ON ' || view_name || ' TO PUBLIC';
						
							EXECUTE 'CREATE TABLE ' || matview || ' AS SELECT * FROM ' || view_name;
						
							EXECUTE 'REVOKE ALL ON ' || matview || ' FROM PUBLIC';
						
							EXECUTE 'GRANT SELECT ON ' || matview || ' TO PUBLIC';
						
							INSERT INTO matviews (mv_name, v_name, last_refresh)
							  VALUES (matview, view_name, CURRENT_TIMESTAMP);
							
							RETURN;
						END;
						$$''')
	}
	
	changeSet(author: "jburgess", id: "1332309793000-3", runOnChange: true)
	{
		createProcedure('''
						CREATE OR REPLACE FUNCTION drop_matview(NAME) RETURNS VOID
						SECURITY DEFINER
						LANGUAGE plpgsql AS 
						$$
						DECLARE
						    matview ALIAS FOR $1;
						    entry matviews%ROWTYPE;
						BEGIN
						
						    SELECT * INTO entry FROM matviews WHERE mv_name = matview;
						
						    IF NOT FOUND THEN
						        RAISE EXCEPTION 'Materialized view % does not exist.', matview;
						    END IF;
						
						    EXECUTE 'DROP TABLE ' || matview;
						    DELETE FROM matviews WHERE mv_name=matview;
						
						    RETURN;
						END;
						$$''')
	}

	changeSet(author: "jburgess", id: "1332309793000-4", runOnChange: true)
	{
		createProcedure('''
						CREATE OR REPLACE FUNCTION refresh_matview(name) RETURNS VOID
						SECURITY DEFINER
						LANGUAGE plpgsql AS 
						$$
						DECLARE 
						    matview ALIAS FOR $1;
						    entry matviews%ROWTYPE;
						BEGIN
						
						    SELECT * INTO entry FROM matviews WHERE mv_name = matview;
						
						    IF NOT FOUND THEN
						         RAISE EXCEPTION 'Materialized view % does not exist.', matview;
						    END IF;
						
						    EXECUTE 'DELETE FROM ' || matview;
						    EXECUTE 'INSERT INTO ' || matview
						        || ' SELECT * FROM ' || entry.v_name;
						
						    UPDATE matviews
						        SET last_refresh=CURRENT_TIMESTAMP
						        WHERE mv_name=matview;
						
						    RETURN;
						END;
						$$''')
	}
}
