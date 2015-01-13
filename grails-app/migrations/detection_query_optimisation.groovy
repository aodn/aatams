databaseChangeLog = {
    changeSet(author: "jburgess", id: "1420588707000-01") {

        [ 'detection_view', 'detection_by_species_view' ].each {
            viewName ->

            grailsChange {
                change {
                    sql.execute(String.valueOf("create or replace view ${viewName} as ${application.config.detection.extract[viewName].definition}"))
                }
            }
        }

        grailsChange {
            change {
                def numValidDets = sql.rows("select count(*) from detection_view").count[0]
                sql.execute("update statistics set value = ${numValidDets} where key = 'numValidDetections'")
            }
        }
    }
}
