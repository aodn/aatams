package au.org.emii.aatams.detection

import au.org.emii.aatams.export.AbstractStreamingExporterService
import au.org.emii.aatams.util.GeometryUtils
import groovy.sql.Sql

class DetectionExtractService extends AbstractStreamingExporterService {
    static transactional = false

    def dataSource
    def permissionUtilsService

    public def extractPage(filterParams) {

        def query =  new QueryBuilder().constructQuery(filterParams)
        def results = performQuery(filterParams.sql, query)
        def rowCount = results.size()

        return [
            results: applyEmbargo(results, filterParams), rowCount: rowCount
        ]
    }

    public Long getCount(filterParams) {
        if (!filterParams || filterParams.isEmpty() || filterParams?.filter == null || filterParams?.filter == [:]) {
            return ValidDetection.count()
        }

        def query =  new QueryBuilder().constructCountQuery(filterParams)
        def results = performQuery(filterParams.sql, query)

        return results.count[0]
    }

    def performQuery(sql, query) {

        def startTime = System.currentTimeMillis()
        def results = sql.rows(query.getSQL(), query.getBindValues())
        def endTime = System.currentTimeMillis()

        log.debug("Query finished, num results: ${results.size()}, elapsed time (ms): ${endTime - startTime}, query: ${query}")

        return results
    }

    protected String getReportName() {
        return "detection"
    }

    protected def applyEmbargo(results, params) {

        def releaseIsProtectedCache = [:]

        results.collect { row ->
            new DetectionVisibilityChecker(row, params, permissionUtilsService, releaseIsProtectedCache).apply()
        }.findAll{ it }
    }

    protected void writeCsvData(final filterParams, OutputStream out)
    {
        filterParams.sql = new Sql(dataSource)
        filterParams.projectPermissionCache = [:]

        super.writeCsvData(filterParams, out)
    }

    protected def readData(filterParams)
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
}
