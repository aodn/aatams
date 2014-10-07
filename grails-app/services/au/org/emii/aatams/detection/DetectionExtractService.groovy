package au.org.emii.aatams.detection

import java.io.OutputStream;

import org.apache.shiro.SecurityUtils
import org.joda.time.DateTime

import au.org.emii.aatams.export.AbstractStreamingExporterService
import au.org.emii.aatams.util.GeometryUtils
import groovy.sql.Sql

class DetectionExtractService extends AbstractStreamingExporterService
{
    static transactional = false

    def dataSource
    def permissionUtilsService

    public List extractPage(filterParams)
    {
        def startTime = System.currentTimeMillis()
        def results = filterParams.sql.rows(constructQuery(filterParams))

        log.debug("Query finished in ${System.currentTimeMillis() - startTime}ms with ${results.size()} results")

        return results
    }

    public Long getCount(filterParams)
    {
        assert false: "shouldn't get here"

        if (!filterParams || filterParams.isEmpty() || filterParams?.filter == null || filterParams?.filter == [:])
        {
            return ValidDetection.count()
        }

        def startTime = System.currentTimeMillis()
        filterParams.countOnly = true
        def results = filterParams.sql.rows(constructQuery(filterParams))
        log.debug("Count query finished in ${System.currentTimeMillis() - startTime}ms with ${results.count[0]} results")

        return results.count[0]
    }

    protected String getReportName()
    {
        return "detection"
    }

    protected def applyEmbargo(results, params)
    {
        def now = new Date()

        results = results.grep {

            row ->

            boolean isEmbargoed = (row.embargo_date && row.embargo_date.after(now) && !hasReadPermission(row.release_project_id, params))

            return !isEmbargoed
        }

        return results
    }

    protected void writeCsvData(final filterParams, OutputStream out)
    {
        filterParams.sql = new Sql(dataSource)
        filterParams.projectPermissionCache = [:]

        super.writeCsvData(filterParams, out)
    }

    def generateReport(filterParams, req, res)
    {
        super.generateReport(filterParams, req, res)
    }

    protected List readData(filterParams)
    {
        return extractPage(filterParams)
    }

    protected def writeCsvChunk(resultList, OutputStream out)
    {
        resultList.each
        {
            row ->

            out << row.formatted_timestamp << ","
            out << row.installation_station_name << ","
            out << GeometryUtils.scrambleCoordinate(row.latitude) << ","
            out << GeometryUtils.scrambleCoordinate(row.longitude) << ","
            out << row.receiver_name << ","
            out << (row.transmitter_id ?: '') << ","
            out << (row.spcode ? ("${row.spcode} - ${row.scientific_name} (${row.common_name})") : '') << ","
            out << row.uploader << ","
            out << row.valid_detection_transmitter_id << ","
            out << row.organisation_name << ","
            out << (row.sensor_value ?: '') << ","
            out << (row.sensor_unit ?: '')

            out << "\n"
        }

        return resultList.size()
    }

    protected void writeCsvHeader(OutputStream out)
    {
        out << "timestamp,"
        out << "station name,"
        out << "latitude,"
        out << "longitude,"
        out << "receiver ID,"
        out << "tag ID,"
        out << "species,"
        out << "uploader,"
        out << "transmitter ID,"
        out << "organisation,"
        out << "sensor value,"
        out << "sensor unit"

        out << "\n"
    }

    protected void updateIndex(results, params) {
        if (results.size() > 0) {

            params.pageIndex = PageIndex.fromRequestParams([
                timestamp: results.last().timestamp,
                receiverName: results.last().receiver_name,
                transmitterId: results.last().valid_detection_transmitter_id
            ])
        }
    }

    private String toSqlFormat(String formParam)
    {
        List<String> values = Arrays.asList(formParam.trim().split(",")).collect { it.trim() }

        String sqlFormat = ""
        values.eachWithIndex
        {
            value, i ->

            sqlFormat += "'" + value + "'" + ", "
        }

        return sqlFormat[0..sqlFormat.length() - 3]
    }

    private boolean hasReadPermission(projectId, params)
    {
        if (projectId == null)
        {
            return true
        }

        if (!params.projectPermissionCache.containsKey(projectId))
        {
            String permissionString = permissionUtilsService.buildProjectReadPermission(projectId)
            params.projectPermissionCache.put(projectId, SecurityUtils.subject.isPermitted(permissionString))
        }

        assert(params.projectPermissionCache.containsKey(projectId))
        return params.projectPermissionCache[projectId]
    }

