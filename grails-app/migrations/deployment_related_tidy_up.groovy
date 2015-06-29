databaseChangeLog = {
    changeSet(author: 'jburgess', id: '1435557080000-00') {
        dropTable(tableName: 'installation_station_receiver')
    }

    changeSet(author: 'jburgess', id: '1435557080000-01') {
        dropColumn(tableName: 'installation_station', columnName: 'num_deployments')
    }
}
