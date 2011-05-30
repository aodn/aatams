package au.org.emii.aatams

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

    Integer deploymentNumber
    Date deploymentDate
    
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
    String receiverOrientation
    
    Point location
    
    String comments
    
    String toString()
    {
        return String.valueOf(receiver) + " - " + String.valueOf(deploymentDate)
    }
}
