package au.org.emii.aatams

import org.hibernatespatial.GeometryUserType
import org.joda.time.*
import org.joda.time.contrib.hibernate.*
import org.joda.time.format.ISODateTimeFormat

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point


/**
 * Receiver deployment is the process of deploying a receiver in the ocean. The
 * receiver is attached to a mooring and sits under the surface at a pre-defined
 * depth.  It has an additional device attached that's used to release the
 * receiver from its main connection to the mooring so it can float to the
 * surface.  A separate tether keeps the receiver attached to the mooring.
 */
class ReceiverDeployment
{
    static belongsTo = [station: InstallationStation, receiver: Receiver]
    static transients = [ 'scrambledLocation', 'active', 'latitude', 'longitude', 'deploymentInterval', 'deploymentNumber' ]
    static auditable = true

    static hasMany = [ events: ValidReceiverEvent ]

    DateTime initialisationDateTime

    DateTime deploymentDateTime = new DateTime(Person.defaultTimeZone())

    static mapping = {
        initialisationDateTime type: PersistentDateTimeTZ,
        {
            column name: "initialisationDateTime_timestamp"
            column name: "initialisationDateTime_zone"
        }

        deploymentDateTime type: PersistentDateTimeTZ,
        {
            column name: "deploymentDateTime_timestamp"
            column name: "deploymentDateTime_zone"
        }

        comments type: 'text'

        // Speed up candidateEntitiesService.
        cache: true
        station cache:true

        location type: GeometryUserType
    }

    static searchable = {
        receiver(component:true)
        station(component:true)
    }

    /**
     * Date the receiver is scheduled to be recovered.
     */
    Date recoveryDate

    /**
     * An identifier used during recovery to remotely release the tether
     * allowing the receiver to come to the surface.
     */
    String acousticReleaseID

    MooringType mooringType

    String mooringDescriptor

    /**
     * Depth to bottom (m)
     */
    Float bottomDepthM

    /**
     * Depth below surface (m)
     */
    Float depthBelowSurfaceM

    /**
     * TODO: probably should be enum - what are possible values?
     */
    ReceiverOrientation receiverOrientation

    Point location

    Integer batteryLifeDays

    String comments

    /**
     * Will initially be NULL, until the recovery occurs.  This relationship
     * is here for the "receiver recovery" list view, which is actually a list
     * of receiver deployments, including those with and without associated
     * recoveries.
     */
    static hasOne = [recovery:ReceiverRecovery]

    /**
     * Date when data from this deployment is no longer embargoed (may be null to
     * indicate that no embargo exists).
     */

    static constraints = {
        receiver()
        station()
        initialisationDateTime(nullable: true, validator: conflictingDeploymentValidator)
        deploymentDateTime(validator: dateTimeValidator)
        recoveryDate(nullable: true, validator: recoveryDateValidator)
        acousticReleaseID(nullable: true)
        mooringType()
        mooringDescriptor(nullable: true, blank: true)
        bottomDepthM(nullable: true, min: 0F)
        depthBelowSurfaceM(nullable: true, min: 0F)
        receiverOrientation(nullable: true)
        location(nullable: true)
        comments(nullable: true)
        recovery(nullable: true)
        batteryLifeDays(nullable: true)
    }

    static def recoveryDateValidator = {
        recoveryDate, obj ->

        if (recoveryDate)
        {
            return recoveryDate.after(obj.deploymentDateTime.toDate())
        }

        return true
    }

    static def conflictingDeploymentValidator = { initialisationDateTime, deployment ->
        ReceiverDeploymentValidator.conflictingDeploymentValidator(
            initialisationDateTime, deployment
        )
    }

    static def dateTimeValidator = { deploymentDateTime, deployment ->
        def initDateTime = deployment.initialisationDateTime
        if (!initDateTime) {
            return true
        }

        if (!deploymentDateTime.isBefore(initDateTime)) {
            return true
        }

        [
            'receiverDeployment.deploymentDateTime.notAfterInitialisationDateTime',
            deployment.receiver,
            deploymentDateTime,
            initDateTime
        ]
    }

    // Don't want to override 'equals()' as this causes unexpected behaviour with GORM.
    boolean same(Object other) {
        if (other == null) {
            return false
        }

        return (this.receiver.same(other.receiver) && this.undeployableInterval == other.undeployableInterval)
    }

    String toString() {
        return "${String.valueOf(receiver)} @ ${String.valueOf(station)}, " +
        "deployed ${ISODateTimeFormat.dateTimeNoMillis().print(deploymentDateTime)}"
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation() {
        return GeometryUtils.scrambleLocation(location ?: station?.location)
    }

    double getLatitude() {
        return getScrambledLocation()?.coordinate?.y
    }

    double getLongitude() {
        return getScrambledLocation()?.coordinate?.x
    }

    private DateTime now() {
        return new DateTime()
    }

    boolean isActive() {
        isActive(now())
    }

    boolean isActive(dateTime) {
        if (!recovery) {
            return !deploymentDateTime.isAfter(dateTime)
        }

        return (!deploymentDateTime.isAfter(dateTime)) && dateTime.isBefore(recovery?.recoveryDateTime)
    }

    def getUndeployableInterval() {
        def startDateTime = initialisationDateTime ?: deploymentDateTime

        if (startDateTime && recovery) {
            if (recovery.status == DeviceStatus.RECOVERED) {
                return new Interval(startDateTime, recovery.recoveryDateTime)
            }
            else {
                return new OpenInterval(startDateTime)
            }
        }
    }

    def getDeploymentNumber() {
        receiver?.deployments?.sort { it.deploymentDateTime }.findIndexOf {
            it.same(this)
        } + 1
    }
}
