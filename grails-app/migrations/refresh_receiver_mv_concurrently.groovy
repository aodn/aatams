databaseChangeLog = {
    changeSet(author: "jburgess", id: "1446761404-01") {
        createProcedure(
            '''CREATE OR REPLACE FUNCTION refresh_receiver_mv()
               RETURNS trigger AS $$
                 BEGIN
                   REFRESH MATERIALIZED VIEW CONCURRENTLY receiver;
                   RETURN NEW;
                 END;
               $$ LANGUAGE plpgsql;'''
        )

        // See: http://www.postgresql.org/docs/9.4/static/sql-refreshmaterializedview.html -
        // "[Concurrent refresh] is only allowed if there is at least one UNIQUE index..."
        createIndex(indexName: 'receiver_id_idx', tableName: 'receiver', unique: true) {
            column(name: 'id')
        }
    }
}
