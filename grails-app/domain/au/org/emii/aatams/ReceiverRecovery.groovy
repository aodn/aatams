package au.org.emii.aatams

import au.org.emii.aatams.util.GeometryUtils

import com.vividsolutions.jts.geom.Point
import org.joda.time.*
import org.jadira.usertype.dateandtime.joda.*

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
        recoveryDateTime type: PersistentDateTimeWithZone,
        {
            column name: "recoveryDateTime_timestamp"
            column name: "recoveryDateTime_zone"
        }

        comments type: 'text'
    }
    static auditable = true

    DateTime recoveryDateTime = new DateTime(Person.defaultTimeZone())
    Point location
    DeviceStatus status

    String comments

    static constraints =
    {
        recoveryDateTime(validator: recoveryDateTimeValidator)
        location()
        status()
        recoverer()
        deployment()
        comments(nullable:true)
    }

    static searchable =
    {
        deployment(component:true)
    }

    static def recoveryDateTimeValidator = {
        recoveryDateTime, obj ->

        return recoveryDateTime.isAfter(obj.deployment.deploymentDateTime) ?
            true :
            ['invalid.beforeDeploymentDateTime', recoveryDateTime, obj.deployment.deploymentDateTime]
    }

    String toString()
    {
        return String.valueOf(deployment?.receiver) + " recovered on " + String.valueOf(recoveryDateTime)
    }

    /**
     * Non-authenticated users can only see scrambled locations.
     */
    Point getScrambledLocation()
    {
        return GeometryUtils.scrambleLocation(location)
    }
}
