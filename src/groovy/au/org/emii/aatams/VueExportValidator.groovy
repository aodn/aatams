package au.org.emii.aatams

import java.text.DateFormat;
import java.text.SimpleDateFormat

import au.org.emii.aatams.detection.*
import org.apache.log4j.Logger

abstract class VueExportValidator  {
    protected static final Logger log = Logger.getLogger(VueExportValidator.class)

    def params

    ReceiverDownloadFile receiverDownload

    Receiver receiver
    Collection<ReceiverDeployment> deploymentsByDateTime
    Collection<ReceiverRecovery> recoveries

    ReceiverDeployment deployment

    InvalidDetectionReason invalidReason
    String invalidMessage

    static DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT:00"))
    }

    protected abstract boolean isDuplicate()

    private boolean isUnknownReceiver() {
        return (receiver == null)
    }


    private boolean hasNoDeploymentsAtDateTime() {
        assert(receiver)

        deploymentsByDateTime = receiver.deployments?.grep {
            theDeployment ->

            if (!theDeployment) {
                return false
            }

            if (isEventBeforeDeploymentDateTime(theDeployment)) {
                return false
            }

            return true
        }

        return (!deploymentsByDateTime || deploymentsByDateTime.isEmpty())
    }

    protected abstract boolean isEventBeforeDeploymentDateTime(theDeployment)

    private boolean hasNoRecoveriesAtDateTime() {
        assert(receiver)
        assert(deploymentsByDateTime)
        assert(!deploymentsByDateTime.isEmpty())

        recoveries = deploymentsByDateTime*.recovery.grep {
            recovery ->

            if (!recovery) {
                return false
            }

            if (recovery.recoveryDateTime.toDate().before(params.timestamp)) {
                return false
            }

            deployment = recovery.deployment
            return true
        }

        return (!recoveries || recoveries.isEmpty())
    }

    boolean validate(theReceiverDownload, theParams) {
        reset(theReceiverDownload, theParams)

        if (isDuplicate()) {
            log.debug("Invalid detection: duplicate")
            invalidMessage = ""
            invalidReason = InvalidDetectionReason.DUPLICATE

            return false
        }

        if (isUnknownReceiver()) {
            log.debug("Invalid detection: unknown receiver code name " + params.receiverName)
            invalidMessage = "Unknown receiver code name " + params.receiverName
            invalidReason = InvalidDetectionReason.UNKNOWN_RECEIVER

            return false
        }

        if (hasNoDeploymentsAtDateTime()) {
            log.debug("Invalid detection: no deployment at time " + simpleDateFormat.format(params.timestamp) + " for receiver " + params.receiverName)
            invalidMessage = "No deployment at time " + simpleDateFormat.format(params.timestamp) + " for receiver " + params.receiverName
            invalidReason = InvalidDetectionReason.NO_DEPLOYMENT_AT_DATE_TIME

            return false
        }

        if (hasNoRecoveriesAtDateTime()) {
            log.debug("Invalid detection: no recovery at time " + simpleDateFormat.format(params.timestamp) + " for receiver " + params.receiverName)
            invalidMessage = "No recovery at time " + simpleDateFormat.format(params.timestamp) + " for receiver " + params.receiverName
            invalidReason = InvalidDetectionReason.NO_RECOVERY_AT_DATE_TIME

            return false
        }

        return true
    }

    private void reset(theReceiverDownload, theParams) {
        invalidMessage = null
        invalidReason = null

        receiverDownload = theReceiverDownload
        params = theParams

        receiver = Receiver.findByName(params.receiverName, [cache:true])
        deploymentsByDateTime?.clear()
        recoveries?.clear()
        deployment = null
    }
}
