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
class ReceiverDeployment {
    static belongsTo = [station: InstallationStation, receiver: Receiver]
    static transients = [ 'scrambledLocation', 'active', 'latitude', 'longitude', 'deploymentInterval', 'deploymentNumber' ]
    static auditable = true

    static hasMany = [ events: ValidReceiverEvent ]

    DateTime initialisationDateTime

    DateTime deploymentDateTime = new DateTime(Person.defaultTimeZone())

    static mapping = {
        initialisationDateTime type: PersistentDateTimeTZ, {
            column name: "initialisationDateTime_timestamp"
            column name: "initialisationDateTime_zone"
        }

        deploymentDateTime type: PersistentDateTimeTZ, {
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
        initialisationDateTime(nullable: true)
        deploymentDateTime(validator: dateTimeValidator)
        recoveryDate(nullable: true, validator: scheduledRecoveryDateValidator)
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

    static def scheduledRecoveryDateValidator = { recoveryDate, obj ->

        if (recoveryDate && !recoveryDate.after(obj.deploymentDateTime.toDate())) {
            return [
                'not scheduledRecoverDate after deploymentDateTime',
                recoveryDate,
                deploymentDateTime.toDate()
            ]
        }

        return true
    }

    static def dateTimeValidator = { deploymentDateTime, deployment ->

        /*
            Xavier says,
            GUI logic should be,
              Within each record (row) --> initialisation_date < deployment_date < recovery_date
              Across records (row)     --> deployment_date (t+1) > recovery_date (t)

            interpret < as <=
            must fall through to the next test, if current one passes
        */

        def initDateTime = deployment.initialisationDateTime
        def recoveryDateTime = deployment?.recovery?.recoveryDateTime

        def scheduledRecovery = deployment.recoveryDate ? new DateTime(deployment.recoveryDate) : null

        if (initDateTime && deploymentDateTime.isBefore(initDateTime)) {

            return [
                    'deploymentDateTime is before initDateTime',
                    deploymentDateTime.toDate(),
                    initDateTime
            ]
        }

        if(recoveryDateTime && recoveryDateTime.isBefore(deploymentDateTime)) {

            return [
                    'recoveryDateTime is before deploymentDateTime',
                    recoveryDateTime,
                    deploymentDateTime.toDate()
            ]
        }

        def deployments = deployment.receiver?.deployments?.sort { it.deploymentDateTime }
        def deploymentIndex = deployments.findIndexOf { it.id == deployment.id }
        def nextDeployment = deployments ? deployments[deploymentIndex + 1] : null
        def nextDeploymentDateTime = nextDeployment?.deploymentDateTime

        if(nextDeploymentDateTime && nextDeploymentDateTime.isBefore(recoveryDateTime)) {

            return [
                    'nextDeploymentDateTime is before recoveryDateTime',
                    nextDeploymentDateTime,
                    recoveryDateTime
            ]
        }

        return true
    }

    String formatValidationErrors() {

        this.errors.allErrors.collect {

            // - note that it.code may exist even if it doesn't appear in the debugger view-
            // presumably through some extended dynamic property introspection mechanism.
            // - also, we only have field and rejectedValue in the Java peer class, which makes it difficult
            // to create properly formatted messages for inequality errors involving two field values
            // I don't know why the original code returns 3-tuples hen we can't properly access those values,
            // but I have stuck to the convention
            "&bull; ${it.code} ${it.getField()}=${it.getRejectedValue()}";

        }.join('\\n')
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
}
