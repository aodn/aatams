package au.org.emii.aatams

/**
 * Status of the device. e.g. NEW, DEPLOYED, RECOVERED, RETIRED, LOST, STOLEN,
 * DAMAGED.
 *
 * Devices start off as NEW and go to DEPLOYED automatically.  The statuses which
 * follow are set by the user/upload as part of device recovery process.
 */
enum DeviceStatus {
    NEW,
    DEPLOYED,
    RECOVERED,
    RETIRED,
    LOST,
    STOLEN,
    DAMAGED,
    RETURNED_TO_VENDOR

    String getKey() {
        name()
    }

    static List listRecoveryStatuses()
    {
        return (values() - DeviceStatus.NEW - DeviceStatus.DEPLOYED)
    }
}
