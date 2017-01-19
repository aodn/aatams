databaseChangeLog = {
    changeSet(author: 'ankit', id: '1675295815000-00') {
        grailsChange {
            change {
                sql.execute("INSERT INTO code_map(id, version, code_map) VALUES (17, 0, 'A69-9006')")
            }
        }
    }
}
