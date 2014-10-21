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

    def getQueryBuilder() {
        return new DetectionQueryBuilder()
    }

    private String constructQuery(constraints) {
        def query = getQueryBuilder().toSql(requestParamsToQueryBuilderParams(constraints))
        log.debug("Query: ${query}")

        return query
    }

    def requestParamsToQueryBuilderParams(constraints) {
        if (!constraints) {
            return null
        }

        def params = constraints.clone()
        params.filter = requestParamsToFilter(constraints)
        params.limit = params.max

        return params
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
}
