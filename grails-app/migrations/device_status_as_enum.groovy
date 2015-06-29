databaseChangeLog = {
    changeSet(author: 'jburgess', id: '1435295815000-00') {
        grailsChange {
            change {
                sql.execute("update aatams.device_status set status = 'RETURNED_TO_VENDOR' where status = 'RETURNED TO VENDOR'")
            }
        }
    }

    changeSet(author: 'jburgess', id: '1435295815000-01') {

        addColumn(tableName: 'receiver_recovery') {
            column(name: 'status', type: 'VARCHAR(255)', defaultValue: 'RECOVERED') {
                constraints(nullable: 'false')
            }
        }

        grailsChange {
            change {
                sql.execute('update aatams.receiver_recovery set status = (select status from device_status where id = status_id)')
            }
        }

        dropColumn(tableName: 'receiver_recovery', columnName: 'status_id')
    }

    changeSet(author: 'jburgess', id: '1435295815000-02') {
        dropIndex(tableName: 'device', indexName: 'device_status_index')
        dropIndex(tableName: 'device', indexName: 'status_index')

        addColumn(tableName: 'device') {
            column(name: 'status', type: 'VARCHAR(255)', defaultValue: 'NEW') {
                constraints(nullable: 'true')
            }
        }

        grailsChange {
            change {
                sql.execute('update aatams.device set status = (select status from device_status where id = status_id)')
            }
        }

        dropColumn(tableName: 'device', columnName: 'status_id')

        createIndex(indexName: 'device_status_index', tableName: 'device', unique: 'false') {
            column(name: 'status')
        }
    }

    changeSet(author: 'jburgess', id: '1435295815000-03') {
        dropTable(tableName: 'device_status')
    }
}
