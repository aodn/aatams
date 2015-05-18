package au.org.emii.aatams.detection

import au.org.emii.aatams.Embargoable
import au.org.emii.aatams.InstallationStation
import au.org.emii.aatams.ReceiverDeployment
import au.org.emii.aatams.Project
import au.org.emii.aatams.Surgery

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

    Long receiverDeploymentId

    Long stationId
    Long surgeryId

// embargo date
    // receiver_deployment_id?
    // txr_project_name
    // release_project_id

    static def fromSqlRow(def row) {
        def detection = Detection.fromSqlRow(row) as DetectionView

        detection.receiverDeploymentId = row.receiver_deployment_id
        detection.stationId = row.station_id
        detection.surgeryId = row.surgery_id

        return detection
    }

    def applyEmbargo() {
        return this
    }

    InstallationStation getInstallationStation() {
        return InstallationStation.get(stationId)
    }

    ReceiverDeployment getReceiverDeployment() {
        return ReceiverDeployment.get(receiverDeploymentId)
    }

    Project getTransmitterProject() {
        // TODO
        return null
    }

    // Alias for transmitterProject - for compatibility with existing code - VisibilityControlService.
    Project getProject() {
        return this.transmitterProject
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation() {
        return installationStation?.scrambledLocation
    }

    List<Surgery> getSurgeries() {
        []
    }

    // GORM-like methods.
    static Detection get(id, dataSource) {
        DSLContext create = DSL.using(SQLDialect.POSTGRES);
        def query =
            create
                .select()
                .where(DSL.fieldByName('detection_id').equal(Long.valueOf(id)))
                .from(table(getViewName()))

        def sql = new Sql(dataSource)

        return DetectionView.fromSqlRow(sql.firstRow(query.getSQL(), query.getBindValues()))
    }

    static String getViewName() {
        return 'detection_view'
    }
}
