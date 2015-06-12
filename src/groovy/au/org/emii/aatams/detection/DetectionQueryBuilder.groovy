package au.org.emii.aatams.detection

import au.org.emii.aatams.QueryBuilder

import org.jooq.impl.DSL

class DetectionQueryBuilder extends QueryBuilder {
    String getViewName(filterParams) {
        return "valid_detection"
    }

    void addInClauses(query, filterParams) {
        [
            "rxr_project_name": filterParams?.filter?.receiverDeployment?.station?.installation?.project?.in?.getAt(1),
            "installation_name": filterParams?.filter?.receiverDeployment?.station?.installation?.in?.getAt(1),
            "station_name": filterParams?.filter?.receiverDeployment?.station?.in?.getAt(1),
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
