package au.org.emii.aatams

import org.joda.time.DateTime

/**
 * A receiver is a device deployed in the ocean that is able to receive 'ping'
 * transmissions from tags attached or surgically implanted into fish.
 */
class Receiver extends Device {
    static hasMany = [ deployments: ReceiverDeployment ]
    static belongsTo = [ organisation: Organisation ]
    static transients = ['name',
                         'deviceID',
                         'status',
                         'mostRecentRecovery',
                         'mostRecentDeployment',
                         'recoveriesBeforeOrEqualToDateTime',
                         'sortedByDateTime']

    static auditable = true

    static mapping = {
        organisation sort:'name'
        cache true
    }

    static searchable = [only: ['name']]

    String getName() {
        return String.valueOf(model) + "-" + serialNumber
    }

    String getDeviceID() {
        return getName()
    }

    String toString() {
        return getName()
    }

    private ReceiverDeployment getMostRecentDeployment(dateTime) {
        if (!deployments || deployments.isEmpty()) {
            return null
        }

        def chronoDeployments = deployments.sort { a, b -> a.deploymentDateTime <=> b.deploymentDateTime }
        chronoDeployments = chronoDeployments.grep {
            !it.deploymentDateTime.isAfter(dateTime)
        }

        if (chronoDeployments.isEmpty()) {
            return null
        }

        return chronoDeployments.last()
    }

    private boolean hasActiveDeployment(dateTime) {
        // A receiver is only considered to have an active deployment if the *last* deployment is active.
        return getMostRecentDeployment(dateTime)?.isActive(dateTime)
    }

    private ReceiverRecovery getMostRecentRecovery(dateTime) {
        if (hasActiveDeployment(dateTime)) {
            return null
        }

        def recoveries =
            getSortedByRecoveryDateTime(getRecoveriesBeforeOrEqualToDateTime(dateTime))

        if (recoveries && !recoveries.isEmpty()) {
            return recoveries.last()
        }

        return null
    }

    private List getRecoveriesBeforeOrEqualToDateTime(dateTime) {
        return (deployments*.recovery.grep {
            it && !it.recoveryDateTime.isAfter(dateTime)
        })
    }

    private List getSortedByRecoveryDateTime(sortees) {
        return (sortees?.sort {
            a, b ->

            a.recoveryDateTime <=> b.recoveryDateTime
        })
    }

    DeviceStatus getStatus(DateTime dateTime) {
        if (hasActiveDeployment(dateTime)) {
            return DeviceStatus.DEPLOYED
        }
        else {
            def mostRecentRecovery = getMostRecentRecovery(dateTime)
            if (mostRecentRecovery) {
                return mostRecentRecovery.status
            }
        }

        return DeviceStatus.NEW
    }

    DeviceStatus getStatus() {
        return getStatus(now())
    }

    private DateTime now() {
        return new DateTime()
    }

    private boolean hasDeployment(deployment) {
        return deployments*.id?.contains(deployment.id)
    }

    boolean same(Object other) {
        if (other == null) {
            return false
        }

        return (this.name == other.name)
    }

    /**
     * Backward compatibility.
     */
    static Receiver findByName(name, params=[:]) {
        def tokens = name.tokenize("-")
        assert(tokens.size() >= 2): "Invalid receiver name: ${name}"

        def modelName = tokens[0..-2].join('-')
        def serialNumber = tokens.last()

        return Receiver.findByModelAndSerialNumber(ReceiverDeviceModel.findByModelName(modelName, params),
                                                   serialNumber, params)
    }
}
