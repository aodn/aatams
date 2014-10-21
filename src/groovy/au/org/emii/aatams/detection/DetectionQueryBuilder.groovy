package au.org.emii.aatams.detection

class DetectionQueryBuilder {

    def getFromTable() {
        return 'detection_ids'
    }

    def getSelectColumns() {
        // Need to alias some columns due to name clashes.
        [
            "valid_detection.id as valid_detection_id",
            "to_char((timestamp::timestamp with time zone) at time zone '00:00', 'YYYY-MM-DD HH24:MI:SS') as formatted_timestamp",
            "valid_detection.transmitter_id as valid_detection_transmitter_id",
            "installation_station.name as installation_station_name",
            "st_y(installation_station.location) as latitude",
            "st_x(installation_station.location) as longitude",
            "sec_user.name as uploader",
            "organisation.name as organisation_name",
            "animal_release.project_id as release_project_id",
            "*"
        ]
    }

    def getSelect() {
        return "SELECT ${selectColumns.join(', ')}"
    }

    def getFrom() {
        return "FROM ${fromTable}"
    }

    def getOrderBy() {
        return "ORDER BY timestamp, receiver_name, valid_detection.transmitter_id"
    }

    def getJoins(params) {
        return [ getValidDetectionJoin() ] + getRxrJoins(params) + getTxrJoins(params) + getOrgAndUserJoins(params)
    }

    def getRxrJoins(params) {
        return [
            "join receiver_deployment on receiver_deployment_id = receiver_deployment.id",
            "join installation_station on receiver_deployment.station_id = installation_station.id",
            "join installation on installation_station.installation_id = installation.id",
            "join project on installation.project_id = project.id"
        ]
    }

    def getTxrJoins(params) {
        return [
            "left join sensor on valid_detection.transmitter_id = sensor.transmitter_id",
            "left join device tag on sensor.tag_id = tag.id",
            "left join surgery on tag.id = surgery.tag_id",
            "left join animal_release on surgery.release_id = animal_release.id",
            "left join animal on animal_release.animal_id = animal.id",
            "left join species on animal.species_id = species.id"
        ]
    }

    def getValidDetectionJoin() {
        return "join valid_detection on detection_ids.vd_id = valid_detection.id"
    }

    def getOrgAndUserJoins(params) {
        return [
            "join device on receiver_deployment.receiver_id = device.id",
            "join device_model on device.model_id = device_model.id",
            "join receiver_download_file on receiver_download_id = receiver_download_file.id",
            "left join sec_user on receiver_download_file.requesting_user_id = sec_user.id",
            "join organisation on device.organisation_id = organisation.id"
        ]
    }

    boolean isFiltered(params) {
        return isFilteredByRxr(params) || isFilteredByTxr(params)
    }

    boolean isFilteredByRxr(params) {
        def fieldNames = params?.filter?.in?.collect { it.field }
        return (
            fieldNames?.contains('project.name') ||
            fieldNames?.contains('installation.name') ||
            fieldNames?.contains('installation_station.name')
        )
    }

    boolean isFilteredByTxr(params) {
        def fieldNames = params?.filter?.in?.collect { it.field }
        return (
            fieldNames?.contains('spcode')
        )
    }

    def getWhere(params) {
        def pageIndex = params?.pageIndex?.toSqlString()

        def inFilters = params?.filter?.in?.collect {
            def values = it.values.collect { val -> val.trim() }.grep { val -> val }.collect { val ->  "'${val}'" }.join(', ')
            if (!values.isEmpty()) {
                return "${it.field} IN (${values})"
            }

            return ''
        }.grep { it.trim() }

        def betweenFilters = params?.filter?.between?.collect {
            return "${it.field} BETWEEN '${it.start}' AND '${it.end}'"
        }

        return [pageIndex, inFilters, betweenFilters].flatten().grep { it }
    }

    def getWhereClause(params) {
        def wheres = getWhere(params)

        wheres.isEmpty() ? '' : "WHERE ${wheres.join(' AND ')}"

    }

    def getLimit(params) {
        return "LIMIT ${params?.limit}"
    }

    def getWith(params) {
        return [
            "WITH filtered_detections as (${getFilteredDetectionsCet(params)})",
            "detection_ids as (select vd_id from filtered_detections ${getLimit(params)})"
        ].join(",${System.getProperty('line.separator')}")
    }

    def getFilteredDetectionsCet(params) {
        def filteredQuery = new FilteredDetectionQueryBuilder()
        return filteredQuery.toSql(params)
    }

    def toSql(params) {
        return [
            getWith(params),
            select,
            from,
            getJoins(params).join(System.getProperty('line.separator')),
            getWhereClause(params),
            orderBy,
            getLimit(params)
        ].join(System.getProperty('line.separator'))
    }
}
