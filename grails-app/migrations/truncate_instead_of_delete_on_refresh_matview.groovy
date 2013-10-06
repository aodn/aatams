databaseChangeLog = {

    changeSet(author: "jburgess", id: "1380846695000-01") {
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

                            EXECUTE 'TRUNCATE ' || matview;
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
