databaseChangeLog = {

    changeSet(author: "jburgess", id: "1386282692000-1") {
        grailsChange {
            change {
                def manufacturerId =
                    sql.firstRow("select * from aatams.device_manufacturer where manufacturer_name = 'Vemco';").id
                def id = sql.firstRow('select max(id) from aatams.device_model;').max + 1
                sql.execute("insert into aatams.device_model values (${id}, 0, ${manufacturerId}, 'VR4-UWM', 'au.org.emii.aatams.ReceiverDeviceModel');")
            }
        }
    }
}
