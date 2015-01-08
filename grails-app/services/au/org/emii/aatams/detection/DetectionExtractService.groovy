package au.org.emii.aatams.detection

import java.io.OutputStream;

import org.apache.shiro.SecurityUtils

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
        log.debug("Querying database, offset: " + filterParams.offset)
        def query = new QueryBuilder().constructQuery(filterParams)
        log.error("query: ${query.getSQL(org.jooq.conf.ParamType.INLINED)}")
        def results = filterParams.sql.rows(query.getSQL(), query.getBindValues())

        log.debug("Query finished, num results: " + results.size())

        return results
    }

    public Long getCount(filterParams)
    {
        if (!filterParams || filterParams.isEmpty() || filterParams?.filter == null || filterParams?.filter == [:])
        {
            return ValidDetection.count()
        }

        def query = new QueryBuilder().constructCountQuery(filterParams)
        def results = filterParams.sql.rows(query.getSQL(), query.getBindValues())

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
