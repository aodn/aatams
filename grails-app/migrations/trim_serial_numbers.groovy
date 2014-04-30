databaseChangeLog = {

    // merge any duplicates
    changeSet(author: "jburgess", id: "1398819711000-01") {

        // This change searches for cases where device serial numbers are duplicated, differing only
        // in leading/trailing whitespace.
        //
        // In these cases, the "non-trimmed" versions are deleted, with references to them updated to the trimmed
        // version first.
        grailsChange {
            change {
                removeDuplicates(
                    sql,
                    ['installation_station_receiver', 'receiver_deployment'],
                    'receiver_id',
                    'au.org.emii.aatams.Receiver'
                )

                removeDuplicates(
                    sql,
                    ['sensor', 'surgery'],
                    'tag_id',
                    'au.org.emii.aatams.Tag'
                )
            }
        }

        update(tableName: 'device') {
            column name: 'serial_number', valueComputed: "trim(both ' ' from serial_number)"
            where "serial_number like '% '"
        }
    }
}

def removeDuplicates(sql, referencingTables, columnName, entityClass) {
    sql.eachRow("select * from device where class = ${entityClass} and serial_number like '% '") {
        whitespace_device ->

        sql.eachRow(
            "select * from device where class = ${entityClass} and serial_number = trim(both ' ' from ${whitespace_device.serial_number})") {
            non_whitespace_device ->

            referencingTables.each {
                tableName ->
                    replaceField(sql, tableName, columnName, whitespace_device.id, non_whitespace_device.id)
            }

            sql.execute("delete from device where id = ${whitespace_device.id}")
        }
    }
}

def replaceField(sql, table, column, oldVal, newVal) {
    sql.execute(String.format("update %1$s set %2$s = %3$d where %2$s = %4$d", table, column, newVal, oldVal))
}
