/*
 * Copyright 2014 IMOS
 *
 * The AATAMS system is distributed under the terms of the GNU General Public License
 *
 */

databaseChangeLog = {

    changeSet(author: "ja", id: "1458164416000-01") {
        grailsChange {
            change {
                sql.execute("update notification set html_fragment = 'Click here to register to use the IMOS Animal Tracking Database' where key = 'REGISTER'")
            }
        }
    }
}
