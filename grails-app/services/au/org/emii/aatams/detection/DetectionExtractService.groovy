package au.org.emii.aatams.detection

import au.org.emii.aatams.export.AbstractStreamingExporterService
import au.org.emii.aatams.util.GeometryUtils

class DetectionExtractService extends AbstractStreamingExporterService {
    static transactional = false

    def permissionUtilsService

    public def extractPage(filterParams) {

        def query =  new DetectionQueryBuilder().constructQuery(filterParams)
        def detections = performQuery(filterParams.sql, query).collect {
            DetectionView.fromSqlRow(it)
        }

        def releaseIsProtectedCache = [:]
        def rowCount = detections.size()

        return [
            results: applyEmbargo(detections, filterParams, releaseIsProtectedCache), rowCount: rowCount
        ]
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

    protected def applyEmbargo(detections, params, releaseIsProtectedCache) {
        detections.collect { detection ->
            new DetectionVisibilityChecker(
                detection,
                params,
                permissionUtilsService,
                releaseIsProtectedCache
            ).apply()
        }.findAll { it }
    }

    protected void writeCsvData(final filterParams, OutputStream out) {
        filterParams.projectPermissionCache = [:]

        super.writeCsvData(filterParams, out)
    }

    protected def writeCsvRows(params, closure) {
        def startTime = System.currentTimeMillis()
        def query =  new DetectionQueryBuilder().constructQuery(params)
        def releaseIsProtectedCache = [:]
        int count = 0

        params.sql.eachRow(query.getSQL(), query.getBindValues()) { row ->

            def detection = DetectionView.fromSqlRow(row)

            def checker = new DetectionVisibilityChecker(
                detection,
                params,
                permissionUtilsService,
                releaseIsProtectedCache
            )

            def checkedDetection = checker.apply()
            if (checkedDetection) {
                closure.call(checkedDetection)
            }
            count++
        }

        def endTime = System.currentTimeMillis()
        def sqlLine = query.toString().replaceAll("[\n\r]", " ").replaceAll("\\s{2,}", " ");

        log.info("Export finished user: ${getUserInfo()}, sqlRows: ${count}, elapsed time (ms): ${endTime - startTime}, query: ${sqlLine}")
    }

    def getUserInfo() {
        def principal = permissionUtilsService.principal()
        return (principal != null) ? "${principal.id}:${principal.name}" : "unauthenticated"
    }

    protected def writeCsvRow(detection, OutputStream out) {
        def formattedCols = []

        formattedCols << detection.csvFormattedTimestamp
        formattedCols << detection.stationStationName
        formattedCols << GeometryUtils.scrambleCoordinate(detection.stationLatitude)
        formattedCols << GeometryUtils.scrambleCoordinate(detection.stationLongitude)
        formattedCols << detection.receiverName
        formattedCols << (detection.sensorId ?: '')
        formattedCols << detection.speciesName
        formattedCols << detection.uploader
        formattedCols << detection.transmitterId
        formattedCols << detection.organisationName
        formattedCols << (detection.sensorValue ?: '')
        formattedCols << (detection.sensorUnit ?: '')

        out << formattedCols.join(',') << '\n'
    }

    protected void writeCsvHeader(OutputStream out) {
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
