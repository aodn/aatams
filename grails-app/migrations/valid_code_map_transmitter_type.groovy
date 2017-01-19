databaseChangeLog = {
    changeSet(author: 'ankit', id: '1675295815000-01') {
        sqlFile( path: "tables/valid_code_map_transmitter_type.sql")
    }

    changeSet(author: 'ankit', id: '1675295815000-02') {
        sqlFile( path: "data/valid_code_map_transmitter_type.sql")
    }
}
