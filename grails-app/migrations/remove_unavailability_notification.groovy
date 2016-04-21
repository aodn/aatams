/*
 * Copyright 2016 IMOS
 *
 * The AATAMS system is distributed under the terms of the GNU General Public License
 *
 */

databaseChangeLog = {

    changeSet(author: "ja", id: "1458164417000-01") {
        grailsChange {
            change {
                sql.execute("delete from notification where id = 6 and key = 'UNAVAILABLE'")
            }
        }
    }
}
