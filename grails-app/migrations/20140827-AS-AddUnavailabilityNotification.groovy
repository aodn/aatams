/*
 * Copyright 2014 IMOS
 *
 * The AATAMS system is distributed under the terms of the GNU General Public License
 *
 */

databaseChangeLog = {

    changeSet(author: "anguss00 (generated)", id: "1409100062-1") {
        insert(tableName: "notification") {
            column(name: "id", value: 6)
            column(name: "version", value: 0)
            column(name: "anchor_selector", value: "#logo > a > img")
            column(name: "html_fragment", value: "The AATAMS system is unavailable between 8pm and 3am")
            column(name: "key", value: "UNAVAILABLE")
            column(name: "unauthenticated", value: true)
        }
    }
}
