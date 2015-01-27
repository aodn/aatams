package au.org.emii.aatams.detection

import au.org.emii.aatams.Project
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

        return applyEmbargo(results, filterParams)
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

        clearProjectIsProtectedCache()

        def resultsToKeep = results.grep { row ->
            shouldKeepRow(row, params)
        }

        resultsToKeep.findAll{ it }.findAll { row ->
            shouldSanitiseRow(row, params)
        }.each{
            sanitise(it)
        }

        return resultsToKeep
    }

    def shouldKeepRow(row, params) {

        _isPublic(row) || _isReadable(row, params) || (_allowSanitisedResults(params) && !_isProtected(row))
    }

    def shouldSanitiseRow(row, params) {

        _isEmbargoed(row) && !_isProtected(row) && !_isReadable(row, params)
    }

    def _isPublic(row) {
        !(_isEmbargoed(row) || _isProtected(row))
    }

    def _isReadable(row, params) {
        hasReadPermission(row.release_project_id, params)
    }

    def _isProtected(row) {
        row.release_project_id && projectIsProtected(row.release_project_id)
    }

    def _isEmbargoed(row) {
        row.embargo_date && row.embargo_date.after(new Date())
    }

    def _allowSanitisedResults(params) {
        !_isFilteredOnSpecies(params.filter)
    }

    def _isFilteredOnSpecies(filter) {
        def speciesFilterValue = filter?.detectionSurgeries?.surgery?.release?.animal?.species?.in?.grep{ it.trim() }
        speciesFilterValue?.size() > 1
    }

    def sanitise(row) {
        row.species_name = ""
        row.spcode = ""
        row.sensor_id = ""
    }

    def _projectIsProtectedCache

    def clearProjectIsProtectedCache(){

        _projectIsProtectedCache = [:]
    }

    def projectIsProtected(projectId) {
        if (!_projectIsProtectedCache.containsKey(projectId)) {

            def project = Project.get(projectId)

            _projectIsProtectedCache[projectId] = project.isProtected
        }

        return _projectIsProtectedCache[projectId]
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
