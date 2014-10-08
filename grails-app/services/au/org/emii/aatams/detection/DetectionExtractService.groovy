package au.org.emii.aatams.detection

import org.apache.shiro.SecurityUtils

import au.org.emii.aatams.export.AbstractStreamingExporterService
import au.org.emii.aatams.util.GeometryUtils
import groovy.sql.Sql

class DetectionExtractService extends AbstractStreamingExporterService {
    static transactional = false

    def dataSource
    def permissionUtilsService

    public List extractPage(filterParams)
    {
        def query = new QueryBuilder().constructQuery(filterParams)

        def startTime = System.currentTimeMillis()
        def results = filterParams.sql.rows(query.getSQL(), query.getBindValues())
        def endTime = System.currentTimeMillis()
        log.debug("Query finished, num results: ${results.size()}, elapsed time (ms): ${endTime - startTime}")

        return results
    }

    public Long getCount(filterParams) {
        if (!filterParams || filterParams.isEmpty() || filterParams?.filter == null || filterParams?.filter == [:]) {
            return ValidDetection.count()
        }

        def query = new QueryBuilder().constructCountQuery(filterParams)
        def startTime = System.currentTimeMillis()
        def results = filterParams.sql.rows(query.getSQL(), query.getBindValues())
        def endTime = System.currentTimeMillis()
        log.debug("Count query finished, num results: ${results.size()}, elapsed time (ms): ${endTime - startTime}")

        return results.count[0]
    }

    protected String getReportName() {
        return "detection"
    }

    protected def applyEmbargo(results, params) {

        def now = new Date()

        results.each {
            row ->

            if (row.embargo_date && row.embargo_date.after(now)) {

                if (!hasReadPermission(row.project_id, params)) {

                    row.species_name = ""
                    row.spcode = ""
                    row.sensor_id = ""
                }
            }
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
            out << row.station << ","
            out << GeometryUtils.scrambleCoordinate(row.latitude) << ","
            out << GeometryUtils.scrambleCoordinate(row.longitude) << ","
            out << row.receiver_name << ","
            out << row.sensor_id << ","
            out << row.species_name << ","
            out << row.uploader << ","
            out << row.transmitter_id << ","
            out << row.organisation << ","
            out << ((row.sensor_value == null) ? "" : row.sensor_value) << ","
            out << ((row.sensor_unit == null) ? "" : row.sensor_unit)

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
}
