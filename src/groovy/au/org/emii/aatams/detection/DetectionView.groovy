package au.org.emii.aatams.detection

import au.org.emii.aatams.Embargoable
import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.ReceiverDeployment
import au.org.emii.aatams.Project
import au.org.emii.aatams.Surgery
import au.org.emii.aatams.Tag

import com.vividsolutions.jts.geom.Point

import groovy.sql.Sql

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat

import org.jooq.*
import org.jooq.conf.ParamType
import org.jooq.impl.DSL

import static org.jooq.impl.DSL.*

/**
 * This class adds in attributes from associated join tables.
 * The associated attributes are used in security logic and views/reporting/downloads.
 *
 * Aside from inserting detections to the DB, this is the class with which client code will typically interact.
 */
class DetectionView extends Detection implements Embargoable {

    DateTime embargoDate
    Double stationLatitude
    Double stationLongitude
    Long receiverDeploymentId
    Long releaseId
    Long releaseProjectId
    Long stationId
    Long surgeryId
    Long tagProjectId
    String commonName
    String invalidReason
    String organisationName
    String scientificName
    String sensorId
    String spcode
    String stationStationName
    String uploader

    InstallationStation getInstallationStation() {
        return InstallationStation.get(stationId)
    }

    ReceiverDeployment getReceiverDeployment() {
        return ReceiverDeployment.get(receiverDeploymentId)
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation() {
        return installationStation?.scrambledLocation
    }

    Project getTagProject() {
        return Project.get(tagProjectId)
    }

    // Alias for transmitterProject - for compatibility with existing code - VisibilityControlService.
    Project getProject() {
        return this.tagProject
    }

    Surgery getSurgery() {
        return Surgery.get(surgeryId)
    }

    String getSpeciesName() {
        if (!spcode) {
            return ''
        }

        return "${spcode} - ${scientificName} (${commonName})"
    }

    def applyEmbargo(allowSanitised = true) {

        def releaseEmbargoed = surgery.release.isEmbargoed()

        def censoredDetection = new HashMap(this.properties)

        if (releaseEmbargoed) {
            censoredDetection.surgery = null
            censoredDetection.spcode = ''
            censoredDetection.sensorId = ''
            censoredDetection.speciesName = ''
        }
        else {
            censoredDetection.surgery = surgery
            censoredDetection.sensorId = sensorId
            censoredDetection.speciesName = speciesName
        }

        censoredDetection.isSanitised = { -> true }

        def protectionRequired = project.isProtected && releaseEmbargoed

        def hideFromResults = releaseEmbargoed && !allowSanitised

        if (protectionRequired || hideFromResults) {
            return null
        }

        return censoredDetection
    }

    def isSanitised() {
        !sensorId && !speciesName
    }

    static def fromSqlRow(def row, def detection) {
        Detection.fromSqlRow(row, detection)

        detection.commonName = row.common_name
        detection.embargoDate = new DateTime(row.embargo_date).withZone(DateTimeZone.UTC)
        detection.invalidReason = row.invalid_reason
        detection.organisationName = row.organisation_name
        detection.receiverDeploymentId = row.receiver_deployment_id
        detection.releaseId = row.release_id
        detection.releaseProjectId = row.release_project_id
        detection.tagProjectId = row.tag_project_id
        detection.scientificName = row.scientific_name
        detection.sensorId = row.sensor_id
        detection.spcode = row.spcode
        detection.stationId = row.station_id
        detection.stationLatitude = row.station_latitude
        detection.stationLongitude = row.station_longitude
        detection.stationStationName = row.station_station_name
        detection.surgeryId = row.surgery_id
        detection.uploader = row.uploader

        return detection as DetectionView

    }

    static def fromSqlRow(def row) {
        def detection = [:]
        return fromSqlRow(row, detection)
    }

    // GORM-like methods.
    def refresh(dataSource) {
        get(this.id, dataSource, this)
    }

    static Detection get(id, dataSource) {
        return get(id, dataSource, [:])
    }

    static Detection get(id, dataSource, detection) {

        DSLContext create = DSL.using(SQLDialect.POSTGRES);
        def query =
            create
                .select()
                .where(DSL.fieldByName('detection_id').equal(Long.valueOf(id)))
                .from(table(getViewName()))

        def sql = new Sql(dataSource)

        return DetectionView.fromSqlRow(sql.firstRow(query.getSQL(), query.getBindValues()), detection)
    }

    static String getViewName() {
        return 'detection_view'
    }
}
