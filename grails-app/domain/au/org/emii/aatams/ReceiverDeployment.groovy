package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point
import org.joda.time.*
import org.joda.time.contrib.hibernate.*


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
    static transients = ['scrambledLocation']

    Integer deploymentNumber
    
    DateTime deploymentDateTime

    static mapping =
    {
        deploymentDateTime type: PersistentDateTimeTZ,
        {
            column name: "deploymentDateTime_timestamp"
            column name: "deploymentDateTime_zone"
        }
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
    ReceiverRecovery recovery
    
    /**
     * Date when data from this deployment is no longer embargoed (may be null to
     * indicate that no embargo exists).
     */
    Date embargoDate

    
    static constraints =
    {
        receiver()
        station()
        deploymentNumber(nullable:true, min:0)
        deploymentDateTime()
        recoveryDate(nullable:true)
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
    }
    
    String toString()
    {
        return String.valueOf(receiver) + " - " + String.valueOf(deploymentDate)
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
}
