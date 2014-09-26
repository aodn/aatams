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
        def startTime = System.currentTimeMillis()
        log.debug("Querying database, offset: " + filterParams.offset)
        def results = filterParams.sql.rows(constructQuery(filterParams, filterParams.max, filterParams.offset))
        log.debug("Query finished in ${System.currentTimeMillis() - startTime}ms with ${results.size()} results")

        return results
    }

    public Long getCount(filterParams)
    {
        if (!filterParams || filterParams.isEmpty() || filterParams?.filter == null || filterParams?.filter == [:])
        {
            return ValidDetection.count()
        }

        def startTime = System.currentTimeMillis()
        log.debug("Querying database")
        def results = filterParams.sql.rows(constructQuery(filterParams, filterParams.max, null, true))
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

    private String constructQuery(filterParams, limit, offset)
    {
        return constructQuery(filterParams, limit, offset, false)
    }

    private String constructQuery(filterParams, limit, offset, count)
    {
        String query

        if (count)
        {
            query = SELECT_COUNT
        }
        else
        {
            query = SELECT
        }

        query += " "

        List<String> whereClauses = []

        ["project.name": filterParams?.filter?.receiverDeployment?.station?.installation?.project?.in?.getAt(1),
         "installation.name": filterParams?.filter?.receiverDeployment?.station?.installation?.in?.getAt(1),
         "installation_station.name": filterParams?.filter?.receiverDeployment?.station?.in?.getAt(1),
         "valid_detection.transmitter_id": filterParams?.filter?.in?.getAt(1),
         "spcode": filterParams?.filter?.surgeries?.release?.animal?.species?.in?.getAt(1)
         ].each
         {
            k, v ->

            if (v)
            {
                whereClauses += (k + " in (" + toSqlFormat(v) + ") ")
            }
        }

        ["timestamp": filterParams?.filter?.between].each
        {
            k, v ->

            if (v)
            {
                assert(v?."1") : "Start date/time must be specified"
                assert(v?."2") : "End date/time must be specified"

                whereClauses += (k + " between '" + new java.sql.Timestamp(v?."1"?.getTime()) + "' and '" + new java.sql.Timestamp(v?."2"?.getTime()) + "' ")
            }
        }

        if (whereClauses.size() > 0)
        {
            query += "where "

            whereClauses.eachWithIndex
            {
                clause, i ->

                if (i != 0)
                {
                    query += "and "
                }

                query += clause
            }
        }

        if (limit != null)
        {
            query += "limit " + limit
        }

        if (offset != null)
        {
            query += " offset " + offset
        }

        log.debug("Query: " + query)

        return query
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

    static String SELECT = '''select timestamp, to_char((timestamp::timestamp with time zone) at time zone '00:00', 'YYYY-MM-DD HH24:MI:SS') as formatted_timestamp,
            installation_station.name as station,
            installation_station.id as station_id,
            installation_station.location as location,
            st_y(installation_station.location) as latitude, st_x(installation_station.location) as longitude,
            (device_model.model_name || '-' || device.serial_number) as receiver_name,
            COALESCE(sensor.transmitter_id, '') as sensor_id,
            COALESCE((species.spcode || ' - ' || species.scientific_name || ' (' || species.common_name || ')'), '') as species_name,

            sec_user.name as uploader,
            valid_detection.transmitter_id as "transmitter_id",
            organisation.name as organisation,
            project.name as project,
            installation.name as installation,
            COALESCE(species.spcode, '') as spcode,
            animal_release.id as animal_release_id,
            animal_release.embargo_date as embargo_date,
            project.id as project_id,
            valid_detection.id as detection_id,

            animal_release.project_id as release_project_id,
            valid_detection.sensor_value, valid_detection.sensor_unit
''' + " ${FROM_JOIN}"

    static String SELECT_COUNT = "select count(*) ${FROM_JOIN}"

    static String FROM_JOIN = '''from valid_detection

            -- receiver metadata must exist (otherwise the detection would be invalid)
	    join receiver_deployment on receiver_deployment_id = receiver_deployment.id
            join installation_station on receiver_deployment.station_id = installation_station.id
            join installation on installation_station.installation_id = installation.id
            join project on installation.project_id = project.id
            join device on receiver_deployment.receiver_id = device.id
            join device_model on device.model_id = device_model.id
            join receiver_download_file on receiver_download_id = receiver_download_file.id
            join sec_user on receiver_download_file.requesting_user_id = sec_user.id
            join organisation on device.organisation_id = organisation.id

            -- tag metadata *may* exist
            left join sensor on valid_detection.transmitter_id = sensor.transmitter_id
            left join device tag on sensor.tag_id = tag.id

            -- as with surgery metadata
            left join surgery on tag.id = surgery.tag_id
            left join animal_release on surgery.release_id = animal_release.id
            left join animal on animal_release.animal_id = animal.id
            left join species on animal.species_id = species.id'''

}
