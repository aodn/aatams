package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point
import org.joda.time.*
import org.joda.time.contrib.hibernate.*

/**
 * Receiver recovery is the process of retrieving a receiver from the field and
 * either (a) downloading data from the receiver and immediately redeploying it
 * or (b) returning the receiver to the office for downloading and storage for
 * future redeployment.
 */
class ReceiverRecovery 
{
    static transients = ['scrambledLocation']

    /**
     * Every recovery must have a (chronologically) preceding deployment.
     */
    static belongsTo = [deployment: ReceiverDeployment, recoverer: ProjectRole]
    static mapping =
    {
        recoveryDateTime type: PersistentDateTimeTZ,
        {
            column name: "recoveryDateTime_timestamp"
            column name: "recoveryDateTime_zone"
        }
    }
    
    DateTime recoveryDateTime
    Point location
    DeviceStatus status
    ReceiverDownload download
    
    String comments

    static constraints =
    {
        recoveryDateTime()
        location()
        status()
        download(nullable:true)
        recoverer()
        deployment()
        comments(nullable:true)
    }
    
    String toString()
    {
        return String.valueOf(deployment.receiver) + " recovered on " + String.valueOf(recoveryDateTime)
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
}
