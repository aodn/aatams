package au.org.emii.aatams.detection

import au.org.emii.aatams.export.AbstractStreamingExporterService
import au.org.emii.aatams.util.GeometryUtils

class DetectionExtractService extends AbstractStreamingExporterService {
    static transactional = false

    def permissionUtilsService

    public def extractPage(filterParams) {

        def query =  new DetectionQueryBuilder().constructQuery(filterParams)
        def results = performQuery(filterParams.sql, query).collect {
            DetectionView.fromSqlRow(it)
        }

        def releaseIsProtectedCache = [:]
        def rowCount = results.size()

        return [
            results: applyEmbargo(results, filterParams, releaseIsProtectedCache), rowCount: rowCount
        ]
    }

    public Long getCount(filterParams) {
        if (!filterParams || filterParams.isEmpty() || filterParams?.filter == null || filterParams?.filter == [:]) {
            return ValidDetection.count()
        }

        def query =  new DetectionQueryBuilder().constructCountQuery(filterParams)
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

    protected def applyEmbargo(results, params, releaseIsProtectedCache) {
        results.collect { row ->
            new DetectionVisibilityChecker(row, params, permissionUtilsService, releaseIsProtectedCache).apply()
        }.findAll{ it }
    }

    protected void writeCsvData(final filterParams, OutputStream out)
    {
        filterParams.projectPermissionCache = [:]

        super.writeCsvData(filterParams, out)
    }

    protected def eachRow(params, closure)
    {
        def startTime = System.currentTimeMillis()
        def query =  new DetectionQueryBuilder().constructQuery(params)
        def releaseIsProtectedCache = [:]
        int count = 0

        params.sql.eachRow(query.getSQL(), query.getBindValues()) { row ->
            def checker = new DetectionVisibilityChecker(row.toRowResult(), params, permissionUtilsService, releaseIsProtectedCache)
            def checkedRow = checker.apply()
            if (checkedRow) {
                closure.call(checkedRow)
            }
            count++
        }

        def endTime = System.currentTimeMillis()
        log.debug("Export finished, num results: ${count}, elapsed time (ms): ${endTime - startTime}, query: ${query}")
    }

    protected def writeCsvRow(row, OutputStream out)
    {
        def formattedCols = []

        formattedCols << row.timestamp
        formattedCols << row.station_name
        formattedCols << GeometryUtils.scrambleCoordinate(row.latitude)
        formattedCols << GeometryUtils.scrambleCoordinate(row.longitude)
        formattedCols << row.receiver_name
        formattedCols << (row.sensor_id ?: '')
        formattedCols << getSpeciesValueFromRow(row)
        formattedCols << row.uploader
        formattedCols << row.transmitter_id
        formattedCols << row.organisation_name
        formattedCols << (row.sensor_value ?: '')
        formattedCols << (row.sensor_unit ?: '')

        out << formattedCols.join(',') << '\n'
    }

    def getSpeciesValueFromRow(row) {
        if (!row.spcode) {
            return ''
        }

        return "${row.spcode} - ${row.scientific_name} - ${row.common_name}"
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
