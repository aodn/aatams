databaseChangeLog = {
    changeSet(author: "dnahodil", id: "1412032256000-1", failOnError: true) {

        preConditions(onFail: "MARK_RAN") {
            sqlCheck(expectedResult: 0, '''select count(*) from matviews where mv_name like 'detection_count_per_station_mv';''')
        }

        insert(tableName: "matviews") {
            column(name: "mv_name", value: "detection_count_per_station_mv")
            column(name: "v_name", value: "detection_count_per_station")
        }
    }

    changeSet(author: "dnahodil", id: "1412032256000-2", failOnError: true) {

        preConditions(onFail: "MARK_RAN") {
            sqlCheck(expectedResult: 0, '''select count(*) from matviews where mv_name like 'detection_extract_view_mv';''')
        }

        insert(tableName: "matviews") {
            column(name: "mv_name", value: "detection_extract_view_mv")
            column(name: "v_name", value: "detection_extract_view")
        }
    }
}
