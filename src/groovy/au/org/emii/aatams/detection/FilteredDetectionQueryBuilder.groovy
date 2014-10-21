package au.org.emii.aatams.detection

class FilteredDetectionQueryBuilder extends DetectionQueryBuilder {

    def getFromTable() {
        return 'valid_detection'
    }

    def getSelect() {
        return "SELECT ${fromTable}.id as vd_id"
    }

    def getJoins(params) {
        return getRxrJoins(params) + getTxrJoins(params)
    }

    def getRxrJoins(params) {
        return isFilteredByRxr(params) ? super.getRxrJoins(params) : []
    }

    def getTxrJoins(params) {
        return isFilteredByTxr(params) ? super.getTxrJoins(params) : []
    }

    def getLimit(params) {
        return isFiltered(params) ? '' : "LIMIT ${params?.limit}"
    }

    def toSql(params) {
        return [
            select,
            from,
            getJoins(params).join(System.getProperty('line.separator')),
            getWhereClause(params),
            orderBy,
            getLimit(params)
        ].join(System.getProperty('line.separator'))
    }
}