    private String constructQuery(constraints)
    {
        def query = [
            getSelect(constraints),
            getWhereClause(
                filter: requestParamsToFilter(constraints),
                pageIndex: constraints?.pageIndex
            ),
            getOrderByClause(constraints),
            getLimitClause(limit: constraints?.max)
        ].grep { it }
        .join(System.getProperty("line.separator"))

        log.debug("Query: ${query}")

        return query
    }

    private String getSelect(constraints) {
        return String.format(SELECT, constraints?.countOnly ? selectCountColumns: selectColumns)
    }

    private String getSelectColumns() {
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
        ].join(', ')
    }

    private String getSelectCountColumns() {
        return "COUNT(*)"
    }

    private String getWhereClause(constraints)
    {
        if (!constraints?.filter && !constraints?.pageIndex)
        {
            return ''
        }

        def pageIndexClause = constraints?.pageIndex?.toSqlString()

        def inClauses = constraints?.filter?.in?.collect {
            def values = it.values.collect { val -> val.trim() }.grep { val -> val }.collect { val ->  "'${val}'" }.join(', ')
            if (!values.isEmpty()) {
                return "${it.field} IN (${values})"
            }

            return ''
        }

        def betweenClauses = constraints?.filter?.between?.collect {
            return "${it.field} BETWEEN '${it.start}' AND '${it.end}'"
        }

        def nonEmptyClauses = [pageIndexClause, inClauses, betweenClauses].flatten().grep {it}

        if (!nonEmptyClauses.isEmpty()) {
            return "WHERE ${nonEmptyClauses.join(' AND ')}"
        }

        return ''
    }

    private String getOrderByClause(constraints)
    {
        return constraints?.countOnly ? '' : 'ORDER BY timestamp, receiver_name, valid_detection.transmitter_id'
    }

    private String getLimitClause(constraints)
    {
        return constraints?.limit ? "LIMIT ${constraints?.limit}" : ''
    }

    private def requestParamsToFilter(requestParams) {

        if (!requestParams?.filter) {
            return null
        }

        def filter = [:]

        filter.in = [
            "project.name": requestParams?.filter?.receiverDeployment?.station?.installation?.project?.in?.getAt(1),
            "installation.name": requestParams?.filter?.receiverDeployment?.station?.installation?.in?.getAt(1),
            "installation_station.name": requestParams?.filter?.receiverDeployment?.station?.in?.getAt(1),
            "valid_detection.transmitter_id": requestParams?.filter?.in?.getAt(1),
            "spcode": requestParams?.filter?.surgeries?.release?.animal?.species?.in?.getAt(1)
        ].findAll { it.value }
        .collect {
            k, v ->

            [field: k, values: v.trim().split(',').grep { it }.collect { it.trim() }]
        }

        filter.between = [
            "timestamp": requestParams?.filter?.between
        ].findAll { it.value }
        .collect {
            k, v ->

                assert (v && v.'0' && v.'1' && v.'2') : "Invalid timestamp range query."

            [field: k, start: new DateTime(v.'1'), end: new DateTime(v.'2')]
        }

        return filter.findAll { !it.value.isEmpty() }
    }

    static String SELECT = '''SELECT %s FROM valid_detection

-- tag metadata *may* or *may not* exist
left join sensor on valid_detection.transmitter_id = sensor.transmitter_id
left join device tag on sensor.tag_id = tag.id

-- as with surgery metadata
left join surgery on tag.id = surgery.tag_id
left join animal_release on surgery.release_id = animal_release.id
left join animal on animal_release.animal_id = animal.id
left join species on animal.species_id = species.id

-- receiver metadata must exist (otherwise the detection would be invalid)
join receiver_deployment on receiver_deployment_id = receiver_deployment.id
join installation_station on receiver_deployment.station_id = installation_station.id
join installation on installation_station.installation_id = installation.id
join project on installation.project_id = project.id
join device on receiver_deployment.receiver_id = device.id
join device_model on device.model_id = device_model.id
join receiver_download_file on receiver_download_id = receiver_download_file.id
join sec_user on receiver_download_file.requesting_user_id = sec_user.id
join organisation on device.organisation_id = organisation.id'''
}
