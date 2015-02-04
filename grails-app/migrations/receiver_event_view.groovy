databaseChangeLog = {
    changeSet(author: "jburgess", id: "1423026565000-01") {

        grailsChange {
            change {
                sql.execute(String.valueOf("create or replace view receiver_event_view as ${application.config.receiver_event.extract.receiver_event_view.definition}"))
            }
        }
    }
}
