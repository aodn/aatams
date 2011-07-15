package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point

/**
 * Receiver recovery is the process of retrieving a receiver from the field and
 * either (a) downloading data from the receiver and immediately redeploying it
 * or (b) returning the receiver to the office for downloading and storage for
 * future redeployment.
 */
class ReceiverRecovery 
{
    static transients = ['scrambledLocation']

    Date recoveryDate
    Point location
    DeviceStatus status
    ReceiverDownload download
    ProjectRole recoverer
    
    /**
     * Every recovery must have a (chronologically) preceding deployment.
     */
    ReceiverDeployment deployment
    
    String comments

    static constraints =
    {
        recoveryDate()
        location()
        status()
        download(nullable:true)
        recoverer()
        deployment()
        comments(nullable:true)
    }
    
    String toString()
    {
        return String.valueOf(deployment.receiver) + " recovered on " + String.valueOf(recoveryDate)
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
}
