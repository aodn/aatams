package au.org.emii.aatams

import org.jooq.impl.DSL

class ReceiverEventQueryBuilder extends QueryBuilder {
    String getViewName(filterParams) {
        return "receiver_event_view"
    }

    void addInClauses(query, filterParams) {
        [
            "project": filterParams?.filter?.receiverDeployment?.station?.installation?.project?.in?.getAt(1),
            "installation": filterParams?.filter?.receiverDeployment?.station?.installation?.in?.getAt(1),
            "station": filterParams?.filter?.receiverDeployment?.station?.in?.getAt(1),
            "transmitter_id": filterParams?.filter?.in?.getAt(1),
            "spcode": filterParams?.filter?.surgeries?.release?.animal?.species?.in?.getAt(1)
        ].each {
            field, filterValues ->

            if (filterValues) {
                query.where(DSL.fieldByName(field).in(delimitedFilterValuesToList(filterValues)))
            }
        }
    }
}
