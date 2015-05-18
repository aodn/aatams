package au.org.emii.aatams.detection

import au.org.emii.aatams.QueryBuilder

class DetectionQueryBuilder extends QueryBuilder {
    String getViewName(filterParams) {
        return "valid_detection"
    }
}
