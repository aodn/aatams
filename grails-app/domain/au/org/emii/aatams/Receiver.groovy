package au.org.emii.aatams

import au.org.emii.aatams.detection.ValidDetection
import au.org.emii.aatams.detection.InvalidDetection
import org.joda.time.DateTime

/**
 * A receiver is a device deployed in the ocean that is able to receive 'ping'
 * transmissions from tags attached or surgically implanted into fish.
 */
class Receiver extends Device
{
    /**
     * Detections recorded at the receiver.
     */
    Set<ValidDetection> detections = new HashSet<ValidDetection>()

    static hasMany = [detections: ValidDetection, deployments: ReceiverDeployment]
    static belongsTo = [organisation: Organisation]
    static transients = ['name', 'deviceID', 'status', 'currentRecovery', 'mostRecentDeployment']
    static auditable = true

    static mapping =
    {
        organisation sort:'name'
        cache true
        validDetections cache:true
        invalidDetections cache:true
    }

    static searchable = [only: ['name']]

    String getName()
    {
        return String.valueOf(model) + "-" + serialNumber
    }

    String getDeviceID()
    {
        return getName()
    }

    String toString()
    {
        return getName()
    }

    private ReceiverDeployment getMostRecentDeployment(dateTime)
    {
        if (!deployments || deployments.isEmpty())
        {
            return null
        }

        def chronoDeployments = deployments.sort { a, b -> a.deploymentDateTime <=> b.deploymentDateTime }
        chronoDeployments = chronoDeployments.grep
        {
            !it.deploymentDateTime.isAfter(dateTime)
        }

        if (chronoDeployments.isEmpty())
        {
            return null
        }

        return chronoDeployments.last()
    }

    private boolean hasActiveDeployment(dateTime)
    {
        // A receiver is only considered to have an active deployment if the *last* deployment is active.
        return getMostRecentDeployment(dateTime)?.isActive(dateTime)
    }

    private ReceiverRecovery getCurrentRecovery(dateTime)
    {
        if (hasActiveDeployment(dateTime))
        {
            return null
        }

        def recoveries = deployments*.recovery
        recoveries?.removeAll
        {
            (it == null) || it.recoveryDateTime.isAfter(dateTime)
        }

        recoveries = recoveries?.sort
        {
            a, b ->

            a.recoveryDateTime <=> b.recoveryDateTime
        }

        if (recoveries && !recoveries.isEmpty())
        {
            return recoveries.last()
        }

        return null
    }

    DeviceStatus getStatus(DateTime dateTime)
    {
        if (hasActiveDeployment(dateTime))
        {
            return DeviceStatus.findByStatus(DeviceStatus.DEPLOYED)
        }
        else
        {
            def currentRecovery = getCurrentRecovery(dateTime)
            if (currentRecovery)
            {
                return currentRecovery.status
            }
        }

        return DeviceStatus.findByStatus(DeviceStatus.NEW, [cache:true])
    }

    DeviceStatus getStatus()
    {
        return getStatus(now())
    }

    private DateTime now()
    {
        return new DateTime()
    }

    DeviceStatus getStatus(ReceiverDeployment deployment)
    {
        if (deployments*.id?.contains(deployment.id))
        {
            try
            {
                removeFromDeployments(deployment)
                return getStatus(deployment.deploymentDateTime)
            }
            finally
            {
                addToDeployments(deployment)
            }
        }

        return getStatus(deployment.deploymentDateTime)
    }

    boolean canDeploy(deployment)
    {
        if (deployments*.id?.contains(deployment.id))
        {
            try
            {
                removeFromDeployments(deployment)
                return canDeployAtTime(deployment.deploymentDateTime)
            }
            finally
            {
                addToDeployments(deployment)
            }
        }

        return canDeployAtTime(deployment.deploymentDateTime)
    }

    boolean canDeployAtTime(dateTime)
    {
        DeviceStatus deployedStatus = DeviceStatus.findByStatus('DEPLOYED')
        DeviceStatus retiredStatus = DeviceStatus.findByStatus('RETIRED')

        log.debug("Status: " + getStatus(dateTime) + ", at time: " + dateTime)
        if ([deployedStatus, retiredStatus].contains(getStatus(dateTime)))
        {
            return false
        }

        return true
    }

    /**
     * Backward compatibility.
     */
    static Receiver findByName(name, params=[:])
    {
        def tokens = name.tokenize("-")
        assert(tokens.size() >= 2): "Invalid receiver name: " + name

        def modelName = tokens[0..-2].join('-')
        def serialNumber = tokens.last()

        return Receiver.findByModelAndSerialNumber(ReceiverDeviceModel.findByModelName(modelName, params),
                                                   serialNumber, params)
    }
}
