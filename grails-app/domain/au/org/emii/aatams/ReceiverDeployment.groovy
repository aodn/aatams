package au.org.emii.aatams

import org.joda.time.*
import org.jadira.usertype.dateandtime.joda.*

import au.org.emii.aatams.detection.ValidDetection
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
    static transients = ['scrambledLocation', 'active', 'latitude', 'longitude']
    static auditable = true

    Set<ValidDetection> detections = new HashSet<ValidDetection>()
    static hasMany = [detections: ValidDetection, events: ValidReceiverEvent]

    Integer deploymentNumber

    DateTime initialisationDateTime = new DateTime(Person.defaultTimeZone())

    DateTime deploymentDateTime = new DateTime(Person.defaultTimeZone())

    static mapping =
    {
        initialisationDateTime type: PersistentDateTimeWithZone,
        {
            column name: "initialisationDateTime_timestamp"
            column name: "initialisationDateTime_zone"
        }

        deploymentDateTime type: PersistentDateTimeWithZone,
        {
            column name: "deploymentDateTime_timestamp"
            column name: "deploymentDateTime_zone"
        }

        comments type: 'text'

        // Speed up candidateEntitiesService.
        cache: true
        station cache:true
        detections cache:true
    }

    static searchable =
    {
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
    Date embargoDate


    static constraints =
    {
        receiver()
        station()
        initialisationDateTime(nullable:true)
        deploymentNumber(nullable:true, min:0)
        deploymentDateTime()
        recoveryDate(nullable:true, validator:recoveryDateValidator)
        acousticReleaseID(nullable:true)
        mooringType()
        mooringDescriptor(nullable:true, blank:true)
        bottomDepthM(nullable:true, min:0F)
        depthBelowSurfaceM(nullable:true, min:0F)
        receiverOrientation(nullable:true)
        location(nullable:true)
        comments(nullable:true)
        recovery(nullable:true)
        embargoDate(nullable:true)
        batteryLifeDays(nullable:true)
    }

    static def recoveryDateValidator =
    {
        recoveryDate, obj ->

        if (recoveryDate)
        {
            return recoveryDate.after(obj.deploymentDateTime.toDate())
        }

        return true
    }

    String toString()
    {
        return String.valueOf(receiver) + " - " + String.valueOf(deploymentDateTime)
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location ?: station?.location)
    }

    double getLatitude()
    {
        return getScrambledLocation()?.coordinate?.y
    }

    double getLongitude()
    {
        return getScrambledLocation()?.coordinate?.x
    }

    private DateTime now()
    {
        return new DateTime()
    }

    boolean isActive()
    {
        isActive(now())
    }

    boolean isActive(dateTime)
    {
        if (!recovery)
        {
            return !deploymentDateTime.isAfter(dateTime)
        }

        return (!deploymentDateTime.isAfter(dateTime)) && dateTime.isBefore(recovery?.recoveryDateTime)
    }

    ReceiverDeployment toKmlClone(List<ValidDetection> detections)
    {
        ReceiverDeployment kmlClone = new ReceiverDeployment(receiver: this.receiver, deploymentDateTime: this.deploymentDateTime)

        kmlClone.detections.addAll(detections.grep { it.receiverDeployment.id == this.id })

        return kmlClone
    }
}
