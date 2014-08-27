/*
 * Copyright 2014 IMOS
 *
 * The AATAMS system is distributed under the terms of the GNU General Public License
 *
 */

databaseChangeLog = {

    changeSet(author: "anguss00 (generated)", id: "1409101938-1") {
        insert(tableName: "device_model") {
            column(name: "id", value: 30)
            column(name: "version", value: 0)
            column(name: "manufacturer_id", value: 14)
            column(name: "model_name", value: "VR3UWM")
            column(name: "class", value: "au.org.emii.aatams.ReceiverDeviceModel")
        }
    }
}
